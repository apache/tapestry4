package com.primix.tapestry.components.html.form;

import com.primix.tapestry.*;

/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

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
	private boolean immediateSubmit;

	public SelectPropertySelectionRenderer()
	{
		super();
	}

	public SelectPropertySelectionRenderer(boolean immediateSubmit)
	{
		this.immediateSubmit = immediateSubmit;
	}

	/**
	 *  Returns true if this renderer was created to perform an immediate submit.
	 *  If so, changing the value of the &lt;select&gt; causes the form containing
	 *  it to be submitted.
	 *
	 */

	public boolean getImmediateSubmit()
	{
		return immediateSubmit;
	}

	/**
	 *  Writes the &lt;select&gt; element.
	 *
	 */

	public void beginRender(PropertySelection component, IResponseWriter writer, 
		IRequestCycle cycle)
	throws RequestCycleException
	{
		writer.begin("select");
		writer.attribute("name", component.getName());

		if (immediateSubmit)
			writer.attribute("onChange", "javascript:this.form.submit();");
	}

	/**
	 *  Closes the &lt;select&gt; element.
	 *
	 */

	public void endRender(PropertySelection component, IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		writer.end(); // <select>
	}

	/**
	 *  Writes an &lt;option&gt; element.
	 *
	 */

	public void renderOption(PropertySelection component, IResponseWriter writer, IRequestCycle cycle,
		IPropertySelectionModel model, Object option, int index, boolean selected)
	throws RequestCycleException
	{
		writer.beginEmpty("option");
		writer.attribute("value", model.getValue(index));

		if (selected)
			writer.attribute("selected");

		writer.print(model.getLabel(index));	
	}
}
