package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;

/** 
 *  Remote interface for the Person entity bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPerson extends IEntityBean
{
	public void setLastName(String value) throws RemoteException;

	public String getLastName() throws RemoteException;

	public void setFirstName(String value) throws RemoteException;

	public String getFirstName() throws RemoteException;

	public void setPassword(String value) throws RemoteException;

	public String getPassword() throws RemoteException;

	public void setEmail(String value) throws RemoteException;

	public String getEmail() throws RemoteException;

	public boolean getAdmin() throws RemoteException;

	public void setAdmin(boolean value) throws RemoteException;

	public boolean getLockedOut() throws RemoteException;

	public void setLockedOut(boolean value) throws RemoteException;

	public boolean getVerified() throws RemoteException;

	public void setVerified(boolean value) throws RemoteException;

	/**
	 *  Returns the natural concatination of the first and last name, or just
	 *  the last name if there is no first name.
	 *
	 **/

	public String getNaturalName() throws RemoteException;

	public void setLastAccess(Timestamp value) throws RemoteException;

	public Timestamp getLastAccess() throws RemoteException;
}