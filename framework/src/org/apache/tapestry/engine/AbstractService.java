//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import java.io.IOException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.StringSplitter;
import org.apache.tapestry.util.io.DataSqueezer;

/**
 *  Abstract base class for implementing engine services.  Instances of services
 *  are shared by many engines and threads, so they must be threadsafe.
 * 
 *  <p>
 *  Note; too much of the URL encoding/decoding stategy is fixed here.
 *  A future release of Tapestry may extract this strategy, allowing developers
 *  to choose the method via which URLs are encoded.
 * 
 *  @see org.apache.tapestry.engine.AbstractEngine#getService(String)
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.3
 * 
 **/

public abstract class AbstractService implements IEngineService
{
    /**
     *  Constructs a link for the service.
     *
     *  @param cycle the request cycle
     *  @param serviceName the name of the service
     *  @param serviceContext context related to the service itself which is added to the URL as-is
     *  @param parameters additional service parameters provided by the component; 
     *  this is application specific information, and is encoded with 
     *  {@link java.net.URLEncoder#encode(String)} before being added
     *  to the query.
     *  @param stateful if true, the final URL must be encoded with the HttpSession id
     *
     **/

    protected ILink constructLink(
        IRequestCycle cycle,
        String serviceName,
        String[] serviceContext,
        Object[] parameters,
        boolean stateful)
    {
        DataSqueezer squeezer = cycle.getEngine().getDataSqueezer();
        String[] squeezed = null;

        try
        {
            squeezed = squeezer.squeeze(parameters);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        return new EngineServiceLink(cycle, serviceName, serviceContext, squeezed, stateful);
    }

    /**
     *  Returns the service context as an array of Strings.
     *  Returns null if there are no service context strings.
     *
     **/

    protected String[] getServiceContext(RequestContext context)
    {
        String service = context.getParameter(Tapestry.SERVICE_QUERY_PARAMETER_NAME);

		int slashx = service.indexOf('/');
		
		if (slashx < 0)
			return null;
			
		String serviceContext = service.substring(slashx + 1);

        return new StringSplitter('/').splitToArray(serviceContext);
    }

    /**
     *  Returns the service parameters as an array of Strings.
     *
     **/

    protected Object[] getParameters(IRequestCycle cycle)
    {
        RequestContext context = cycle.getRequestContext();

        String[] squeezed = context.getParameters(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);

        if (Tapestry.size(squeezed) == 0)
            return squeezed;

        try
        {
            DataSqueezer squeezer = cycle.getEngine().getDataSqueezer();

            return squeezer.unsqueeze(squeezed);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }
}