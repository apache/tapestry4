/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.valid.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.*;
import javax.rmi.*;

// Appease Javadoc
import com.primix.tapestry.valid.ValidatingTextField;

/**
 * Edit's a user's profile:  names, email and password.  
 *
 * @author Howard Ship
 * @version $Id$
 */

public class EditProfile extends Protected
{
	private Map attributes;
	private String password1;
	private String password2;
	private boolean cancel;

	private static final int MAP_SIZE = 11;

	public void detach()
	{
		attributes = null;
		password1 = null;
		password2 = null;
		cancel = false;

		super.detach();
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

	public boolean getCancel()
	{
		return cancel;
	}

	public void setCancel(boolean value)
	{
		cancel = value;
	}

	public Map getAttributes()
	{
		if (attributes == null)
			attributes = new HashMap(MAP_SIZE);

		return attributes;
	}

	/**
	 *  Invoked (from {@link MyLibrary}) to begin editting the user's
	 *  profile.  We get the entity attributes from the {@link IPerson},
	 *  and store them in the attributes page property, ready to provide
	 *  default values to the {@link ValidatingTextField} components.
	 *
	 */

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

	/**
	 *  Invoked when the form is submitted, validates the form and
	 *  updates the {@link IPerson} for the user, before returning
	 *  to {@link MyLibrary}.
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
		if (cancel)
		{
			cycle.setPage("MyLibrary");
			return;
		}

		// Possibly one of the validating text fields found an error.

		if (getError() != null)
		{
			resetPasswords();
			return;
		}

		if (Tapestry.isNull(password1) != Tapestry.isNull(password2))
		{
			setErrorField(
				"inputPassword1",
				"Enter the password, then re-enter it to confirm.");

			resetPasswords();
			return;
		}

		if (!Tapestry.isNull(password1))
		{
			if (!password1.equals(password2))
			{
				setErrorField("inputPassword1", "Enter the same password in both fields.");
				resetPasswords();
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
				 */

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

	private void resetPasswords()
	{
		IValidatingTextField field;

		password1 = null;
		password2 = null;

		field = (IValidatingTextField) getComponent("inputPassword1");
		field.refresh();

		field = (IValidatingTextField) getComponent("inputPassword2");
		field.refresh();
	}
}