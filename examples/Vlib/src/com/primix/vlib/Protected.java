 package com.primix.vlib;

import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.valid.*;
import com.primix.tapestry.*;
import com.primix.vlib.pages.*;

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
 *  Base page used for pages that should be protected by the {@link Login} page.
 *  If the user is not logged in, they are redirected to the Login page first.
 *  Also, implements an error property and a validationDelegate.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Protected
extends BasePage
implements IErrorProperty
{
	private String error;
    private IValidationDelegate validationDelegate;

	public void detach()
	{
		super.detach();

		error = null;
	}
	
    public IValidationDelegate getValidationDelegate()
    {
        if (validationDelegate == null)
            validationDelegate = new SimpleValidationDelegate(this);

        return validationDelegate;
    }

	public void setError(String value)
	{
		error = value;
	}
	
	public String getError()
	{
		return error;
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
	 *  Checks if the user is logged in ... if not, they are sent
	 *  to the {@link Login} page before coming back to whatever this
	 *  page is.
	 *
	 */
	 
	public void validate(IRequestCycle cycle)
	throws RequestCycleException
	{
		Visit visit = (Visit)getVisit();
		Login page;
		
		if (visit.isUserLoggedIn())
			return;
		
		// User not logged in ... redirect through the Login page.
		
		page = (Login)cycle.getPage("Login");
		page.setTargetPage(getName());
		
		throw new PageRedirectException("Login");			
	}
}
