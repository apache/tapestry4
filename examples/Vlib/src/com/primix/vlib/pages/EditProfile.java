package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.valid.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.*;
import javax.rmi.*;
import com.primix.foundation.prop.*;

// To appease Javadoc
import com.primix.tapestry.components.html.valid.ValidatingTextField;

/*
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 * Edit's a user's profile:  names, email and password.  
 *
 * @author Howard Ship
 * @version $Id$
 */


public class EditProfile
extends Protected
{
	private Map attributes;	
	private String password1;
	private String password2;
	
	private static final int MAP_SIZE = 11;
	
	public void detach()
	{
		super.detach();

		attributes = null;
		password1 = null;
		password2 = null;
	}
		
	public String getPassword1()
	{
		return password1;
	}
	
	public void setPassword1(String value)
	{
		password1 = value;
	}

	public String getPassword2()
	{
		return password2;
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
	 *  Invoked (from {@link MyBooks}) to begin editting the user's
	 *  profile.  We get the entity attributes from the {@link IPerson},
	 *  and store them in the attributes page property, ready to provide
	 *  default values to the {@link ValidatingTextField} components.
	 *
	 */
	 
	public void beginEdit(IRequestCycle cycle)
	{
        Visit visit = (Visit)getVisit();
		
		try
		{
			attributes = visit.getUser().getEntityAttributes();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		attributes.remove("password");
		
		cycle.setPage(this);	
	}
	
	/**
	 *  Invoked when the form is submitted, validates the form and
	 *  updates the {@link IPerson} for the user, before returning
	 *  to {@link MyBooks}.
	 *
	 */
	 
	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				// Update the current user, or set an error message.
				
				updateProfile(cycle);
			}
		};
	}
	
	private void updateProfile(IRequestCycle cycle)
	{
        // Possibly one of the validating text fields found an error.

        if (getError() != null)
        {
            resetPasswords();
            return;
        }
	
		if (isEmpty(password1) != isEmpty(password2))
		{
			setErrorField("inputPassword1", 
			    "Enter the password, then re-enter it to confirm.");

            resetPasswords();
			return;
		}
		
		if (!isEmpty(password1))
		{
			if (!password1.equals(password2))
			{
				setErrorField("inputPassword1",
				    "Enter the same password in both fields.");
				resetPasswords();
				return;
			}
			
			attributes.put("password", password1);
		}
		
        Visit visit = (Visit)getVisit();
        		
		try
		{
			/**
			 *  Note:  this allows the user to change thier e-mail
			 *  such that it conflicts with another user!  Need yet-another
			 *  IOperations method to perform the update!
			 *
			 */
			 
			visit.getUser().updateEntityAttributes(attributes);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		visit.clearCache();
		
		cycle.setPage("MyBooks");
	}
	
	private boolean isEmpty(String value)
	{
		if (value == null)
			return true;
		
		if (value.trim().length() == 0)
			return true;
			
		return false;
	}	

    private void resetPasswords()
    {
        IValidatingTextField field;

        password1 = null;
        password2 = null;

        field = (IValidatingTextField)getComponent("inputPassword1");
        field.refresh();

        field = (IValidatingTextField)getComponent("inputPassword2");
        field.refresh();
    }
}