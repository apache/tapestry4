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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseContributor;
import org.apache.tapestry.web.WebResponse;

import java.io.IOException;

/**
 * Factory that is used if no other has been chosen, handles normal html
 * responses.
 * 
 * TODO: Should probably implement this as a hivemind pipeline.
 * 
 * @author jkuhnert
 */
public class DefaultResponseContributorImpl implements ResponseContributor
{
    protected RequestLocaleManager _localeManager;
    
    protected MarkupWriterSource _markupWriterSource;

    protected WebResponse _webResponse;
    
    protected AssetFactory _assetFactory;
    
    /**
     * {@inheritDoc}
     */
    public ResponseBuilder createBuilder(IRequestCycle cycle)
    throws IOException
    {
        return new DefaultResponseBuilder(cycle, _localeManager, _markupWriterSource,
                _webResponse, _assetFactory, _webResponse.getNamespace());
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
    
    public void setAssetFactory(AssetFactory factory)
    {
        _assetFactory = factory;
    }
}
