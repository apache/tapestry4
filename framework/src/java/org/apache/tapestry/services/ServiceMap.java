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

import org.apache.tapestry.engine.IEngineService;

/**
 * Service interface for <code>tapestry.services.ServiceMap</code>, provides access to engine
 * services defined in the HiveMind module deployment descriptors.
 * <p>
 * Note: In Tapestry 3.0 and earlier, a &lt;service&gt; element in the application and library
 * specifications was used to define services. This is no longer supported.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ServiceMap
{
    /**
     * Returns the named service.
     * 
     * @param name
     *            the unique name for the service, as defined by each service instance's
     *            {@link IEngineService#getName()}method.
     * @returns the named service
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the named service does not exist
     */
    public IEngineService getService(String name);
}