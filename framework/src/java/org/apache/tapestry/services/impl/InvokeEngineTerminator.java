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

import org.apache.tapestry.Constants;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.services.EngineManager;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.RequestGlobals;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * The terminatior for the <code>tapestry.RequestProcessor</code> pipeline, this service is
 * responsible for:
 * <ul>
 * <li>Storing the request and response into {@link org.apache.tapestry.services.RequestGlobals}.
 * <li>Locating the correct engine instance and letting it to the rest of the request.
 * <li>Returning the engine instance to the pool at the end of the request. </ul
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class InvokeEngineTerminator implements WebRequestServicer
{
    private EngineManager _engineManager;

    private Infrastructure _infrastructure;

    private RequestGlobals _requestGlobals;

    public void service(WebRequest request, WebResponse response) throws IOException
    {
        _requestGlobals.store(request, response);

        IEngine engine = _engineManager.getEngineInstance();

        // Until we can inject the infrastructure into the engine
        // we do this to let the engine know about it.

        request.setAttribute(Constants.INFRASTRUCTURE_KEY, _infrastructure);

        try
        {
            engine.service(request, response);
        }
        finally
        {
            _engineManager.storeEngineInstance(engine);
        }

    }

    public void setEngineManager(EngineManager manager)
    {
        _engineManager = manager;
    }

    public void setInfrastructure(Infrastructure infrastructure)
    {
        _infrastructure = infrastructure;
    }

    public void setRequestGlobals(RequestGlobals requestGlobals)
    {
        _requestGlobals = requestGlobals;
    }
}