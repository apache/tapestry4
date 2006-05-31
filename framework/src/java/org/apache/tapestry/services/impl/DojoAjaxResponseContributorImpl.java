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

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseContributor;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;


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
    
    /** 
     * {@inheritDoc}
     */
    public ResponseBuilder createBuilder(IRequestCycle cycle)
        throws IOException
    {
        return new DojoAjaxResponseBuilder(_localeManager, _markupWriterSource,
                _webResponse, _exceptionPageName);
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean handlesResponse(IRequestCycle cycle)
    {
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
}
