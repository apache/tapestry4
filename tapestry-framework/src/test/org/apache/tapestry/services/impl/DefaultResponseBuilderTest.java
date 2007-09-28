// Copyright May 9, 2006 The Apache Software Foundation
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
package org.apache.tapestry.services.impl;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.markup.UTFMarkupFilter;
import org.apache.tapestry.services.ResponseBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link DefaultResponseBuilder}.
 * 
 * @author jkuhnert
 */
@SuppressWarnings("cast")
@Test(sequential=true)
public class DefaultResponseBuilderTest extends BaseComponentTestCase
{

    private static CharArrayWriter _writer;
    
    private static String LINE_SEPARATOR = (String)java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
    
    private PrintWriter newPrintWriter()
    {
        _writer = new CharArrayWriter();

        return new PrintWriter(_writer);
    }

    @AfterClass(alwaysRun=true)
    protected void cleanup() throws Exception
    {
        _writer = null;
    }

    private void assertOutput(String expected)
    {
        assertEquals(_writer.toString(), expected);
        
        _writer.reset();
    }
    
    public void test_Null_Render() 
    {
        IRender render = (IRender)newMock(IRender.class);
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        ResponseBuilder builder = new DefaultResponseBuilder(null);
        
        render.render(null, cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), NullWriter.getSharedInstance());
    }
    
    public void test_Normal_Render()
    {
        IRender render = (IRender)newMock(IRender.class);
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        IMarkupWriter writer = (IMarkupWriter)newMock(IMarkupWriter.class);
        
        ResponseBuilder builder = new DefaultResponseBuilder(writer);
        
        render.render(writer, cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void test_Get_Writer_Type()
    {
        IMarkupWriter writer = (IMarkupWriter)newMock(IMarkupWriter.class);
        ResponseBuilder builder = new DefaultResponseBuilder(writer);
        
        assertSame(builder.getWriter("test", "type"), writer);
    }
    
    public void test_Allowed_Scripts()
    {
        IComponent component = (IComponent)newMock(IComponent.class);
        ResponseBuilder builder = new DefaultResponseBuilder(null);
        
        assertTrue(builder.isBodyScriptAllowed(component));
        assertTrue(builder.isExternalScriptAllowed(component));
        assertTrue(builder.isInitializationScriptAllowed(component));
    }
    
    public void test_Write_Body_Script()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        ResponseBuilder builder = new DefaultResponseBuilder(mw);
        
        String bscript = "var e=4;";
        String imageInit = "image initializations";
        String preload = "preloadedvarname";
        
        verify();
        replay();
        
        builder.beginBodyScript(mw, cycle);
        
        assertOutput("<script type=\"text/javascript\"><!--"+LINE_SEPARATOR);
        
        builder.writeImageInitializations(mw, imageInit, preload, cycle);
        
        assertOutput(LINE_SEPARATOR
                + "tapestry.addOnLoad(function(e) {\n"
                + preload + " = [];\n"
                + "if (document.images)\n"
                + "{\n" + imageInit + "}\n"
                + "});");
        
        builder.writeBodyScript(mw, bscript, cycle);
        
        assertOutput(bscript);
        
        builder.endBodyScript(mw, cycle);
        
        assertOutput(LINE_SEPARATOR + "// --></script>");
        
        verify();
    }
    
    public void test_Write_External_Scripts()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        ResponseBuilder builder = new DefaultResponseBuilder(mw);
        
        String script1 = "http://noname/js/package.js";
        String script2 = "http://noname/js/package.js";
        
        verify();
        replay();
        
        builder.writeExternalScript(mw, script1, cycle);
        
        assertOutput("<script type=\"text/javascript\" src=\""
                + script1 + "\"></script>" + LINE_SEPARATOR);
        
        builder.writeExternalScript(mw, script2, cycle);
        
        assertOutput("<script type=\"text/javascript\" src=\""
                + script2 + "\"></script>" + LINE_SEPARATOR);
        
        verify();
    }
    
    public void test_Write_Initialization_Script()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        ResponseBuilder builder = new DefaultResponseBuilder(mw);
        
        String script = "doThisInInit();";
        
        verify();
        replay();
        
        builder.writeInitializationScript(mw, script);
        
        assertOutput("<script type=\"text/javascript\"><!--\n"
                + "tapestry.addOnLoad(function(e) {\n"
                + script 
                + "});"
                + "\n// -->"
                + "</script>");
        
        verify();
    }
}
