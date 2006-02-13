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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.services.CookieSource;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.VirtualLibraryDelegate;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.VlibPage;
import org.apache.tapestry.vlib.ejb.LoginException;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Allows the user to login, by providing email address and password. After succesfully logging in,
 * a cookie is placed on the client browser that provides the default email address for future
 * logins (the cookie persists for a week).
 * 
 * @author Howard Lewis Ship
 */

@Meta("anonymous-access=true")
public abstract class Login extends VlibPage implements PageBeginRenderListener
{
    /**
     * The name of a cookie to store on the user's machine that will identify them next time they
     * log in.
     */

    private static final String COOKIE_NAME = "org.apache.tapestry.vlib.Login.email";

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    @Bean(VirtualLibraryDelegate.class)
    public abstract IValidationDelegate getValidationDelegate();

    public abstract void setEmail(String value);

    public abstract String getEmail();

    public abstract String getPassword();

    public abstract void setPassword(String password);

    @InjectObject("infrastructure:cookieSource")
    public abstract CookieSource getCookieSource();

    @Persist("client:app")
    public abstract ICallback getCallback();

    public abstract void setCallback(ICallback value);

    @InjectComponent("email")
    public abstract IFormComponent getEmailField();

    @InjectComponent("password")
    public abstract IFormComponent getPasswordField();

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    /**
     * Attempts to login.
     * <p>
     * If the user name is not known, or the password is invalid, then an error message is
     * displayed.
     */

    public void attemptLogin(IRequestCycle cycle)
    {
        final String password = getPassword();

        // Do a little extra work to clear out the password.

        setPassword(null);
        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent(getPasswordField());
        delegate.recordFieldInputValue(null);

        // An error, from a validation field, may already have occured.

        if (delegate.getHasErrors())
            return;

        RemoteCallback callback = new RemoteCallback()
        {
            public Object doRemote() throws RemoteException
            {

                try
                {
                    Person person = getOperations().login(getEmail(), password);

                    loginUser(person);

                    return null;
                }
                catch (LoginException ex)
                {
                    IFormComponent field = ex.isPasswordError() ? getPasswordField()
                            : getEmailField();

                    getValidationDelegate().record(field, ex.getMessage());

                    return null;
                }

            }
        };

        getRemoteTemplate().execute(callback, "Error validating user.");

    }

    /**
     * Sets up the {@link Person} as the logged in user, creates a cookie for thier email address
     * (for subsequent logins), and redirects to the appropriate page ({@link MyLibrary}, or a
     * specified page).
     */

    public void loginUser(Person person)
    {
        IRequestCycle cycle = getRequestCycle();

        String email = person.getEmail();

        // Get the visit object; this will likely force the
        // creation of the visit object and an HttpSession.

        Visit visit = getVisitState();
        visit.setUser(person);

        // After logging in, go to the Home page, unless otherwise
        // specified.

        ICallback callback = getCallback();

        if (callback == null)
            getMyLibrary().activate();
        else
            callback.performCallback(cycle);

        getCookieSource().writeCookieValue(COOKIE_NAME, email, COOKIE_MAX_AGE);

        cycle.forgetPage(getPageName());
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getEmail() == null)
            getCookieSource().readCookieValue(COOKIE_NAME);
    }

}
