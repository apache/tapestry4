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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.Visit;

/**
 * Implementation of {@link org.apache.tapestry.vlib.services.ErrorPresenter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ErrorPresenterImpl implements ErrorPresenter
{
    private ApplicationStateManager _stateManager;

    public void setStateManager(ApplicationStateManager stateManager)
    {
        _stateManager = stateManager;
    }

    public void presentError(String message, IRequestCycle cycle)
    {
        String pageName = isLoggedIn() ? "MyLibrary" : "Home";

        IErrorProperty page = (IErrorProperty) cycle.getPage(pageName);

        page.setError(message);

        cycle.activate(page);
    }

    private boolean isLoggedIn()
    {
        if (_stateManager.exists("visit"))
        {
            Visit visit = (Visit) _stateManager.get("visit");

            return visit.isUserLoggedIn();
        }

        return false;
    }
}
