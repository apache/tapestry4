package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.valid.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.rmi.*;
import javax.servlet.http.*;


/*
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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
 *  Allows the user to login, by providing email address and password.
 *  After succesfully logging in, a cookie is placed on the client browser
 *  that provides the default email address for future logins (the cookie
 *  persists for a week).
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Login 
extends BasePage
implements IErrorProperty
{
	private String email;
	private String password;
	private String error;
	private String targetPage;
    private IValidationDelegate validationDelegate;

	/**
	 *  The name of a cookie to store on the user's machine that will identify
	 *  them next time they log in.
	 *
	 */

	private static final String COOKIE_NAME = "com.primix.vlib.Login.email";

	private final static int ONE_WEEK = 7 * 24 * 60 * 60;
	
	public void detach()
	{
		email = null;
		password = null;
		error = null;
		targetPage = null;

		super.detach();
	}

    public IValidationDelegate getValidationDelegate()
    {
        if (validationDelegate == null)
            validationDelegate = new SimpleValidationDelegate(this);

        return validationDelegate;
    }

	public void setEmail(String value)
	{
		email = value;
	}
	
	/**
	 *  Gets the email address.  If not previously set, it is retrieve from
	 *  the cookie (thus forming the default).
	 *
	 */
	 
	public String getEmail()
	{
		// If not set, see if a value was previously recorded in a Cookie
		
		if (email == null)
			email = getRequestCycle().getRequestContext().getCookieValue(COOKIE_NAME);
		
		return email;
	}
	
	public void setPassword(String value)
	{
		password = value;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setError(String value)
	{
		error = value;
	}
	
	public String getError()
	{
		return error;
	}
	
	public void setTargetPage(String value)
	{
		targetPage = value;
		
		fireObservedChange("targetPage", value);
	}
	
	public String getTargetPage()
	{
		return targetPage;
	}

    protected void setErrorField(String componentId, String message)
    {
        IValidatingTextField field;

        field = (IValidatingTextField)getComponent(componentId);
        field.setError(true);

        if (error == null)
            error = message;
    }
	
	/**
	 *  Attempts to login.  If successful, updates the application's user property
	 *  and redirects to the target page (or the home page if no target page is specified).
	 *
	 *  <p>Clears the target page property.
	 *
	 *  <p>If the user name is not known, or the password is invalid, then an error
	 *  message is displayed.
	 *
	 */
	 
	private void attemptLogin(IRequestCycle cycle)
	{		
        // An error, from a validation field, may already have occured.

        if (getError() != null)
            return;

		try
		{
            Visit visit = (Visit)getVisit();
			IPersonHome personHome = visit.getPersonHome();
			IPerson person = personHome.findByEmail(email);
			
			if (!person.getPassword().equals(password))
			{
				setErrorField("inputPassword", "Invalid password.");
				return;
			}
			
			loginUser(person, cycle);
			
		}
		catch (FinderException e)
		{
			setErrorField("inputEmail", "E-mail address not known.");
			return;
		}
		catch (Throwable t)
		{
			setError("Could not validate user and password: " + t);
			return;			
		}
	}
	

	/**
	 *  Invoked when the login form is submitted.
	 *
	 */
	 
	public IActionListener getLoginFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			throws RequestCycleException
			{
				attemptLogin(cycle);
			}
		};
	}
	
	/**
	 *  Sets up the {@link IPerson} as the logged in user, creates
	 *  a cookie for thier email address (for subsequent logins),
	 *  and redirects to the appropriate page ({@link MyBooks}, or
	 *  a specified page).
	 *
	 */
	 
	public void loginUser(IPerson person, IRequestCycle cycle)
	{
		String email;
		Cookie cookie;
				
		try
		{
			email = person.getEmail();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}

        Visit visit = (Visit)getVisit();
		visit.setUser(person);

		// After logging in, go to the MyBooks page, unless otherwise
		// specified.

		if (targetPage == null)
			cycle.setPage("MyBooks");
		else	
			cycle.setPage(targetPage);

		// I've found that failing to set a maximum age and a path means that
		// the browser (IE 5.0 anyway) quietly drops the cookie.
		
		cookie = new Cookie(COOKIE_NAME, email);
		cookie.setPath(engine.getServletPrefix());
		cookie.setMaxAge(ONE_WEEK);
		
		// Record the user's email address in a cookie
		
		cycle.getRequestContext().addCookie(cookie);

		engine.forgetPage(getName());
	}	
}
