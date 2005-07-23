//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.IdAllocator;

/**
 *  Parses Tapestry templates, breaking them into a series of
 *  {@link org.apache.tapestry.parse.TemplateToken tokens}.
 *  Although often referred to as an "HTML template", there is no real
 *  requirement that the template be HTML.  This parser can handle
 *  any reasonable SGML derived markup (including XML), 
 *  but specifically works around the ambiguities
 *  of HTML reasonably.
 * 
 *  <p>Dynamic markup in Tapestry attempts to be invisible.
 *  Components are arbitrary tags containing a <code>jwcid</code> attribute.
 *  Such components must be well balanced (have a matching close tag, or
 *  end the tag with "<code>/&gt;</code>".
 * 
 *  <p>Generally, the id specified in the template is matched against
 *  an component defined in the specification.  However, implicit
 *  components are also possible.  The jwcid attribute uses
 *  the syntax "<code>@Type</code>" for implicit components.
 *  Type is the component type, and may include a library id prefix.  Such
 *  a component is anonymous (but is given a unique id).
 * 
 *  <p>
 *  (The unique ids assigned start with a dollar sign, which is normally
 *  no allowed for component ids ... this helps to make them stand out
 *  and assures that they do not conflict with
 *  user-defined component ids.  These ids tend to propagate
 *  into URLs and become HTML element names and even JavaScript
 *  variable names ... the dollar sign is acceptible in these contexts as 
 *  well).
 * 
 *  <p>Implicit component may also be given a name using the syntax
 *  "<code>componentId:@Type</code>".  Such a component should
 *  <b>not</b> be defined in the specification, but may still be
 *  accessed via {@link org.apache.tapestry.IComponent#getComponent(String)}.
 * 
 *  <p>
 *  Both defined and implicit components may have additional attributes
 *  defined, simply by including them in the template.  They set formal or
 *  informal parameters of the component to static strings.
 *  {@link org.apache.tapestry.spec.IComponentSpecification#getAllowInformalParameters()},
 *  if false, will cause such attributes to be simply ignored.  For defined
 *  components, conflicting values defined in the template are ignored.
 * 
 *  <p>Attributes in component tags will become formal and informal parameters
 *  of the corresponding component.  Most attributes will be
 *
 *  <p>The parser removes
 *  the body of some tags (when the corresponding component doesn't
 *  {@link org.apache.tapestry.spec.IComponentSpecification#getAllowBody() allow a body},
 *  and allows
 *  portions of the template to be completely removed.
 *
 *  <p>The parser does a pretty thorough lexical analysis of the template,
 *  and reports a great number of errors, including improper nesting
 *  of tags.
 *
 *  <p>The parser supports <em>invisible localization</em>:
 *  The parser recognizes HTML of the form:
 *  <code>&lt;span key="<i>value</i>"&gt; ... &lt;/span&gt;</code>
 *  and converts them into a {@link TokenType#LOCALIZATION}
 *  token.  You may also specifify a <code>raw</code> attribute ... if the value
 *  is <code>true</code>, then the localized value is 
 *  sent to the client without filtering, which is appropriate if the
 *  value has any markup that should not be escaped.
 *
 *  @author Howard Lewis Ship, Geoff Longman
 *  @version $Id$
 * 
 **/

public class TemplateParser
{
    /**
     *  A Factory used by {@link org.apache.tapestry.parse.TemplateParser} to create 
     *  {@link org.apache.tapestry.parse.TemplateToken} objects.
     * 
     *  <p>
     *  This class is extended by Spindle - the Eclipse Plugin for Tapestry.
     *  <p>
     *  @author glongman@intelligentworks.com
     *  @since 3.0
     */
    protected static class TemplateTokenFactory
    {

        public OpenToken createOpenToken(String tagName, String jwcId, String type, ILocation location)
        {
            return new OpenToken(tagName, jwcId, type, location);
        }

        public CloseToken createCloseToken(String tagName, ILocation location)
        {
            return new CloseToken(tagName, location);
        }

        public TextToken createTextToken(char[] templateData, int blockStart, int end, ILocation templateLocation)
        {
            return new TextToken(templateData, blockStart, end, templateLocation);
        }

        public LocalizationToken createLocalizationToken(
            String tagName,
            String localizationKey,
            boolean raw,
            Map attributes,
            ILocation startLocation)
        {
            return new LocalizationToken(tagName, localizationKey, raw, attributes, startLocation);
        }
    }

    /**
     *  Attribute value prefix indicating that the attribute is an OGNL expression.
     * 
     *  @since 3.0
     **/

    public static final String OGNL_EXPRESSION_PREFIX = "ognl:";

    /**
     *  Attribute value prefix indicating that the attribute is a localization
     *  key.
     * 
     *  @since 3.0
     * 
     **/

    public static final String LOCALIZATION_KEY_PREFIX = "message:";

    /**
     *  A "magic" component id that causes the tag with the id and its entire
     *  body to be ignored during parsing.
     *
     **/

    private static final String REMOVE_ID = "$remove$";

