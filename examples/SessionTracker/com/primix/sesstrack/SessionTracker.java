package com.primix.sesstrack;

import javax.ejb.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.*;
import java.rmi.*;
import java.util.*;
import com.ibm.logging.*;

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
 
/**
 *  The implementation for the {@link ISessionTracker} EJB.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class SessionTracker implements EntityBean, IRecordType
{
	private static final String DATA_SOURCE_NAME = "jdbc/sessiondb";

	private transient DataSource dataSource;

	private static TraceLogger logger;
	
	private boolean dirty;
    
    static
    {
	    Handler handler;

	    logger = new TraceLogger();
		logger.setLogging(Boolean.getBoolean("com.primix.SessionTracker.enable-logging"));
		logger.setSynchronous(true);

	    handler = new ConsoleHandler();
	    logger.addHandler(handler);
    }
    
	private EntityContext context;
    
    // Fields that store database information.
    
	private String sessionId;
    private String remoteAddress;
    private String remoteHost;
    
    private Timestamp created;
    private Timestamp updated;
    
	private List hits;

	public SessionTracker()
	{
    	if (logger.isLogging)
        		logger.text(TYPE_PUBLIC | TYPE_OBJ_CREATE, this, "<init>", 
                "Created SessionTracker instance.");
	}    

	private void trace(String methodName)
	{
		if (logger.isLogging)
			logger.text(TYPE_PUBLIC | TYPE_LEVEL1, this, methodName, null);
	}


	public String getSessionId()
	{
		return sessionId;
	}

	public String getRemoteAddress()
	{
    	return remoteAddress;
	}
    
    public String getRemoteHost()
    {
    	return remoteHost;
    }
    
    public long getTimeCreated()
    {
    	if (created == null)
        	return 0;
            
        return created.getTime();
    }   
    
    public long getTimeUpdated()
    {
    	if (updated == null)
        	return 0;
        
        return updated.getTime();
    }    

	/**
	 *  Indicates whether the SessionTracker bean is dirty; i.e., requires
	 *  and ejbStore().  This is used in association with the WebLogic
	 *  deployment descriptor.
	 *
	 */
	 
	public boolean isDirty()
	{
		return dirty;
	}

	public void addHit(ServerHit hit)
	{
    	Connection connection = null;
        PreparedStatement statement = null;
        Timestamp requestTime;
        Map map;
        Map.Entry entry;
        Iterator i;
        String parameterName;
        String[] parameterValue;
        
		if (logger.isLogging)
			logger.text(TYPE_PUBLIC | TYPE_LEVEL1, this, "addHit", 
				"Adding: {0}", hit);

		// Force a re-read of the hits next time we're asked.
        
		hits = null;
        
        requestTime = new Timestamp(hit.getRequestTime());
        
	    try
	    {
	    	connection = getConnection();
	        
	        statement = connection.prepareStatement(
            	"insert into HIT (SESSION_ID, SERVER_NAME, REQUEST_TIME, PMETHOD, URI)\n" +
                "  values (?, ?, ?, ?, ?)");
                
            statement.setString(1, sessionId);
            statement.setString(2, hit.getServerName());
            statement.setTimestamp(3, requestTime);
            statement.setString(4, hit.getMethod());
            statement.setString(5, hit.getURI());            
            
	        statement.executeUpdate();
            statement.close();
            statement = null;

			map = hit.getParameterMap();
            if (map != null  && !map.isEmpty())
            {
            
				statement = connection.prepareStatement(
	            	"insert into HIT_PARMS (SESSION_ID, REQUEST_TIME, NAME, PVALUE)\n" +
	                "  values (?, ?, ?, ?)");
                    
                i = map.entrySet().iterator();
                while (i.hasNext())
                {
                	entry = (Map.Entry)i.next();
                    
                    parameterName = (String)entry.getKey();
                    parameterValue = (String[])entry.getValue();
            
            		if (logger.isLogging)
                    	logger.text(TYPE_PUBLIC | TYPE_LEVEL2, this, "addHit",
                    		"Writing row for parameter {0}.", parameterName);
                    
                    statement.setString(1, sessionId);
                    statement.setTimestamp(2, requestTime);
                    statement.setString(3, parameterName);
                    statement.setObject(4, parameterValue);
                    
                    // Batching the update didn't seem to work using Cloudscape from
                    // a WebLogic pool ... hard to say where the blame is.
                    
                    // statement.addBatch();
                    
                    statement.executeUpdate();
                }
                
                // statement.executeBatch();
            }
	            
			// Mark the bean as dirty, so that an update (of its timestamp)
			// will be forced.
			
			dirty = true;
	    }
	    catch (SQLException e)
	    {
	    	logger.exception(TYPE_PUBLIC, this, "addHit", e);
	        
	    	throw new EJBException(e);
	    }
	    finally
	    {
        	cleanup("addHit", connection, statement, null);
	    }
	}


	public List getHits()
	{
		if (logger.isLogging)
			logger.text(TYPE_PUBLIC | TYPE_LEVEL1, this, "getHits", "");

		if (hits == null)
			readHits();

		return hits;
	}

	public String ejbCreate(String sessionId, String remoteAddress, String remoteHost)
    throws CreateException
	{
    	Connection connection = null;
        PreparedStatement statement = null;
        
 		trace("ejbCreate");
               
	   	if (sessionId == null)
        	throw new CreateException("Session id must not be null.");
        
        this.sessionId = sessionId;
        this.remoteAddress = remoteAddress;
        
        if (remoteHost == null)
        	this.remoteHost = remoteAddress;
        else
        	this.remoteHost = remoteHost;    
    
        
        created = new Timestamp(System.currentTimeMillis());
        updated = created;
        
        try
        {
        	connection = getConnection();
            
            statement = connection.prepareStatement(
   				"insert into CSESSION (SESSION_ID, CREATED, UPDATED, REMOTE_ADDRESS, REMOTE_HOST)\n" +
                "  values(?, ?, ?, ?, ?)");
                
            statement.setString(1, sessionId);
            statement.setTimestamp(2, created);
            statement.setTimestamp(3, updated);
            statement.setString(4, remoteAddress);
            statement.setString(5, remoteHost);
            
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, "ejbCreate", e);
            
        	throw new EJBException(e);
        }
        finally
        {
        	cleanup("ejbCreate", connection, statement, null);
        }
                   
        hits = null;
        
        // Return the primary key of the newly created object.  The sessionId is
        // used as the primary key.
        
        return sessionId;
	}    

	/**
     *  Each ejbCreate(...) must have an ejbPostCreate(...).
     *
     */
     
	public void ejbPostCreate(String sessionId, String remoteAddress, String remoteHost)
	{
    	trace("ejbPostCreate");
	}
    
	public void ejbRemove()
	{
    	String sessionId;
        Connection connection = null;
        PreparedStatement statement = null;
        
        sessionId = (String)context.getPrimaryKey();
        
        logger.text(TYPE_PUBLIC, this, "ejbRemove",
        		"Removing SessionTracker for session {0}.", sessionId);
                
        try
        {
        	connection = getConnection();
            
        	statement = connection.prepareStatement(
            	"delete from HIT_PARMS where SESSION_ID = ?");
            
            statement.setString(1, sessionId);
            
            statement.executeUpdate();
            statement.close();
            statement = null;
            
            statement = connection.prepareStatement(
            	"delete from HIT where SESSION_ID = ?");
            statement.setString(1, sessionId);
            
            statement.executeUpdate();
            statement.close();
            statement = null;
            
            statement = connection.prepareStatement(
            	"delete from CSESSION where SESSION_ID = ?");
            statement.setString(1, sessionId);
            
	        statement.executeUpdate();
       	}
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, "ejbRemove", e);
            
        	throw new EJBException(e);
        }
        finally
		{
        	cleanup("ejbRemove", connection, statement, null);
		}
	}

	public void ejbLoad()
	{
		String sessionId;
	    Connection connection = null;
	    ResultSet set = null;
	    PreparedStatement statement = null;
	   
	    sessionId = (String)context.getPrimaryKey();
	   
        if (logger.isLogging)
        	logger.text(TYPE_PUBLIC, this, "ejbLoad",
            "Loading SessionTracker for session {0}.", sessionId);
             
	    try
	    {
	    	connection = getConnection();
	        
	        statement = connection.prepareStatement(
	        	"select CREATED, UPDATED, REMOTE_ADDRESS, REMOTE_HOST \n" +
	            "  from CSESSION\n" +
	            "  where SESSION_ID = ?");
	        statement.setString(1, sessionId);
	        
	        set = statement.executeQuery();
	        
	        if (!set.next())
            	throw new EJBException("No SessionTracker instance for session " +
                	sessionId + ".");
	        
	        created = set.getTimestamp(1);
	        updated = set.getTimestamp(2);
	        remoteAddress = set.getString(3);
	        remoteHost = set.getString(4);
	        
	        this.sessionId = sessionId;
            
            // We really should check that there isn't a second row, but sessionId is
            // the primary key and is supposed to be unique, so we'll trust that the
            // database is correct.
            
			dirty = false;	
		
	    }
	    catch (SQLException e)
	    {
	    	logger.exception(TYPE_PUBLIC, this, "ejbLoad", e);
	        
	    	throw new EJBException(e);
	    }
	    finally
	    {
			cleanup("ejbLoad", connection, statement, set);
	    }
	        
	}

	public String ejbFindByPrimaryKey(String sessionId)
    throws ObjectNotFoundException
	{
        Connection connection = null;
        ResultSet set = null;
        PreparedStatement statement = null;

        logger.text(TYPE_PUBLIC, this, "ejbFindByPrimaryKey",
        		"Locating SessionTracker for session {0}.", sessionId);
        
        try
        {
        	connection = getConnection();
            
            statement = connection.prepareStatement(
            	"select CREATED from CSESSION \n" +
                "  where SESSION_ID = ?");
            statement.setString(1, sessionId);
            
            set = statement.executeQuery();
           
            if (!set.next())
            	throw new ObjectNotFoundException("No SessionTracker for session \"" +
                sessionId + "\".");
 
 			return sessionId;       
         }
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, "ejbLoad", e);
            
        	throw new EJBException(e);
        }
        finally
        {
        	cleanup("ejbLoad", connection, statement, set);
        }          
 	}
      
    public void ejbStore() 
    {
        Connection connection = null;
        PreparedStatement statement = null;
        
        trace("ejbStore");
        
        sessionId = (String)context.getPrimaryKey();
        
        try
        {
        	connection = getConnection();
            
            updated = new Timestamp(System.currentTimeMillis());
            
            statement = connection.prepareStatement(
            "update CSESSION\n" +
            "  set UPDATED = ?\n" +
            "  where SESSION_ID = ?");
            
            statement.setTimestamp(1, updated);
            statement.setString(2, sessionId);
            
            statement.executeUpdate();    
			
			dirty = false;        
            
        }
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, "ejbStore", e);
            
        	throw new EJBException(e);
        }
        finally
        {
         	cleanup("ejbStore", connection, statement, null);      
        }          
    }
    
    
    private void readHits()
    {
    	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        Map hitMap = new HashMap();
        Timestamp requestTime;
        String serverName;
        String method;
        String URI;
        ServerHit hit;
        String parameterName;
        String[] parameterValue;
        
        if (logger.isLogging)
        	logger.text(TYPE_LEVEL2, this, "readHits", null);
        
        try
        {
        	connection = getConnection();
            
            // First, read all HIT table to generate the ServerHit instances
            
            statement = connection.prepareStatement(
            	"select REQUEST_TIME, SERVER_NAME, PMETHOD, URI\n" +
                "  from HIT\n" +
                "  where SESSION_ID = ?\n" +
                "  order by REQUEST_TIME");
              
           	statement.setString(1, sessionId);
            
            set = statement.executeQuery();
            
            hits = new ArrayList();
            
            while (set.next())
            {
            	requestTime = set.getTimestamp(1);
                serverName = set.getString(2);
                method = set.getString(3);
                URI = set.getString(4);
                
                hit = new ServerHit(requestTime.getTime(), serverName, method, URI);
                
                hits.add(hit);
                
                // Index this hit for later, when we absorb all the ServerHit request
                // parameters from HIT_PARMS
                
                hitMap.put(requestTime, hit);
            }
            
            set.close(); 
            set = null;
            
            statement.close(); 
            statement = null;
            
            statement = connection.prepareStatement(
            	"select REQUEST_TIME, NAME, PVALUE\n" +
                "  from HIT_PARMS\n" +
                "  where SESSION_ID = ?");
                
            statement.setString(1, sessionId);
            
            set = statement.executeQuery();
            
            // Ok, now get all the parameters for all the ServerHits
            // just read.
            
            while (set.next())
            {
            	requestTime = set.getTimestamp(1);
                parameterName = set.getString(2);
                parameterValue = (String[])set.getObject(3);
                
                hit = (ServerHit)hitMap.get(requestTime);
                
                // This should never be null, unless there's some faulty
                // janitor process that deletes HIT rows without deleting
                // the corresponding HIT_PARMS rows.
                
                if (hit != null)
                	hit.setParameterValue(parameterName, parameterValue);
            }
            
        }
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, "readHits", e);
            
        	throw new EJBException(e);
        }
        finally
        {
        	cleanup("readHits", connection, statement, set);
        }
        
    }
    
    private void cleanup(String methodName,
    	Connection connection, Statement statement, ResultSet set)
    {
   		try
   		{
        	if (set != null)
            	set.close();
   		}
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, methodName, e);
        }
        
        try
        {
        	if (statement != null)
            	statement.close();
        }
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, methodName, e);
        }
        
        try
        {
        	if (connection != null)
            	connection.close();
        }
        catch (SQLException e)
        {
        	logger.exception(TYPE_PUBLIC, this, methodName, e);
        }
    }
       	    
       
        
	/**
	*  Gets a JDBC Connection object from the data source.  The data source
	*  is located from the bean's environment in {@link #ejbActivate()}.
	*
	*/

	protected Connection getConnection()
	throws SQLException
	{
		Context initial;
        Context environment;
        Connection result;
        String name;
        
		if (dataSource == null)
        {
	   		try
			{
				initial = new InitialContext();
				environment = (Context)initial.lookup("java:comp/env");

				dataSource = (DataSource)environment.lookup(DATA_SOURCE_NAME);
          
				logger.text(TYPE_PUBLIC | TYPE_LEVEL3, this, "getConnection",
					"Located data source: {0}.", dataSource);
			}
			catch (NamingException e)
			{
				logger.exception(TYPE_PUBLIC, this, "ejbCreate", e);
                
                throw new SQLException("Could not access data source: " + e);
			}
		}
        
        result = dataSource.getConnection();
        
        if (logger.isLogging)
        	logger.text(TYPE_PUBLIC | TYPE_LEVEL3, this, "getConnection",
            			"Got connection: {0}.", result);
                
        return result;      
	}

	public void ejbActivate()
	{
		trace("ejbActivate");
	}

	public void ejbPassivate()
	{
		trace("ejbPassivate");
	}

	public void setEntityContext(EntityContext context)
	{
    	trace("setEntityContext");
        
		this.context = context;
	}

	public void unsetEntityContext()
	{
    	trace("unsetEntityContext");
        
    	context = null;
	}
}


