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

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.RequestContext;

/**
 * A factory for creating instances of {@link org.apache.tapestry.IRequestCycle}for the current
 * request.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface RequestCycleFactory
{
    /**
     * Constructs the new instance using the request context. This includes accessing
     * {@link org.apache.tapestry.engine.ServiceEncoder}s to restore any missing query parameters
     * (that were encoding into the URL).
     */

    public IRequestCycle newRequestCycle(IEngine engine, RequestContext context);
}