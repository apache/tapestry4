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

package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.valid.*;

/**
 *  A base page for pages that contain an error property.
 *
 *  @author Howard Ship
 *  @version $Id: ErrorPage.java,v 1.1 2001/05/28 05:52:01 hship Exp $
 */

public class ErrorPage extends BasePage
{
	private String error;
	private IValidationDelegate validationDelegate;

	private class MyDelegate extends BaseValidationDelegate
	{
		public void invalidField(
			IValidatingTextField field,
			ValidationConstraint constraint,
			String defaultErrorMessage)
		{
			if (error == null)
				error = defaultErrorMessage;
		}
	}

	public IValidationDelegate getValidationDelegate()
	{
		if (validationDelegate == null)
			validationDelegate = new MyDelegate();

		return validationDelegate;
	}

	/**
	 *  Marks a particular {@link IValidatingTextField} as in error,
	 *  and sets the page's error property, if not already
	 *  set.
	 *
	 */

	protected void setErrorField(String idPath, String fieldError)
	{
		IValidatingTextField field = (IValidatingTextField) getNestedComponent(idPath);

		field.setError(true);

		if (error == null)
			error = fieldError;
	}

	public void detach()
	{
		error = null;

		super.detach();
	}

	public String getError()
	{
		return error;
	}

	public void setError(String value)
	{
		error = value;
	}

}