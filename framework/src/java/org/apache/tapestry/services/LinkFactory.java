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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;

/**
 * A source of {@link org.apache.tapestry.engine.ILink}instances. This is primarily used by
 * {@link org.apache.tapestry.engine.IEngineService}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface LinkFactory
{
    /**
     * Constructs an {@link org.apache.tapestry.engine.ILink}&nbsp; from the service name, context
     * and parameters information provided.
     * <p>
     * Note: this API is subject to significant change shortly!
     * </p>
     * 
     * @param cycle
     *            the current request cycle
     * @param serviceName
     *            the name of the service
     * @param context
     *            an array of context information used by the service when decoding the request. May
     *            be null. Values should be URL safe (generally speaking, Java identifiers)
     * @param parameters
     *            An array of object values encoded with the service, but ultimately meaningful to
     *            the application <b>Note: the name serviceParameters is entrenched, but a better
     *            name would be serviceParameters </b>
     * @param stateful
     *            If true, then the final URL should be encoded (with the session id) if necessary.
     *            If false, the session encoding should not occur. The latter case is useful for
     *            services that will absolutely not need any access to user-specific state.
     */
    public ILink constructLink(IRequestCycle cycle, String serviceName, String[] context,
            Object[] serviceParameters, boolean stateful);

    /**
     * A secondary function of the service is to convert encoded (aka "squeezed") service parameters
     * back into an array of Objects. This does makes sense .. the link factory is responsible for
     * encoding the service parameters, it should be responsible for decoding them.
     * 
     * @param cycle
     *            the current request cycle
     * @return an array of Object[]. May return an empty array, but won't return null.
     */

    public Object[] extractServiceParameters(IRequestCycle cycle);
}