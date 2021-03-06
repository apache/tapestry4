// Copyright May 8, 2006 The Apache Software Foundation
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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseContributor;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Handles determining dojo ajax requests.
 * 
 * @author jkuhnert
 */
public class DojoAjaxResponseContributorImpl implements ResponseContributor
{
    public static final String DOJO_AJAX_HEADER = "dojo-ajax-request";
    
    private RequestLocaleManager _localeManager;
    
    private MarkupWriterSource _markupWriterSource;
    
    private WebResponse _webResponse;
    
    private WebRequest _webRequest;
    
    private String _exceptionPageName;
    
    private String _staleSessionPageName;
    
    private String _staleLinkPageName;
    
    private AssetFactory _assetFactory;
    
    private IEngineService _pageService;
    
    /** 
     * {@inheritDoc}
     */
    public ResponseBuilder createBuilder(IRequestCycle cycle)
        throws IOException
    {
        List errorPages = new ArrayList();
        errorPages.add(_exceptionPageName);
        errorPages.add(_staleSessionPageName);
        errorPages.add(_staleLinkPageName);
        
        return new DojoAjaxResponseBuilder(cycle, _localeManager, 
                _markupWriterSource,
                _webResponse, errorPages, _assetFactory, 
                _webResponse.getNamespace(), _pageService);
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean handlesResponse(IRequestCycle cycle)
    {
        String parm = cycle.getParameter(DOJO_AJAX_HEADER);
        
        if (parm != null && Boolean.valueOf(parm).booleanValue())
            return true;
        return _webRequest.getHeader(DOJO_AJAX_HEADER) != null;
    }
    
    public void setLocaleManager(RequestLocaleManager localeManager)
    {
        _localeManager = localeManager;
    }
    
    public void setMarkupWriterSource(MarkupWriterSource markupWriterSource)
    {
        _markupWriterSource = markupWriterSource;
    }
    
    public void setWebResponse(WebResponse webResponse)
    {
        _webResponse = webResponse;
    }
    
    public void setWebRequest(WebRequest webRequest)
    {
        _webRequest  = webRequest;
    }
    
    public void setExceptionPageName(String name)
    {
        _exceptionPageName = name;
    }
    
    public void setStaleSessionPageName(String name)
    {
        _staleSessionPageName = name;
    }
    
    public void setStaleLinkPageName(String name)
    {
        _staleLinkPageName = name;
    }
    
    public void setAssetFactory(AssetFactory factory)
    {
        _assetFactory = factory;
    }
    
    public void setPageService(IEngineService service)
    {
        _pageService = service;
    }
}
