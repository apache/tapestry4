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

package net.sf.tapestry.vlib;

import com.primix.tapestry.*;
import net.sf.tapestry.valid.*;

import net.sf.tapestry.*;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.*;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Implementation of {@link IValidationDelegate} uses the
 *  correct CSS class when rendering errors.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class SimpleValidationDelegate extends ValidationDelegate
{
	public void writeLabelPrefix(
		IFormComponent component,
		IMarkupWriter writer,
		IRequestCycle cycle)
	{
		if (isInError(component))
		{
			writer.begin("span");
			writer.attribute("class", "clsInvalidField");
		}
	}

	public void writeLabelSuffix(
		IFormComponent component,
		IMarkupWriter writer,
		IRequestCycle cycle)
	{
		if (isInError(component))
			writer.end();
	}

	public void writePrefix(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		if (isInError())
		{
			writer.begin("span");
			writer.attribute("class", "error");
		}
	}
	
	public void writeSuffix(
		IMarkupWriter writer,
		IRequestCycle cycle)
	{
		if (isInError())
			writer.end(); // <span>
	}


}