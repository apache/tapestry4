/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

import java.io.IOException;

import org.apache.hivemind.ApplicationRuntimeException;
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
     *  {@link URLEncoder#encode(String)} before being added
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