/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship and Primix
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.valid;

import com.primix.tapestry.util.pool.*;
import java.util.*;

/**
 *  A simple implementation of {@link IValidationDelegate} that can be used
 *  as a helper bean.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 */


public class ValidationDelegate
	extends BaseValidationDelegate
	implements IPoolable
{
	private List errors;
	
	public void invalidField(IValidatingTextField field,
			ValidationConstraint constraint,
			String defaultErrorMessage)
	{
		if (errors == null)
			errors = new ArrayList();
		
		errors.add(defaultErrorMessage);
	}
	
	public void resetForPool()
	{
		if (errors != null)
			errors.clear();
	}
	
	/**
	 *  Returns the first error message, or null if there
	 *  are no error messages.
	 *
	 */
	
	public String getError()
	{
		if (errors == null || errors.size() == 0)
			return null;
		
		return (String)errors.get(0);
	}
	
	/**
	 *  Returns a {@link List} of {@link String}, the errors collected
	 *  during this request cycle.  May return null, or an empty list,
	 *  if there are no errors.
	 *
	 */
	
	public List getErrors()
	{
		return errors;
	}
}

