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
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.markup.MarkupWriterImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(sequential=true)
public class TestMarkupWriter extends BaseComponentTestCase
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
        return newMock(MarkupFilter.class);
    }

    private PrintWriter newPrintWriter()
    {
        _writer = new CharArrayWriter();

        return new PrintWriter(_writer);
    }

    @AfterClass
    protected void tearDown() throws Exception
    {
        _writer = null;
    }

    private void assertOutput(String expected)
    {
        assertEquals(_writer.toString(), expected);

        _writer.reset();
    }

    public void testIntAttribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.attribute("width", 5);
        mw.end();

        verify();
        
        assertOutput("<span width=\"{5}\"></span>");
    }

    public void testIntAttributeRequiresTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

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

        verify();
    }

    public void testBooleanAttribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("div");
        mw.attribute("true", true);
        mw.attribute("false", false);
        
        mw.end();
        
        verify();
        
        assertOutput("<div true=\"{true}\" false=\"{false}\"></div>");
    }

    public void testBooleanAttributeRequiresTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

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

        verify();
    }

    public void testAttribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.attribute("width", "100%");
        mw.end();
        
        // Braces added by EchoMarkupFilter, to prove its there.

        assertOutput("<span width=\"{100%}\"></span>");
    }

    public void testAttributeNull()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.attribute("width", null);
        mw.end();
        
        // Braces added by EchoMarkupFilter, to prove its there.
        
        assertOutput("<span width=\"\"></span>");
        
        verify();
    }
    
    public void test_Duplicate_Attributes()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.attribute("width", "100%");
        mw.attribute("width", "80%");
        mw.end();
        
        assertOutput("<span width=\"{80%}\"></span>");
    }
    
    public void testAttributeRequiresTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

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

        verify();
    }

    public void test_Append_Attribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.appendAttribute("class", "fred");
        mw.appendAttribute("class", "barney");
        mw.appendAttribute("type", false);
        mw.end();
        
        assertOutput("<span class=\"{fred barney}\" type=\"{false}\"></span>");
    }
    
    public void test_Append_Attribute_Null()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.appendAttribute("class", "fred");
        mw.appendAttribute("class", null);
        mw.end();
        
        assertOutput("<span class=\"{fred}\"></span>");
    }
    
    public void test_Append_Attribute_Raw()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.appendAttributeRaw("class", null);
        mw.appendAttributeRaw("type", "&lt;&gt;");
        mw.end();
        
        assertOutput("<span class=\"\" type=\"&lt;&gt;\"></span>");
    }
    
    public void test_Get_Attribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.appendAttribute("class", "fred");
        
        assertNotNull(mw.getAttribute("class"));
        assertEquals(mw.getAttribute("class").toString(), "fred");
        
        mw.end();
        
        assertOutput("<span class=\"{fred}\"></span>");
    }
    
    public void test_Has_Attribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.appendAttribute("class", "fred");
        
        assertTrue(mw.hasAttribute("class"));
        assertEquals(mw.getAttribute("class").toString(), "fred");
        
        mw.end();
        
        assertOutput("<span class=\"{fred}\"></span>");
    }
    
    public void test_Remove_Attribute()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.appendAttribute("class", "fred");
        
        assertTrue(mw.hasAttribute("class"));
        
        assertEquals(mw.removeAttribute("class").toString(), "fred");
        
        assertFalse(mw.hasAttribute("class"));
        
        mw.end();
        
        assertOutput("<span></span>");
    }
    
    public void test_Clear_Attributes()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        
        mw.begin("span");
        mw.attribute("class", "fred");
        mw.attribute("barney", "bam bam");
        
        assertTrue(mw.hasAttribute("barney"));
        mw.clearAttributes();
        
        assertFalse(mw.hasAttribute("barney"));
        assertFalse(mw.hasAttribute("class"));
        
        mw.end();
        
        assertOutput("<span></span>");
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

    public void testPrintWithRawFlag()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.print("Filtered", false);

        assertOutput("{Filtered}");

        mw.print("Raw", true);

        assertOutput("Raw");
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

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("span");
        mw.print(null);
        mw.end();

        assertOutput("<span></span>");

        verify();
    }

    public void testCloseTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.closeTag();

        assertOutput("<div>");

        mw.beginEmpty("img");
        mw.closeTag();

        assertOutput("<img/>");

        verify();
    }

    public void testNestedEnd()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

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

        verify();
    }

    public void testEndNamed()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

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

        verify();
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

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.comment("Tapestry Rocks!");

        // Note: not filtered

        assertOutput("<!-- Tapestry Rocks! -->" + NEWLINE);

        verify();
    }

    public void testCommentClosesTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("div");
        mw.comment("Tapestry Rocks!");

        // Note: not filtered

        assertOutput("<div><!-- Tapestry Rocks! -->" + NEWLINE);

        verify();
    }
    
    public void testFlush()
    {
        _writer = new CharArrayWriter();

        MarkupFilter filter = newFilter();
        PrintWriter writer = org.easymock.classextension.EasyMock.createMock(PrintWriterFixture.class);

        writer.flush();
        
        replay();
        org.easymock.classextension.EasyMock.replay(writer);
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.flush();

        verify();
        org.easymock.classextension.EasyMock.verify(writer);
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

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.println();

        assertOutput(NEWLINE);

        verify();
    }

    public void testPrintLnClosesTag()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("p");

        assertOutput("<p");

        mw.println();

        assertOutput(">" + NEWLINE);

        verify();
    }

    public void testPrintRawCharArray()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("p");

        assertOutput("<p");

        mw.printRaw(new char[]
        { 'a', 'b', 'c', 'd' }, 1, 2);

        assertOutput(">bc");

        verify();
    }

    public void testPrintRawString()
    {
        MarkupFilter filter = newFilter();
        PrintWriter writer = newPrintWriter();

        replay();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        mw.begin("p");

        assertOutput("<p");

        mw.print("Fred", true);

        assertOutput(">Fred");

        verify();
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
        
        nested.close();

        // Close the <div>, then comes the inner/nested content.
        
        mw.print("after content");
        mw.end();
        
        assertOutput("<div class=\"{outer}\"><span class=\"{inner}\">{nested content}</span>{after content}</div>");
    }

    public void testRepeatCloseOnNestedWriter()
    {
        MarkupFilter filter = new EchoMarkupFilter();
        PrintWriter writer = newPrintWriter();

        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);

        IMarkupWriter nested = mw.getNestedWriter();

        nested.close();

        try
        {
            nested.close();
        }
        catch (IllegalStateException ex)
        {
            // Expected.
        }
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