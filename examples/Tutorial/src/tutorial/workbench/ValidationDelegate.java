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

package tutorial.workbench;

import com.primix.tapestry.*;
import com.primix.tapestry.valid.*;
import com.primix.tapestry.util.pool.*;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 */

public class ValidationDelegate
	extends BaseValidationDelegate
	implements IPoolable
{
	private String errorMessage;

	public void resetForPool()
	{
		errorMessage = null;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void invalidField(
		IValidatingTextField field,
		ValidationConstraint constraint,
		String defaultErrorMessage)
	{
		if (errorMessage == null)
			errorMessage = defaultErrorMessage;
	}

	public void writeAttributes(
		IValidatingTextField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (field.getError())
			writer.attribute("class", "field-error");
	}

	public void writeErrorSuffix(
		IValidatingTextField field,
		IResponseWriter writer,
		IRequestCycle cycle)
	{
		if (field.getError())
		{
			writer.print(" ");
			writer.beginEmpty("img");
			writer.attribute("src", "images/workbench/Warning-small.gif");
			writer.attribute("height", 20);
			writer.attribute("width", 20);
		}
	}

	public void writeLabelPrefix(
		IValidatingTextField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (field.getError())
		{
			writer.begin("span");
			writer.attribute("class", "label-error");
		}
	}

	public void writeLabelSuffix(
		IValidatingTextField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		if (field.getError())
			writer.end(); // <span>
	}
}