package com.primix.sesstrack;

import java.util.*;
import java.io.*;
import javax.servlet.http.*;


/**
 *	Encapsulates information about a 'hit' against a web/application server, identifying
 *  the method, URI, parameters and the time.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class ServerHit implements Serializable
{
	private String serverName;
    private String method;
    private String URI;
    private Map parameters;
	private long requestTime;
    private transient Date requestDate;
    
 	private static final int MAP_SIZE = 7;
    
	public ServerHit(HttpServletRequest request)
	{

		method = request.getMethod();
		serverName = request.getServerName();
		URI = request.getRequestURI();
		requestTime = System.currentTimeMillis();
 
		setupParameters(request);
	}
	    
    /**
     *  Constructor used by {@link SessionTracker} to create one of these beasts
     *  when it is retrieved from the database.
     *
     */
     
	public ServerHit(long requestTime, String serverName, String method, String URI)
	{
    	this.requestTime = requestTime;
        this.serverName = serverName;
        this.method = method;
        this.URI = URI;
        
        // Parameters are set up seperately.
	}
    

	private void setupParameters(HttpServletRequest request)
	{
		Enumeration e;
		String[] parameterValues;
        String name;
        
		e = request.getParameterNames();

		while (e.hasMoreElements())
		{
			name = (String)e.nextElement();

			parameterValues = request.getParameterValues(name);

			if (parameters == null)
				parameters = new HashMap(MAP_SIZE);

			parameters.put(name, parameterValues);
		}

		if (parameters != null)
        	System.out.println("ServerHit: request parameters\n" + parameters);
	}      
    

    public String getMethod()
    {
        return method;
    }

    public String getURI()
    {
        return URI;
    }

	public String getServerName()
	{
    	return serverName;
	}
    
    /** 
     *  Method provided for {@link SessionTracker} to more efficiently
     *  access the parameter map.  The returned Map has String keys
     *  that match the names of the HTTP request parameters, and
     *  String[] values.  The return value may be null in the (frequent)
     *  case that there are no query parameters.
     *
     *  <p>Note: originally, this was default protection and returned
     *  the actual parameters array.  That would work in a world with one
     *  big happy class loader.  Because the ServerHit originates in the client
     *  and the SessionTracker is loaded from an EJB deployment jar (with its
     *  own special class loader), an IllegalAccessError is thrown invoking
     *  what should be an ok method.  Well, it compiled anyway.
     *
     *  <p>This method now returns null, or an unmodifiable copy of the parmeter
     *  map.  Still it should be used with caution (there no such thing as deep
     *  unmodifiable).
     *
     */
     
    public Map getParameterMap()
    {
   		if (parameters == null)
        	return null;
        
        return Collections.unmodifiableMap(parameters);    
    }
    

	/**
     *  Method used by {@link SessionTracker} when assembling a ServerHit from
     *  the database.  Ideally, this would be package-private, but it has to
     *  be public due to very strange class loader issues related to RMI.
     *
     */

    public void setParameterValue(String name, String[] value)
    {
    	if (parameters == null)
        	parameters = new HashMap(MAP_SIZE);
        
        parameters.put(name, value);
    }
        
    /**
     *  Returns an (unmodifiable) collection of the String names of all parameters.
     *
     */
     
    public Collection getParameterNames()
    {
    	if (parameters == null)
        	return Collections.EMPTY_LIST;
        
       	return Collections.unmodifiableCollection(parameters.keySet());
    }    
    
    /**
     *  Returns an unmodifiable collection of Strings which is the value for
     *  the named parameter.  Returns null if the name is not found.
     *
     */
     
    public Collection getParameterValues(String name)
    {
    	String[] values;
        
    	if (parameters == null)
        	return Collections.EMPTY_LIST;
            
        values = (String[])parameters.get(name);
        if (values == null)
        	return null;
            
        return Collections.unmodifiableList(Arrays.asList(values));    
    }
    
    /**
     *  Returns the time at which this <code>ServerHit</code> was created, 
     *  as a number of
     *  milliseconds since January 1, 1970, 00:00:00 GMT.
     *
     */
     
    public long getRequestTime()
    {
    	return requestTime;
    }
    
    /**
     *  Returns the request time converted to a <code>Date</code>.
     *
     */
     
    public Date getRequestDate()
    {
    	if (requestDate == null)
        		requestDate = new Date(requestTime);
            
        return requestDate;
    }
        
    public String toString()
    {        
    	StringBuffer buffer;
        
        buffer = new StringBuffer("ServerHit[");
        
        buffer.append(method);
        
		buffer.append(' ');
        buffer.append(serverName);
        buffer.append(' ');
        buffer.append(URI);
        
      	buffer.append(' ');
        
        if (requestDate != null)
            buffer.append(requestDate);
        else
        {
        	buffer.append(requestTime);
            buffer.append(" millis");
        }        
       
        buffer.append(']');
        
        return buffer.toString();        
    }
}




