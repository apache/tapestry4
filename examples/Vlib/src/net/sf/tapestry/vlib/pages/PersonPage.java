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

package net.sf.tapestry.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import net.sf.tapestry.vlib.ejb.*;
import net.sf.tapestry.vlib.*;
import net.sf.tapestry.vlib.components.*;

import net.sf.tapestry.*;
import net.sf.tapestry.html.*;
import net.sf.tapestry.spec.*;

import javax.ejb.*;
import java.util.*;
import javax.rmi.*;
import java.rmi.*;

/**
 * Displays the book inventory list for a single {@link IPerson}, showing
 * what books are owned by the person, who has them borrowed, etc.  If the
 * user is logged in, then books can be borrowed from this page as well.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class PersonPage extends BasePage implements IExternalPage
{
	private IBookQuery query;
	private Book currentMatch;
	private Person person;
	private Browser browser;

	public void detach()
	{
		query = null;
		currentMatch = null;
		person = null;

		super.detach();
	}

	public void finishLoad(IPageLoader loader, ComponentSpecification spec)
		throws PageLoaderException
	{
		super.finishLoad(loader, spec);

		browser = (Browser) getComponent("browser");
	}

	public void setPerson(Person value)
	{
		person = value;

		fireObservedChange("person", value);
	}

	public Person getPerson()
	{
		return person;
	}

	public String getEmailURL()
	{
		return "mailto:" + person.getEmail();
	}

	/**
	 *  Gets the {@link IBookQuery} session bean that contains
	 *  the books owned by the user, creating it fresh as needed.
	 *
	 */

	public IBookQuery getQuery()
	{
		if (query == null)
		{
			VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;
			setQuery(vengine.createNewQuery());
		}

		return query;
	}

	/**
	 *  Sets the query persistent page property.
	 *
	 */

	public void setQuery(IBookQuery value)
	{
		query = value;

		fireObservedChange("query", value);
	}

	/**
	 *  Invoked by the external service to being viewing the
	 *  identified person.
	 *
	 */

	public void setup(Integer personPK, IRequestCycle cycle)
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;

		for (int i = 0; i < 2; i++)
		{
			try
			{
				IBookQuery query = getQuery();

				int count = query.ownerQuery(personPK);

				browser.initializeForResultCount(count);

				IOperations operations = vengine.getOperations();

				setPerson(operations.getPerson(personPK));

				break;
			}
			catch (FinderException e)
			{
				vengine.presentError(
					"Person " + personPK + " not found in database.",
					getRequestCycle());
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Remote exception for owner query.", ex, i > 0);

				setQuery(null);
			}
		}

		cycle.setPage(this);
	}

	public Book getCurrentMatch()
	{
		return currentMatch;
	}

	public void setCurrentMatch(Book value)
	{
		currentMatch = value;
	}

	public boolean getOmitHolderLink()
	{
		return !currentMatch.isBorrowed();
	}

	/**
	 *  Removes the book query bean, if the handle to the bean
	 *  is non-null.
	 *
	 */

	public void cleanupPage()
	{
		try
		{
			if (query != null)
				query.remove();
		}
		catch (RemoveException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}
		catch (RemoteException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}

		super.cleanupPage();
	}

}