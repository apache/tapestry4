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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.servlet.http.Cookie;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.LoginException;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  Allows the user to login, by providing email address and password.
 *  After succesfully logging in, a cookie is placed on the client browser
 *  that provides the default email address for future logins (the cookie
 *  persists for a week).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Login extends BasePage implements IErrorProperty, PageRenderListener
{
    /**
     *  The name of a cookie to store on the user's machine that will identify
     *  them next time they log in.
     *
     **/

    private static final String COOKIE_NAME = "org.apache.tapestry.vlib.Login.email";

    private final static int ONE_WEEK = 7 * 24 * 60 * 60;

    public abstract void setEmail(String value);

    public abstract String getEmail();

    public abstract String getPassword();

    public abstract void setPassword(String password);

    protected IValidationDelegate getValidationDelegate()
    {
        return (IValidationDelegate) getBeans().getBean("delegate");
    }

    protected void setErrorField(String componentId, String message)
    {
        IFormComponent field = (IFormComponent) getComponent(componentId);

        IValidationDelegate delegate = getValidationDelegate();
        delegate.setFormComponent(field);
        delegate.record(new ValidatorException(message));
    }

    public abstract void setCallback(ICallback value);

    public abstract ICallback getCallback();

    /**
     *  Attempts to login. 
     *
     *  <p>If the user name is not known, or the password is invalid, then an error
     *  message is displayed.
     *
     **/

    public void attemptLogin(IRequestCycle cycle)
    {
        String password = getPassword();

        // Do a little extra work to clear out the password.

        setPassword(null);
        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent((IFormComponent) getComponent("inputPassword"));
        delegate.recordFieldInputValue(null);

        // An error, from a validation field, may already have occured.

        if (delegate.getHasErrors())
            return;

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                Person person = operations.login(getEmail(), password);

                loginUser(person, cycle);

                break;

            }
            catch (LoginException ex)
            {
                String fieldName = ex.isPasswordError() ? "inputPassword" : "inputEmail";

                setErrorField(fieldName, ex.getMessage());
                return;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception validating user.", ex, i++);
            }
        }
    }

    /**
     *  Sets up the {@link Person} as the logged in user, creates
     *  a cookie for thier email address (for subsequent logins),
     *  and redirects to the appropriate page ({@link MyLibrary}, or
     *  a specified page).
     *
     **/

    public void loginUser(Person person, IRequestCycle cycle) throws RemoteException
    {
        String email = person.getEmail();

        // Get the visit object; this will likely force the
        // creation of the visit object and an HttpSession.

        Visit visit = (Visit) getVisit();
        visit.setUser(person);

        // After logging in, go to the MyLibrary page, unless otherwise
        // specified.

        ICallback callback = getCallback();

        if (callback == null)
            cycle.activate("Home");
        else
            callback.performCallback(cycle);

        // I've found that failing to set a maximum age and a path means that
        // the browser (IE 5.0 anyway) quietly drops the cookie.

        IEngine engine = getEngine();
        Cookie cookie = new Cookie(COOKIE_NAME, email);
        cookie.setPath(engine.getServletPath());
        cookie.setMaxAge(ONE_WEEK);

        // Record the user's email address in a cookie

        cycle.getRequestContext().addCookie(cookie);

        engine.forgetPage(getPageName());
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getEmail() == null)
            setEmail(getRequestCycle().getRequestContext().getCookieValue(COOKIE_NAME));
    }

}