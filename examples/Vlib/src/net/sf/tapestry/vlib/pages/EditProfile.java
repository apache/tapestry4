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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.FinderException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.vlib.Protected;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.ejb.IOperations;

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
     *  {@link net.sf.tapestry.vlib.ejb.IPerson},
     *  and store them in the attributes page property, ready to provide
     *  default values to the {@link net.sf.tapestry.valid.ValidField} 
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
            setErrorField("inputPassword1", "Enter the password, then re-enter it to confirm.", null);

            return;
        }

        if (!Tapestry.isNull(password1))
        {
            if (!password1.equals(password2))
            {
                setErrorField("inputPassword1", "Enter the same password in both fields.", null);
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