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

package com.primix.tapestry.form;

import com.primix.tapestry.*;

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

	/**
	 *  This method is deprecated; the behavior can be better
	 *  achieved by using a {@link com.primix.tapestry.script.Script} component
	 *  to provide the necessary <code>onChange</code> handler.
	 *
	 *  @deprecated
	 *
	 */
	 
	public SelectPropertySelectionRenderer(boolean immediateSubmit)
	{
		this.immediateSubmit = immediateSubmit;
	}

	/**
	 *  Returns true if this renderer was created to perform an immediate submit.
	 *  If so, changing the value of the &lt;select&gt; causes the form containing
	 *  it to be submitted.
	 *
	 *  @deprecated
	 */

	public boolean getImmediateSubmit()
	{
		return immediateSubmit;
	}

	/**
	 *  Writes the &lt;select&gt; element.  If the
	 *  {@link PropertySelection} is {@link PropertySelection#isDisabled() disabled}
	 *  then a <code>disabled</code> attribute is written into the tag
	 *  (though Navigator will ignore this).
	 *
	 */

	public void beginRender(PropertySelection component, IResponseWriter writer, 
		IRequestCycle cycle)
	throws RequestCycleException
	{
		writer.begin("select");
		writer.attribute("name", component.getName());

		if (component.isDisabled())
			writer.attribute("disabled");
			
		// We're in the process of removing this immediateSubmit business.
		
		if (immediateSubmit)
			writer.attribute("onChange", "javascript:this.form.submit();");
		
		writer.println();	
	}

	/**
	 *  Closes the &lt;select&gt; element.
	 *
	 */

	public void endRender(PropertySelection component, IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
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
		
		writer.println();	
	}
}
