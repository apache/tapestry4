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
import javax.sql.*;
import java.sql.*;
import javax.naming.*;
import com.primix.tapestry.util.ejb.*;

/**
 *  Implementation of the KeyAllocator stateless session bean.
 *
 *  <p>We're cheating a little; they KeyAllocator does have
 *  state, it just doesn't get persisted ever.  Since the
 *  operation on it is atomic ("gimme a key") it doesn't
 *  need to have conversational state with its clients.
 *
 *  <p>The KeyAllocator records in the database the "next key
 *  to allocate".  When it needs a key, it allocates a block
 *  of keys (by advancing the next key by a some number).
 *
 *  <p>If the KeyAllocator instance is purged from the pool,
 *  then some number of keys that it has allocated will
 *  be lost.  Big deal.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class KeyAllocatorBean implements SessionBean
{
    private static final String  PROPERTY_NAME = "next-key";

	private SessionContext context;
	
	/**
	 *  List of Integer instances; these are keys
	 *  acquired from the database.
	 *
	 */
	 
	private LinkedList keys;
	
	/**
	 *  Number of keys to allocate from the database
	 *  at a time.  Set from the ENC property "blockSize".
	 *
	 */
	 
	private int blockSize = 0;
	
	/**
	 *  Data source, retrieved from the ENC property 
	 *  "jdbc/dataSource".
	 *
	 */
	 
	private DataSource dataSource;
	
	/**
	 *  Activates the bean.  Gets the block size
	 *  and DataSource from the environment.
	 *
	 */
	 
	public void ejbCreate()
	{
		Context initial;
		Context environment;
		Integer blockSizeProperty;
		
		try
		{
			initial = new InitialContext();
			environment = (Context)initial.lookup("java:comp/env");
		}
		catch (NamingException e)
		{
			throw new XEJBException("Could not lookup environment.", e);
		}
		
		try
		{
			blockSizeProperty = (Integer)environment.lookup("blockSize");
		}
		catch (NamingException e)
		{
			throw new XEJBException("Could not lookup blockSize property.", e);
		}

		blockSize = blockSizeProperty.intValue();
		
		try
		{
			dataSource = (DataSource)environment.lookup("jdbc/dataSource");
		}
		catch (NamingException e)
		{
			throw new XEJBException("Could not lookup data source.", e);
		}
		
		if (keys == null)
			keys = new LinkedList();
	}
	
	/**
	 *  Does nothing, not invoked in stateless session beans.
	 */
	 
	public void ejbPassivate()
	{
	}
	
	public void setSessionContext(SessionContext value)
	{
		context = value;
	}
	
	/**
	 *  Does nothing, not invoked in stateless session beans.
	 *
	 */
	 
	public void ejbActivate()
	{
	}
	
	/**
	 *  Does nothing.  This is invoked when the bean moves from
	 *  the method ready pool to the "does not exist" state.
	 *  The EJB container will lost its reference to the
	 *  bean, and the garbage collector will take it
	 *  (including any keys it has cached from the database).
	 *
	 */
	 
	public void ejbRemove()
	{
		// Does nothing.
	}

	/**
	 *  Allocates a single key, going to the database
	 *  only if it has no keys in its internal cache.
	 *
	 */
	
	public Integer allocateKey()
	{
		if (keys.isEmpty())
			allocateBlock(1);
		
		return (Integer)keys.removeFirst();
	}

	/**
	 *  Allocates a block of keys, going to the database
	 *  if there are insufficient keys in its internal
	 *  cache.
	 *
	 */
	 
	public Integer[] allocateKeys(int count)
	{
		Integer[] result;
		int i;
		
		if (keys.size() < count)
			allocateBlock(count);
		
		result = new Integer[count];
		
		for (i = 0; i < count; i++)
		{
			result[i] = (Integer)keys.removeFirst();
		}
		
		return result;
	}
	
	/**
	 *  Allocates a block of keys from the database.
	 *  Allocates count keys, or the configured block size,
	 *  whichever is greater.
	 *
	 *  <p>It is assumed that this operation takes place
	 *  within a transaction.
	 *
	 */
	 
	protected void allocateBlock(int count)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet set = null;
		int nextKey;
		int allocationCount;
		int i;
		
		allocationCount = Math.max(count, blockSize);
		
		try
		{
			connection = getConnection();
			
			statement = connection.prepareStatement
				("select PROP_VALUE from PROP where NAME = ?");
			statement.setString(1, PROPERTY_NAME);
			
			set = statement.executeQuery();
			
			// Advance to the first row.
			
			set.next();
			
			nextKey = set.getInt(1);
			
			set.close();
			set = null;
			
			statement.close();
			statement = null;
			
			// Now, take those keys and advance nextKey
			
			for (i = 0; i < allocationCount; i++)
				keys.add(new Integer(nextKey++));
			
			// Update nextKey back to the database.
			
			statement = connection.prepareStatement(
				"update PROP\n" +
				"set PROP_VALUE = ?\n" +
				"where NAME	 = ?");
			statement.setInt(1, nextKey);
			statement.setString(2, PROPERTY_NAME);
			
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
            e.printStackTrace();

			throw new XEJBException("Unable to allocate keys from the database.", e);
		}
		finally
		{
			if (set != null)
			{
				try
				{
					set.close();
				}
				catch (SQLException e) {}
			}
			
			if (statement != null)
			{
				try
				{
					statement.close();
				}
				catch (SQLException e) {}
			}
			
			if (connection != null)
			{
				try
				{
					connection.close();
				}
				catch (SQLException e) {}
			}
		}

			
	}
	
	/**
	 *  Gets a database connection from the pool.
	 *
	 *  @throws EJBException if a {@link SQLException}
	 *  is thrown.
	 *
	 */
	 
	protected Connection getConnection()
	{
		try
		{
			return dataSource.getConnection();
		}
		catch (SQLException e)
		{
			throw new XEJBException("Unable to get database connection from pool.", e);
		}
	}
}  