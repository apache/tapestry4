package net.sf.tapestry.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.Tapestry;

/**
 *  A new implementation of the template parser; this does <em>not</em>
 *  rely on GNU Regexp, and performs a much more complex parsing job.
 *  This parser supports the &lt;jwc id="<i>id</id>"&gt; syntax (standard
 *  through release 1.0.1).    In addition, any HTML tag can become
 *  the equivalent of a &lt;jwc&gt; tag by including a <code>jwcid</code>
 *  attribute.  That latter is referred to as <em>invisible instrumentation</em>,
 *  as the instrumentation is invisible to a WYSIWYG editor.
 *
 *  <p>The parser removes
 *  the body of some tags (when the corresponding component doesn't
 *  allow a body), identifies more template errors, and allows
 *  portions of the template to be completely removed.
 *
 *  <p>The parser does a more thorough lexical analysis of the template,
 *  and reports a great number of errors, including improper nesting
 *  of tags.
 *
 *  <p>The parser identifies attributes in dynamic tags (ordinary 
 *  tags with the <code>jwcid</code> attribute) and records them,
 *  where they later become static bindings on the component.
 *
 *  <p>Although the &lt;jwc&gt; tag is still supported (and will always
 *  be), it can now be avoided by using the jwcid attribute on
 *  existing tags, such as &lt;span&gt;.
 * 
 *  Starting in release 2.0.4 is a new option, <em>invisible localization</em>
 *  where the parser recognizes HTML of the form:
 *  <code>&lt;span key="<i>value</i>"&gt; ... &lt;/span&gt;</code>
 *  and converts them into a {@link TokenType#LOCALIZATION}
 *  token.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TemplateParser
{
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

    private ITemplateParserDelegate _delegate;

    /**
     *  Identifies the template being parsed; used with error messages.
     *
     **/

    private String _resourcePath;

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
     *  A {@link Map} of {@link Strings}, used to store attributes collected
     *  while parsing a tag.
     *
     **/

    private Map _attributes = new HashMap();

    /**
     *  Parses the template data into an array of {@link TemplateToken}s.
     *
     *  <p>The parser is very much not threadsafe, so care should be taken
     *  that only a single thread accesses it.
     *
     *  @param templateData the HTML template to parse.  Some tokens will hold
     *  a reference to this array.
     *  @param delegate delegate object that "knows" about components
     *  @param resourcePath a description of where the template originated from,
     *  used with error messages.
     *
     **/

    public TemplateToken[] parse(char[] templateData, ITemplateParserDelegate delegate, String resourcePath)
        throws TemplateParseException
    {
        TemplateToken[] result = null;

        try
        {
            _templateData = templateData;
            _resourcePath = resourcePath;
            _delegate = delegate;
            _ignoring = false;
            _line = 1;

            parse();

            result = (TemplateToken[]) _tokens.toArray(new TemplateToken[_tokens.size()]);
        }
        finally
        {
            _delegate = null;
            _templateData = null;
            _resourcePath = null;
            _stack.clear();
            _tokens.clear();
            _attributes.clear();
        }

        return result;
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
        }
        catch (IndexOutOfBoundsException ex)
        {
            return false;
        }
    }

    private static final char[] COMMENT_START = new char[] { '<', '!', '-', '-' };
    private static final char[] COMMENT_END = new char[] { '-', '-', '>' };
    private static final char[] CLOSE_TAG = new char[] { '<', '/' };

    private void parse() throws TemplateParseException
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
                throw new TemplateParseException(
                    Tapestry.getString("TemplateParser.comment-not-ended", Integer.toString(startLine)),
                    startLine,
                    _resourcePath);

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
            TemplateToken token = new TextToken(_templateData, _blockStart, end);

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

                throw new TemplateParseException(
                    Tapestry.getString(key, tagName, Integer.toString(startLine)),
                    startLine,
                    _resourcePath);
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
                        throw new TemplateParseException(
                            Tapestry.getString(
                                "TemplateParser.missing-attiribute-value",
                                tagName,
                                Integer.toString(_line),
                                attributeName),
                            _line,
                            _resourcePath);

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
                        break;
                    }

                    // Not whitespace or quote, must be start of unquoted attribute.

                    state = COLLECT_UNQUOTED_VALUE;
                    attributeValueStart = _cursor;
                    break;

                case COLLECT_QUOTED_VALUE :

                    // Start collecting the quoted attribute value.  Stop at the matching quote character,
                    // unless bare, in which case, stop at the next whitespace.

                    if (ch == quoteChar)
                    {
                        String attributeValue =
                            new String(_templateData, attributeValueStart, _cursor - attributeValueStart);

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

                        _attributes.put(attributeName, attributeValue);

                        state = WAIT_FOR_ATTRIBUTE_NAME;
                        break;
                    }

                    advance();
                    break;
            }
        }

        // Check for invisible localizations

        String localizationKey = findValueCaselessly(LOCALIZATION_KEY_ATTRIBUTE_NAME, _attributes);

        if (localizationKey != null && tagName.equalsIgnoreCase("span"))
        {
            if (_ignoring)
                throw new TemplateParseException(
                    Tapestry.getString(
                        "TemplateParser.component-may-not-be-ignored",
                        tagName,
                        Integer.toString(startLine)),
                    startLine,
                    _resourcePath);

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
            }
            else
            {
                // Cursor is at the closing carat, advance over it and any whitespace.                
                advance();
                advanceOverWhitespace();
            }

            // End any open block.

            addTextToken(cursorStart - 1);

            boolean raw = checkBoolean(RAW_ATTRIBUTE_NAME, _attributes);

            Map attributes = filter(_attributes, new String[] { LOCALIZATION_KEY_ATTRIBUTE_NAME, RAW_ATTRIBUTE_NAME });

            TemplateToken token = new LocalizationToken(tagName, localizationKey, raw, attributes);

            _tokens.add(token);

            return;
        }

        String jwcId = findValueCaselessly(JWCID_ATTRIBUTE_NAME, _attributes);

        if (jwcId != null)
        {
            if (jwcId.equalsIgnoreCase(CONTENT_ID))
            {
                if (_ignoring)
                    throw new TemplateParseException(
                        Tapestry.getString(
                            "TemplateParser.content-block-may-not-be-ignored",
                            tagName,
                            Integer.toString(startLine)),
                        startLine,
                        _resourcePath);

                if (emptyTag)
                    throw new TemplateParseException(
                        Tapestry.getString(
                            "TemplateParser.content-block-may-not-be-empty",
                            tagName,
                            Integer.toString(startLine)),
                        startLine,
                        _resourcePath);

                _tokens.clear();
                _blockStart = -1;

                Tag tag = new Tag(tagName, startLine);

                tag._mustBalance = true;
                tag._content = true;

                _stack.clear();
                _stack.add(tag);

                advance();

                return;
            }

            boolean isRemoveId = jwcId.equalsIgnoreCase(REMOVE_ID);

            if (!(isRemoveId || _delegate.getKnownComponent(jwcId)))
                throw new TemplateParseException(
                    Tapestry.getString(
                        "TemplateParser.unknown-component-id",
                        tagName,
                        Integer.toString(startLine),
                        jwcId),
                    startLine,
                    _resourcePath);

            if (_ignoring && !isRemoveId)
                throw new TemplateParseException(
                    Tapestry.getString(
                        "TemplateParser.component-may-not-be-ignored",
                        tagName,
                        Integer.toString(startLine)),
                    startLine,
                    _resourcePath);

            // Ignore the body if we're removing the entire tag,
            // of if the corresponding component doesn't allow
            // a body.

            boolean ignoreBody = !emptyTag && (isRemoveId || !_delegate.getAllowBody(jwcId));

            if (_ignoring && ignoreBody)
                throw new TemplateParseException(
                    Tapestry.getString("TemplateParser.nested-ignore", tagName, Integer.toString(startLine)),
                    startLine,
                    _resourcePath);

            if (!emptyTag)
            {
                Tag tag = new Tag(tagName, startLine);

                tag._component = !isRemoveId;
                tag._removeTag = isRemoveId;

                tag._ignoringBody = ignoreBody;

                _ignoring = tag._ignoringBody;

                tag._mustBalance = true;

                _stack.add(tag);
            }

            // End any open block.

            addTextToken(cursorStart - 1);

            if (!isRemoveId)
            {
                addOpenToken(tagName, jwcId);

                if (emptyTag)
                    _tokens.add(new CloseToken(tagName));
            }

            advance();

            return;
        }

        // A static tag (not a <jwc> tag, or an ordinary tag without a jwcid attribute).
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

    private void addOpenToken(String tagName, String jwcId)
    {
        OpenToken token = new OpenToken(tagName, jwcId);
        _tokens.add(token);
        
        if (_attributes.isEmpty())
            return;
            
        Iterator i = _attributes.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry)i.next();
            
            String key = (String)entry.getKey();
            
            if (key.equalsIgnoreCase(JWCID_ATTRIBUTE_NAME))
                continue;
            
            String value = (String)entry.getValue();
            
            // OGNL expressions look like "[[ expression ]]"
            
            if (value.startsWith("[[") && value.endsWith("]]"))
            {
                value = extractExpression(value);
                
                
                token.addExpressionValue(key, value);
                continue;
            }
                        
            token.addStaticValue(key, value);
        }        
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

        _cursor += CLOSE_TAG.length;

        int tagStart = _cursor;

        while (true)
        {
            if (_cursor >= length)
                throw new TemplateParseException(
                    Tapestry.getString("TemplateParser.incomplete-close-tag", Integer.toString(startLine)),
                    startLine,
                    _resourcePath);

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
                throw new TemplateParseException(
                    Tapestry.getString(
                        "TemplateParser.improperly-nested-close-tag",
                        new Object[] {
                            tagName,
                            Integer.toString(startLine),
                            tag._tagName,
                            Integer.toString(tag._line)}),
                    startLine,
                    _resourcePath);

            stackPos--;
        }

        if (stackPos < 0)
            throw new TemplateParseException(
                Tapestry.getString("TemplateParser.unmatched-close-tag", tagName, Integer.toString(startLine)),
                startLine,
                _resourcePath);

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

            _tokens.add(new CloseToken(tagName));
        }
        else
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
            return;
        }

        // A \r, or a \r\n also counts as a new line.

        if (ch == '\r')
        {
            _line++;

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
     *  Returns a copy of the input Map, with the identified
     *  key removed.  The check for matching keys is caseless.
     *  May return null if the input Map is empty, or null, or
     *  contains only the matching key.
     * 
     **/

    private Map filter(Map input, String removeKey)
    {
        if (input == null || input.isEmpty())
            return null;

        Map result = null;

        Iterator i = input.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String key = (String) entry.getKey();

            if (key.equalsIgnoreCase(removeKey))
                continue;

            if (result == null)
                result = new HashMap(input.size());

            result.put(key, entry.getValue());
        }

        return result;
    }

    /**
     *  Variation of {@link #filter(Map, String)} when multiple keys
     *  should be removed.
     * 
     **/

    private Map filter(Map input, String[] removeKeys)
    {
        if (input == null || input.isEmpty())
            return null;

        Map result = null;

        Iterator i = input.entrySet().iterator();
        
nextkey:        
        while (i.hasNext())
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

    private String findValueCaselessly(String key, Map map)
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
    
    private static final String[] CONVERSIONS = {
            "&lt;", "<",
            "&gt;", ">",
            "&quot;", "\"",
            "&amp;", "&"
    };
    
    /**
     *  Provided a raw input string that has been recognized to be an expression
     *  (starts with "[[" and ends with "]]"), this removes the outer brackets
     *  and excess white space and converts &amp;amp;, &amp;quot; &amp;lt; and &amp;gt;
     *  to their normal character values (otherwise its impossible to specify
     *  those values in expressions in the template).
     * 
     **/
    
    private String extractExpression(String input)
    {
        int inputLength = input.length() - 2;
        
        StringBuffer buffer = new StringBuffer(inputLength - 2);

        int cursor = 2;
        

        while (cursor < inputLength)
        {
outer: 
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
                    break outer;
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
}