    /**
     * A "magic" component id that causes the tag to represent the true
     * content of the template.  Any content prior to the tag is discarded,
     * and any content after the tag is ignored.  The tag itself is not
     * included.
     *
     **/

    private static final String CONTENT_ID = "$content$";

    /**
     *  
     *  The attribute, checked for in &lt;span&gt; tags, that signfies
     *  that the span is being used as an invisible localization.
     * 
     *  @since 2.0.4
     * 
     **/

    public static final String LOCALIZATION_KEY_ATTRIBUTE_NAME = "key";

    /**
     *  Used with {@link #LOCALIZATION_KEY_ATTRIBUTE_NAME} to indicate a string
     *  that should be rendered "raw" (without escaping HTML).  If not specified,
     *  defaults to "false".  The value must equal "true" (caselessly).
     * 
     *  @since 2.3
     * 
     **/

    public static final String RAW_ATTRIBUTE_NAME = "raw";

    /**
     *  Attribute used to identify components.
     * 
     *  @since 2.3
     * 
     **/

    public static final String JWCID_ATTRIBUTE_NAME = "jwcid";

    private static final String PROPERTY_NAME_PATTERN = "_?[a-zA-Z]\\w*";

    /**
     *  Pattern used to recognize ordinary components (defined in the specification).
     * 
     *  @since 3.0
     * 
     **/

    public static final String SIMPLE_ID_PATTERN = "^(" + PROPERTY_NAME_PATTERN + ")$";

    /**
     *  Pattern used to recognize implicit components (whose type is defined in
     *  the template).  Subgroup 1 is the id (which may be null) and subgroup 2
     *  is the type (which may be qualified with a library prefix).
     *  Subgroup 4 is the library id, Subgroup 5 is the simple component type.
     * 
     *  @since 3.0
     * 
     **/

    public static final String IMPLICIT_ID_PATTERN =
        "^(" + PROPERTY_NAME_PATTERN + ")?@(((" + PROPERTY_NAME_PATTERN + "):)?(" + PROPERTY_NAME_PATTERN + "))$";

    private static final int IMPLICIT_ID_PATTERN_ID_GROUP = 1;
    private static final int IMPLICIT_ID_PATTERN_TYPE_GROUP = 2;
    private static final int IMPLICIT_ID_PATTERN_LIBRARY_ID_GROUP = 4;
    private static final int IMPLICIT_ID_PATTERN_SIMPLE_TYPE_GROUP = 5;

    private Pattern _simpleIdPattern;
    private Pattern _implicitIdPattern;
    private PatternMatcher _patternMatcher;

    private IdAllocator _idAllocator = new IdAllocator();

    private ITemplateParserDelegate _delegate;

    /**
     *  Identifies the template being parsed; used with error messages.
     *
     **/

    private IResourceLocation _resourceLocation;

    /**
     *  Shared instance of {@link Location} used by
     *  all {@link TextToken} instances in the template.
     * 
     **/

    private ILocation _templateLocation;

    /**
     *  Location with in the resource for the current line.
     * 
     **/

    private ILocation _currentLocation;

    /**
     *  Local reference to the template data that is to be parsed.
     *
     **/

    private char[] _templateData;

    /**
     *  List of Tag
     *
     **/

    private List _stack = new ArrayList();

    private static class Tag
    {
        // The element, i.e., <jwc> or virtually any other element (via jwcid attribute)
        String _tagName;
        // If true, the tag is a placeholder for a dynamic element
        boolean _component;
        // If true, the body of the tag is being ignored, and the
        // ignore flag is cleared when the close tag is reached
        boolean _ignoringBody;
        // If true, then the entire tag (and its body) is being ignored
        boolean _removeTag;
        // If true, then the tag must have a balanced closing tag.
        // This is always true for components.
        boolean _mustBalance;
        // The line on which the start tag exists
        int _line;
        // If true, then the parse ends when the closing tag is found.
        boolean _content;

        Tag(String tagName, int line)
        {
            _tagName = tagName;
            _line = line;
        }

        boolean match(String matchTagName)
        {
            return _tagName.equalsIgnoreCase(matchTagName);
        }
    }

    /**
     *  List of {@link TemplateToken}, this forms the ultimate response.
     *
     **/

    private List _tokens = new ArrayList();

    /**
     *  The location of the 'cursor' within the template data.  The
     *  advance() method moves this forward.
     *
     **/

    private int _cursor;

    /**
     *  The start of the current block of static text, or -1 if no block
     *  is active.
     *
     **/

    private int _blockStart;

    /**
     *  The current line number; tracked by advance().  Starts at 1.
     *
     **/

    private int _line;

    /**
     *  Set to true when the body of a tag is being ignored.  This is typically
     *  used to skip over the body of a tag when its corresponding
     *  component doesn't allow a body, or whe the special
     *  jwcid of $remove$ is used.
     *
     **/

    private boolean _ignoring;

    /**
     *  A {@link Map} of {@link String}s, used to store attributes collected
     *  while parsing a tag.
     *
     **/

    private Map _attributes = new HashMap();

    /**
     *  A factory used to create template tokens.
     * <p>
     * author glongman@intelligentworks.com
     */

