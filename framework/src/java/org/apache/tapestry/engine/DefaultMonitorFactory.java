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

import org.apache.tapestry.web.WebRequest;

/**
 * Implementation of {@link org.apache.tapestry.engine.IMonitorFactory}&nbsp;that returns the
 * {@link org.apache.tapestry.engine.NullMonitor}.
 * 
 * @author Howard Lewis Ship
 */
public class DefaultMonitorFactory implements IMonitorFactory
{
    private final IMonitor _shared = new NullMonitor();

    /**
     * Returns a shared instance of {@link NullMonitor}.
     */
    public IMonitor createMonitor(WebRequest request)
    {
        return _shared;
    }
}