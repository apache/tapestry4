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

package com.primix.vlib.pages.admin;

import com.primix.tapestry.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import com.primix.vlib.*;
import com.primix.vlib.ejb.*;

/**
 *  Allows editting of the users.  Simple flags about the
 *  user can be changed; additionally, the user can have their
 *  password reset (to a random value, which is mailed to them),
 *  or the user can be out-right deleted.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class EditUsers extends AdminPage
{
	private Person[] users;

	/**
	 *  Map of users, keyed on Person primaryKey.
	 *
	 */

	private Map userMap;

	/**
	 *  The PK of the current user being editted.
	 *
	 */

	private Integer userKey;

	/**
	 *  The Person corresponding to userKey.
	 *
	 */

	private Person user;

	/**
	 *  List of Person PKs of users to have passwords reset.
	 *
	 */

	private List resetPassword;

	/**
	 *  List of Person PKs, of users to be removed.
	 *
	 */

	private List deleteUser;

	public void detach()
	{
		users = null;
		userMap = null;
		userKey = null;
		user = null;
		resetPassword = null;
		deleteUser = null;

		super.detach();
	}

	public void beginResponse(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		super.beginResponse(writer, cycle);

		readUsers();
	}

	private void readUsers()
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;

		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();

				users = operations.getPersons();

				break;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Unable to retrieve list of users.", ex, i > 0);
			}
		}

		userMap = new HashMap();

		for (int i = 0; i < users.length; i++)
			userMap.put(users[i].getPrimaryKey(), users[i]);
	}

	/**
	 *  Returns the primary keys of all the Persons, in a sort order (by last name, then first name).
	 *
	 */

	public Integer[] getUserKeys()
	{
		Integer[] result = new Integer[users.length];

		for (int i = 0; i < users.length; i++)
			result[i] = users[i].getPrimaryKey();

		return result;
	}

	/**
	 *  Sets the user property from the primary key (value parameter).
	 *
	 */

	public void setUserKey(Integer value)
	{
		userKey = value;

		if (users == null)
			readUsers();

		user = (Person) userMap.get(userKey);

		// Latent bug:  what if the user was deleted between the time the form was rendered and 
		// now?  user will be null, which will trip up some of the components.
	}

	public Person getUser()
	{
		return user;
	}

	public boolean getResetPassword()
	{
		return false;
	}

	public void setResetPassword(boolean value)
	{
		if (value)
		{
			if (resetPassword == null)
				resetPassword = new ArrayList();

			resetPassword.add(userKey);
		}
	}

	public boolean getDeleteUser()
	{
		return false;
	}

	public void setDeleteUser(boolean value)
	{
		if (value)
		{
			if (deleteUser == null)
				deleteUser = new ArrayList();

			deleteUser.add(userKey);

			// Remove the user from the userMap ... this will prevent it from
			// being included in the update list.

			userMap.remove(userKey);
		}
	}

	/**
	 *  Invoked when the form is submitted.
	 *
	 */

	public void updateUsers(IRequestCycle cycle)
	{
		Visit visit = (Visit) getVisit();
		VirtualLibraryEngine vengine = visit.getEngine();

		// Collection of non-deleted persons.

		Collection updatedPersons = userMap.values();

		Person[] updated =
			(Person[]) updatedPersons.toArray(new Person[updatedPersons.size()]);

		Integer[] resetPasswordArray = toArray(resetPassword);
		Integer[] deleted = toArray(deleteUser);

		Integer adminPK = visit.getUserPK();

		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();

				operations.updatePersons(updated, resetPasswordArray, deleted, adminPK);

				break;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Unable to update users.", ex, i > 0);
			}
			catch (RemoveException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (FinderException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
		}

		setMessage("Users updated.");

		users = null;
		userMap = null;

	}

	private Integer[] toArray(List list)
	{
		if (list == null)
			return null;

		if (list.size() == 0)
			return null;

		return (Integer[]) list.toArray(new Integer[list.size()]);
	}
}