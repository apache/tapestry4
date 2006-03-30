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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.engine.EngineMessages;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.util.PageRenderSupportImpl;
import org.apache.tapestry.web.WebResponse;

/**
 * The guts of rendering a page as a portlet response; used by
 * {@link org.apache.tapestry.portlet.RenderService} and
 * {@link org.apache.tapestry.portlet.PortletHomeService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletRendererImpl implements PortletRenderer
{
    private WebResponse _response;

    private MarkupWriterSource _markupWriterSource;

    private AssetFactory _assetFactory;

    private String _applicationId;

    public void renderPage(IRequestCycle cycle, String pageName) throws IOException
    {
    	try {
	        cycle.activate(pageName);
	
	        IPage page = cycle.getPage();
	
	        ContentType contentType = page.getResponseContentType();
	
	        PrintWriter printWriter = _response.getPrintWriter(contentType);
	
	        IMarkupWriter writer = _markupWriterSource.newMarkupWriter(printWriter, contentType);
	
	        String namespace = _response.getNamespace();
	
	        PageRenderSupportImpl support = new PageRenderSupportImpl(_assetFactory, namespace, null);
	
	        TapestryUtils.storePageRenderSupport(cycle, support);
	
	        IMarkupWriter nested = writer.getNestedWriter();
	
	        cycle.renderPage(nested);
	
	        String id = "Tapestry Portlet " + _applicationId + " " + namespace;
	
	        writer.comment("BEGIN " + id);
	        writer.comment("Page: " + page.getPageName());
	        writer.comment("Generated: " + new Date());
	        writer.comment("Framework version: " + Tapestry.VERSION);
	
	        support.writeBodyScript(writer, cycle);
	
	        nested.close();
	
	        support.writeInitializationScript(writer);
	
	        writer.comment("END " + id);

	        writer.close();
	        
	        // TODO: Trap errors and do some error reporting here?
    	}
    	catch (PageRedirectException e) {
    		handlePageRedirectException(cycle, e);
    	}
    }

    protected void handlePageRedirectException(IRequestCycle cycle, PageRedirectException exception)
		throws IOException
	{
		List pageNames = new ArrayList();
		
		String pageName = exception.getTargetPageName();
		
		while (true)
		{
		    if (pageNames.contains(pageName))
		    {
		        pageNames.add(pageName);
		
		        throw new ApplicationRuntimeException(EngineMessages.validateCycle(pageNames));
		    }
		
		    // Record that this page has been a target.
		
		    pageNames.add(pageName);
		
		    try
		    {
		        // Attempt to activate the new page.
		
		        cycle.activate(pageName);
		
		        break;
		    }
		    catch (PageRedirectException secondRedirectException)
		    {
		        pageName = secondRedirectException.getTargetPageName();
		    }
		}
		
		renderPage(cycle, pageName);
	}
    
    public void setMarkupWriterSource(MarkupWriterSource markupWriterSource)
    {
        _markupWriterSource = markupWriterSource;
    }

    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    public void setAssetFactory(AssetFactory assetFactory)
    {
        _assetFactory = assetFactory;
    }

    public void setApplicationId(String applicationId)
    {
        _applicationId = applicationId;
    }
}