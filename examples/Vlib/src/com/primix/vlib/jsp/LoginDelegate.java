package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.rmi.*;
import javax.rmi.*;

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
 *  Controller for the Login page.  An instance is stored in the {@link HttpSession},
 *  and persists until the user succesfully logs in.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class LoginDelegate extends VlibDelegate
{
	private final static String SESSION_ATTRIBUTE_NAME = "pages.login";

	/**
	 *  Creates the LoginDelegate and stores it as an attribute
	 *  of the {@link HttpSession}.
	 *
	 */
	 

	public LoginDelegate(RequestContext context)
	{
		super(context);
		
		context.setSessionAttribute(SESSION_ATTRIBUTE_NAME, this);
	}
	
	/**
	 *  Gets the delegate from the {@link HttpSession}, or creates a new
	 *  one (and stores it in the session).
	 *
	 */
	 
	public static LoginDelegate get(RequestContext context)
	{
		LoginDelegate result;
		
		result = (LoginDelegate)context.getSessionAttribute(SESSION_ATTRIBUTE_NAME);
		if (result == null)
			result = new LoginDelegate(context);
		
		return result;	
	}

	/**
	 *  Name of text field used to enter the user's email.
	 *
	 */
	 
	public static final String EMAIL_NAME = "email";
	
	
	/**
	 *  Name of the text field used to enter the password..
	 *
	 */
	 
	public static final String PASSWORD_NAME = "password";

																			
	/**
	 *  Name ("<code>com.primix.vlib-jsp.Login.email</code>") of a HTTP cookie
	 *  used to remember the user's email address on subsequent
	 *  visits.
	 *
	 */
	 
	public static final String COOKIE_NAME  = "com.primix.vlib-jsp.Login.email";

	private final static int ONE_WEEK = 7 * 24 * 60 * 60;
	 
	private transient String error;
	private transient String email;
	
	/**
	 *  Object to be invoked once the login is complete.
	 *
	 */
	
	private ILoginCallback callback;
	
	public String getError()
	{
		return error;
	}
	
	public void setError(String value)
	{
		error = value;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setCallback(ILoginCallback value)
	{
		callback = value;
	}
	
	public ILoginCallback getCallback()
	{
		return callback;
	}
	
	public void service(RequestContext context) throws ServletException, IOException
	{
		String action;
		
		action = context.getPathInfo(0);
		if (action == null)
			action = "";
			
		try
		{
			if (action.equals("submit"))
			{
				if (attemptLogin(context))
					return;
			}
			else
            {
                // On the first time to the page, set the email property
                // to the default, stored in a cookie.  This default
                // is updated after the user successfully logs in.

			    email = context.getCookieValue(COOKIE_NAME);
            }
			
			// Default action ... show the Login page.
			
			forward("/jsp/Login.jsp", "Login", null, context);
		}
		finally
		{
			application.cleanup();
		}
	}
	
	public void performLogin(ILoginCallback callback, RequestContext context)
	throws IOException, ServletException
	{
		this.callback = callback;
		
		forward("/jsp/Login.jsp", "Login", null, context);
	}
	
	private void setDefaultEmail(RequestContext context)
	{
	}

	private boolean attemptLogin(RequestContext context)
	throws ServletException, IOException
	{
		String password;
		IPersonHome personHome;
		IPerson person;
		
		email = context.getParameter(EMAIL_NAME);
		password = context.getParameter(PASSWORD_NAME);
		
        if (isNull(email))
        {
            setError("You must enter a value for E-Mail.");
            return false;
        }

        if (isNull(password))
        {
            setError("You must enter a password.");
            return false;
        }

		try
		{
			personHome = application.getPersonHome();
			person = personHome.findByEmail(email);
			
			if (!person.getPassword().equals(password))
			{
				setError("Invalid password.");
				return false;
			}
			
		}
		catch (FinderException e)
		{
			setError("E-mail address not known.");
			return false;
		}
		catch (Throwable t)
		{
			setError("Could not validate user and password: " + t);
			return false;			
		}

		return loginUser(person, context);
	}
		

	/**
	 *  Sets up the {@link IPerson} as the logged in user, creates
	 *  a cookie for thier email address (for subsequent logins),
	 *  and redirects to the appropriate page.
	 *
	 */
	 
	public boolean loginUser(IPerson person, RequestContext context)
	throws IOException, ServletException
	{
		String email;
		Cookie cookie;

		try
		{
			email = person.getEmail();
		}
		catch (RemoteException e)
		{
			throw new ServletException(e);
		}

		application.setUser(person);

		// I've found that failing to set a maximum age and a path means that
		// the browser (IE 5.0 anyway) quietly drops the cookie.
		
		cookie = new Cookie(COOKIE_NAME, email);
		cookie.setPath("/");
		cookie.setMaxAge(ONE_WEEK);
		
		// Record the user's email address in a cookie
		
		context.addCookie(cookie);

		// If not specified otherwise, use the MyBooks page
		// as the place to go after logging in.
		
		if (callback == null)
			callback = MyBooksDelegate.get(context);
		
		// Inform the callback that the user has logged in.
		// There are certainly some issues if the callback
		// throws an exception!

		callback.postLogin(context);

		// No longer need the information in this page, remove it
		// from the session.
		
		context.removeSessionAttribute(SESSION_ATTRIBUTE_NAME);
		
		return true;
		
	}	

    private boolean isNull(String value)
    {
        if (value == null)
            return true;

        return value.trim().length() == 0;
    }
}
