// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.vlib.services;

import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.vlib.Visit;

/**
 * Implementation of service vlib.ApplicationLifecycle. Uses the threaded model.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ApplicationLifecycleImpl implements ApplicationLifecycle
{
    private boolean _discardSession;

    private ApplicationStateManager _stateManager;

    public void setStateManager(ApplicationStateManager stateManager)
    {
        _stateManager = stateManager;
    }

    public void logout()
    {
        _discardSession = true;

        if (_stateManager.exists("visit"))
        {
            Visit visit = (Visit) _stateManager.get("visit");
            visit.setUser(null);
        }
    }

    public boolean getDiscardSession()
    {
        return _discardSession;
    }

}
