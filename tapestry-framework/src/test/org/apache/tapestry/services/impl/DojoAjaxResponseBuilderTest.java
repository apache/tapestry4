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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.markup.MarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.markup.UTFMarkupFilter;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link DojoAjaxResponseBuilder}.
 * 
 * @author jkuhnert
 */
@SuppressWarnings("cast")
@Test(sequential=true)
public class DojoAjaxResponseBuilderTest extends BaseComponentTestCase
{

    private static CharArrayWriter _writer;
    
    private PrintWriter newPrintWriter()
    {
        _writer = new CharArrayWriter();

        return new PrintWriter(_writer);
    }

    @AfterClass
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
        IRender render = newMock(IRender.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newWriter();
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, writer, null);
        
        render.render(NullWriter.getSharedInstance(), cycle);
        
        replay();
        
        builder.render(writer, render, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void test_Normal_Render()
    {
        IRender render = newMock(IRender.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newMock(IMarkupWriter.class);
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, writer, null);
        
        render.render(NullWriter.getSharedInstance(), cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void test_Null_Contains() 
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newWriter();
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, writer, null);
        
        replay();
        
        builder.isBodyScriptAllowed(null);
        
        verify();
    }
    
    public void test_Partial_Render()
    {
        IRender render = newMock(IRender.class);
        
        IComponent comp1 = newMock(IComponent.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newMock(IMarkupWriter.class);
        NestedMarkupWriter nested = newMock(NestedMarkupWriter.class);
        
        Infrastructure infra = newMock(Infrastructure.class);
        
        List parts = new ArrayList();
        parts.add("id1");
        
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, writer, parts);
        
        render.render(NullWriter.getSharedInstance(), cycle);
        
        expect(comp1.getClientId()).andReturn("id1").anyTimes();
        
        expect(comp1.peekClientId()).andReturn("id1").anyTimes();
        
        expect(cycle.getInfrastructure()).andReturn(infra);
        
        expect(infra.getOutputEncoding()).andReturn("UTF-8");
        
        writer.printRaw("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.printRaw("<!DOCTYPE html "
                + "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n"
                + "<!ENTITY nbsp '&#160;'>\n"
                + "]>\n");
        writer.printRaw("<ajax-response>");
        
        expect(writer.getNestedWriter()).andReturn(nested);
        
        nested.begin("response");
        nested.attribute("id", "id1");
        nested.attribute("type", ResponseBuilder.ELEMENT_TYPE);
        
        comp1.render(nested, cycle);
        
        replay();
        
        builder.render(null, render, cycle);
        
        assertTrue(builder.contains(comp1));
        assertTrue(IComponent.class.isInstance(comp1));
        
        builder.render(null, comp1, cycle);
        
        verify();
        
        assertSame(builder.getWriter(), writer);
    }
    
    public void test_Page_Render()
    {
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        // IMarkupWriter writer = newMock(IMarkupWriter.class);
        RequestLocaleManager rlm = newMock(RequestLocaleManager.class);
        MarkupWriterSource mrs = newMock(MarkupWriterSource.class);
        WebResponse resp = newMock(WebResponse.class);
        WebRequest req = newMock(WebRequest.class);
        AssetFactory assetFactory = newMock(AssetFactory.class);
        IEngineService pageService = newEngineService();
        
        List errorPages = new ArrayList();
        
        List parts = new ArrayList();
        parts.add("id1");
        
        DojoAjaxResponseBuilder builder = 
            new DojoAjaxResponseBuilder(cycle, rlm, mrs, resp, req,
                    errorPages, assetFactory, "", pageService);
        
        expect(page.getPageName()).andReturn("RequestPage").anyTimes();
        
        expect(cycle.getParameter(ServiceConstants.PAGE)).andReturn("RequestPage").anyTimes();
        
        expect(page.peekClientId()).andReturn("pageId");
        
        expect(cycle.renderStackIterator()).andReturn(Collections.EMPTY_LIST.iterator());
        
        page.render(NullWriter.getSharedInstance(), cycle);
        
        replay();
        
        builder.render(null, page, cycle);
        
        verify();
    }
    
    public void test_New_Page_Render()
    {
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newMock(IMarkupWriter.class);
        NestedMarkupWriter nwriter = newNestedWriter();
        
        ILink link = newMock(ILink.class);
        
        RequestLocaleManager rlm = newMock(RequestLocaleManager.class);
        MarkupWriterSource mrs = newMock(MarkupWriterSource.class);
        WebResponse resp = newMock(WebResponse.class);
        WebRequest req = newMock(WebRequest.class);
        AssetFactory assetFactory = newMock(AssetFactory.class);
        IEngineService pageService = newEngineService();
        
        List errorPages = new ArrayList();
        
        List parts = new ArrayList();
        parts.add("id1");
        
        DojoAjaxResponseBuilder builder = 
            new DojoAjaxResponseBuilder(cycle, rlm, mrs, resp, req,
                    errorPages, assetFactory, "", pageService);
        
        builder.setWriter(writer);
        
        expect(page.getPageName()).andReturn("RequestPage").anyTimes();
        
        expect(cycle.getParameter(ServiceConstants.PAGE)).andReturn("anotherPage").anyTimes();
        
        expect(writer.getNestedWriter()).andReturn(nwriter);
        nwriter.begin("response");
        nwriter.attribute("type", ResponseBuilder.PAGE_TYPE);
        
        expect(pageService.getLink(true, "RequestPage")).andReturn(link);
        expect(link.getAbsoluteURL()).andReturn("/new/url");
        
        nwriter.attribute("url", "/new/url");
        
        replay();
        
        builder.render(null, page, cycle);
        
        verify();
    }
    
    public void test_Allowed_Scripts()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IComponent comp = newMock(IComponent.class);
        IMarkupWriter writer = newWriter();
        
        List parts = new ArrayList();
        parts.add("comp1");
        
        ResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, writer, parts);
        
