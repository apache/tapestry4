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

import javax.ejb.CreateException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.ejb.RegistrationException;

/**
 *  Invoked from the {@link Login} page, to allow a user to register
 *  into the system on-the-fly.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Register extends BasePage implements IErrorProperty
{

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getEmail();

    public abstract String getPassword1();

    public abstract String getPassword2();

    public abstract void setPassword1(String value);

    public abstract void setPassword2(String value);

    private IValidationDelegate getValidationDelegate()
    {
        return (IValidationDelegate) getBeans().getBean("delegate");
    }

    private void setErrorField(String componentId, String message)
    {
        IValidationDelegate delegate = getValidationDelegate();
        IFormComponent field = (IFormComponent) getComponent(componentId);

        delegate.setFormComponent(field);
        delegate.record(message, null);
    }

    private void clear(String componentId)
    {
        IValidationDelegate delegate = getValidationDelegate();
        IFormComponent component = (IFormComponent) getComponent(componentId);

        delegate.setFormComponent(component);
        delegate.recordFieldInputValue(null);
    }

    public void attemptRegister(IRequestCycle cycle)
    {
        IValidationDelegate delegate = getValidationDelegate();

        String password1 = getPassword1();
        String password2 = getPassword2();

        setPassword1(null);
        setPassword2(null);

        clear("inputPassword1");
        clear("inputPassword2");

        if (delegate.getHasErrors())
            return;
        // Note: we know password1 and password2 are not null
        // because they are required fields.

        if (!password1.equals(password2))
        {
            setErrorField("inputPassword1", getMessage("password-must-match"));
            return;
        }

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Login login = (Login) cycle.getPage("Login");

        int i = 0;
        while (true)
        {
            try
            {
                IOperations bean = vengine.getOperations();
                Person user =
                    bean.registerNewUser(getFirstName(), getLastName(), getEmail(), password1);

                // Ask the login page to return us to the proper place, as well
                // as set a cookie identifying the user for next time.

                login.loginUser(user, cycle);

                break;
            }
            catch (RegistrationException ex)
            {
                setError(ex.getMessage());
                return;
            }
            catch (CreateException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception registering new user.", ex, i++);
            }
        }
    }
}