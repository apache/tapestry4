//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//


package net.sf.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

import javax.ejb.CreateException;

/**
 *  Implementation of the Person entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see net.sf.tapestry.vlib.ejb.IPerson
 *  @see net.sf.tapestry.vlib.ejb.IPersonHome
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class PersonBean extends AbstractEntityBean
{

	protected String[] getAttributePropertyNames()
	{
		return new String[] {
			"firstName",
			"lastName",
			"email",
			"password",
			"verified",
			"lockedOut",
			"admin",
			"authorizationCode",
			"lastAccess" };
	}

    public abstract void setPersonId(Integer value);
    
    public abstract Integer getPersonId();

	public abstract void setEmail(String value);

	public abstract String getEmail();

	public abstract void setFirstName(String value);

	public abstract String getFirstName();

	public abstract void setLastName(String value);

	public abstract String getLastName();

	public abstract void setPassword(String value);

	public abstract String getPassword();

	public abstract void setVerified(boolean value);

	public abstract boolean getVerified();

	public abstract void setLockedOut(boolean value);

	public abstract boolean getLockedOut();

	public abstract void setAdmin(boolean value);

	public abstract boolean getAdmin();

	public abstract String getAuthorizationCode();

	public abstract void setAuthorizationCode(String value);

	public abstract void setLastAccess(Timestamp value);

	public abstract Timestamp getLastAccess();

	public Integer ejbCreate(Map attributes) throws CreateException, RemoteException
	{
        setVerified(true);
        updateEntityAttributes(attributes);
        
        return allocateKey();
	}

	public void ejbPostCreate(Map attributes)
	{
		// Do nothing
	}

	public String getNaturalName()
	{
        String firstName = getFirstName();
        String lastName = getLastName();
        
		if (firstName == null)
			return lastName;

		return firstName + " " + lastName;
	}
}