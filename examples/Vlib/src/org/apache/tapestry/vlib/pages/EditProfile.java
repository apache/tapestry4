/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.ActivatePage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 *  Edit's a user's profile:  names, email and password.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class EditProfile extends ActivatePage implements PageRenderListener
{
    public abstract String getPassword1();

    public abstract void setPassword1(String value);

    public abstract String getPassword2();

    public abstract void setPassword2(String value);

    public abstract Map getAttributes();

    public abstract void setAttributes(Map attributes);

    /**
     *  Invoked (from {@link MyLibrary}) to begin editting the user's
     *  profile.  We get the entity attributes from the 
     *  {@link org.apache.tapestry.vlib.ejb.IPerson},
     *  and store them in the attributes page property, ready to provide
     *  default values to the {@link org.apache.tapestry.valid.ValidField} 
     *  components.
     *
     **/

    public void activate(IRequestCycle cycle)
    {
        Visit visit = (Visit) getVisit();
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();

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

        cycle.activate(this);
    }

    public void updateProfile(IRequestCycle cycle)
    {
        String password1 = getPassword1();
        String password2 = getPassword2();

        setPassword1(null);
        setPassword2(null);

        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent((IFormComponent) getComponent("inputPassword1"));
        delegate.recordFieldInputValue(null);

        delegate.setFormComponent((IFormComponent) getComponent("inputPassword2"));
        delegate.recordFieldInputValue(null);

        if (delegate.getHasErrors())
            return;

        Map attributes = getAttributes();

        if (Tapestry.isBlank(password1) != Tapestry.isBlank(password2))
        {
            setErrorField("inputPassword1", getMessage("enter-password-twice"));

            return;
        }

        if (Tapestry.isNonBlank(password1))
        {
            if (!password1.equals(password2))
            {
                setErrorField("inputPassword1", getMessage("password-must-match"));
                return;
            }

            attributes.put("password", password1);
        }

        Visit visit = (Visit) getVisit();
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();
        Integer userId = visit.getUserId();

        int i = 0;
        while (true)
        {
            try
            {
                /**
                 *  Note:  this allows the user to change thier e-mail
                 *  such that it conflicts with another user!  Need yet-another
                 *  IOperations method to perform the update!
                 *
                 **/

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

        MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");
        myLibrary.activate(cycle);
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
            setAttributes(new HashMap());
    }

}