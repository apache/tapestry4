/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.junit.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.tapestry.parse.ITemplateParserDelegate;
import net.sf.tapestry.parse.LocalizationToken;
import net.sf.tapestry.parse.OpenToken;
import net.sf.tapestry.parse.TemplateParseException;
import net.sf.tapestry.parse.TemplateParser;
import net.sf.tapestry.parse.TemplateToken;
import net.sf.tapestry.parse.TextToken;
import net.sf.tapestry.parse.TokenType;

/**
 *  Tests for the Tapestry HTML template parser.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TemplateParserTest extends TestCase
{
    private static class ParserDelegate implements ITemplateParserDelegate
    {
        public boolean getKnownComponent(String componentId)
        {
            return true;
        }

        public boolean getAllowBody(String componentId)
        {
            return true;
        }

        public boolean getAllowBody(String libraryId, String type)
        {
            return true;
        }

    }

    public TemplateParserTest(String name)
    {
        super(name);
    }

    protected TemplateToken[] run(char[] templateData, ITemplateParserDelegate delegate, String resourcePath)
        throws TemplateParseException
    {
        return new TemplateParser().parse(templateData, delegate, resourcePath);
    }

    protected TemplateToken[] run(InputStream stream, ITemplateParserDelegate delegate, String resourcePath)
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

        return run(buffer.toString().toCharArray(), delegate, resourcePath);
    }

    protected TemplateToken[] run(String file) throws TemplateParseException
    {
        return run(file, new ParserDelegate());
    }

    protected TemplateToken[] run(String file, ITemplateParserDelegate delegate) throws TemplateParseException
    {
        InputStream stream = getClass().getResourceAsStream(file);

        if (stream == null)
            throw new TemplateParseException("File " + file + " not found.");

        return run(stream, delegate, file);
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

    /** @since 2.0.4 **/

    protected void assertLocalizationToken(TemplateToken token, String key, Map attributes)
    {
        LocalizationToken t = (LocalizationToken) token;

        assertEquals("Localization token type.", TokenType.LOCALIZATION, t.getType());
        assertEquals("Localization key.", key, t.getKey());

        assertEquals("Localization attributes.", attributes, t.getAttributes());
    }

    protected void assertOpenToken(TemplateToken token, String id, String tag)
    {
        assertOpenToken(token, id, null, tag, null, null);
    }

    protected void assertOpenToken(
        TemplateToken token,
        String id,
        String componentType,
        String tag,
        Map staticValues,
        Map expressionValues)
    {
        OpenToken t = (OpenToken) token;

        assertEquals("Open token type", TokenType.OPEN, t.getType());
        assertEquals("Open token id", id, t.getId());
        assertEquals("Open token component type", componentType, t.getComponentType());
        assertEquals("Open token tag", tag, t.getTag());
        assertEquals("Static values", staticValues, t.getStaticValuesMap());
        assertEquals("Expression values", expressionValues, t.getExpressionValuesMap());
    }

    protected void assertCloseToken(TemplateToken token)
    {
        assertEquals("Close token type.", TokenType.CLOSE, token.getType());
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
            assertEquals(file, ex.getResourcePath());
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
        assertOpenToken(tokens[1], "emptyTag", "span");
        assertCloseToken(tokens[2]);
        assertTextToken(tokens[3], 63, 102);
    }

    public void testSimpleNested() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SimpleNested.html");

        assertTokenCount(tokens, 8);
        assertOpenToken(tokens[1], "outer", "span");
        assertOpenToken(tokens[3], "inner", "span");
        assertCloseToken(tokens[4]);
        assertCloseToken(tokens[6]);
    }

    public void testMixedNesting() throws TemplateParseException
    {
        TemplateToken[] tokens = run("MixedNesting.html");

        assertTokenCount(tokens, 5);
        assertOpenToken(tokens[1], "row", "span");
        assertCloseToken(tokens[3]);
    }

    public void testSingleQuotes() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SingleQuotes.html");

        assertTokenCount(tokens, 7);
        assertOpenToken(tokens[1], "first", "span");
        assertOpenToken(tokens[4], "second", "span");
    }

    public void testComplex() throws TemplateParseException
    {
        TemplateToken[] tokens = run("Complex.html");

        assertTokenCount(tokens, 19);

        // Just pick a few highlights out of it.

        assertOpenToken(tokens[1], "ifData", "span");
        assertOpenToken(tokens[3], "e", "span");
        assertOpenToken(tokens[5], "row", "tr");
    }

    public void testStartWithStaticTag() throws TemplateParseException
    {
        TemplateToken[] tokens = run("StartWithStaticTag.html");

        assertTokenCount(tokens, 4);
        assertTextToken(tokens[0], 0, 232);
        assertOpenToken(tokens[1], "justBecause", "span");
    }

    public void testUnterminatedCommentFailure()
    {
        runFailure("UnterminatedComment.html", "Comment on line 3 did not end.");
    }

    public void testUnclosedOpenTagFailure()
    {
        runFailure("UnclosedOpenTag.html", "Tag <body> on line 4 is never closed.");
    }

    public void testMissingAttributeValueFailure()
    {
        runFailure("MissingAttributeValue.html", "Tag <img> on line 9 is missing a value for attribute src.");
    }

    public void testIncompleteCloseFailure()
    {
        runFailure("IncompleteClose.html", "Incomplete close tag on line 6.");
    }

    public void testMismatchedCloseTagsFailure()
    {
        runFailure("MismatchedCloseTags.html", "Closing tag </th> on line 9 does not have a matching open tag.");
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

            public boolean getAllowBody(String componentId)
            {
                return true;
            }

            public boolean getAllowBody(String libraryId, String type)
            {
                return true;
            }
        };

        runFailure("Complex.html", delegate, "Tag <tr> on line 11 references unknown component id 'row'.");
    }

    public void testBasicRemove() throws TemplateParseException
    {
        TemplateToken[] tokens = run("BasicRemove.html");

        assertTokenCount(tokens, 10);
        assertTextToken(tokens[0], 0, 119);
        assertTextToken(tokens[1], 188, 268);
        assertOpenToken(tokens[2], "e", "span");
        assertTextToken(tokens[3], 341, 342);
        assertOpenToken(tokens[4], "row", "tr");
        assertTextToken(tokens[5], 359, 377);
        assertCloseToken(tokens[6]);
        assertTextToken(tokens[7], 383, 383);
        assertCloseToken(tokens[8]);
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

            public boolean getAllowBody(String id)
            {
                return id.equals("form");
            }

            public boolean getAllowBody(String libraryId, String type)
            {
                return true;
            }
        };

        TemplateToken[] tokens = run("BodyRemove.html", delegate);

        assertTokenCount(tokens, 8);
        assertOpenToken(tokens[1], "form", "form");
        assertOpenToken(tokens[3], "inputType", "select");
        assertCloseToken(tokens[4]);
        assertCloseToken(tokens[6]);
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
        assertOpenToken(tokens[1], "nested", "span");
        assertCloseToken(tokens[2]);
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
        assertOpenToken(
            tokens[1],
            "tag",
            null,
            "span",
            buildMap(new String[] { "class", "zip", "align", "right", "color", "#ff00ff" }),
            null);
    }

    /**
     *   @since 2.0.4
     * 
     **/

    public void testBasicLocalization() throws TemplateParseException
    {
        TemplateToken[] tokens = run("BasicLocalization.html");

        assertTokenCount(tokens, 3);
        assertTextToken(tokens[0], 0, 37);
        assertLocalizationToken(tokens[1], "the.localization.key", null);
        assertTextToken(tokens[2], 93, 122);
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
        assertTextToken(tokens[0], 0, 64);
        assertLocalizationToken(tokens[1], "empty.localization", null);
        assertTextToken(tokens[2], 101, 127);
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

        assertLocalizationToken(tokens[1], "localization.with.attributes", attributes);
    }

    /**
     *  Tests for implicit components (both named and anonymous).
     * 
     *  @since 2.4
     * 
     **/

    public void testImplicitComponents() throws TemplateParseException
    {
        TemplateToken[] tokens = run("ImplicitComponents.html");

        assertTokenCount(tokens, 18);

        assertOpenToken(tokens[1], "$Body", "Body", "body", null, null);
        assertOpenToken(
            tokens[3],
            "loop",
            "Foreach",
            "tr",
            buildMap(new String[] { "element", "tr" }),
            buildMap(new String[] { "source", "items" }));
        assertOpenToken(
            tokens[5],
            "$Insert",
            "Insert",
            "span",
            null,
            buildMap(new String[] { "value", "components.loop.value.name" }));
        assertOpenToken(
            tokens[8],
            "$Insert_0",
            "Insert",
            "span",
            null,
            buildMap(new String[] { "value", "components.loop.value.price" }));
        assertOpenToken(tokens[13], "$InspectorButton", "contrib:InspectorButton", "span", null, null);
    }
}