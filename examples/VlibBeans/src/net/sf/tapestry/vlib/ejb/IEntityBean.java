package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.EJBObject;

/** 
 *  Defines the remove interface for an entity which can download and upload
 *  a subset of its properties as a {@link Map}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IEntityBean extends EJBObject
{
	/**
	 *  Returns the simple attributes of the entity as a Map.
	 *
	 **/

	public Map getEntityAttributes() throws RemoteException;

	/**
	 *  Updates some or all of the properties of the entity.
	 *
	 **/

	public void updateEntityAttributes(Map data) throws RemoteException;
}