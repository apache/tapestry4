package com.primix.tapestry.pages;

import com.primix.foundation.exception.*;
import com.primix.foundation.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;

/* Tapestry Web Application Framework
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
 *  An exception reporting page.
 *
 *  @author Howard Ship
 *  @version $Id$
 */



public class Exception extends BasePage
{
	private ExceptionDescription[] exceptions;

	public Exception(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public ExceptionDescription[] getExceptions()
	{
		return exceptions;
	}

	/**
	*  Returns a delegate that invokes {@link
	*  RequestContext#write(IResponseWriter)}.
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

	public void setException(Throwable value)
	{
		ExceptionAnalyzer analyzer;

		analyzer = new ExceptionAnalyzer();

		exceptions = analyzer.analyze(value);
	}
}

