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
 *  
 *
 * @author Howard Ship
 * @version $Id$
 */

public class NewBook extends Protected
{
	private Map attributes;
	private String publisherName;

	public void detach()
	{
		attributes = null;
		publisherName = null;

		super.detach();
	}

	public Map getAttributes()
	{
		if (attributes == null)
			attributes = new HashMap();

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

	public void addBook(IRequestCycle cycle)
	{
		Map attributes = getAttributes();

		Integer publisherPK = (Integer) attributes.get("publisherPK");

		if (publisherPK == null && Tapestry.isNull(publisherName))
		{
			setErrorField(
				"inputPublisherName",
				"Must enter a publisher name or select an existing publisher from the list.", null);
			return;
		}

		if (publisherPK != null && !Tapestry.isNull(publisherName))
		{
			setErrorField(
				"inputPublisherName",
				"Must either select an existing publisher or enter a new publisher name.",
				publisherName);
			return;
		}

		if (isInError())
			return;

		Visit visit = (Visit) getVisit();
		Integer userPK = visit.getUserPK();
		VirtualLibraryEngine vengine = visit.getEngine();

		attributes.put("ownerPK", userPK);
		attributes.put("holderPK", userPK);

		for (int i = 0; i < 2; i++)
		{
			try
			{

				IOperations operations = vengine.getOperations();

				if (publisherPK != null)
					operations.addBook(attributes);
				else
				{
					operations.addBook(attributes, publisherName);

					// Clear the app's cache of info; in this case, known publishers.

					visit.clearCache();
				}

				break;
			}
			catch (CreateException ex)
			{
				setError("Error adding book: " + ex.getMessage());
				return;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Remote exception adding new book.", ex, i > 0);
			}
		}

		// Success.  First, update the message property of the return page.

		MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");

		myLibrary.setMessage("Added: " + attributes.get("title"));

		cycle.setPage(myLibrary);
	}

}