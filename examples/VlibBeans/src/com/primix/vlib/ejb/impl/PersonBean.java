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

package com.primix.vlib.ejb.impl;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import com.primix.tapestry.util.ejb.*;
import com.primix.vlib.ejb.*;
import java.sql.Timestamp;

/**
 *  Implementation of the Person entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see IPerson
 *  @see IPersonHome
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 

public class PersonBean extends AbstractEntityBean
{
	// Primary key
	
	public Integer personId;
	
	// CMP fields
	
	public String email;
	public String firstName;
	public String lastName;
	public String password;
	public boolean verified;
	public boolean lockedOut;
	public boolean admin;
	public String authorizationCode;
	public Timestamp lastAccess;
	
	protected String[] getAttributePropertyNames()
	{
		return new String[] 
		{ "firstName", "lastName", "email", "password", 
				"verified", "lockedOut", "admin", "authorizationCode",
				"lastAccess"
		};
	}
	
	public void setEmail(String value)
	{
		email = value;
		dirty = true;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setFirstName(String value)
	{
		firstName = value;
		dirty = true;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setLastName(String value)
	{
		lastName = value;
		dirty = true;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setPassword(String value)
	{
		password = value;
		
		dirty = true;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setVerified(boolean value)
	{
		verified = value;
		
		dirty = true;
	}
	
	public boolean isVerified()
	{
		return verified;
	}
	
	public void setLockedOut(boolean value)
	{
		lockedOut = value;
		
		dirty = true;
	}
	
	public boolean isLockedOut()
	{
		return lockedOut;
	}
	
	public void setAdmin(boolean value)
	{
		admin = value;
		
		dirty = true;
	}
	
	public boolean isAdmin()
	{
		return admin;
	}
	
	public String getAuthorizationCode()
	{
		return authorizationCode;
	}
	
	public void setAuthorizationCode(String value)
	{
		authorizationCode = value;
		
		dirty = true;
	}
	
	public void setLastAccess(Timestamp value)
	{
		lastAccess = value;
		dirty = true;
	}
	
	public Timestamp getLastAccess()
	{
		return lastAccess;
	}
	
	public Integer ejbCreate(Map attributes)
	throws RemoteException
	{
		// Defaults
		
		verified = true;
		lockedOut = false;
		admin = false;
			
		updateEntityAttributes(attributes);
				
		this.personId = allocateKey();
		
		dirty = true;
		
		return null;
	}

	public void ejbPostCreate(Map attributes)
	{
		// Do nothing
	}
	
	public String getNaturalName()
	{
		if (firstName == null)
			return lastName;
		
		return firstName + " " + lastName;	
	}
}
