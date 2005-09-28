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

import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.pages.Login;

/**
 * Base page used for pages that should be protected by the {@link Login} page. If the user is not
 * logged in, they are redirected to the Login page first. Also, implements an error property and a
 * validationDelegate.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Protected extends BasePage implements IErrorProperty, PageValidateListener
{
    @Bean(VirtualLibraryDelegate.class)
    public abstract IValidationDelegate getValidationDelegate();

    protected void setErrorField(String componentId, String message)
    {
        IFormComponent component = (IFormComponent) getComponent(componentId);

        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent(component);
        delegate.record(message, null);
    }

    /**
     * Returns true if the delegate indicates an error, or the error property is not null.
     */

    protected boolean isInError()
    {
        return getError() != null || getValidationDelegate().getHasErrors();
    }

    @InjectState("visit")
    public abstract Visit getVisitState();

    @InjectPage("Login")
    public abstract Login getLogin();

    /**
     * Checks if the user is logged in. If not, they are sent to the {@link Login} page before
     * coming back to whatever this page is.
     */

    public void pageValidate(PageEvent event)
    {
        Visit visit = getVisitState();

        if (visit.isUserLoggedIn())
            return;

        // User not logged in ... redirect through the Login page.

        Login login = getLogin();

        login.setCallback(new PageCallback(this));

        throw new PageRedirectException(login);
    }
}