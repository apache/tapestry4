package net.sf.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

/**
 *  Implementation of the Publisher entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see net.sf.tapestry.vlib.ejb.IPublisher
 *  @see net.sf.tapestry.vlib.ejb.IPublisherHome
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class PublisherBean extends AbstractEntityBean
{
    public abstract void setPublisherId(Integer value);
    
    public abstract Integer getPublisherId();
    
	public abstract String getName();
    
	public abstract void setName(String value);

	protected String[] getAttributePropertyNames()
	{
		return new String[] { "name" };
	}

	public Integer ejbCreate(String name) throws CreateException, RemoteException
	{
		setPublisherId(allocateKey());
		setName(name);
		
		return null;
	}

	public void ejbPostCreate(String name)
	{
		// Do nothing
	}
}