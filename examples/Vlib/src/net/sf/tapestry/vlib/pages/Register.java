//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.IField;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.valid.ValidatorException;
import net.sf.tapestry.vlib.IErrorProperty;
import net.sf.tapestry.vlib.SimpleValidationDelegate;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.ejb.Person;
import net.sf.tapestry.vlib.ejb.RegistrationException;

/**
 *  Invoked from the {@link Login} page, to allow a user to register
 *  into the system on-the-fly.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Register extends BasePage implements IErrorProperty
{
    private String error;
    private String firstName;
    private String lastName;
    private String email;
    private String password1;
    private String password2;
    private IValidationDelegate validationDelegate;

    public void detach()
    {
        error = null;
        firstName = null;
        lastName = null;
        email = null;
        password1 = null;
        password2 = null;

        super.detach();
    }

    public String getError()
    {
        return error;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    /** Passwords are read-only. **/

    public String getPassword1()
    {
        return null;
    }

    /** Passwords are read-only. **/

    public String getPassword2()
    {
        return null;
    }

    public void setError(String value)
    {
        error = value;
    }

    public void setFirstName(String value)
    {
        firstName = value;
    }

    public void setLastName(String value)
    {
        lastName = value;
    }

    public void setEmail(String value)
    {
        email = value;
    }

    public void setPassword1(String value)
    {
        password1 = value;
    }

    public void setPassword2(String value)
    {
        password2 = value;
    }

    public IValidationDelegate getValidationDelegate()
    {
        if (validationDelegate == null)
            validationDelegate = new SimpleValidationDelegate();

        return validationDelegate;
    }

    private void setErrorField(String componentId, String message)
    {
        IValidationDelegate delegate = getValidationDelegate();
        IField field;

        field = (IField) getComponent(componentId);

        delegate.setFormComponent(field);
        delegate.record(new ValidatorException(message));
    }

    public void attemptRegister(IRequestCycle cycle) throws RequestCycleException
    {
        IValidationDelegate delegate = getValidationDelegate();

        if (delegate.getHasErrors())
            return;

        // Note: we know password1 and password2 are not null
        // because they are required fields.

        if (!password1.equals(password2))
        {
            setErrorField("inputPassword1", "Enter the same password twice.");
            return;
        }

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;
        Login login = (Login) cycle.getPage("Login");

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations bean = vengine.getOperations();
                Person user = bean.registerNewUser(firstName, lastName, email, password1);

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
                vengine.rmiFailure("Remote exception registering new user.", ex, i > 0);
            }
        }
    }
}