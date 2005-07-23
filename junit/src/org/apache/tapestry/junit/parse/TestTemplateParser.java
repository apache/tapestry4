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

package org.apache.tapestry.junit.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.parse.AttributeType;
import org.apache.tapestry.parse.ITemplateParserDelegate;
import org.apache.tapestry.parse.LocalizationToken;
import org.apache.tapestry.parse.OpenToken;
import org.apache.tapestry.parse.TemplateAttribute;
import org.apache.tapestry.parse.TemplateParseException;
import org.apache.tapestry.parse.TemplateParser;
import org.apache.tapestry.parse.TemplateToken;
import org.apache.tapestry.parse.TextToken;
import org.apache.tapestry.parse.TokenType;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 *  Tests for the Tapestry HTML template parser.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TestTemplateParser extends TestCase
{
    private static class ParserDelegate implements ITemplateParserDelegate
    {
        public boolean getKnownComponent(String componentId)
        {
            return true;
        }

        public boolean getAllowBody(String componentId, ILocation location)
        {
            return true;
        }

        public boolean getAllowBody(String libraryId, String type, ILocation location)
        {
            return true;
        }

    }

    public TestTemplateParser(String name)
    {
        super(name);
    }

    protected TemplateToken[] run(
        char[] templateData,
        ITemplateParserDelegate delegate,
        IResourceLocation location)
        throws TemplateParseException
    {
        return new TemplateParser().parse(templateData, delegate, location);
    }

    protected TemplateToken[] run(
        InputStream stream,
        ITemplateParserDelegate delegate,
        IResourceLocation location)
        throws TemplateParseException
    {
        StringBuffer buffer = new StringBuffer();
        char[] block = new char[1000];
        InputStreamReader reader = new InputStreamReader(stream);

        try
        {
            while (true)
            {
                int count = reader.read(block, 0, block.length);

                if (count < 0)
                    break;

                buffer.append(block, 0, count);
            }

            reader.close();
        }
        catch (IOException ex)
        {
            fail("Unable to read from stream.");
        }

        return run(buffer.toString().toCharArray(), delegate, location);
    }

    protected TemplateToken[] run(String file) throws TemplateParseException
    {
        return run(file, new ParserDelegate());
    }

    protected TemplateToken[] run(String file, ITemplateParserDelegate delegate)
        throws TemplateParseException
    {
        String thisClassName = getClass().getName();
        String thisPath = "/" + thisClassName.replace('.', '/') + "/" + file;

        IResourceLocation location =
            new ClasspathResourceLocation(new DefaultResourceResolver(), thisPath);

        InputStream stream = getClass().getResourceAsStream(file);

        if (stream == null)
            throw new TemplateParseException("File " + file + " not found.");

        return run(stream, delegate, location);
    }

    private Map buildMap(String[] input)
    {
        Map result = new HashMap();

        for (int i = 0; i < input.length; i += 2)
            result.put(input[i], input[i + 1]);

        return result;
    }

    protected void assertTextToken(TemplateToken token, int startIndex, int endIndex)
    {
        TextToken t = (TextToken) token;

        assertEquals("Text token type.", TokenType.TEXT, t.getType());
        assertEquals("Text token start index.", startIndex, t.getStartIndex());
        assertEquals("Text token end index.", endIndex, t.getEndIndex());
    }

    /** @since 3.0 **/

    protected void checkLine(TemplateToken token, int line)
    {
        assertEquals("Token line", line, token.getLocation().getLineNumber());
    }

    /** @since 2.0.4 **/

    protected void assertLocalizationToken(
        TemplateToken token,
        String key,
        Map attributes,
        int line)
    {
        LocalizationToken t = (LocalizationToken) token;

        assertEquals("Localization token type.", TokenType.LOCALIZATION, t.getType());
        assertEquals("Localization key.", key, t.getKey());

        assertEquals("Localization attributes.", attributes, t.getAttributes());

        checkLine(token, line);
    }

    protected void assertOpenToken(TemplateToken token, String id, String tag, int line)
    {
        assertOpenToken(token, id, null, tag, line);
    }

    protected void assertOpenToken(
        TemplateToken token,
        String id,
        String componentType,
        String tag,
        int line)
    {
        OpenToken t = (OpenToken) token;

        assertEquals("Open token type", TokenType.OPEN, t.getType());
        assertEquals("Open token id", id, t.getId());
        assertEquals("Open token component type", componentType, t.getComponentType());
        assertEquals("Open token tag", tag, t.getTag());

        checkLine(token, line);
    }

    protected void assertTemplateAttributes(TemplateToken token, AttributeType type, Map expected)
    {
        OpenToken t = (OpenToken) token;

        Map attributes = t.getAttributesMap();

        Map actual = null;

        if (attributes != null)
        {
            actual = new HashMap();
            Iterator i = attributes.entrySet().iterator();
            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry) i.next();

                TemplateAttribute attribute = (TemplateAttribute) entry.getValue();

                if (attribute.getType() == type)
                    actual.put(entry.getKey(), attribute.getValue());
            }
        }

        assertEquals(type.getName() + " attributes", expected, actual);
    }

    protected void assertCloseToken(TemplateToken token, int line)
    {
        assertEquals("Close token type.", TokenType.CLOSE, token.getType());

        checkLine(token, line);
    }

    protected void assertTokenCount(TemplateToken[] tokens, int count)
    {
        assertNotNull("Parsed tokens.", tokens);
        assertEquals("Parsed token count.", count, tokens.length);
    }

    private void runFailure(String file, String message)
    {
        runFailure(file, new ParserDelegate(), message);
    }

    private void runFailure(String file, ITemplateParserDelegate delegate, String message)
    {
        try
        {
            run(file, delegate);

            fail("Invalid document " + file + " parsed without exception.");
        }
        catch (TemplateParseException ex)
        {
            assertEquals(message, ex.getMessage());
            assertTrue(ex.getLocation().toString().indexOf(file) > 0);
        }
    }

    public void testAllStatic() throws TemplateParseException
    {
        TemplateToken[] tokens = run("AllStatic.html");

        assertTokenCount(tokens, 1);
        assertTextToken(tokens[0], 0, 172);
    }

    public void testSingleEmptyTag() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SingleEmptyTag.html");

        assertTokenCount(tokens, 4);

        assertTextToken(tokens[0], 0, 38);
        assertOpenToken(tokens[1], "emptyTag", "span", 3);
        assertCloseToken(tokens[2], 3);
        assertTextToken(tokens[3], 63, 102);
    }

    public void testSimpleNested() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SimpleNested.html");

        assertTokenCount(tokens, 8);
        assertOpenToken(tokens[1], "outer", "span", 3);
        assertOpenToken(tokens[3], "inner", "span", 4);
        assertCloseToken(tokens[4], 4);
        assertCloseToken(tokens[6], 5);
    }

    public void testMixedNesting() throws TemplateParseException
    {
        TemplateToken[] tokens = run("MixedNesting.html");

        assertTokenCount(tokens, 5);
        assertOpenToken(tokens[1], "row", "span", 4);
        assertCloseToken(tokens[3], 7);
    }

    public void testSingleQuotes() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SingleQuotes.html");

        assertTokenCount(tokens, 7);
        assertOpenToken(tokens[1], "first", "span", 5);
        assertOpenToken(tokens[4], "second", "span", 7);
    }

    public void testComplex() throws TemplateParseException
    {
        TemplateToken[] tokens = run("Complex.html");

        assertTokenCount(tokens, 19);

        // Just pick a few highlights out of it.

        assertOpenToken(tokens[1], "ifData", "span", 3);
        assertOpenToken(tokens[3], "e", "span", 10);
        assertOpenToken(tokens[5], "row", "tr", 11);
    }

    public void testStartWithStaticTag() throws TemplateParseException
    {
        TemplateToken[] tokens = run("StartWithStaticTag.html");

        assertTokenCount(tokens, 4);
        assertTextToken(tokens[0], 0, 232);
        assertOpenToken(tokens[1], "justBecause", "span", 9);
    }

    public void testUnterminatedCommentFailure()
    {
        runFailure("UnterminatedComment.html", "Comment on line 3 did not end.");
    }
    
    public void testDuplicateTagAttributeFailure()
    {
        runFailure("DuplicateTagAttribute.html", "Tag <input> on line 3 contains more than one 'value' attribute.");
    }
    
    public void testDuplicateTagAttributeFailureII()
    {
        runFailure("DuplicateTagAttributeII.html", "Tag <input> on line 3 contains more than one 'value' attribute.");
    }

    public void testUnclosedOpenTagFailure()
    {
        runFailure("UnclosedOpenTag.html", "Tag <body> on line 4 is never closed.");
    }

    public void testMissingAttributeValueFailure()
    {
        runFailure(
            "MissingAttributeValue.html",
            "Tag <img> on line 9 is missing a value for attribute src.");
    }

    public void testIncompleteCloseFailure()
    {
        runFailure("IncompleteClose.html", "Incomplete close tag on line 6.");
    }

    public void testMismatchedCloseTagsFailure()
    {
        runFailure(
            "MismatchedCloseTags.html",
            "Closing tag </th> on line 9 does not have a matching open tag.");
    }

    public void testInvalidDynamicNestingFailure()
    {
        runFailure(
            "InvalidDynamicNesting.html",
            "Closing tag </body> on line 12 is improperly nested with tag <span> on line 8.");
    }

    public void testUnknownComponentIdFailure()
    {
        ITemplateParserDelegate delegate = new ITemplateParserDelegate()
        {
            public boolean getKnownComponent(String componentId)
            {
                return !componentId.equals("row");
            }

            public boolean getAllowBody(String componentId, ILocation location)
            {
                return true;
            }

            public boolean getAllowBody(String libraryId, String type, ILocation location)
            {
                return true;
            }
        };

        runFailure(
            "Complex.html",
            delegate,
            "Tag <tr> on line 11 references unknown component id 'row'.");
    }

    public void testBasicRemove() throws TemplateParseException
    {
        TemplateToken[] tokens = run("BasicRemove.html");

        assertTokenCount(tokens, 10);
        assertTextToken(tokens[0], 0, 119);
        assertTextToken(tokens[1], 188, 268);
        assertOpenToken(tokens[2], "e", "span", 23);
        assertTextToken(tokens[3], 341, 342);
        assertOpenToken(tokens[4], "row", "tr", 24);
        assertTextToken(tokens[5], 359, 377);
        assertCloseToken(tokens[6], 26);
        assertTextToken(tokens[7], 383, 383);
        assertCloseToken(tokens[8], 27);
        assertTextToken(tokens[9], 391, 401);
    }

    public void testBodyRemove() throws TemplateParseException
    {
        ITemplateParserDelegate delegate = new ITemplateParserDelegate()
        {
            public boolean getKnownComponent(String id)
            {
                return true;
            }

            public boolean getAllowBody(String id, ILocation location)
            {
                return id.equals("form");
            }

            public boolean getAllowBody(String libraryId, String type, ILocation location)
            {
                return true;
            }
        };

        TemplateToken[] tokens = run("BodyRemove.html", delegate);

        assertTokenCount(tokens, 8);
        assertOpenToken(tokens[1], "form", "form", 9);
        assertOpenToken(tokens[3], "inputType", "select", 11);
        assertCloseToken(tokens[4], 15);
        assertCloseToken(tokens[6], 16);
    }

    public void testRemovedComponentFailure()
    {
        runFailure(
            "RemovedComponent.html",
            "Tag <span> on line 5 is a dynamic component, and may not appear inside an ignored block.");
    }

    public void testNestedRemoveFailure()
    {
        runFailure(
            "NestedRemove.html",
            "Tag <span> on line 4 should be ignored, but is already inside "
                + "an ignored block (ignored blocks may not be nested).");
    }

    public void testBasicContent() throws TemplateParseException
    {
        TemplateToken[] tokens = run("BasicContent.html");

        assertTokenCount(tokens, 4);
        assertTextToken(tokens[0], 108, 165);
        assertOpenToken(tokens[1], "nested", "span", 9);
        assertCloseToken(tokens[2], 9);
        assertTextToken(tokens[3], 188, 192);
    }

    public void testIgnoredContentFailure()
    {
        runFailure(
            "IgnoredContent.html",
            "Tag <td> on line 7 is the template content, and may not be in an ignored block.");
    }

    public void testTagAttributes() throws TemplateParseException
    {
        TemplateToken[] tokens = run("TagAttributes.html");

        assertTokenCount(tokens, 5);
        assertOpenToken(tokens[1], "tag", null, "span", 3);

        assertTemplateAttributes(
            tokens[1],
            AttributeType.LITERAL,
            buildMap(new String[] { "class", "zip", "align", "right", "color", "#ff00ff" }));

    }

    /**
     *   @since 2.0.4
     * 
     **/

    public void testBasicLocalization() throws TemplateParseException
    {
        TemplateToken[] tokens = run("BasicLocalization.html");

        assertTokenCount(tokens, 3);
        assertTextToken(tokens[0], 0, 35);
        assertLocalizationToken(tokens[1], "the.localization.key", null, 3);
        assertTextToken(tokens[2], 89, 117);
    }

    /**
     * 
     *  Test that the parser fails if a localization block contains
     *  a component.
     * 
     *  @since 2.0.4
     * 
     **/

    public void testComponentInsideLocalization()
    {
        runFailure(
            "ComponentInsideLocalization.html",
            "Tag <span> on line 9 is a dynamic component, and may not appear inside an ignored block.");
    }

    /**
     *  Test that the parser fails if an invisible localization is
     *  nested within another invisible localization.
     * 
     *  @since 2.0.4
     * 
     **/

    public void testNestedLocalizations()
    {
        runFailure(
            "NestedLocalizations.html",
            "Tag <span> on line 4 is a dynamic component, and may not appear inside an ignored block.");
    }

    /**
     *  Test that the abbreviated form (a tag with no body) works.
     * 
     *  @since 2.0.4
     * 
     **/

    public void testEmptyLocalization() throws TemplateParseException
    {
        TemplateToken[] tokens = run("EmptyLocalization.html");

        assertTokenCount(tokens, 3);
        assertTextToken(tokens[0], 0, 62);
        assertLocalizationToken(tokens[1], "empty.localization", null, 3);
        assertTextToken(tokens[2], 97, 122);
    }

    /**
     *  Test attributes in the span.  Also, checks that the parser
     *  caselessly identifies the "key" attribute and the tag name ("span").
     * 
     *  @since 2.0.4
     * 
     **/

    public void testLocalizationAttributes() throws TemplateParseException
    {
        TemplateToken[] tokens = run("LocalizationAttributes.html");

        Map attributes = buildMap(new String[] { "alpha", "beta", "Fred", "Wilma" });

        assertLocalizationToken(tokens[1], "localization.with.attributes", attributes, 3);
    }

    /**
     *  Tests for implicit components (both named and anonymous).
     * 
     *  @since 3.0
     * 
     **/

    public void testImplicitComponents() throws TemplateParseException
    {
        TemplateToken[] tokens = run("ImplicitComponents.html");

        assertTokenCount(tokens, 18);

        assertOpenToken(tokens[1], "$Body", "Body", "body", 4);
        assertOpenToken(tokens[3], "loop", "Foreach", "tr", 7);

        assertTemplateAttributes(
            tokens[3],
            AttributeType.LITERAL,
            buildMap(new String[] { "element", "tr" }));

        assertTemplateAttributes(
            tokens[3],
            AttributeType.OGNL_EXPRESSION,
            buildMap(new String[] { "source", "items" }));

        assertOpenToken(tokens[5], "$Insert", "Insert", "span", 10);

        assertTemplateAttributes(
            tokens[5],
            AttributeType.OGNL_EXPRESSION,
            buildMap(new String[] { "value", "components.loop.value.name" }));

        assertOpenToken(tokens[8], "$Insert$0", "Insert", "span", 11);

        assertTemplateAttributes(
            tokens[8],
            AttributeType.OGNL_EXPRESSION,
            buildMap(new String[] { "value", "components.loop.value.price" }));

        assertOpenToken(tokens[13], "$InspectorButton", "contrib:InspectorButton", "span", 15);
    }

    /**
     *  Test for encoded characters in an expression.
     * 
     *  @since 3.0
     * 
     **/

    public void testEncodedExpressionCharacters() throws TemplateParseException
    {
        TemplateToken[] tokens = run("EncodedExpressionCharacters.html");

        assertTokenCount(tokens, 4);

        assertOpenToken(tokens[1], "$Insert", "Insert", "span", 2);

        String expression = "{ \"<&>\", \"Fun!\" }";

        assertTemplateAttributes(
            tokens[1],
            AttributeType.OGNL_EXPRESSION,
            buildMap(new String[] { "value", expression }));

    }

    /**
     *  Test ability to read string attributes.
     * 
     **/

    public void testStringAttributes() throws TemplateParseException
    {
        TemplateToken[] tokens = run("StringAttributes.html");

        assertTokenCount(tokens, 4);

        assertOpenToken(tokens[1], "$Image", "Image", "img", 3);

        assertTemplateAttributes(
            tokens[1],
            AttributeType.OGNL_EXPRESSION,
            buildMap(new String[] { "image", "assets.logo" }));

        assertTemplateAttributes(
            tokens[1],
            AttributeType.LOCALIZATION_KEY,
            buildMap(new String[] { "alt", "logo-title" }));

    }

}