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
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseContributor;
import org.apache.tapestry.services.ResponseDelegateFactory;

/**
 * Implementation of {@link ResponseDelegateFactory}.
 *
 * @author jkuhnert
 */
public class ResponseDelegateFactoryImpl implements ResponseDelegateFactory {
    
    /** Configured response contribution choosers. */
    protected List _responseContributors;
    
    /**
     * {@inheritDoc}
     */
    public ResponseBuilder getResponseBuilder(IRequestCycle cycle)
    throws IOException
    {
        if (_responseContributors == null)
            return null;
        
        for (int i = 0; i < _responseContributors.size(); i++) {
            ResponseContributor rc = (ResponseContributor)_responseContributors.get(i);
            if (rc.handlesResponse(cycle))
                return rc.createBuilder(cycle);
        }
        
        throw new ApplicationRuntimeException(ImplMessages.unknownRequest());
    }
    
    /**
     * Sets a configured list of {@link ResponseContributor} objects to be used
     * in processing ajax requests.
     * 
     * @param responseContributors
     */
    public void setResponseContributors(List responseContributors)
    {
        this._responseContributors = responseContributors;
    }
}
