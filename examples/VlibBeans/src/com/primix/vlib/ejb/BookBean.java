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
 
package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;

public class BookBean extends AbstractEntityBean
{
	// All must be public for access by container
	
	// Primary key
	public Integer bookId;
	
	// Contrary to the O'Reilly book, we can't use int here and java.lang.Integer
	// elsewhere; it has to be an Object and it has to match exactly.
	
	// Other CMP fields 
	
	public String author;
	public String title;
	public String description;
	public String ISBN;
	public int rating;
	
	// CMP fields that shadow relationships
	
	public Integer ownerPK;
	public Integer holderPK;
	public Integer publisherPK;
		
	protected String[] getAttributePropertyNames()
	{
		return new String[] 
		{
			"title", "description", "ISBN",  
			"holderPK", "ownerPK", "publisherPK",
			"author",
		};
	}
	
	// Business methods
	
	public String getAuthor()
	{
		return author;
	}
	
	public void setAuthor(String value)
	{
		author = value;
		dirty = true;
	}
	
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
		
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String value)
	{
		title = value;
		dirty = true;
	}
	
	public Integer getHolderPK()
	throws RemoteException
	{
		return holderPK;
	}
	
	public void setHolderPK(Integer value)
	{
		holderPK = value;

		dirty = true;
	}
	
	public Integer getOwnerPK()
	throws RemoteException
	{
		return ownerPK;
	}
	
	public void setOwnerPK(Integer value)
	{
		ownerPK = value;
        
		dirty = true;
	}	
	
	public void setPublisherPK(Integer value)
	{
		publisherPK = value;
		
		dirty = true;
	}
	
	public Integer getPublisherPK()
	throws RemoteException
	{
		return publisherPK;
	}

	/**
	 *  Nulls-out the owner and holder properties; the Owner and Holder
	 *  will be determined in the accessor.
	 *
	 */
	 
	public void ejbLoad() 
	{
		dirty = false;
	}
	
 
	public void ejbStore() 
	{
		dirty = false;
	}
		
	// Create methods
	
	public Integer ejbCreate(String title, String author, String ISBN, Integer publisherPK, 
		Integer personPK)
	throws RemoteException
	{
		this.title = title;
		this.ISBN = ISBN;
		this.author = author;
		
		ownerPK = personPK;
		holderPK = personPK;
		this.publisherPK = publisherPK; 
		
		// These are given default (unspecified) values.
		
		bookId = allocateKey();
		description = null;
		
		// Rating really isn't implemented yet.
		
		rating = 0;
		
		dirty = true;
		
		return null;
	}
	
	public void ejbPostCreate(String title, String author, String ISBN, Integer publisherPK,
		Integer personPK)
	{
		// No post create work needed but the method must be implemented
	}

}