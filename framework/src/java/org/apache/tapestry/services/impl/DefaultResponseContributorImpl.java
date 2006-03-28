// Copyright 2004, 2005 The Apache Software Foundation
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
import java.io.PrintWriter;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseContributor;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

/**
 * Determines if incoming request is a valid dojo request via the javascript
 * added parameter of "dojoRequest" = "true".
 * 
 * @author jkuhnert
 */
public class DefaultResponseContributorImpl implements ResponseContributor
{
    /**
     * Inside a {@link org.apache.tapestry.util.ContentType}, the output encoding is called
     * "charset".
     */
    
    public static final String ENCODING_KEY = "charset";
    
    protected RequestLocaleManager _localeManager;
    
    protected MarkupWriterSource _markupWriterSource;

    protected WebResponse _webResponse;
    
    /**
     * {@inheritDoc}
     */
    public ResponseBuilder createBuilder(IRequestCycle cycle)
    throws IOException
    {
        _localeManager.persistLocale();
        
        IPage page = cycle.getPage();
        
        ContentType contentType = page.getResponseContentType();
        
        String encoding = contentType.getParameter(ENCODING_KEY);
        
        if (encoding == null)
        {
            encoding = cycle.getEngine().getOutputEncoding();
            
            contentType.setParameter(ENCODING_KEY, encoding);
        }
        
        PrintWriter printWriter = _webResponse.getPrintWriter(contentType);
        
        IMarkupWriter writer = _markupWriterSource.newMarkupWriter(printWriter, contentType);
        
        DefaultResponseBuilder builder = new DefaultResponseBuilder(writer);
        
        return builder;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean handlesResponse(IRequestCycle cycle)
    {
        return true;
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
}
