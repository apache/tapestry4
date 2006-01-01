// Copyright 2004, 2005, 2006 The Apache Software Foundation
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
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.services.RemoteCallback;

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

    @InjectComponent("password1")
    public abstract IFormComponent getPassword1Field();

    @InjectComponent("password2")
    public abstract IFormComponent getPassword2Field();

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

        final Integer userId = visit.getUserId();

        RemoteCallback<Map> callback = new RemoteCallback()
        {
            public Map doRemote() throws RemoteException
            {
                try
                {
                    return getOperations().getPersonAttributes(userId);
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        Map attributes = getRemoteTemplate().execute(callback, "Error reading user information.");

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

        delegate.setFormComponent(getPassword1Field());
        delegate.recordFieldInputValue(null);

        delegate.setFormComponent(getPassword2Field());
        delegate.recordFieldInputValue(null);

        if (delegate.getHasErrors())
            return;

        final Map attributes = getAttributes();

        if (HiveMind.isBlank(password1) != HiveMind.isBlank(password2))
        {
            delegate.record(getPassword1Field(), enterPasswordTwice());

            return;
        }

        if (HiveMind.isNonBlank(password1))
        {
            if (!password1.equals(password2))
            {
                delegate.record(getPassword1Field(), passwordMustMatch());
                return;
            }

            attributes.put("password", password1);
        }

        Visit visit = getVisitState();
        final Integer userId = visit.getUserId();

        RemoteCallback callback = new RemoteCallback()
        {
            public Object doRemote() throws RemoteException
            {
                try
                {
                    getOperations().updatePerson(userId, attributes);

                    return null;
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        getRemoteTemplate().execute(callback, "Error updating user attributes.");

        getModelSource().clear();

        getMyLibrary().activate();
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
            setAttributes(new HashMap());
    }

}