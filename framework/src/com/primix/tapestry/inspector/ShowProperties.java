package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import java.util.*;

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
 *  Component of the {@link Inspector} page used to display
 *  the persisent properties of the page, and the serialized view
 *  of the {@link IEngine}.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowProperties extends BaseComponent
implements ILifecycle
{
	private List properties;
	private IPageChange change;
	private IPage inspectedPage;

	public void cleanupAfterRender(IRequestCycle cycle)
	{
		properties = null;
		change = null;
		inspectedPage = null;
	}

	private void buildProperties()
	{
		Inspector inspector = (Inspector)page;

		inspectedPage = inspector.getInspectedPage();

		IEngine engine = page.getEngine();
		IPageRecorder recorder = engine.getPageRecorder(inspectedPage.getName());

		// No page recorder?  No properties.
		
		if (recorder == null)
			return;
			
		if (recorder.getHasChanges())
			properties = new ArrayList(recorder.getChanges());
	}

	/**
	 *  Returns a {@link List} of {@link IPageChange} objects.
	*
	* <p>Sort order is not defined.
	 *
	 */

	public List getProperties()
	{
		if (properties == null)
			buildProperties();

		return properties;
	}

	public void setChange(IPageChange value)
	{
		change = value;
	}

	public IPageChange getChange()
	{
		return change;
	}

	/**
	 *  Returns true if the current change has a non-null component path.
	 *
	 */

	public boolean getEnableComponentLink()
	{
		return change.getComponentPath() != null;
	}

	/**
	 *  Returns the name of the value's class, if the value is non-null.
	 *
	 */

	public String getValueClassName()
	{
		Object value;

		value = change.getNewValue();

		if (value == null)
			return "<null>";

		return value.getClass().getName();
	}	

}
