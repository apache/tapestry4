//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.engine;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.util.StringSplitter;

/**
 *  Abstract base class for implementing engine services.  Instances of services
 *  are shared by many engines and threads, so they must be threadsafe.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.3
 * 
 **/

public abstract class AbstractService implements IEngineService
{
    private static StringSplitter splitter = new StringSplitter('/');

    /**
     *  Assembles a URL for the service.
     *
     *  @param the path for the servlet for this Tapestry application
     *  @param serviceName the name of the service
     *  @param serviceContext context related to the service itself which is added to the URL as-is
     *  @param otherContext additional context provided by the component; this is application specific
     *  information, and is encoded with {@link URLEncoder#encode(String)} before being added
     *  to the query.
     *  @param stateful if true, the final URL must be encoded with the HttpSession id
     *
     **/

    protected Gesture assembleGesture(
        IRequestCycle cycle,
        String serviceName,
        String[] serviceContext,
        String[] parameters,
        boolean stateful)
    {
        Map map = new HashMap();
        StringBuffer buffer = null;

        map.put(SERVICE_QUERY_PARAMETER_NAME, serviceName);

        if (serviceContext != null && serviceContext.length > 0)
        {
            buffer = new StringBuffer();

            for (int i = 0; i < serviceContext.length; i++)
            {
                if (i > 0)
                    buffer.append('/');

                buffer.append(serviceContext[i]);
            }

            map.put(CONTEXT_QUERY_PARMETER_NAME, buffer.toString());
        }

        if (parameters != null && parameters.length != 0)
        {
            if (buffer == null)
                buffer = new StringBuffer();
            else
                buffer.setLength(0);

            for (int i = 0; i < parameters.length; i++)
            {
                if (i > 0)
                    buffer.append('/');

                // Note: deprecation warning for compatibility with
                // Servlet API 2.2

                buffer.append(URLEncoder.encode(parameters[i]));
            }

            map.put(PARAMETERS_QUERY_PARAMETER_NAME, buffer.toString());
        }

        return new Gesture(cycle, map, stateful);
    }

    /**
     *  Returns a {@link StringSplitter} configured to split on slashes.
     *
     **/

    protected StringSplitter getSplitter()
    {
        return splitter;
    }

    /**
     *  Returns the service context as an array of Strings.
     *
     **/

    protected String[] getServiceContext(RequestContext context)
    {
        String parameter = context.getParameter(CONTEXT_QUERY_PARMETER_NAME);

        return getSplitter().splitToArray(parameter);
    }

    /**
     *  Returns the service parameters as an array of Strings.
     *  The strings will have been passed through
     *  {@link URLDecoder#decode(String)}.
     *
     **/

    protected String[] getParameters(RequestContext context)
    {
        String parameter = context.getParameter(PARAMETERS_QUERY_PARAMETER_NAME);

        if (parameter == null)
            return null;

        String[] result = getSplitter().splitToArray(parameter);

        // Note: deprecation warning for compatibility with
        // Servlet API 2.2

        for (int i = 0; i < result.length; i++)
            result[i] = URLDecoder.decode(result[i]);

        return result;
    }
}