    protected TemplateTokenFactory _factory;

    public TemplateParser()
    {
        Perl5Compiler compiler = new Perl5Compiler();

        try
        {
            _simpleIdPattern = compiler.compile(SIMPLE_ID_PATTERN);
            _implicitIdPattern = compiler.compile(IMPLICIT_ID_PATTERN);
        } catch (MalformedPatternException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        _patternMatcher = new Perl5Matcher();

        _factory = new TemplateTokenFactory();
    }

    /**
     *  Parses the template data into an array of {@link TemplateToken}s.
     *
     *  <p>The parser is <i>decidedly</i> not threadsafe, so care should be taken
     *  that only a single thread accesses it.
     *
     *  @param templateData the HTML template to parse.  Some tokens will hold
     *  a reference to this array.
     *  @param delegate  object that "knows" about defined components
     *  @param resourceLocation a description of where the template originated from,
     *  used with error messages.
     *
     **/

    public TemplateToken[] parse(
        char[] templateData,
        ITemplateParserDelegate delegate,
        IResourceLocation resourceLocation)
        throws TemplateParseException
    {
        TemplateToken[] result = null;

        try
        {
            beforeParse(templateData, delegate, resourceLocation);

            parse();

            result = (TemplateToken[]) _tokens.toArray(new TemplateToken[_tokens.size()]);
        } finally
        {
            afterParse();
        }

        return result;
    }

    /**
     *  perform default initialization of the parser.
     *  <p>
     *  author glongman@intelligentworks.com
     */

    protected void beforeParse(
        char[] templateData,
        ITemplateParserDelegate delegate,
        IResourceLocation resourceLocation)
    {
        _templateData = templateData;
        _resourceLocation = resourceLocation;
        _templateLocation = new Location(resourceLocation);
        _delegate = delegate;
        _ignoring = false;
        _line = 1;
    }

    /**
     *  Perform default cleanup after parsing completes.
     *  <p>
     *  author glongman@intelligentworks.com
     */

    protected void afterParse()
    {
        _delegate = null;
        _templateData = null;
        _resourceLocation = null;
        _templateLocation = null;
        _currentLocation = null;
        _stack.clear();
        _tokens.clear();
        _attributes.clear();
        _idAllocator.clear();
    }

    /**
     * Used by the parser to report problems in the parse.
     * Parsing <b>must</b> stop when a problem is reported.
     * <p>
     * The default implementation simply throws an exception that contains
     * the message and location parameters.
     * <p>
     * Subclasses may override but <b>must</b> ensure they throw the required exception.
     * <p>
     *
     * author glongman@intelligentworks.com
     *  
     * @param message
     * @param location
     * @param line ignored by the default impl
     * @param cursor ignored by the default impl
     * @throws TemplateParseException always thrown in order to terminate the parse.
     */

    protected void templateParseProblem(String message, ILocation location, int line, int cursor)
        throws TemplateParseException
    {
        throw new TemplateParseException(message, location);
    }

    /**
     * Used by the parser to report tapestry runtime specific problems in the parse.
     * Parsing <b>must</b> stop when a problem is reported.
     * <p>
     * The default implementation simply rethrows the exception.
     * <p>
     * Subclasses may override but <b>must</b> ensure they rethrow the exception.
     * <p>
     *
     * author glongman@intelligentworks.com
     *  
     * @param exception
     * @param line ignored by the default impl
     * @param cursor ignored by the default impl
     * @throws ApplicationRuntimeException always rethrown in order to terminate the parse.
     */

    protected void templateParseProblem(ApplicationRuntimeException exception, int line, int cursor)
        throws ApplicationRuntimeException
    {
        throw exception;
    }

    /**
     * Give subclasses access to the parse results.
     * <p>
     *
     * author glongman@intelligentworks.com
     */
    protected List getTokens()
    {
        if (_tokens == null)
            return Collections.EMPTY_LIST;

        return _tokens;
    }

    /**
     *  Checks to see if the next few characters match a given pattern.
     *
     **/

    private boolean lookahead(char[] match)
    {
        try
        {
            for (int i = 0; i < match.length; i++)
            {
                if (_templateData[_cursor + i] != match[i])
                    return false;
            }

            // Every character matched.

            return true;
        } catch (IndexOutOfBoundsException ex)
        {
            return false;
        }
    }

    private static final char[] COMMENT_START = new char[] { '<', '!', '-', '-' };
    private static final char[] COMMENT_END = new char[] { '-', '-', '>' };
    private static final char[] CLOSE_TAG = new char[] { '<', '/' };

    protected void parse() throws TemplateParseException
    {
        _cursor = 0;
        _blockStart = -1;
        int length = _templateData.length;

        while (_cursor < length)
        {
            if (_templateData[_cursor] != '<')
            {
                if (_blockStart < 0 && !_ignoring)
                    _blockStart = _cursor;

                advance();
                continue;
            }

            // OK, start of something.

            if (lookahead(CLOSE_TAG))
            {
                closeTag();
                continue;
            }

            if (lookahead(COMMENT_START))
            {
                skipComment();
                continue;
            }

            // The start of some tag.

            startTag();
        }

        // Usually there's some text at the end of the template (after the last closing tag) that should
        // be added.  Often the last few tags are static tags so we definately
        // need to end the text block.

        addTextToken(_templateData.length - 1);
    }

    /**
     *  Advance forward in the document until the end of the comment is reached.
     *  In addition, skip any whitespace following the comment.
     *
     **/

    private void skipComment() throws TemplateParseException
    {
        int length = _templateData.length;
        int startLine = _line;

        if (_blockStart < 0 && !_ignoring)
            _blockStart = _cursor;

        while (true)
        {
            if (_cursor >= length)
                templateParseProblem(
                    Tapestry.format("TemplateParser.comment-not-ended", Integer.toString(startLine)),
                    new Location(_resourceLocation, startLine),
                    startLine,
                    _cursor);

            if (lookahead(COMMENT_END))
                break;

            // Not the end of the comment, advance over it.

            advance();
        }

        _cursor += COMMENT_END.length;
        advanceOverWhitespace();
    }

    private void addTextToken(int end)
    {
        // No active block to add to.

        if (_blockStart < 0)
            return;

        if (_blockStart <= end)
        {
            TemplateToken token = _factory.createTextToken(_templateData, _blockStart, end, _templateLocation);

            _tokens.add(token);
        }

        _blockStart = -1;
    }

    private static final int WAIT_FOR_ATTRIBUTE_NAME = 0;
    private static final int COLLECT_ATTRIBUTE_NAME = 1;
    private static final int ADVANCE_PAST_EQUALS = 2;
    private static final int WAIT_FOR_ATTRIBUTE_VALUE = 3;
    private static final int COLLECT_QUOTED_VALUE = 4;
    private static final int COLLECT_UNQUOTED_VALUE = 5;

    private void startTag() throws TemplateParseException
    {
        int cursorStart = _cursor;
        int length = _templateData.length;
        String tagName = null;
        boolean endOfTag = false;
        boolean emptyTag = false;
        int startLine = _line;
        ILocation startLocation = new Location(_resourceLocation, startLine);

        tagBeginEvent(startLine, _cursor);

        advance();

        // Collect the element type

        while (_cursor < length)
        {
            char ch = _templateData[_cursor];

            if (ch == '/' || ch == '>' || Character.isWhitespace(ch))
            {
                tagName = new String(_templateData, cursorStart + 1, _cursor - cursorStart - 1);

                break;
            }

            advance();
        }

        String attributeName = null;
        int attributeNameStart = -1;
        int attributeValueStart = -1;
        int state = WAIT_FOR_ATTRIBUTE_NAME;
        char quoteChar = 0;

        _attributes.clear();

        // Collect each attribute

        while (!endOfTag)
        {
            if (_cursor >= length)
            {
                String key = (tagName == null) ? "TemplateParser.unclosed-unknown-tag" : "TemplateParser.unclosed-tag";

                templateParseProblem(
                    Tapestry.format(key, tagName, Integer.toString(startLine)),
                    startLocation,
                    startLine,
                    cursorStart);

            }

            char ch = _templateData[_cursor];

            switch (state)
            {
                case WAIT_FOR_ATTRIBUTE_NAME :

                    // Ignore whitespace before the next attribute name, while
                    // looking for the end of the current tag.

                    if (ch == '/')
                    {
                        emptyTag = true;
                        advance();
                        break;
                    }

                    if (ch == '>')
                    {
                        endOfTag = true;
                        break;
                    }

                    if (Character.isWhitespace(ch))
                    {
                        advance();
                        break;
                    }

                    // Found non-whitespace, assume its the attribute name.
                    // Note: could use a check here for non-alpha.

                    attributeNameStart = _cursor;
                    state = COLLECT_ATTRIBUTE_NAME;
                    advance();
                    break;

                case COLLECT_ATTRIBUTE_NAME :

                    // Looking for end of attribute name.

                    if (ch == '=' || ch == '/' || ch == '>' || Character.isWhitespace(ch))
                    {
                        attributeName = new String(_templateData, attributeNameStart, _cursor - attributeNameStart);

                        state = ADVANCE_PAST_EQUALS;
                        break;
                    }

                    // Part of the attribute name

                    advance();
                    break;

                case ADVANCE_PAST_EQUALS :

                    // Looking for the '=' sign.  May hit the end of the tag, or (for bare attributes),
                    // the next attribute name.

                    if (ch == '/' || ch == '>')
                    {
                        // A bare attribute, which is not interesting to
                        // us.

                        state = WAIT_FOR_ATTRIBUTE_NAME;
                        break;
                    }

                    if (Character.isWhitespace(ch))
                    {
                        advance();
                        break;
                    }

                    if (ch == '=')
                    {
                        state = WAIT_FOR_ATTRIBUTE_VALUE;
                        quoteChar = 0;
                        attributeValueStart = -1;
                        advance();
                        break;
                    }

                    // Otherwise, an HTML style "bare" attribute (such as <select multiple>).
                    // We aren't interested in those (we're just looking for the id or jwcid attribute).

                    state = WAIT_FOR_ATTRIBUTE_NAME;
                    break;

                case WAIT_FOR_ATTRIBUTE_VALUE :

                    if (ch == '/' || ch == '>')
                        templateParseProblem(
                            Tapestry.format(
                                "TemplateParser.missing-attribute-value",
                                tagName,
                                Integer.toString(_line),
                                attributeName),
                            getCurrentLocation(),
                            _line,
                            _cursor);

                    // Ignore whitespace between '=' and the attribute value.  Also, look
                    // for initial quote.

                    if (Character.isWhitespace(ch))
                    {
                        advance();
                        break;
                    }

                    if (ch == '\'' || ch == '"')
                    {
                        quoteChar = ch;

                        state = COLLECT_QUOTED_VALUE;
                        advance();
                        attributeValueStart = _cursor;
                        attributeBeginEvent(attributeName, _line, attributeValueStart);
                        break;
                    }

                    // Not whitespace or quote, must be start of unquoted attribute.

                    state = COLLECT_UNQUOTED_VALUE;
                    attributeValueStart = _cursor;
                    attributeBeginEvent(attributeName, _line, attributeValueStart);
                    break;

                case COLLECT_QUOTED_VALUE :

                    // Start collecting the quoted attribute value.  Stop at the matching quote character,
                    // unless bare, in which case, stop at the next whitespace.

                    if (ch == quoteChar)
                    {
                        String attributeValue =
                            new String(_templateData, attributeValueStart, _cursor - attributeValueStart);
                        
                       
                        attributeEndEvent(_cursor);
                        
                        if (_attributes.containsKey(attributeName))
                        	templateParseProblem(
                                    Tapestry.format(
                                        "TemplateParser.duplicate-tag-attribute",
                                        tagName,
                                        Integer.toString(_line),
                                        attributeName),
                                    getCurrentLocation(),
                                    _line,
                                    _cursor);
                            
                          _attributes.put(attributeName, attributeValue);

                        // Advance over the quote.
                        advance();
                        state = WAIT_FOR_ATTRIBUTE_NAME;
                        break;
                    }

                    advance();
                    break;

                case COLLECT_UNQUOTED_VALUE :

                    // An unquoted attribute value ends with whitespace 
                    // or the end of the enclosing tag.

                    if (ch == '/' || ch == '>' || Character.isWhitespace(ch))
                    {
                        String attributeValue =
                            new String(_templateData, attributeValueStart, _cursor - attributeValueStart);

                        attributeEndEvent(_cursor);
                        
                        if (_attributes.containsKey(attributeName))
                        	templateParseProblem(
                                    Tapestry.format(
                                        "TemplateParser.duplicate-tag-attribute",
                                        tagName,
                                        Integer.toString(_line),
                                        attributeName),
                                    getCurrentLocation(),
                                    _line,
                                    _cursor);
                            
                          _attributes.put(attributeName, attributeValue);

                        state = WAIT_FOR_ATTRIBUTE_NAME;
                        break;
                    }

                    advance();
                    break;
            }
        }
        
        tagEndEvent(_cursor);

        // Check for invisible localizations

        String localizationKey = findValueCaselessly(LOCALIZATION_KEY_ATTRIBUTE_NAME, _attributes);
        String jwcId = findValueCaselessly(JWCID_ATTRIBUTE_NAME, _attributes);

        if (localizationKey != null && tagName.equalsIgnoreCase("span") && jwcId == null)
        {
            if (_ignoring)
                templateParseProblem(
                    Tapestry.format(
                        "TemplateParser.component-may-not-be-ignored",
                        tagName,
                        Integer.toString(startLine)),
                    startLocation,
                    startLine,
                    cursorStart);

            // If the tag isn't empty, then create a Tag instance to ignore the
            // body of the tag.

            if (!emptyTag)
            {
                Tag tag = new Tag(tagName, startLine);

                tag._component = false;
                tag._removeTag = true;
                tag._ignoringBody = true;
                tag._mustBalance = true;

                _stack.add(tag);

                // Start ignoring content until the close tag.

                _ignoring = true;
            } else
            {
                // Cursor is at the closing carat, advance over it and any whitespace.                
                advance();
                advanceOverWhitespace();
            }

            // End any open block.

            addTextToken(cursorStart - 1);

            boolean raw = checkBoolean(RAW_ATTRIBUTE_NAME, _attributes);

            Map attributes = filter(_attributes, new String[] { LOCALIZATION_KEY_ATTRIBUTE_NAME, RAW_ATTRIBUTE_NAME });

            TemplateToken token =
                _factory.createLocalizationToken(tagName, localizationKey, raw, attributes, startLocation);

            _tokens.add(token);

            return;
        }

        if (jwcId != null)
        {
            processComponentStart(tagName, jwcId, emptyTag, startLine, cursorStart, startLocation);
            return;
        }

        // A static tag (not a tag without a jwcid attribute).
        // We need to record this so that we can match close tags later.

        if (!emptyTag)
        {
            Tag tag = new Tag(tagName, startLine);
            _stack.add(tag);
        }

        // If there wasn't an active block, then start one.

        if (_blockStart < 0 && !_ignoring)
            _blockStart = cursorStart;

        advance();
    }

    /**
     *  Processes a tag that is the open tag for a component (but also handles
     *  the $remove$ and $content$ tags).
     * 
     **/

    /**
     * Notify that the beginning of a tag has been detected.
     * <p>
     * Default implementation does nothing.
     * <p>
     *
     * author glongman@intelligentworks.com
     */
    protected void tagBeginEvent(int startLine, int cursorPosition)
    {
    }

    /**
     * Notify that the end of the current tag has been detected.
     * <p>
     * Default implementation does nothing.
     * <p>
     * author glongman@intelligentworks.com
     */
    protected void tagEndEvent(int cursorPosition)
    {
    }

    /**
     * Notify that the beginning of an attribute value has been detected.
     * <p>
     * Default implementation does nothing.
     * <p>
     * author glongman@intelligentworks.com
     */
    protected void attributeBeginEvent(String attributeName, int startLine, int cursorPosition)
    {
    }

    /**
     * Notify that the end of the current attribute value has been detected.
     * <p>
     * Default implementation does nothing.
     * <p>
     * author glongman@intelligentworks.com
     */
    protected void attributeEndEvent(int cursorPosition)
    {
    }

    private void processComponentStart(
        String tagName,
        String jwcId,
        boolean emptyTag,
        int startLine,
        int cursorStart,
        ILocation startLocation)
        throws TemplateParseException
    {
        if (jwcId.equalsIgnoreCase(CONTENT_ID))
        {
            processContentTag(tagName, startLine, cursorStart, emptyTag);

            return;
        }

        boolean isRemoveId = jwcId.equalsIgnoreCase(REMOVE_ID);

        if (_ignoring && !isRemoveId)
            templateParseProblem(
                Tapestry.format("TemplateParser.component-may-not-be-ignored", tagName, Integer.toString(startLine)),
                startLocation,
                startLine,
                cursorStart);

        String type = null;
        boolean allowBody = false;

        if (_patternMatcher.matches(jwcId, _implicitIdPattern))
        {
            MatchResult match = _patternMatcher.getMatch();

            jwcId = match.group(IMPLICIT_ID_PATTERN_ID_GROUP);
            type = match.group(IMPLICIT_ID_PATTERN_TYPE_GROUP);

            String libraryId = match.group(IMPLICIT_ID_PATTERN_LIBRARY_ID_GROUP);
            String simpleType = match.group(IMPLICIT_ID_PATTERN_SIMPLE_TYPE_GROUP);

            // If (and this is typical) no actual component id was specified,
            // then generate one on the fly.
            // The allocated id for anonymous components is
            // based on the simple (unprefixed) type, but starts
            // with a leading dollar sign to ensure no conflicts
            // with user defined component ids (which don't allow dollar signs
            // in the id).

            if (jwcId == null)
                jwcId = _idAllocator.allocateId("$" + simpleType);

            try
            {
                allowBody = _delegate.getAllowBody(libraryId, simpleType, startLocation);
            } catch (ApplicationRuntimeException e)
            {
                // give subclasses a chance to handle and rethrow
                templateParseProblem(e, startLine, cursorStart);
            }

        } else
        {
            if (!isRemoveId)
            {
                if (!_patternMatcher.matches(jwcId, _simpleIdPattern))
                    templateParseProblem(
                        Tapestry.format(
                            "TemplateParser.component-id-invalid",
                            tagName,
                            Integer.toString(startLine),
                            jwcId),
                        startLocation,
                        startLine,
                        cursorStart);

                if (!_delegate.getKnownComponent(jwcId))
                    templateParseProblem(
                        Tapestry.format(
                            "TemplateParser.unknown-component-id",
                            tagName,
                            Integer.toString(startLine),
                            jwcId),
                        startLocation,
                        startLine,
                        cursorStart);

                try
                {
                    allowBody = _delegate.getAllowBody(jwcId, startLocation);
                } catch (ApplicationRuntimeException e)
                {
                    // give subclasses a chance to handle and rethrow
                    templateParseProblem(e, startLine, cursorStart);
                }
            }
        }

        // Ignore the body if we're removing the entire tag,
        // of if the corresponding component doesn't allow
        // a body.

        boolean ignoreBody = !emptyTag && (isRemoveId || !allowBody);

        if (_ignoring && ignoreBody)
            templateParseProblem(
                Tapestry.format("TemplateParser.nested-ignore", tagName, Integer.toString(startLine)),
                new Location(_resourceLocation, startLine),
                startLine,
                cursorStart);

        if (!emptyTag)
            pushNewTag(tagName, startLine, isRemoveId, ignoreBody);

        // End any open block.

        addTextToken(cursorStart - 1);

        if (!isRemoveId)
        {
            addOpenToken(tagName, jwcId, type, startLocation);

            if (emptyTag)
                _tokens.add(_factory.createCloseToken(tagName, getCurrentLocation()));
        }

        advance();
    }

    private void pushNewTag(String tagName, int startLine, boolean isRemoveId, boolean ignoreBody)
    {
        Tag tag = new Tag(tagName, startLine);

        tag._component = !isRemoveId;
        tag._removeTag = isRemoveId;

        tag._ignoringBody = ignoreBody;

        _ignoring = tag._ignoringBody;

        tag._mustBalance = true;

        _stack.add(tag);
    }

    private void processContentTag(String tagName, int startLine, int cursorStart, boolean emptyTag)
        throws TemplateParseException
    {
        if (_ignoring)
            templateParseProblem(
                Tapestry.format(
                    "TemplateParser.content-block-may-not-be-ignored",
                    tagName,
                    Integer.toString(startLine)),
                new Location(_resourceLocation, startLine),
                startLine,
                cursorStart);

        if (emptyTag)
            templateParseProblem(
                Tapestry.format("TemplateParser.content-block-may-not-be-empty", tagName, Integer.toString(startLine)),
                new Location(_resourceLocation, startLine),
                startLine,
                cursorStart);

        _tokens.clear();
        _blockStart = -1;

        Tag tag = new Tag(tagName, startLine);

        tag._mustBalance = true;
        tag._content = true;

        _stack.clear();
        _stack.add(tag);

        advance();
    }

    private void addOpenToken(String tagName, String jwcId, String type, ILocation location)
    {
        OpenToken token = _factory.createOpenToken(tagName, jwcId, type, location);
        _tokens.add(token);

        if (_attributes.isEmpty())
            return;

        Iterator i = _attributes.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String key = (String) entry.getKey();

            if (key.equalsIgnoreCase(JWCID_ATTRIBUTE_NAME))
                continue;

            String value = (String) entry.getValue();

            addAttributeToToken(token, key, value);
        }
    }

