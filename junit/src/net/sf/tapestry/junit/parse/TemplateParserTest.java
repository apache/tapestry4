package net.sf.tapestry.junit.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.tapestry.parse.ITemplateParserDelegate;
import net.sf.tapestry.parse.TemplateParseException;
import net.sf.tapestry.parse.TemplateParser;
import net.sf.tapestry.parse.TemplateToken;
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
    private TemplateParser parser;

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
    }

    public TemplateParserTest(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        parser = new TemplateParser();
    }

    protected void tearDown()
    {
        parser = null;
    }

    protected TemplateToken[] run(
        char[] templateData,
        ITemplateParserDelegate delegate,
        String resourcePath)
        throws TemplateParseException
    {
        return parser.parse(templateData, delegate, resourcePath);
    }

    protected TemplateToken[] run(
        InputStream stream,
        ITemplateParserDelegate delegate,
        String resourcePath)
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

    protected TemplateToken[] run(String file, ITemplateParserDelegate delegate)
        throws TemplateParseException
    {
        InputStream stream = getClass().getResourceAsStream(file);

        if (stream == null)
            throw new TemplateParseException("File " + file + " not found.");

        return run(stream, delegate, file);
    }

    protected void assertTextToken(TemplateToken token, int startIndex, int endIndex)
    {
        assertEquals("Text token type.", TokenType.TEXT, token.getType());
        assertEquals("Text token start index.", startIndex, token.getStartIndex());
        assertEquals("Text token end index.", endIndex, token.getEndIndex());
    }

    /** @since 2.0.4 **/

    protected void assertLocalizationToken(TemplateToken token, String key, Map attributes)
    {
        assertEquals("Localization token type.", TokenType.LOCALIZATION, token.getType());
        assertEquals("Localization key.", key, token.getId());

        assertEquals("Localization attributes.", attributes, token.getAttributes());
    }

    protected void assertOpenToken(TemplateToken token, String id)
    {
        assertEquals("Open token type.", TokenType.OPEN, token.getType());
        assertEquals("Open token id.", id, token.getId());
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
        assertOpenToken(tokens[1], "emptyTag");
        assertCloseToken(tokens[2]);
        assertTextToken(tokens[3], 63, 102);
    }

    public void testSimpleNested() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SimpleNested.html");

        assertTokenCount(tokens, 8);
        assertOpenToken(tokens[1], "outer");
        assertOpenToken(tokens[3], "inner");
        assertCloseToken(tokens[4]);
        assertCloseToken(tokens[6]);
    }

    public void testMixedNesting() throws TemplateParseException
    {
        TemplateToken[] tokens = run("MixedNesting.html");

        assertTokenCount(tokens, 5);
        assertOpenToken(tokens[1], "row");
        assertCloseToken(tokens[3]);
    }

    public void testSingleQuotes() throws TemplateParseException
    {
        TemplateToken[] tokens = run("SingleQuotes.html");

        assertTokenCount(tokens, 7);
        assertOpenToken(tokens[1], "first");
        assertOpenToken(tokens[4], "second");
    }

    public void testComplex() throws TemplateParseException
    {
        TemplateToken[] tokens = run("Complex.html");

        assertTokenCount(tokens, 19);

        // Just pick a few highlights out of it.

        assertOpenToken(tokens[1], "ifData");
        assertOpenToken(tokens[3], "e");
        assertOpenToken(tokens[5], "row");
    }

    public void testStartWithStaticTag() throws TemplateParseException
    {
        TemplateToken[] tokens = run("StartWithStaticTag.html");

        assertTokenCount(tokens, 4);
        assertTextToken(tokens[0], 0, 232);
        assertOpenToken(tokens[1], "justBecause");
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

            public boolean getAllowBody(String componentId)
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
        assertOpenToken(tokens[2], "e");
        assertTextToken(tokens[3], 341, 342);
        assertOpenToken(tokens[4], "row");
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
        };

        TemplateToken[] tokens = run("BodyRemove.html", delegate);

        assertTokenCount(tokens, 8);
        assertOpenToken(tokens[1], "form");
        assertOpenToken(tokens[3], "inputType");
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
        assertOpenToken(tokens[1], "nested");
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
        assertOpenToken(tokens[1], "tag");

        Map a = tokens[1].getAttributes();

        assertEquals("Attribute count", 3, a.size());
        assertEquals("zip", a.get("class"));
        assertEquals("right", a.get("align"));
        assertEquals("#ff00ff", a.get("color"));
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
    
    public void testEmptyLocalization()
    throws TemplateParseException
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
    
    public void testLocalizationAttributes()
    throws TemplateParseException
    {
           TemplateToken[] tokens = run("LocalizationAttributes.html");     
           
           Map attributes = new HashMap();
           attributes.put("alpha", "beta");
           attributes.put("Fred", "Wilma");
           
           assertLocalizationToken(tokens[1], "localization.with.attributes", attributes);
    }

}