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

package org.apache.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 * A service, provided by the {@link org.apache.tapestry.IEngine}, for its pages and/or components.
 * Services are responsible for constructing {@link EngineServiceLink}s (an encoding of URLs) to
 * represent dynamic application behavior, and for parsing those URLs when a subsequent request
 * involves them.
 * 
 * @see org.apache.tapestry.IEngine#getService(String)
 * @author Howard Lewis Ship
 */

public interface IEngineService
{
    /**
     * Builds a URL for a service. This is performed during the rendering phase of one request cycle
     * and bulds URLs that will invoke activity in a subsequent request cycle.
     * <p>
     * <b>This method changed incompatibly between release 3.0 and release 3.1. </b>
     * </p>
     * 
     * @param cycle
     *            Defines the request cycle being processed.
     * @param parameter
     *            An object that provide any additional information needed by the service. Each
     *            service implementation will expect that an object of the proper type be passed in.
     *            In some cases, a simple String will do; in others, a specific object (possibly
     *            implementing an interface) will be required.
     * @return The URL for the service. The URL will have to be encoded via
     *         {@link javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)}.
     */

    public ILink getLink(IRequestCycle cycle, Object parameter);

    /**
     * Perform the service, interpreting the URL (from the
     * {@link javax.servlet.http.HttpServletRequest}) responding appropriately, and rendering a
     * result page.
     * 
     * @param cycle
     *            the incoming request
     * @param output
     *            stream to which output should ultimately be directed
     * @see org.apache.tapestry.IEngine#service(org.apache.tapestry.request.RequestContext)
     */

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws IOException;

    /**
     * Returns the name of the service.
     * 
     * @since 1.0.1
     */

    public String getName();
}