    /**
     *  Analyzes the attribute value, looking for possible prefixes that indicate
     *  the value is not a literal.  Adds the attribute to the
     *  token.
     * 
     *  @since 3.0
     * 
     **/

    private void addAttributeToToken(OpenToken token, String name, String attributeValue)
    {
        int pos = attributeValue.indexOf(":");

        if (pos > 0)
        {

            String prefix = attributeValue.substring(0, pos + 1);

            if (prefix.equals(OGNL_EXPRESSION_PREFIX))
            {
                token.addAttribute(
                    name,
                    AttributeType.OGNL_EXPRESSION,
                    extractExpression(attributeValue.substring(pos + 1)));
                return;
            }

            if (prefix.equals(LOCALIZATION_KEY_PREFIX))
            {
                token.addAttribute(name, AttributeType.LOCALIZATION_KEY, attributeValue.substring(pos + 1).trim());
                return;

            }
        }

        token.addAttribute(name, AttributeType.LITERAL, attributeValue);
    }

    /**
     *  Invoked to handle a closing tag, i.e., &lt;/foo&gt;.  When a tag closes, it will match against
     *  a tag on the open tag start.  Preferably the top tag on the stack (if everything is well balanced), but this
     *  is HTML, not XML, so many tags won't balance.
     *
     *  <p>Once the matching tag is located, the question is ... is the tag dynamic or static?  If static, then
     * the current text block is extended to include this close tag.  If dynamic, then the current text block
     * is ended (before the '&lt;' that starts the tag) and a close token is added.
     *
     * <p>In either case, the matching static element and anything above it is removed, and the cursor is left
     * on the character following the '&gt;'.
     *
     **/

