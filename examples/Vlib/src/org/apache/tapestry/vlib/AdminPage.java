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

package org.apache.tapestry.vlib;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.vlib.pages.Login;

/**
 *  Base page for any pages restricted to administrators.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class AdminPage extends Protected implements IMessageProperty
{

    public void pageValidate(PageEvent event)
    {
        IRequestCycle cycle = event.getRequestCycle();
        Visit visit = (Visit) getEngine().getVisit();

        if (visit == null || !visit.isUserLoggedIn())
        {
            Login login = (Login) cycle.getPage("Login");

            login.setCallback(new PageCallback(this));

            throw new PageRedirectException(login);
        }

        if (!visit.getUser(cycle).isAdmin())
        {
            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

            vengine.presentError("That function is restricted to adminstrators.", cycle);

            throw new PageRedirectException(cycle.getPage());
        }
    }
}