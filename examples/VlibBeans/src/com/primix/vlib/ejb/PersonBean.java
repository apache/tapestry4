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

package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import com.primix.tapestry.util.ejb.*;

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
	
	protected String[] getAttributePropertyNames()
	{
		return new String[] 
		{ "firstName", "lastName", "email", "password"
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
	
	
	public Integer ejbCreate(String lastName, String firstName, String email, String password)
	throws RemoteException
	{
		this.lastName = lastName;
		this.firstName = firstName;
		this.password = password;
		this.email = email;
		
		this.personId = allocateKey();
		
		dirty = true;
		
		return null;
	}

	public void ejbPostCreate(String lastName, String firstName, String email, String password)
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