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

package org.apache.tapestry.portlet;

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.matches;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletRendererImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletRenderer extends BaseComponentTestCase
{
    
    private PrintWriter newPrintWriter()
    {
        return new PrintWriter(new CharArrayWriter());
    }

    private AssetFactory newAssetFactory()
    {
        return newMock(AssetFactory.class);
    }

    private WebResponse newWebResponse(ContentType contentType, PrintWriter writer)
            throws Exception
    {
        WebResponse response = newMock(WebResponse.class);
        checkOrder(response, false);
        
        expect(response.getPrintWriter(contentType)).andReturn(writer);

        expect(response.getNamespace()).andReturn("NAMESPACE");
        
        return response;
    }

    private MarkupWriterSource newSource(PrintWriter printWriter, ContentType contentType,
            IMarkupWriter writer)
    {
        MarkupWriterSource source = newMock(MarkupWriterSource.class);
        
        expect(source.newMarkupWriter(printWriter, contentType)).andReturn(writer);
        
        return source;
    }

    private IPage newPage(ContentType contentType)
    {
        IPage page = newMock(IPage.class);

        expect(page.getResponseContentType()).andReturn(contentType);
        
        expect(page.getPageName()).andReturn("ZePage");

        return page;
    }

    private IRequestCycle newCycle(String pageName, IPage page)
    {
        return newCycle(pageName, page, null);
    }
    
    private IRequestCycle newCycle(String pageName, IPage page, IMarkupWriter writer)
    {   
        IRequestCycle cycle = newCycle();
        
        cycle.activate(pageName);

        expect(cycle.getPage()).andReturn(page).anyTimes();
        
        Location l = newLocation();
        
        expect(page.getLocation()).andReturn(l);
        
        expect(cycle.getAttribute("org.apache.tapestry.PageRenderSupport")).andReturn(null);
        
        // We can check that an instance of PageRenderSupport is passed in, but
        // we can't (easily) check thta it's configured the way we want.
        
        cycle.setAttribute(eq("org.apache.tapestry.PageRenderSupport"), 
                isA(PageRenderSupport.class));
        
        cycle.renderPage(isA(ResponseBuilder.class));
        
        cycle.removeAttribute("org.apache.tapestry.PageRenderSupport");
        
        return cycle;
    }

    public void testSuccess() throws Exception
    {
        ContentType ct = new ContentType("text/html");
        PrintWriter pw = newPrintWriter();
        
        WebResponse response = newWebResponse(ct, pw);
        
        IMarkupWriter nested = newNestedWriter();
        
        IMarkupWriter writer = newWriter();
        
        expect(writer.getNestedWriter()).andReturn((NestedMarkupWriter)nested);
        
        //nested.flush();
        
        MarkupWriterSource source = newSource(pw, ct, writer);
        IPage page = newPage(ct);
        AssetFactory assetFactory = newAssetFactory();
        
        IRequestCycle cycle = newCycle("ZePage", page);
        
        writer.comment("BEGIN Tapestry Portlet appId NAMESPACE");
        writer.comment("Page: ZePage");
        
        writer.comment(matches("Generated:.*"));
        
        writer.comment(matches("Framework version:.*"));

        nested.close();

        writer.comment("END Tapestry Portlet appId NAMESPACE");

        writer.close();

        replay();
        
        PortletRendererImpl r = new PortletRendererImpl();
        r.setMarkupWriterSource(source);
        r.setResponse(response);
        r.setAssetFactory(assetFactory);
        r.setApplicationId("appId");
        
        r.renderPage(cycle, "ZePage");
        
        verify();
    }

    // TODO: Tests that prove the RenderPageSupport is working properly.
}
