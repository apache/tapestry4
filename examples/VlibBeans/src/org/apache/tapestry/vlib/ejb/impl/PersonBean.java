//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

import javax.ejb.CreateException;

/**
 *  Implementation of the Person entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see org.apache.tapestry.vlib.ejb.IPerson
 *  @see org.apache.tapestry.vlib.ejb.IPersonHome
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
			"lockedOut",
			"admin",
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

	public abstract void setLockedOut(boolean value);

	public abstract boolean getLockedOut();

	public abstract void setAdmin(boolean value);

	public abstract boolean getAdmin();

	public abstract void setLastAccess(Timestamp value);

	public abstract Timestamp getLastAccess();

	public Integer ejbCreate(Map attributes) throws CreateException, RemoteException
	{
        updateEntityAttributes(attributes);
        
        setPersonId(allocateKey());
        
        return null;
	}

	public void ejbPostCreate(Map attributes)
	{
		// Do nothing
	}
}