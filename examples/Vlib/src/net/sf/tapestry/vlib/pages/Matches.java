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
 *  Run's queries and displays matches.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class Matches extends BasePage
{
	private IBookQuery bookQuery;
	private Book currentMatch;
	private Browser browser;

	public void detach()
	{
		bookQuery = null;
		currentMatch = null;

		super.detach();
	}

	public void finishLoad(
		IPageLoader loader,
		ComponentSpecification specification)
		throws PageLoaderException
	{
		super.finishLoad(loader, specification);

		browser = (Browser) getComponent("browser");
	}

	/**
	 *  Gets the {@link IBookQuery} session bean for the query, creating
	 *  it fresh if necessary.
	 *
	 */

	public IBookQuery getBookQuery()
	{
		if (bookQuery == null)
		{
			// No existing handle, so time to create a new bean.

			VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

			for (int i = 0; i < 2; i++)
			{
				try
				{
					IBookQueryHome home = vengine.getBookQueryHome();

					setBookQuery(home.create());

					break;
				}
				catch (CreateException ex)
				{
					throw new ApplicationRuntimeException(ex);
				}
				catch (RemoteException ex)
				{
					vengine.rmiFailure("Remote exception creating BookQuery.", ex, i > 0);
				}
			}
		}

		return bookQuery;
	}

	/**
	 *  Sets the persistent bookQuery property.
	 *
	 */

	public void setBookQuery(IBookQuery value)
	{
		bookQuery = value;

		fireObservedChange("bookQuery", value);
	}

	/**
	 *  Invoked by the {@link Home} page to perform a query.
	 *
	 */

	public void performQuery(
		String title,
		String author,
		Object publisherPK,
		IRequestCycle cycle)
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;

		for (int i = 0; i < 2; i++)
		{

			IBookQuery query = getBookQuery();

			try
			{
				int count = query.masterQuery(title, author, publisherPK);

				if (count == 0)
				{
					Home home = (Home) cycle.getPage("Home");
					home.setMessage("No matches for your query.");
					cycle.setPage(home);
					return;
				}

				browser.initializeForResultCount(count);

				break;
			}
			catch (RemoteException ex)
			{
				String message = "Remote exception processing query.";

				vengine.rmiFailure(message, ex, false);

				if (i > 0)
				{
					// This method is invoked from the Home page.  We return
					// without changing the response page.

					IErrorProperty page = (IErrorProperty) cycle.getPage();
					page.setError(message);
					return;
				}
			}
		}

		cycle.setPage(this);

	}

	public Book getCurrentMatch()
	{
		return currentMatch;
	}

	/**
	 *  Updates the dynamic currentMatch property.
	 *
	 */

	public void setCurrentMatch(Book value)
	{
		currentMatch = value;
	}

	public boolean getOmitHolderLink()
	{
		return !currentMatch.isBorrowed();
	}

	/**
	 *  Removes the book query bean, if not null.
	 *
	 */

	public void cleanupPage()
	{
		try
		{
			if (bookQuery != null)
				bookQuery.remove();

			bookQuery = null;
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