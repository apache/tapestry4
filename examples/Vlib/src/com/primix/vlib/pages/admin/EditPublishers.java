/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
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
 *  Allows editting of the publishers in the database, including deleting
 *  publishers (which can be dangerous if any books are linked to the publisher).
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class EditPublishers
	extends AdminPage
{
	private Publisher[] publishers;
	private Publisher publisher;
	
	/**
	 *  {@link Set} of {@link Integer} primary keys, of Publishers.
	 *
	 */
	
	private Set deletedPublishers;
	
	public void detach()
	{
		publishers = null;
		publisher = null;
		deletedPublishers = null;
		
		super.detach();
	}
	
	public void beginResponse(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		super.beginResponse(writer, cycle);
		
		getPublishers();
	}
	
	public void setPublishers(Publisher[] value)
	{
		publishers = value;
		
		fireObservedChange("publishers", value);
	}
	
	/**
	 *  Gets the publishers; this value is cached as a persistent page property.
	 *
	 */
	
	public Publisher[] getPublishers()
	{
		if (publishers == null)
			readPublishers();
		
		return publishers;
	}
	
	public void setPublisher(Publisher value)
	{
		publisher = value;
	}
	
	public Publisher getPublisher()
	{
		return publisher;
	}
	
	public Set getDeletedPublishers()
	{
		if (deletedPublishers == null)
			deletedPublishers = new HashSet();
		
		return deletedPublishers;
	}
	
	private void readPublishers()
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();
				
				setPublishers(operations.getPublishers());
				
				break;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Unable to obtain list of publishers.", ex, i > 0);
			}
		}
		
	}
		
	public void processForm(IRequestCycle cycle)
	{
		Publisher[] publishers = getPublishers();
		List updateList = new ArrayList(publishers.length);
		Set deletedKeys = getDeletedPublishers();
		
		// Create a List of all the publishers which aren't
		// being deleted.
		for (int i = 0; i < publishers.length; i++)
		{
			if (deletedKeys != null &&
					deletedKeys.contains(publishers[i].getPrimaryKey()))
					continue;
			
			updateList.add(publishers[i]);
		}

		setPublishers(null);
		
		Publisher[] updated = (Publisher[])updateList.toArray(new Publisher[updateList.size()]);
		
		Integer[] deleted = null;
		
		if (deletedKeys != null)
			deleted = (Integer[])deletedKeys.toArray(new Integer[deletedKeys.size()]);
		
		// Clear the cache of Publishers, since that'll likely change
		
		setPublishers(null);
		
		// Now, push the updates through to the database
		
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)getEngine();
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();
				
				operations.updatePublishers(updated, deleted);
				
				break;
			}
			catch (FinderException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoveException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Remote exception updating publishers.", ex, i > 0);
			}
		}
				
		// Clear any cached info about publishers.
		
		vengine.clearCache();
	}
	
	/**
	 *  Always returns false; no publishers are deleted when the form is rendered.
	 *
	 */
	
	public boolean getDeletedPublisher()
	{
		return false;
	}
	
	/**
	 *  If value is true (i.e., the checkbox was checked)
	 *  then the primary key of the current Publisher is added
	 *  to the deletedPublishers set.
	 *
	 */
	
	public void setDeletedPublisher(boolean value)
	{
		if (value)
		{
			Set set = getDeletedPublishers();

			set.add(publisher.getPrimaryKey());
		}
	}
}

