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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.markup.UTFMarkupFilter;
import org.apache.tapestry.services.ResponseBuilder;
import org.testng.annotations.Configuration;


/**
 * Tests functionality of {@link DojoAjaxResponseBuilder}.
 * 
 * @author jkuhnert
 */
@SuppressWarnings("cast")
public class DojoAjaxResponseBuilderTest extends BaseComponentTestCase
{

    private static CharArrayWriter _writer;
    
    private static String LINE_SEPERATOR = (String)java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
    
    private PrintWriter newPrintWriter()
    {
        _writer = new CharArrayWriter();

        return new PrintWriter(_writer);
    }

    @Configuration(afterTestClass = true)
    protected void tearDown() throws Exception
    {
        _writer = null;
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
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(null, null);
        
        render.render(NullWriter.getSharedInstance(), cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), null);
    }
    
    public void testNormalRender()
    {
        IRender render = (IRender)newMock(IRender.class);
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        IMarkupWriter writer = (IMarkupWriter)newMock(IMarkupWriter.class);
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(writer, null);
        
        render.render(NullWriter.getSharedInstance(), cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void testNullContains() 
    {
        ResponseBuilder builder = new DojoAjaxResponseBuilder(null, null);
        
        replay();
        
        builder.isBodyScriptAllowed(null);
        
        verify();
    }
    
    public void testPartialRender()
    {
        IRender render = (IRender)newMock(IRender.class);
        
        IComponent comp1 = (IComponent)newMock(IComponent.class);
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        IMarkupWriter writer = (IMarkupWriter)newMock(IMarkupWriter.class);
        NestedMarkupWriter nested = (NestedMarkupWriter)newMock(NestedMarkupWriter.class);
        
        List parts = new ArrayList();
        parts.add("id1");
        
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(writer, parts);
        
        render.render(NullWriter.getSharedInstance(), cycle);
        
        expect(comp1.getClientId()).andReturn("id1").anyTimes();
        
        expect(writer.getNestedWriter()).andReturn(nested);
        
        nested.begin("response");
        nested.attribute("id", "id1");
        nested.attribute("type", DojoAjaxResponseBuilder.ELEMENT_TYPE);
        
        comp1.render(nested, cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        assertTrue(builder.contains(comp1));
        assertTrue(IComponent.class.isInstance(comp1));
        
        builder.render(null, comp1, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void testAllowedScripts()
    {
        IComponent comp = (IComponent)newMock(IComponent.class);
        List parts = new ArrayList();
        parts.add("comp1");
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(null, parts);
        
        expect(comp.getClientId()).andReturn("comp");
        
        expect(comp.getClientId()).andReturn("comp1");
        
        expect(comp.getClientId()).andReturn("comp");
        
        replay();
        
        assertFalse(builder.isBodyScriptAllowed(comp));
        assertTrue(builder.isExternalScriptAllowed(comp));
        assertFalse(builder.isInitializationScriptAllowed(comp));
        
        verify();
    }
    
    public void testWriteBodyScript()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(mw, null);
        
        String bscript = "var e=4;";
        String imageInit = "image initializations";
        String preload = "preloadedvarname";
        
        verify();
        replay();
        
        builder.beginResponse();
        
        builder.beginBodyScript(cycle);
        
        builder.writeImageInitializations(imageInit, preload, cycle);
        
        builder.writeBodyScript(bscript, cycle);
        
        builder.endBodyScript(cycle);
        
        builder.endResponse();
        
        assertOutput("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n" + 
                "<!ENTITY nbsp \'&#160;\'>\n" + 
                "]>\n" + 
                "<ajax-response><response id=\"bodyscript\" type=\"script\"><script>\n" + 
                "//<![CDATA[\n" + 
                "\n" + 
                "\n" + 
                "var preloadedvarname = new Array();\n" + 
                "if (document.images)\n" + 
                "{\n" + 
                "image initializations}\n" + 
                "var e=4;\n" + 
                "//]]>\n" + 
                "</script></response></ajax-response>");
        
        verify();
    }
    
    public void testWriteExternalScripts()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(mw, null);
        
        String script1 = "http://noname/js/package.js";
        String script2 = "http://noname/js/package.js";
        
        verify();
        replay();
        
        builder.beginResponse();
        
        builder.writeExternalScript(script1, cycle);
        
        builder.writeExternalScript(script2, cycle);
        
        builder.endResponse();
        
        assertOutput("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n" + 
                "<!ENTITY nbsp \'&#160;\'>\n" + 
                "]>\n" + 
                "<ajax-response><response id=\"includescript\" type=\"script\"><script type=\"text/javascript\" src=\"http://noname/js/package.js\"></script>" + 
                LINE_SEPERATOR +
                "<script type=\"text/javascript\" src=\"http://noname/js/package.js\"></script>" +
                LINE_SEPERATOR +
                "</response></ajax-response>");
        
        verify();
    }
    
    public void testWriteInitializationScript()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(mw, null);
        
        String script = "doThisInInit();";
        
        verify();
        replay();
        
        builder.beginResponse();
        
        builder.writeInitializationScript(script);
        
        builder.endResponse();
        
        assertOutput("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n" + 
                "<!ENTITY nbsp \'&#160;\'>\n" + 
                "]>\n" + 
                "<ajax-response><response id=\"initializationscript\" type=\"script\"><script>\n" + 
                "//<![CDATA[\n" + 
                "doThisInInit();\n" + 
                "//]]>\n" + 
                "</script></response></ajax-response>");
        
        verify();
    }
}
