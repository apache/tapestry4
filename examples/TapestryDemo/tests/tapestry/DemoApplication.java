package tests.tapestry;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.app.*;
import com.primix.tapestry.spec.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import java.sql.*;
import com.primix.sesstrack.*;
import java.rmi.*;
import javax.ejb.*;
import javax.rmi.*;

/*
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Test application.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class DemoApplication extends SimpleApplication
{
	
	private transient static int activeCount = 0;

	private transient Context environment;

	private transient ISessionTracker sessionTracker;

	private transient IMonitor monitor;
	
	private boolean monitorEnabled;
	private boolean initialized;
	private static boolean standalone;
	
	private static Map enc;
	
	static
	{
		enc = new HashMap();
		enc.put("ejb/SessionTracker", "com.primix.sesstrack.SessionTracker");
		
		standalone = Boolean.getBoolean("standalone");
	}

	protected void setupForRequest(RequestContext context)
	{
		ServletConfig config;
		String value;

		super.setupForRequest(context);

		if (!initialized)
		{
			value = context.getServlet().getInitParameter("monitor-enabled");
			
			monitorEnabled = "true".equals(value);
		
			initialized = true;
		}

		try
		{
			// This needs to be extended to build a proper hit ..
			// with method & parameters.

			getSessionTracker(context).addHit(new ServerHit(context.getRequest()));
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Unable to notify SessionTracker.", e);
		}

	}


	public DemoApplication(RequestContext context)
	{
		super(context, null);
	}


	public IMonitor getMonitor(RequestContext context)
	{
		if (!monitorEnabled)
			return null;
	
		if (monitor == null)
			monitor = new SimpleMonitor();

		return monitor;
	}

	/**
	*  Returns a delegate that invokes {@link
	*  RequestContext#write(HTMLWriter)}.
	*
	*/

	public IRender getShowDebugDelegate()
	{
		return new IRender()
		{
			public void render(IResponseWriter writer, IRequestCycle cycle) 
			throws RequestCycleException
			{
				if (!cycle.isRewinding())
					cycle.getRequestContext().write(writer);
			}
		};
	}

	public void valueBound(HttpSessionBindingEvent event)
	{
		super.valueBound(event);
		
		System.out.println(
			"New client from " + clientAddress + ", active count " +
			++activeCount + ".");
	}

	public void valueUnbound(HttpSessionBindingEvent event)
	{
		System.out.println(
			"Client " + clientAddress + " timeout, active count " + --activeCount + ".");
	
		super.valueUnbound(event);	
	}


	public ISessionTracker getSessionTracker(RequestContext context)
	{
		ISessionTrackerHome home = null;

		if (sessionTracker == null)
		{
			try
			{
				home = (ISessionTrackerHome)getNamedObject("ejb/SessionTracker", 
                	ISessionTrackerHome.class);

				try
				{
                	sessionTracker = home.findByPrimaryKey(sessionId);
				}
                catch (ObjectNotFoundException e)
                {
                	HttpServletRequest request = context.getRequest();
                    
                	sessionTracker = home.create(sessionId, 
                    	request.getRemoteAddr(), 
                        request.getRemoteHost());
                }
	  			catch (FinderException e)
	  			{
	  				throw new ApplicationRuntimeException(e.getMessage(), e);
	  			}
  			}
			catch (RemoteException e)
			{
				throw new ApplicationRuntimeException(e.getMessage(), e);
			}
			catch (CreateException e)
			{
				throw new ApplicationRuntimeException(e.getMessage(), e);
			}
		}

		return sessionTracker;
	}

	protected Object getNamedObject(String name, Class interfaceClass)
	{
    	Object raw;
		Object result;
		String resolvedName;

		try
		{
			if (standalone)
				resolvedName = (String)enc.get(name);
			else
				resolvedName = name;
					
			raw = getEnvironment().lookup(resolvedName);
            
            result = PortableRemoteObject.narrow(raw, interfaceClass);
            
		}
		catch (NamingException e)
		{
			throw new ApplicationRuntimeException("Could not lookup '" + name + "'.", e);
		}

		return result;
	}

	public Context getEnvironment()
	{
		Context initial;
		Properties props;
		
		if (environment == null)
	    {
		    try
			{
				if (standalone)
				{
					props = new Properties();
					props.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
					props.put(Context.PROVIDER_URL, "t3://localhost:7001");
					props.put(Context.SECURITY_PRINCIPAL, "system");
					props.put(Context.SECURITY_CREDENTIALS, "pmixticket");
					
					environment = new InitialContext(props);
					
					return environment;
				}
				
				initial = new InitialContext();

		    	environment = (Context)initial.lookup("java:comp/env");
			}
			catch (NamingException e)
			{
				throw new ApplicationRuntimeException(
			    	"Could not get access environment.", e);
			}
	    }
	    
	    return environment;
	}
    
    public ISessionTracker getSessionTracker()
    {
    	return sessionTracker;
    }
}

