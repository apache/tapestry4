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
        
        setPersonId(allocateKey());
        
        return null;
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