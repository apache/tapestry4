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

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.Tapestry;

/**
 *  Abstract base class for implementing engine services.  Instances of services
 *  are shared by many engines and threads, so they must be threadsafe.
 * 
 *  <p>
 *  Note; too much of the URL encoding/decoding stategy is fixed here.
 *  A future release of Tapestry may extract this strategy, allowing developers
 *  to choose the method via which URLs are encoded.
 * 
 *  @see net.sf.tapestry.engine.AbstractEngine#
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
        return new Gesture(cycle, serviceName, serviceContext, parameters, stateful);
    }

    /**
     *  Returns the service context as an array of Strings.
     *
     **/

    protected String[] getServiceContext(RequestContext context)
    {
        int count = context.getPathInfoCount();

        // The first element in the path info is the service name, the 
        // context is all the remaining elements.

        String[] result = new String[count - 1];

        for (int i = 1; i < count; i++)
            result[i - 1] = context.getPathInfo(i);

        return result;
    }

    /**
     *  Returns the service parameters as an array of Strings.
     *
     **/

    protected String[] getParameters(RequestContext context)
    {
        return context.getParameters(PARAMETERS_QUERY_PARAMETER_NAME);
    }
}