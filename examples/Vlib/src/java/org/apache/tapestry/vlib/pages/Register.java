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

import javax.ejb.CreateException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.VirtualLibraryDelegate;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.ejb.RegistrationException;

/**
 * Invoked from the {@link Login} page, to allow a user to register into the system on-the-fly.
 * 
 * @author Howard Lewis Ship
 */

@Meta("page-type=Login")
public abstract class Register extends BasePage implements IErrorProperty
{
    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getEmail();

    public abstract String getPassword1();

    public abstract String getPassword2();

    public abstract void setPassword1(String value);

    public abstract void setPassword2(String value);

    @Bean(VirtualLibraryDelegate.class)
    public abstract IValidationDelegate getValidationDelegate();

    @InjectComponent("password1")
    public abstract IFormComponent getPassword1Field();

    @InjectComponent("password2")
    public abstract IFormComponent getPassword2Field();

    @Message
    public abstract String passwordMustMatch();

    @InjectPage("Login")
    public abstract Login getLogin();

    private void clear(IFormComponent field)
    {
        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent(field);
        delegate.recordFieldInputValue(null);
    }

    public void attemptRegister()
    {
        IValidationDelegate delegate = getValidationDelegate();

        String password1 = getPassword1();
        String password2 = getPassword2();

        setPassword1(null);
        setPassword2(null);

        clear(getPassword1Field());
        clear(getPassword1Field());

        if (delegate.getHasErrors())
            return;

        // Note: we know password1 and password2 are not null
        // because they are required fields.

        if (!password1.equals(password2))
        {
            delegate.record(getPassword1Field(), passwordMustMatch());
            return;
        }

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Login login = getLogin();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations bean = vengine.getOperations();
                Person user = bean.registerNewUser(
                        getFirstName(),
                        getLastName(),
                        getEmail(),
                        password1);

                // Ask the login page to return us to the proper place, as well
                // as set a cookie identifying the user for next time.

                login.loginUser(user);

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