    private void closeTag() throws TemplateParseException
    {
        int cursorStart = _cursor;
        int length = _templateData.length;
        int startLine = _line;

        ILocation startLocation = getCurrentLocation();

        _cursor += CLOSE_TAG.length;

        int tagStart = _cursor;

        while (true)
        {
            if (_cursor >= length)
                templateParseProblem(
                    Tapestry.format("TemplateParser.incomplete-close-tag", Integer.toString(startLine)),
                    startLocation,
                    startLine,
                    cursorStart);

            char ch = _templateData[_cursor];

            if (ch == '>')
                break;

            advance();
        }

        String tagName = new String(_templateData, tagStart, _cursor - tagStart);

        int stackPos = _stack.size() - 1;
        Tag tag = null;

        while (stackPos >= 0)
        {
            tag = (Tag) _stack.get(stackPos);

            if (tag.match(tagName))
                break;

            if (tag._mustBalance)
                templateParseProblem(
                    Tapestry.format(
                        "TemplateParser.improperly-nested-close-tag",
                        new Object[] {
                            tagName,
                            Integer.toString(startLine),
                            tag._tagName,
                            Integer.toString(tag._line)}),
                    startLocation,
                    startLine,
                    cursorStart);

            stackPos--;
        }

        if (stackPos < 0)
            templateParseProblem(
                Tapestry.format("TemplateParser.unmatched-close-tag", tagName, Integer.toString(startLine)),
                startLocation,
                startLine,
                cursorStart);

        // Special case for the content tag

        if (tag._content)
        {
            addTextToken(cursorStart - 1);

            // Advance the cursor right to the end.

            _cursor = length;
            _stack.clear();
            return;
        }

        // When a component closes, add a CLOSE tag.
        if (tag._component)
        {
            addTextToken(cursorStart - 1);

            _tokens.add(_factory.createCloseToken(tagName, getCurrentLocation()));
        } else
        {
            // The close of a static tag.  Unless removing the tag
            // entirely, make sure the block tag is part of a text block.

            if (_blockStart < 0 && !tag._removeTag && !_ignoring)
                _blockStart = cursorStart;
        }

        // Remove all elements at stackPos or above.

        for (int i = _stack.size() - 1; i >= stackPos; i--)
            _stack.remove(i);

        // Advance cursor past '>'

        advance();

        // If editting out the tag (i.e., $remove$) then kill any whitespace.
        // For components that simply don't contain a body, removeTag will
        // be false.

        if (tag._removeTag)
            advanceOverWhitespace();

        // If we were ignoring the body of the tag, then clear the ignoring
        // flag, since we're out of the body.

        if (tag._ignoringBody)
            _ignoring = false;
    }

