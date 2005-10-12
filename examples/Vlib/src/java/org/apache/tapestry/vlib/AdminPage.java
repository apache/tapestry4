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

package org.apache.tapestry.vlib;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.InjectStateFlag;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.vlib.pages.Login;

/**
 * Base page for any pages restricted to administrators.
 * 
 * @author Howard Lewis Ship
 */

public abstract class AdminPage extends Protected implements IMessageProperty
{
    @InjectState("visit")
    public abstract Visit getVisitState();

    @InjectStateFlag("visit")
    public abstract boolean getVisitStateExists();

    @InjectPage("Login")
    public abstract Login getLogin();

    public void pageValidate(PageEvent event)
    {
        boolean loggedIn = getVisitStateExists() && getVisitState().isUserLoggedIn();

        if (!loggedIn)
        {
            Login login = getLogin();

            login.setCallback(new PageCallback(this));

            throw new PageRedirectException(login);
        }

        IRequestCycle cycle = getRequestCycle();

        if (!getVisitState().getUser().isAdmin())
        {
            getErrorPresenter().presentError(
                    "That function is restricted to administrators.",
                    cycle);

            throw new PageRedirectException(cycle.getPage());
        }
    }
}