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

package net.sf.tapestry.contrib.form;

import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IResponseWriter;
import com.primix.tapestry.RequestCycleException;
import com.primix.tapestry.form.IPropertySelectionModel;

/**
 *  Defines an object that works with a {@link MultiplePropertySelection} component
 *  to render the individual elements obtained from the {@link IPropertySelectionModel model}.
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 */

public interface IMultiplePropertySelectionRenderer
{
	/**
	 *  Begins the rendering of the {@link PropertySelection}.
	 *
	 */

	public void beginRender(
		MultiplePropertySelection component,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;

	/**
	 *  Invoked for each element obtained from the {@link IPropertySelectionModel model}.
	 *
	 */

	public void renderOption(
		MultiplePropertySelection component,
		IResponseWriter writer,
		IRequestCycle cycle,
		IPropertySelectionModel model,
		Object option,
		int index,
		boolean selected)
		throws RequestCycleException;

	/**
	 *  Ends the rendering of the {@link PropertySelection}.
	 *
	 */

	public void endRender(
		MultiplePropertySelection component,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;
}