// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services;

/**
 * Defines constants for query parameters names commonly used by services.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServiceConstants
{
    /**
     * The name of the service responsible for processing the request.
     */
    public static final String SERVICE = "service";

    /**
     * The name of the page to activate when processing the service.
     */

    public static final String PAGE = "page";

    /**
     * The id path to the component within the page. By convention, this component is within the
     * {@link #PAGE}, unless {@link #CONTAINER_PAGE}&nbsp; is specified.
     */

    public static final String COMPONENT = "component";

    /**
     * The name of the page containing the component; this is only specified when the component is
     * contained by a page other than the activate page ({@link #PAGE}).
     */

    public static final String CONTAINER = "container";

    /**
     * A flag indicating whether a session was active when the link was rendered. If this is true,
     * but no session is active when the request is processed, the a service may at its discression
     * throw a {@iink org.apache.tapestry.StaleLinkException}
     */

    public static final String SESSION = "session";

    /**
     * Contains a number of additional strings meaningful to the application (the term service
     * parameters is something of an entrenched misnomer, a better term would have been application
     * parameters). These parameters are typically objects that have been squeezed into strings by
     * {@link org.apache.tapestry.services.DataSqueezer}.
     */

    public static final String PARAMETER = "sp";
}