        expect(comp.getClientId()).andReturn("comp");
        
        expect(cycle.renderStackIterator()).andReturn(Collections.EMPTY_LIST.iterator());
        
        expect(comp.getClientId()).andReturn("comp1");
        
        expect(comp.getClientId()).andReturn("comp");
        
        expect(cycle.renderStackIterator()).andReturn(Collections.EMPTY_LIST.iterator());
        
        replay();
        
        assertFalse(builder.isBodyScriptAllowed(comp));
        assertTrue(builder.isExternalScriptAllowed(comp));
        assertFalse(builder.isInitializationScriptAllowed(comp));
        
        verify();
    }
    
    public void test_Write_Body_Script()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = newMock(IRequestCycle.class);
        Infrastructure inf = newMock(Infrastructure.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, mw, null);
        
        String bscript = "var e=4;";
        String imageInit = "image initializations";
        String preload = "preloadedvarname";
        
        verify();
        
        expect(cycle.getInfrastructure()).andReturn(inf);
        expect(inf.getOutputEncoding()).andReturn("UTF-8");
        
        replay();
        
        builder.beginResponse();
        
        builder.beginBodyScript(mw, cycle);
        
        builder.writeImageInitializations(mw, imageInit, preload, cycle);
        
        builder.writeBodyScript(mw, bscript, cycle);
        
        builder.endBodyScript(mw, cycle);
        
        builder.endResponse();
        
        assertOutput("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n" + 
                "<!ENTITY nbsp \'&#160;\'>\n" + 
                "]>\n" + 
                "<ajax-response><response id=\"bodyscript\" type=\"script\"><script>\n" + 
                "//<![CDATA[\n" + 
                "\n" + 
                "preloadedvarname = [];\n" + 
                "if (document.images) {\n" + 
                "image initializations}\n" + 
                "var e=4;\n" + 
                "//]]>\n" + 
                "</script></response></ajax-response>");
        
        verify();
    }
    
    public void test_Write_External_Scripts()
    {
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        IRequestCycle cycle = newMock(IRequestCycle.class);
        Infrastructure inf = newMock(Infrastructure.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, mw, null);
        
        String script1 = "http://noname/js/package.js";
        String script2 = "http://noname/js/package2.js";
        
        verify();
        
        expect(cycle.getInfrastructure()).andReturn(inf);
        expect(inf.getOutputEncoding()).andReturn("UTF-8");
        
        replay();
        
        builder.beginResponse();
        
        builder.writeExternalScript(mw, script1, cycle);
        
        builder.writeExternalScript(mw, script2, cycle);
        
        builder.endResponse();
        
        assertOutput("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [\n" + 
                "<!ENTITY nbsp \'&#160;\'>\n" + 
                "]>\n" + 
                "<ajax-response><response id=\"includescript\" type=\"script\">" 
                + "<include url=\"http://noname/js/package.js\" />"
                + "<include url=\"http://noname/js/package2.js\" /></response></ajax-response>");
        
        verify();
    }
    
    public void test_Write_Initialization_Script()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        MarkupFilter filter = new UTFMarkupFilter();
        PrintWriter writer = newPrintWriter();
        Infrastructure inf = newMock(Infrastructure.class);
        
        replay();
        
        IMarkupWriter mw = new MarkupWriterImpl("text/html", writer, filter);
        DojoAjaxResponseBuilder builder = new DojoAjaxResponseBuilder(cycle, mw, null);
        
        String script = "doThisInInit();";
        
        verify();
        
        expect(cycle.getInfrastructure()).andReturn(inf);
        expect(inf.getOutputEncoding()).andReturn("UTF-8");
        
        replay();
        
        builder.beginResponse();
        
        builder.writeInitializationScript(mw, script);
        
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
