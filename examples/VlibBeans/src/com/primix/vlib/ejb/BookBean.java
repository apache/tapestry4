package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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

/**
 *  Implementation of the Book entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see IBook
 *  @see IBookHome
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class BookBean extends AbstractEntityBean
{
	// All must be public for access by container
	
	// Primary key
	public Integer bookId;
	
	// Contrary to the O'Reilly book, we can't use int here and java.lang.Integer
	// elsewhere; it has to be an Object and it has to match exactly.
	
	// Other CMP fields 
	
	public String title;
	public String description;
	public String ISBN;
	public int lendCount;
	
	// CMP fields that shadow relationships
	
	public Integer ownerPK;
	public Integer holderPK;
	public Integer publisherPK;
	
	private IPerson owner;
	private IPerson holder;
	private IPublisher publisher;
	
	protected String[] getAttributePropertyNames()
	{
		return new String[] 
		{ "title", "description", "ISBN", "lendCount"
		};
	}
	
	// Business methods
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String value)
	{
		description = value;
		dirty = true;
	}
	
	public String getISBN()
	{
		return ISBN;
	}
	
	public void setISBN(String value)
	{
		ISBN = value;
		dirty = true;
	}
	
	public int getLendCount()
	{
		return lendCount;
	}
	
	public void setLendCount(int value)
	{
		lendCount = value;
		dirty = true;
	}
	
	public void incrementLendCount()
	{
		lendCount++;
		dirty = true;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String value)
	{
		title = value;
		dirty = true;
	}
	
	public IPerson getHolder()
	throws RemoteException
	{
		if (holder == null)
			holder = locatePerson(holderPK);
		
		return holder;
	}
	
	public void setHolder(IPerson value)
	{
		holder = value;
		holderPK = (Integer)getPrimaryKey(holder, "holder");

		dirty = true;
	}
	
	public IPerson getOwner()
	throws RemoteException
	{
		if (owner == null)
			owner = locatePerson(ownerPK);
		
		return owner;
	}
	
	public void setOwner(IPerson value)
	{
		owner = value;
		ownerPK = (Integer)getPrimaryKey(owner, "owner");
        
		dirty = true;
	}	
	
	public void setPublisher(IPublisher value)
	{
		publisher = value;
		publisherPK = (Integer)getPrimaryKey(publisher, "publisher");
		
		dirty = true;
	}
	
	public IPublisher getPublisher()
	throws RemoteException
	{
		if (publisher == null)
		{
			try
			{
				publisher = getPublisherHome().findByPrimaryKey(publisherPK);
			}
			catch (FinderException e)
			{
				throw new EJBException("Could not locate Publisher with primary key " +
				publisherPK + ": " + e);
			}
		}
		
		return publisher;
	}

	private transient IPublisherHome publisherHome;
	
	private IPublisherHome getPublisherHome()
	{
		if (publisherHome != null)
			return publisherHome;
		try
		{
			publisherHome = (IPublisherHome)getEnvironmentObject("ejb/Publisher",
					IPublisherHome.class);
		}
		catch (Exception e)
		{
			throw new EJBException(e);
		}
		
		return publisherHome;
	}		
			
	
	private transient IPersonHome personHome;
	
	private IPersonHome getPersonHome()
	{
		if (personHome != null)
			return personHome;
			
		try
		{
			personHome = (IPersonHome)getEnvironmentObject("ejb/Person",
					IPersonHome.class);
		}
		catch (Exception e)
		{
			throw new EJBException(e);
		}
		
		return personHome;
	}		

	private IPerson locatePerson(Integer personPK)
	throws RemoteException
	{
		try
		{
			return getPersonHome().findByPrimaryKey(personPK);
		}
		catch (FinderException e)
		{
			throw new EJBException("Could not locate Person with primary key " 
				+ personPK + ": " + e);
		}
	}

	/**
	 *  Nulls-out the owner and holder properties; the Owner and Holder
	 *  will be determined in the accessor.
	 *
	 */
	 
	public void ejbLoad() 
	{
		dirty = false;
		owner = null;
		holder = null;
	}
	
 
	public void ejbStore() 
	{
		dirty = false;
	}
		
	// Create methods
	
	public Integer ejbCreate(String title, String ISBN, IPublisher publisher, 
		IPerson person)
	throws CreateException, RemoteException
	{
		Integer personPK;
		
		this.title = title;
		this.ISBN = ISBN;
		
		personPK = (Integer)getPrimaryKey(person, "person");
		
		owner = person;
		holder = person;
		ownerPK = personPK;
		holderPK = personPK;
		
		this.publisher = publisher;
		publisherPK = (Integer)getPrimaryKey(publisher, "publisher");
		
		// These are given default (unspecified) values.
		
		bookId = allocateKey();
		description = null;
		lendCount = 0;
		
		dirty = true;
		
		return null;
	}
	
	public void ejbPostCreate(String title, String ISBN, IPublisher publisher,
		IPerson person)
	{
		// No post create work needed but the method must be implemented
	}

}