    /**
     *  Advances the cursor to the next character.
     *  If the end-of-line is reached, then increments
     *  the line counter.
     *
     **/

    private void advance()
    {
        int length = _templateData.length;

        if (_cursor >= length)
            return;

        char ch = _templateData[_cursor];

        _cursor++;

        if (ch == '\n')
        {
            _line++;
            _currentLocation = null;
            return;
        }

        // A \r, or a \r\n also counts as a new line.

        if (ch == '\r')
        {
            _line++;
            _currentLocation = null;

            if (_cursor < length && _templateData[_cursor] == '\n')
                _cursor++;

            return;
        }

        // Not an end-of-line character.

    }

    private void advanceOverWhitespace()
    {
        int length = _templateData.length;

        while (_cursor < length)
        {
            char ch = _templateData[_cursor];
            if (!Character.isWhitespace(ch))
                return;

            advance();
        }
    }

    /**
     *  Returns a new Map that is a copy of the input Map with some
     *  key/value pairs removed.  A list of keys is passed in
     *  and matching keys (caseless comparison) from the input
     *  Map are excluded from the output map.  May return null
     *  (rather than return an empty Map).
     * 
     **/

    private Map filter(Map input, String[] removeKeys)
    {
        if (input == null || input.isEmpty())
            return null;

        Map result = null;

        Iterator i = input.entrySet().iterator();

        nextkey : while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String key = (String) entry.getKey();

            for (int j = 0; j < removeKeys.length; j++)
            {
                if (key.equalsIgnoreCase(removeKeys[j]))
                    continue nextkey;
            }

            if (result == null)
                result = new HashMap(input.size());

            result.put(key, entry.getValue());
        }

