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

package net.sf.tapestry.form;

import com.primix.tapestry.*;

import net.sf.tapestry.*;

/**
 *  Implementation of {@link IPropertySelectionRenderer} that
 *  produces a &lt;select&gt; element (containing &lt;option&gt; elements).
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class SelectPropertySelectionRenderer
	implements IPropertySelectionRenderer
{
	/**
	 *  Writes the &lt;select&gt; element.  If the
	 *  {@link PropertySelection} is {@link PropertySelection#isDisabled() disabled}
	 *  then a <code>disabled</code> attribute is written into the tag
	 *  (though Navigator will ignore this).
	 *
	 */

	public void beginRender(
		PropertySelection component,
		IMarkupWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		writer.begin("select");
		writer.attribute("name", component.getName());

		if (component.isDisabled())
			writer.attribute("disabled");

		writer.println();
	}

	/**
	 *  Closes the &lt;select&gt; element.
	 *
	 */

	public void endRender(
		PropertySelection component,
		IMarkupWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException
	{
		writer.end(); // <select>
	}

	/**
	 *  Writes an &lt;option&gt; element.
	 *
	 */

	public void renderOption(
		PropertySelection component,
		IMarkupWriter writer,
		IRequestCycle cycle,
		IPropertySelectionModel model,
		Object option,
		int index,
		boolean selected)
		throws RequestCycleException
	{
		writer.beginEmpty("option");
		writer.attribute("value", model.getValue(index));

		if (selected)
			writer.attribute("selected");

		writer.print(model.getLabel(index));

		writer.println();
	}
}