package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.*;
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
 * Invoked from the {@link Login} page, to allow a user to register
 * into the system on-the-fly.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Register extends BasePage
{
	private String error;
	private String firstName;
	private String lastName;
	private String email;
	private String password1;
	private String password2;
	
	public Register(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public void detachFromApplication()
	{
		super.detachFromApplication();

		error = null;
		firstName = null;
		lastName = null;
		email = null;
		password1 = null;
		password2 = null;
	}
	
	public String getError()
	{
		return error;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getPassword1()
	{
		return password1;
	}
	
	public String getPassword2()
	{
		return password2;
	}
	
	public void setError(String value)
	{
		error = value;
	}
	
	public void setFirstName(String value)
	{
		firstName = value;
	}
	
	public void setLastName(String value)
	{
		lastName = value;
	}
	
	public void setEmail(String value)
	{
		email = value;
	}
	
	public void setPassword1(String value)
	{
		password1 = value;
	}
	
	public void setPassword2(String value)
	{
		password2 = value;
	}
	
	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			throws RequestCycleException
			{
				attemptRegister(cycle);
			}
		};
	}
	
	private void attemptRegister(IRequestCycle cycle)
	{
		VirtualLibraryApplication app;
		IOperations bean;
		Login login;
		IPerson user;
		
		if (!isEqual(password1, password2))
		{
			setError("Enter the same password twice.");
			password1 = null;
			password2 = null;
			return;
		}
		
		if (isEmpty(password1))
		{
			setError("You must enter a password.");
			return;
		}
		
		if (isEmpty(lastName))
		{
			setError("You must enter a last name.");
			return;
		}
		
		if (isEmpty(email))
		{
			setError("You must enter an email address.");
			return;
		}
		
		app = (VirtualLibraryApplication)application;
		bean = app.getOperations();
		
		try
		{
			user = bean.registerNewUser(firstName, lastName, email, password1);
		}
		catch (RegistrationException e)
		{
			setError(e.getMessage());
			return;
		}
		catch (CreateException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		// Ask the login page to return is to the proper place.
		
		login = (Login)cycle.getPage("Login");
		login.loginUser(user, cycle);
	}
	
	private boolean isEqual(String value1, String value2)
	{
		if (value1 == value2)
			return true;
		
		if (value1 == null || value2 == null)
			return false;
			
		return value1.equals(value2);
	}
	
	private boolean isEmpty(String value)
	{
		if (value == null)
			return true;
		
		return isEqual(value, "");
	}	
}
