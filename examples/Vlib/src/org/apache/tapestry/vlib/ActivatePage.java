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

import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.vlib.pages.Login;

/**
 *  Subclass of {@link org.apache.tapestry.vlib.Protected} that
 *  implements {@link org.apache.tapestry.vlib.IActivate}.  Overrides
 *  {@link #validate(org.apache.tapestry.IRequestCycle)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class ActivatePage extends Protected implements IActivate, PageValidateListener
{
    public void pageValidate(PageEvent event)
    {
        Visit visit = (Visit) getVisit();

        if (visit != null && visit.isUserLoggedIn())
            return;

        // User not logged in ... redirect through the Login page.

        Login login = (Login) getRequestCycle().getPage("Login");

        login.setCallback(new ActivateCallback(this));

        throw new PageRedirectException(login);
    }
}
