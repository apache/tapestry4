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

import org.apache.tapestry.IRequestCycle;

/**
 * A service, provided by the {@link org.apache.tapestry.IEngine}, for its pages and/or components.
 * Services are responsible for constructing {@link EngineServiceLink}s (an encoding of URLs) to
 * represent dynamic application behavior, and for parsing those URLs when a subsequent request
 * involves them.
 * 
 * @author Howard Lewis Ship
 */

public interface IEngineService
{
    /**
     * Builds a URL for a service. This is performed during the rendering phase of one request cycle
     * and builds URLs that will invoke activity in a subsequent request cycle.
     * <p>
     * <b>This method changed incompatibly between release 3.0 and release 4.0. </b>
     * </p>
     * @param post
     *            if true, then the link will be used for a post (not a get, i.e., for a HTML form);
     *            this may affect what information is encoded into the link
     * @param parameter
     *            An object that provide any additional information needed by the service. Each
     *            service implementation will expect that an object of the proper type be passed in.
     *            In some cases, a simple String will do; in others, a specific object (possibly
     *            implementing an interface) will be required.
     * 
     * @return The URL for the service. The URL will have to be encoded via
     *         {@link javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)}.
     */

    ILink getLink(boolean post, Object parameter);

    /**
     * Perform the service, interpreting the URL (from the
     * {@link javax.servlet.http.HttpServletRequest}) responding appropriately, and rendering a
     * result page.
     * 
     * @param cycle
     *            the incoming request
     */

    void service(IRequestCycle cycle) throws IOException;

    /**
     * Returns the name of the service.
     * 
     * @since 1.0.1
     */

    String getName();
}
