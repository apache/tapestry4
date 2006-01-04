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
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectMeta;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.InjectStateFlag;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.pages.Login;
import org.apache.tapestry.vlib.services.ModelSource;

/**
 * Base page used for pages that should be protected by the {@link Login} page. If the user is not
 * logged in, they are redirected to the Login page first. Also, implements an error property and a
 * validationDelegate.
 * <p>
 * Meta properties (overridden in some subclasses)
 * <dl>
 * <dt>anonymous-access=false</dt>
 * <dd>If true, the page may be accessed without the user being logged in.</dd>
 * <dt>admin-page=false</dt>
 * <dd>If true, the user must be logged in as an administrator to access this page.</dd>
 * </dl>
 * 
 * @author Howard Lewis Ship
 */

@Meta(
{ "anonymous-access=false", "admin-page=false" })
public abstract class VlibPage extends BasePage implements IErrorProperty, IMessageProperty,
        PageValidateListener, OperationsUser
{
    @Bean(VirtualLibraryDelegate.class)
    public abstract IValidationDelegate getValidationDelegate();

    @InjectState("visit")
    public abstract Visit getVisitState();

    @InjectStateFlag("visit")
    public abstract boolean getVisitStateExists();

    @InjectPage("Login")
    public abstract Login getLogin();

    @InjectMeta("anonymous-access")
    public abstract boolean getAllowAnonymousAccess();

    @InjectMeta("admin-page")
    public abstract boolean isAdminPage();

    @InjectObject("service:vlib.ModelSource")
    public abstract ModelSource getModelSource();

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

    /**
     * Checks if the user is logged in. If not, they are sent to the {@link Login} page before
     * coming back to whatever this page is.
     */

    public void pageValidate(PageEvent event)
    {
        if (isAdminPage())
            ensureUserIsLoggedInAsAdmin();

        if (!getAllowAnonymousAccess())
            ensureUserIsLoggedIn();
    }

    private void ensureUserIsLoggedIn()
    {
        if (isUserLoggedIn())
            return;

        // User not logged in ... redirect through the Login page.

        Login login = getLogin();

        login.setCallback(new PageCallback(this));

        throw new PageRedirectException(login);
    }

    /**
     * Returns true if the {@link Visit} exists, and the user is logged in as well.
     * 
     * @return true if logged in
     */
    protected final boolean isUserLoggedIn()
    {
        return getVisitStateExists() && getVisitState().isUserLoggedIn();
    }

    protected void ensureUserIsLoggedInAsAdmin()
    {
        if (!isUserLoggedIn())
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