// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.markup;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IMarkupWriter;

/**
 * Tests for {@link org.apache.tapestry.markup.MarkupWriterImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestMarkupWriter extends HiveMindTestCase
{
    private static CharArrayWriter _writer;

    private static final String NEWLINE = System.getProperty("line.separator");

    private static class EchoMarkupFilter implements MarkupFilter
    {
        public void print(PrintWriter writer, char[] data, int offset, int length,
                boolean escapeQuotes)
        {
            writer.print('{');
            writer.write(data, offset, length);
            writer.print('}');
        }
    }

    public static class PrintWriterFixture extends PrintWriter
    {

        public PrintWriterFixture()
        {
            super(_writer);
        }
    }

    private MarkupFilter newFilter()
    {
        return (MarkupFilter) newMock(MarkupFilter.class);
    }

    private PrintWriter newPrintWriter()
    {
        _writer = new CharArrayWriter();

        return new PrintWriter(_writer);
    }

    protected void tearDown() throws Exception
    {
        _writer = null;

        super.tearDown();
    }

    private void assertOutput(String expected)
    {
        assertEquals(expected, _writer.toString());

        _writer.reset();
    }

    public void testIntAttribute()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");

        assertOutput("<span");

        mw.attribute("width", 5);

        assertOutput(" width=\"5\"");

        verifyControls();
    }

    public void testIntAttributeRequiresTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        try
        {
            mw.attribute("width", 5);
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testBooleanAttribute()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");

        assertOutput("<div");

        mw.attribute("true", true);

        assertOutput(" true=\"true\"");

        mw.attribute("false", false);

        assertOutput(" false=\"false\"");

        verifyControls();
    }

    public void testBooleanAttributeRequiresTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        try
        {
            mw.attribute("true", true);
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testAttribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.attribute("width", "100%");

        // Braces added by EchoMarkupFilter, to prove its there.

        assertOutput("<span width=\"{100%}\"");
    }

    public void testAttributeNull()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.attribute("width", null);

        // Braces added by EchoMarkupFilter, to prove its there.

        assertOutput("<span width=\"\"");

        verifyControls();
    }

    public void testAttributeRequiresTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        try
        {
            mw.attribute("attribute", "value");
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testEnd()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.attribute("width", "100%");
        mw.end();

        assertOutput("<div width=\"{100%}\"></div>");
    }

    public void testPrint()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.print("Hello");

        assertOutput("{Hello}");
    }

    public void testPrintClosesCurrentTag()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.print("Hello");
        mw.end();

        assertOutput("<div>{Hello}</div>");
    }

    public void testPrintNull()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.print(null);
        mw.end();

        assertOutput("<span></span>");

        verifyControls();
    }

    public void testCloseTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.closeTag();

        assertOutput("<div>");

        mw.beginEmpty("img");
        mw.closeTag();

        assertOutput("<img/>");

        verifyControls();
    }

    public void testNestedEnd()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("h1");
        mw.begin("b");
        mw.beginEmpty("img");
        mw.begin("span");
        mw.closeTag();

        assertOutput("<h1><b><img/><span>");

        mw.end();

        assertOutput("</span>");

        mw.end();

        // Note: skipping the empty <img> tag

        assertOutput("</b>");

        mw.end();

        assertOutput("</h1>");

        verifyControls();
    }

    public void testEndNamed()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("h1");
        mw.begin("b");
        mw.beginEmpty("img");
        mw.begin("span");
        mw.closeTag();

        assertOutput("<h1><b><img/><span>");

        // Uses the stack to close elements.

        mw.end("h1");

        assertOutput("</span></b></h1>");

        verifyControls();
    }

    public void testClose()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.begin("div");
        mw.print("text");

        assertOutput("<span><div>{text}");

        mw.close();

        assertOutput("</div></span>");

        assertEquals(false, writer.checkError());

        writer.println();

        assertEquals(true, writer.checkError());
    }

    public void testComment()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.comment("Tapestry Rocks!");

        // Note: not filtered

        assertOutput("<!-- Tapestry Rocks! -->" + NEWLINE);

        verifyControls();
    }

    public void testCommentClosesTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.comment("Tapestry Rocks!");

        // Note: not filtered

        assertOutput("<div><!-- Tapestry Rocks! -->" + NEWLINE);

        verifyControls();
    }

    public void testFlush()
    {
        _writer = new CharArrayWriter();

        MarkupFilter filter = newFilter();
        PrintWriter writer = (PrintWriter) newMock(PrintWriterFixture.class);

        writer.flush();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.flush();

        verifyControls();
    }

    public void testPrintCharArray()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.print(new char[]
        { 'A', 'j', 'a', 'x', 'i', 'a', 'n' }, 1, 3);

        assertOutput("{jax}");
    }

    public void testPrintChar()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.print('x');

        assertOutput("{x}");
    }

    public void testPrintInt()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.print(123);

        assertOutput("123");
    }

    public void testPrintIntClosesTag()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");

        assertOutput("<span");

        mw.print(123);

        assertOutput(">123");
    }

    public void testPrintLn()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.println();

        assertOutput(NEWLINE);

        verifyControls();
    }

    public void testPrintLnClosesTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("p");

        assertOutput("<p");

        mw.println();

        assertOutput(">" + NEWLINE);

        verifyControls();
    }

    public void testPrintRawCharArray()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("p");

        assertOutput("<p");

        mw.printRaw(new char[]
        { 'a', 'b', 'c', 'd' }, 1, 2);

        assertOutput(">bc");

        verifyControls();
    }

    public void testPrintRawString()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replayControls();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("p");

        assertOutput("<p");

        mw.printRaw("Fred");

        assertOutput(">Fred");

        verifyControls();
    }

    public void testNestedWriter()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");

        IMarkupWriter nested = mw.getNestedWriter();

        assertEquals("text/html", nested.getContentType());

        nested.begin("span");
        nested.attribute("class", "inner");
        nested.print("nested content");

        mw.attribute("class", "outer");

        assertOutput("<div class=\"{outer}\"");

        nested.close();

        // Close the <div>, then comes the inner/nested content.

        assertOutput("><span class=\"{inner}\">{nested content}</span>");

        mw.print("after content");

        assertOutput("{after content}");

        mw.end();

        assertOutput("</div>");
    }

    public void testEndNamedNotOnStack()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.begin("span");
        mw.begin("b");

        try
        {
            mw.end("table");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Can not close to element 'table', because no such element is on the active elements stack (div, span, b).",
                    ex.getMessage());
        }
    }

    public void testEndWithEmptyStack()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        try
        {
            mw.end();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Can not end most recent element because the stack of active elements is empty.",
                    ex.getMessage());
        }
    }
}