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
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.*;
import javax.rmi.*;

/**
 *  Edits the properties of at book.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class EditBook extends Protected
{
	private Integer bookPK;
	private Map attributes;
	private String publisherName;

	private static final int MAP_SIZE = 11;

	public void detach()
	{
		attributes = null;
		bookPK = null;
		publisherName = null;

		super.detach();
	}

	public Map getAttributes()
	{
		if (attributes == null)
			attributes = new HashMap(MAP_SIZE);

		return attributes;
	}

	public String getPublisherName()
	{
		return publisherName;
	}

	public void setPublisherName(String value)
	{
		publisherName = value;
	}

	/**
	 *  Gets the book's primary key as a String.
	 *
	 */

	public String getBookPrimaryKey()
	{
		return bookPK.toString();
	}

	/**
	 *  Updates the book's primary key value (converting from String to Integer).
	 *  This allows a Hidden component in the form to synchronize the book being
	 *  editted ... which fixes the Browser Back Button problem.
	 *
	 */

	public void setBookPrimaryKey(String value)
	{
		bookPK = new Integer(value);
	}

	/**
	 *  Invoked (from {@link MyLibrary}) to begin editting a book.
	 *  Gets the attributes from the {@link IBook} and updates
	 *  the request cycle to render this page,
	 *
	 */

	public void beginEdit(Integer bookPK, IRequestCycle cycle)
	{
		this.bookPK = bookPK;

		VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;

		for (int i = 0; i < 2; i++)
		{
			try
			{
				// Get the attributes as a source for our input fields.

				IOperations operations = vengine.getOperations();

				attributes = operations.getBookAttributes(bookPK);

				break;
			}
			catch (FinderException ex)
			{
				// TBD:  Dress this up and send them back to the Search or
				// MyLibrary page.

				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure(
					"Remote exception setting up page for book #" + bookPK + ".",
					ex,
					i > 0);
			}
		}

		cycle.setPage(this);
	}

	/**
	 *  Used to update the book when the form is submitted.
	 *
	 */

	public void formSubmit(IRequestCycle cycle)
	{
		Integer publisherPK = (Integer) attributes.get("publisherPK");

		if (publisherPK == null && Tapestry.isNull(publisherName))
		{
			setErrorField(
				"inputPublisherName",
				"Must provide a publisher name if the publisher option is empty.",
				null);
			return;
		}

		if (publisherPK != null && !Tapestry.isNull(publisherName))
		{
			setErrorField(
				"inputPublisherName",
				"Must leave the publisher name blank if selecting a publisher from the list.",
				publisherName);
			return;
		}

		// Check for an error from a validation field

		if (isInError())
			return;

		// OK, do the update.

		Visit visit = (Visit) getVisit();
		VirtualLibraryEngine vengine = visit.getEngine();

		for (int i = 0; i < 2; i++)
		{

			IOperations bean = vengine.getOperations();

			try
			{
				if (publisherPK != null)
					bean.updateBook(bookPK, attributes);
				else
				{
					bean.updateBook(bookPK, attributes, publisherName);
					visit.clearCache();
				}

				break;
			}
			catch (FinderException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (CreateException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure(
					"Remote exception updating book #" + bookPK + ".",
					ex,
					i > 0);

				continue;
			}
		}

		MyLibrary page = (MyLibrary) cycle.getPage("MyLibrary");
		page.setMessage("Updated book.");

		cycle.setPage(page);
	}

}