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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.Protected;
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

public class EditProfile extends Protected
{
    private Map attributes;
    private String password1;
    private String password2;

    private static final int MAP_SIZE = 11;

    public void detach()
    {
        attributes = null;
        password1 = null;
        password2 = null;

        super.detach();
    }

    /** Passwords are read-only. **/

    public String getPassword1()
    {
        return null;
    }

    public void setPassword1(String value)
    {
        password1 = value;
    }

    /** Passwords are read-only. **/

    public String getPassword2()
    {
        return null;
    }

    public void setPassword2(String value)
    {
        password2 = value;
    }

    public Map getAttributes()
    {
        if (attributes == null)
            attributes = new HashMap(MAP_SIZE);

        return attributes;
    }

    /**
     *  Invoked (from {@link MyLibrary}) to begin editting the user's
     *  profile.  We get the entity attributes from the 
     *  {@link org.apache.tapestry.vlib.ejb.IPerson},
     *  and store them in the attributes page property, ready to provide
     *  default values to the {@link org.apache.tapestry.valid.ValidField} 
     *  components.
     *
     **/

    public void beginEdit(IRequestCycle cycle)
    {
        Visit visit = (Visit) getVisit();
        VirtualLibraryEngine vengine = visit.getEngine();

        Integer primaryKey = visit.getUserPK();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                attributes = operations.getPersonAttributes(primaryKey);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception reading user.", ex, i > 0);
            }
        }

        attributes.remove("password");

        cycle.setPage(this);
    }

    public void updateProfile(IRequestCycle cycle)
    {
        IValidationDelegate delegate = getValidationDelegate();

        if (delegate.getHasErrors())
            return;

        if (Tapestry.isNull(password1) != Tapestry.isNull(password2))
        {
            setErrorField("inputPassword1", "Enter the password, then re-enter it to confirm.");

            return;
        }

        if (!Tapestry.isNull(password1))
        {
            if (!password1.equals(password2))
            {
                setErrorField("inputPassword1", "Enter the same password in both fields.");
                return;
            }

            attributes.put("password", password1);
        }

        Visit visit = (Visit) getVisit();
        VirtualLibraryEngine vengine = visit.getEngine();
        Integer primaryKey = visit.getUserPK();

        for (int i = 0; i < 2; i++)
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

                operations.updatePerson(primaryKey, attributes);
                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception updating user attributes.", ex, i > 0);
            }
        }

        visit.clearCache();

        cycle.setPage("MyLibrary");
    }

}