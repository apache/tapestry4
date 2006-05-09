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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.markup.UTFMarkupFilter;
import org.apache.tapestry.services.ResponseBuilder;


/**
 * Tests functionality of {@link DefaultResponseBuilder}.
 * 
 * @author jkuhnert
 */
public class DefaultResponseBuilderTest extends HiveMindTestCase
{

    private static CharArrayWriter _writer;

    private static String lineSeperator = java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
    
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
    
    public void testNullRender() 
    {
        IRender render = (IRender)newMock(IRender.class);
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        ResponseBuilder builder = new DefaultResponseBuilder(null);
        
        render.render(null, cycle);
        
        replayControls();
        
        builder.render(null, render, cycle);
        
        verifyControls();
        
        assertSame(builder.getWriter(), NullWriter.getSharedInstance());
    }
    
    public void testNormalRender()
    {
        IRender render = (IRender)newMock(IRender.class);
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        IMarkupWriter writer = (IMarkupWriter)newMock(IMarkupWriter.class);
        
        ResponseBuilder builder = new DefaultResponseBuilder(writer);
        
        render.render(writer, cycle);
        
        replayControls();
        
        builder.render(null, render, cycle);
        
        verifyControls();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void testGetWriterType()
    {
        IMarkupWriter writer = (IMarkupWriter)newMock(IMarkupWriter.class);
        ResponseBuilder builder = new DefaultResponseBuilder(writer);
        
        assertSame(builder.getWriter("test", "type"), writer);
    }
    
    public void testAllowedScripts()
    {
        IComponent component = (IComponent)newMock(IComponent.class);
        ResponseBuilder builder = new DefaultResponseBuilder(null);
        
        assertTrue(builder.isBodyScriptAllowed(component));
        assertTrue(builder.isExternalScriptAllowed(component));
        assertTrue(builder.isInitializationScriptAllowed(component));
    }
    
    public void testWriteBodyScript()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        replayControls();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        ResponseBuilder builder = new DefaultResponseBuilder(mw);
        
        String bscript = "var e=4;";
        String imageInit = "image initializations";
        String preload = "preloadedvarname";
        
        verifyControls();
        replayControls();
        
        builder.beginBodyScript(cycle);
        
        assertOutput("<script type=\"text/javascript\"><!--");
        
        builder.writeImageInitializations(imageInit, preload, cycle);
        
        assertOutput("\n\nvar " + preload + " = new Array();\n"
                + "if (document.images)\n"
                + "{\n" + imageInit + "}\n");
        
        builder.writeBodyScript(bscript, cycle);
        
        assertOutput("\n\n" + bscript);
        
        builder.endBodyScript(cycle);
        
        assertOutput("\n\n// --></script>");
        
        verifyControls();
    }
    
    public void testWriteExternalScripts()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        replayControls();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        ResponseBuilder builder = new DefaultResponseBuilder(mw);
        
        String script1 = "http://noname/js/package.js";
        String script2 = "http://noname/js/package.js";
        
        verifyControls();
        replayControls();
        
        builder.writeExternalScript(script1, cycle);
        
        assertOutput("<script type=\"text/javascript\" src=\""
                + script1 + "\"></script>" + lineSeperator);
        
        builder.writeExternalScript(script2, cycle);
        
        assertOutput("<script type=\"text/javascript\" src=\""
                + script2 + "\"></script>" + lineSeperator);
        
        verifyControls();
    }
    
    public void testWriteInitializationScript()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        replayControls();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        ResponseBuilder builder = new DefaultResponseBuilder(mw);
        
        String script = "doThisInInit();";
        
        verifyControls();
        replayControls();
        
        builder.writeInitializationScript(script);
        
        assertOutput("<script type=\"text/javascript\"><!--\n"
                + "dojo.event.connect(window, 'onload', function(e) {\n"
                + script 
                + "});"
                + "\n// -->"
                + "</script>");
        
        verifyControls();
    }
}
