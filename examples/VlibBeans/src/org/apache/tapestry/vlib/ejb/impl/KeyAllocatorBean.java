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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tapestry.contrib.ejb.XEJBException;

/**
 *  Implementation of the {@link org.apache.tapestry.vlib.ejb.IKeyAllocator}
 *  stateless session bean.
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
 *  @author Howard Lewis Ship
 *
 **/

public class KeyAllocatorBean implements SessionBean
{
    private static final String PROPERTY_NAME = "next-key";

    private SessionContext context;

    /**
     *  List of Integer instances; these are keys
     *  acquired from the database.
     *
     **/

    private LinkedList keys;

    /**
     *  Number of keys to allocate from the database
     *  at a time.  Set from the ENC property "blockSize".
     *
     **/

    private int blockSize = 0;

    /**
     *  Data source, retrieved from the ENC property 
     *  "jdbc/dataSource".
     *
     **/

    private DataSource dataSource;

    /**
     *  Activates the bean.  Gets the block size
     *  and DataSource from the environment.
     *
     **/

    public void ejbCreate()
    {
        Context initial;
        Context environment;
        Integer blockSizeProperty;

        try
        {
            initial = new InitialContext();
            environment = (Context) initial.lookup("java:comp/env");
        }
        catch (NamingException ex)
        {
            throw new XEJBException("Could not lookup environment.", ex);
        }

        try
        {
            blockSizeProperty = (Integer) environment.lookup("blockSize");
        }
        catch (NamingException ex)
        {
            throw new XEJBException("Could not lookup blockSize property.", ex);
        }

        blockSize = blockSizeProperty.intValue();

        try
        {
            dataSource = (DataSource) environment.lookup("jdbc/dataSource");
        }
        catch (NamingException ex)
        {
            throw new XEJBException("Could not lookup data source.", ex);
        }

        if (keys == null)
            keys = new LinkedList();
    }

    /**
     *  Does nothing, not invoked in stateless session beans.
     **/

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
     **/

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
     **/

    public void ejbRemove()
    {
        // Does nothing.
    }

    /**
     *  Allocates a single key, going to the database
     *  only if it has no keys in its internal cache.
     *
     **/

    public Integer allocateKey()
    {
        if (keys.isEmpty())
            allocateBlock(1);

        return (Integer) keys.removeFirst();
    }

    /**
     *  Allocates a block of keys, going to the database
     *  if there are insufficient keys in its internal
     *  cache.
     *
     **/

    public Integer[] allocateKeys(int count)
    {
        Integer[] result;
        int i;

        if (keys.size() < count)
            allocateBlock(count);

        result = new Integer[count];

        for (i = 0; i < count; i++)
        {
            result[i] = (Integer) keys.removeFirst();
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
     **/

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

            statement = connection.prepareStatement("select PROP_VALUE from PROP where NAME = ?");
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

            statement =
                connection.prepareStatement("update PROP\n" + "set PROP_VALUE = ?\n" + "where NAME	 = ?");
            statement.setInt(1, nextKey);
            statement.setString(2, PROPERTY_NAME);

            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();

            throw new XEJBException("Unable to allocate keys from the database.", ex);
        }
        finally
        {
            if (set != null)
            {
                try
                {
                    set.close();
                }
                catch (SQLException ex)
                {
                }
            }

            if (statement != null)
            {
                try
                {
                    statement.close();
                }
                catch (SQLException ex)
                {
                }
            }

            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException ex)
                {
                }
            }
        }

    }

    /**
     *  Gets a database connection from the pool.
     *
     *  @throws EJBException if a {@link SQLException}
     *  is thrown.
     *
     **/

    protected Connection getConnection()
    {
        try
        {
            return dataSource.getConnection();
        }
        catch (SQLException ex)
        {
            throw new XEJBException("Unable to get database connection from pool.", ex);
        }
    }
}