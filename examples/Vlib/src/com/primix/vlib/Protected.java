/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.vlib;

import com.primix.tapestry.components.*;
import com.primix.tapestry.valid.*;
import com.primix.tapestry.callback.*;
import com.primix.tapestry.*;
import com.primix.vlib.pages.*;

/**
 *  Base page used for pages that should be protected by the {@link Login} page.
 *  If the user is not logged in, they are redirected to the Login page first.
 *  Also, implements an error property and a validationDelegate.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class Protected extends BasePage implements IErrorProperty
{
	private String error;
	private IValidationDelegate validationDelegate;

	public void detach()
	{
		error = null;
		validationDelegate = null;

		super.detach();
	}

	public IValidationDelegate getValidationDelegate()
	{
		if (validationDelegate == null)
			validationDelegate = new SimpleValidationDelegate();

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
		IField field;

		field = (IField) getComponent(componentId);

		IValidationDelegate delegate = getValidationDelegate();

		delegate.setField(field);
		delegate.record(new ValidatorException(message));

	}

	/**
	 *  Checks if the user is logged in ... if not, they are sent
	 *  to the {@link Login} page before coming back to whatever this
	 *  page is.
	 *
	 */

	public void validate(IRequestCycle cycle) throws RequestCycleException
	{
		Visit visit = (Visit) getVisit();

		if (visit != null && visit.isUserLoggedIn())
			return;

		// User not logged in ... redirect through the Login page.

		Login login = (Login) cycle.getPage("Login");

		login.setCallback(new PageCallback(this));

		throw new PageRedirectException(login);
	}
}