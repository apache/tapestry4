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

import org.apache.commons.logging.Log;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ResponseDelegateFactory;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * Responsible for rendering a response to the client.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ResponseRendererImpl implements ResponseRenderer
{

    private Log _log;

    private ResponseDelegateFactory _responseDelegateFactory;

    public void renderResponse(IRequestCycle cycle)
        throws IOException
    {
        ResponseBuilder builder = getResponseDelegateFactory().getResponseBuilder(
                cycle);

        cycle.setResponseBuilder(builder);

        builder.renderResponse(cycle);
    }

    /** For subclass access to the injected Log. */
    public Log getLog()
    {
        return _log;
    }

    /** For injection. */
    public final void setLog(Log log)
    {
        _log = log;
    }

    /**
     * For injection.
     */
    public void setResponseDelegateFactory(ResponseDelegateFactory responseDelegate)
    {
        _responseDelegateFactory = responseDelegate;
    }

    /**
     * For subclass access.
     */
    public ResponseDelegateFactory getResponseDelegateFactory()
    {
        return _responseDelegateFactory;
    }
}
