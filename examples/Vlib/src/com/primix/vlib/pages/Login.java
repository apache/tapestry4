package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;

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
 *  
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Login extends BasePage
{
	private String email;
	private String password;
	private String error;
	private String targetPage;

	public Login(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public void detachFromApplication()
	{
		super.detachFromApplication();
		
		email = null;
		password = null;
		error = null;
	}
	
	public void setEmail(String value)
	{
		email = value;
		
		fireObservedChange("email", value);
	}
	
	public String getEmail()
	{
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
		
		fireObservedChange(targetPage, value);
	}
	
	public String getTargetPage()
	{
		return targetPage;
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
		VirtualLibraryApplication app;
		IPersonHome personHome;
		IPerson person;
		
		try
		{
			app = (VirtualLibraryApplication)application;
			
			personHome = app.getPersonHome();
			person = personHome.findByEmail(email);
			
			if (!person.getPassword().equals(password))
			{
				setError("Invalid password.");
				return;
			}
			
			app.setUser(person);
			
			if (targetPage == null)
				cycle.setPage("Home");
			else	
				cycle.setPage(targetPage);
			
			app.forgetPage(getName());	
			
		}
		catch (FinderException e)
		{
			setError("E-mail address not known.");
			return;
		}
		catch (Throwable t)
		{
			setError("Could not validate user and password: " + t);
			return;			
		}
	}
	
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
}
