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

package org.apache.tapestry.services;

import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoder;

/**
 * A source of {@link org.apache.tapestry.engine.ILink}instances. This is primarily used by
 * {@link org.apache.tapestry.engine.IEngineService}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface LinkFactory
{
    /**
     * Constructs an {@link org.apache.tapestry.engine.ILink}.
     * 
     * @param cycle
     *            the current request cycle
     * @param post
     *            if true, then the link will be used for a post (not a get, i.e., for a HTML form);
     *            this may affect what information is encoded into the link
     * @param parameters
     *            A map; keys are strings and values are strings or string arrays (exception: key
     *            {@link ServiceConstants#PARAMETER} is an array of objects. Certain keys, defined
     *            in {@link ServiceConstants} may have special meaning. The map will typically be
     *            modified internally. May not be null.
     * @param stateful
     *            If true, then the final URL should be encoded (with the session id) if necessary.
     *            If false, the session encoding should not occur. The latter case is useful for
     *            services that will absolutely not need any access to user-specific state.
     */
    public ILink constructLink(IRequestCycle cycle, boolean post, Map parameters, boolean stateful);

    /**
     * A secondary function of the service is to convert encoded (aka "squeezed") listener
     * parameters back into an array of Objects. This does (barely) makes sense .. the link factory
     * is responsible for encoding the listener parameters, it should be responsible for decoding
     * them.
     * 
     * @param cycle
     *            the current request cycle
     * @return an array of Object[]. May return an empty array, but won't return null.
     */

    public Object[] extractListenerParameters(IRequestCycle cycle);

    /**
     * Returns an array of {@link org.apache.tapestry.engine.ServiceEncoder}, ordering into
     * execution order. May return an empty array, but won't return null.
     */

    public ServiceEncoder[] getServiceEncoders();
}