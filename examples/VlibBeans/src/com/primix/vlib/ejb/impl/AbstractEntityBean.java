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
import javax.rmi.*;
import javax.naming.*;
import java.util.*;
import com.primix.tapestry.util.prop.*;
import com.primix.tapestry.util.ejb.*;
import com.primix.vlib.ejb.*;

/**
 *  Provides basic support for the entity context, empty or minimal
 *  implementations of the required methods, and some utilties.
 *  
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public abstract class AbstractEntityBean implements EntityBean
{
	/**
	 *  The EntityContext provided by the application server.
	 *
	 */
	 
	protected EntityContext context;
	
	/**
	 *  Flag indicating that the object is 'dirty' and needs to
	 *  be written back to the database.  Each mutator method
	 *  should set this to true.
	 *
	 */
	 
	protected transient boolean dirty;
	
	private transient String[] attributePropertyNames;
	private static final int MAP_SIZE = 11;
	private transient PropertyHelper helper;
	
	private transient IKeyAllocatorHome keyAllocatorHome;

	/**
	 *  The environment naming context, which is configured for this bean
	 *  in the deployment descriptor.
	 *
	 */
	 
	private transient Context environment;
	
	public void setEntityContext(EntityContext context)
	{
		this.context = context;
	}
	
	public void unsetEntityContext()
	{
		context = null;
	}

	/**
	 *  Returns a flag that indicates that the entity must be
	 *  saved to the database.
	 *
	 */
	 
	public boolean isDirty()
	{
		return dirty;
	}

	/**
	 *  Gets a named object from the bean's environment naming context.
	 *
	 */
	 
	protected Object getEnvironmentObject(String name, Class objectClass)
	throws RemoteException, NamingException
	{
		Object raw;
		Object result;
		Context initial;
		
		if (environment == null)
		{
			initial = new InitialContext();
			environment = (Context)initial.lookup("java:comp/env");
		}
		
		raw = environment.lookup(name);
		
		try
		{
			result = PortableRemoteObject.narrow(raw, objectClass);
		}
		catch (ClassCastException e)
		{
			throw new RemoteException("Could not narrow " + raw + " (" + name + 
						") to class " + objectClass + ".");
		}
		
		return result;
	}

	/**
	 *  Empty implementation; subclasses may override.
	 *
	 */
	 
	public void ejbActivate() throws EJBException, RemoteException 
	{
		// does nothing
	}

	/**
	 *  Empty implementation; subclasses may override.
	 *
	 */
	 
	public void ejbPassivate() throws EJBException, RemoteException 
	{
		// does nothing
	}

	/**
	 *  Empty implementation; subclasses may override.
	 *
	 */
	 
	public void ejbRemove() throws EJBException, RemoteException 
	{
		// does nothing
	}
	
    /**
	 *  Clears the dirty flag.  Subclasses may invoke this implementation,
	 *  or simply do so themselves.
	 *
	 */
	 
	public void ejbLoad() throws EJBException, RemoteException 
	{
		dirty = false;
	}

	/**
	 *  Clears the dirty flag.  Subclasses may invoke this implementation,
	 *  or simply do so themselves.
	 *
	 */
	 
	public void ejbStore() throws EJBException, RemoteException 
	{
		dirty = false;
	}
	
	/**
	 *  Uses the KeyAllocator session bean to allocate a necessary key.
	 *
	 */
	 
 	public Integer allocateKey()
	throws RemoteException
	{
		Context initial;
		Context environment;
		IKeyAllocator allocator;
		Object raw;
		
		if (keyAllocatorHome == null)
		{
			try
			{
				initial = new InitialContext();
				environment = (Context)initial.lookup("java:comp/env");
				
				raw = environment.lookup("ejb/KeyAllocator");
				keyAllocatorHome = 
					(IKeyAllocatorHome)PortableRemoteObject.narrow
						(raw, IKeyAllocatorHome.class);
			}
			catch (NamingException e)
			{
				throw new XEJBException("Unable to locate IKeyAllocatorHome.", e);
			}
		}
		
		// Get a reference to *some* KeyAllocator bean ... it may be fresh,
		// or one reused from a pool.
		
		try
		{
			allocator = keyAllocatorHome.create();
		}
		catch (CreateException e)
		{
			throw new RemoteException(
				"Unable to create a KeyAllocator from " + 
				keyAllocatorHome + ".", e);
		}
		
		// Finally, invoke the method that gets a key.
			
		return allocator.allocateKey();
	}
	
	/**
	 *  Implemented in subclasses to provide a list of property names to be included
	 *  in the entity attributes map.
	 *
	 */
	 
	protected abstract String[] getAttributePropertyNames();
	
	/**
	 *  Returns a {@link Map} of the properties of the bean.  This Map is
	 *  returned to the client, where it can be modified and then used to update
	 *  the entity bean in a single method
	 *
	 *  <p>The properties included in the Map are defined by the
	 *  {@link #getAttributePropertyNames()} method, which is implemented
	 *  by concrete subclasses.
	 *
	 */
	 
	public Map getEntityAttributes()
	{
		Map result;
		int i;
		String key;
		Object value;
		
		result = new HashMap(MAP_SIZE);
		
		if (attributePropertyNames == null)
			attributePropertyNames = getAttributePropertyNames();
		
		if (helper == null)
			helper = PropertyHelper.forClass(getClass());	
		
		for (i = 0; i < attributePropertyNames.length; i++)
		{
			key = attributePropertyNames[i];
			
			value = helper.get(this, key);
			result.put(key, value);
		}
		
		return result;
	}
	
	/**
	 *  Updates the bean with property changes from the update {@link Map}.
	 *  Only the keys defined by {@link #getAttributePropertyNames()} will be
	 *  accessed (keys and values that are not in that list are ignored). 
	 *
	 *  <p>The corresponding bean property will only be updated
	 *  if the key is present ... this means that the update may contain just
	 *  the <em>changed</em> keys.  Remember that a Map may store null values.
	 *
	 */
	 
	public void updateEntityAttributes(Map update)
	{
		int i;
		String key;
		Object value;
		
		if (attributePropertyNames == null)
			attributePropertyNames = getAttributePropertyNames();
		
		if (helper == null)
			helper = PropertyHelper.forClass(getClass());	
		
		for (i = 0; i < attributePropertyNames.length; i++)
		{
			key = attributePropertyNames[i];
			
			if (update.containsKey(key))
			{
				value = update.get(key);
				
				helper.set(this, key, value);
			}
				
		}
	
	}
}