        return result;
    }

    /**
     *  Searches a Map for given key, caselessly.  The Map is expected to consist of Strings for keys and
     *  values.  Returns the value for the first key found that matches (caselessly) the input key.  Returns null
     *  if no value found.
     * 
     **/

    protected String findValueCaselessly(String key, Map map)
    {
        String result = (String) map.get(key);

        if (result != null)
            return result;

        Iterator i = map.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String entryKey = (String) entry.getKey();

            if (entryKey.equalsIgnoreCase(key))
                return (String) entry.getValue();
        }

        return null;
    }

    /**
     *  Conversions needed by {@link #extractExpression(String)}
     * 
     **/

    private static final String[] CONVERSIONS = { "&lt;", "<", "&gt;", ">", "&quot;", "\"", "&amp;", "&" };

    /**
     *  Provided a raw input string that has been recognized to be an expression,
     *  this removes excess white space and converts &amp;amp;, &amp;quot; &amp;lt; and &amp;gt;
     *  to their normal character values (otherwise its impossible to specify
     *  those values in expressions in the template).
     * 
     **/

    private String extractExpression(String input)
    {
        int inputLength = input.length();

        StringBuffer buffer = new StringBuffer(inputLength);

        int cursor = 0;

        outer : while (cursor < inputLength)
        {
            for (int i = 0; i < CONVERSIONS.length; i += 2)
            {
                String entity = CONVERSIONS[i];
                int entityLength = entity.length();
                String value = CONVERSIONS[i + 1];

                if (cursor + entityLength > inputLength)
                    continue;

                if (input.substring(cursor, cursor + entityLength).equals(entity))
                {
                    buffer.append(value);
                    cursor += entityLength;
                    continue outer;
                }
            }

            buffer.append(input.charAt(cursor));
            cursor++;
        }

        return buffer.toString().trim();
    }

    /**
     *  Returns true if the  map contains the given key (caseless search) and the value
     *  is "true" (caseless comparison).
     * 
     **/

    private boolean checkBoolean(String key, Map map)
    {
        String value = findValueCaselessly(key, map);

        if (value == null)
            return false;

        return value.equalsIgnoreCase("true");
    }

    /**
     *  Gets the current location within the file.  This allows the location to be
     *  created only as needed, and multiple objects on the same line can share
     *  the same Location instance.
     * 
     *  @since 3.0
     * 
     **/

    protected ILocation getCurrentLocation()
    {
        if (_currentLocation == null)
            _currentLocation = new Location(_resourceLocation, _line);

        return _currentLocation;
    }
}