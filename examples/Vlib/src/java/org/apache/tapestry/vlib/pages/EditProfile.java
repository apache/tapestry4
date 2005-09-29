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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.ActivatePage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 * Edit's a user's profile: names, email and password.
 * 
 * @author Howard Lewis Ship
 */

public abstract class EditProfile extends ActivatePage implements PageBeginRenderListener
{
    public abstract String getPassword1();

    public abstract void setPassword1(String value);

    public abstract String getPassword2();

    public abstract void setPassword2(String value);

    public abstract Map getAttributes();

    public abstract void setAttributes(Map attributes);

    @InjectState("visit")
    public abstract Visit getVisitState();

    @InjectComponent("inputPassword1")
    public abstract IFormComponent getInputPassword1();

    @InjectComponent("inputPassword2")
    public abstract IFormComponent getInputPassword2();

    @Message
    public abstract String enterPasswordTwice();

    @Message
    public abstract String passwordMustMatch();

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    /**
     * Invoked (from {@link MyLibrary}) to begin editting the user's profile. We get the entity
     * attributes from the {@link org.apache.tapestry.vlib.ejb.IPerson}, and store them in the
     * attributes page property, ready to provide default values to the
     * {@link org.apache.tapestry.valid.ValidField} components.
     */

    public void activate()
    {
        Visit visit = getVisitState();
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getRequestCycle().getEngine();

        Integer userId = visit.getUserId();
        Map attributes = null;

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                attributes = operations.getPersonAttributes(userId);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception reading user.", ex, i++);
            }
        }

        attributes.remove("password");
        setAttributes(attributes);

        getRequestCycle().activate(this);
    }

    public void updateProfile(IRequestCycle cycle)
    {
        String password1 = getPassword1();
        String password2 = getPassword2();

        setPassword1(null);
        setPassword2(null);

        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent(getInputPassword1());
        delegate.recordFieldInputValue(null);

        delegate.setFormComponent(getInputPassword2());
        delegate.recordFieldInputValue(null);

        if (delegate.getHasErrors())
            return;

        Map attributes = getAttributes();

        if (HiveMind.isBlank(password1) != HiveMind.isBlank(password2))
        {
            setErrorField("inputPassword1", enterPasswordTwice());

            return;
        }

        if (HiveMind.isNonBlank(password1))
        {
            if (!password1.equals(password2))
            {
                setErrorField("inputPassword1", passwordMustMatch());
                return;
            }

            attributes.put("password", password1);
        }

        Visit visit = getVisitState();
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();
        Integer userId = visit.getUserId();

        int i = 0;
        while (true)
        {
            try
            {
                /**
                 * Note: this allows the user to change thier e-mail such that it conflicts with
                 * another user! Need yet-another IOperations method to perform the update!
                 */

                IOperations operations = vengine.getOperations();

                operations.updatePerson(userId, attributes);
                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception updating user attributes.", ex, i++);
            }
        }

        vengine.clearCache();

        getMyLibrary().activate();
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
            setAttributes(new HashMap());
    }

}