/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.SimpleValidationDelegate;
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
        IFormComponent field = (IFormComponent) getComponent(componentId);

        delegate.setFormComponent(field);
        delegate.record(new ValidatorException(message));
    }

    public void attemptRegister(IRequestCycle cycle)
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

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
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