//  Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.request.RequestContext;

/**
 * Interface for an object that can create a {@link IMonitor} instance
 * for a particular {@link org.apache.tapestry.request.RequestContext}.
 * The engine expects there to be a monitor factory
 * as application extension
 * <code>org.apache.tapestry.monitor-factory</code>.  If no such
 * extension exists, then {@link org.apache.tapestry.engine.DefaultMonitorFactory}
 * is used instead.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */
public interface IMonitorFactory
{
    /**
     * Create a new {@link IMonitor} instance.  Alternately, return a shared instance.
     * This method may be invoked by multiple threads.
     * 
     */

    public IMonitor createMonitor(RequestContext context);
}
