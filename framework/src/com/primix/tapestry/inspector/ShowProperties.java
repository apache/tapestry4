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


package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.util.prop.*;
import java.util.*;

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

public class ShowProperties
	extends BaseComponent
	implements PageRenderListener
{
	/**
	 *  Stores elements in the explorePath.
	 *
	 *  @since 1.0.6
	 */
	
	public static class ExplorePathElement 
		implements IPublicBean
	{
		public ExplorePathElement(String path, String propertyName)
		{
			this.path = path;
			this.propertyName = propertyName;
		}
		
		public String path;
		public String propertyName;
	}

	/**
	 *  Sorts {@link IPropertyAccessor}s by name.
	 *
	 *  @since 1.0.6
	 *
	 */
	
	private static class AccessorSorter implements Comparator
	{
		public int compare(Object left, Object right)
		{
			IPropertyAccessor aLeft = (IPropertyAccessor)left;
			IPropertyAccessor aRight = (IPropertyAccessor)right;
			
			return aLeft.getName().compareTo(aRight.getName());
		}
	}
	private List properties;
	private IPageChange change;
	private IPage inspectedPage;
	private IPropertyAccessor accessor;
	
	/**
	 *  Registers this component as a {@link PageRenderListener}.
	 *
	 *  @since 1.0.5
	 *
	 */
	
	protected void finishLoad()
	{
		page.addPageRenderListener(this);
	}
	
	/**
	 *  Does nothing.
	 *
	 *  @since 1.0.5
	 *
	 */
	
	public void pageBeginRender(PageEvent event)
	{
	}
	
	/**
	 *  @since 1.0.5
	 *
	 */
	
	public void pageEndRender(PageEvent event)
	{
		properties = null;
		change = null;
		inspectedPage = null;
		accessor = null;
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
	 *  Returns true if the current change has a null component path.
	 *
	 */

	public boolean getDisableComponentLink()
	{
		return change.getComponentPath() == null;
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

	public List getExplorePath()
	{
		Inspector inspector = (Inspector)page;
		
		String explorePath = inspector.getExplorePath();
		if (explorePath == null)
			return null;
		
		String[] propertyName = PropertyHelper.splitPropertyPath(explorePath);
		List result = new ArrayList(propertyName.length);
		StringBuffer buffer = new StringBuffer(explorePath.length());
		
		for (int i = 0; i < propertyName.length; i++)
		{
			if (i > 0)
				buffer.append(PropertyHelper.PATH_SEPERATOR);
			
			buffer.append(propertyName[i]);
			
			result.add(new ExplorePathElement(buffer.toString(), propertyName[i]));
		}
		
		return result;
	}
	
	private Object getExploredObject()
	{
		Inspector inspector = (Inspector)page;
		
		return inspector.getExploredObject();
	}
	
	/**
	 *  Gets the class name of the explored object.  This does some minor
	 *  translation to be more useful with array types.
	 *
	 */
	
	public String getExploredClassName()
	{
		Object explored = getExploredObject();
		
		if (explored == null)
			return "<Null>";
		
		return convertClassToName(explored.getClass());
	}
	
	private String convertClassToName(Class cl)
	{
		// TODO: This only handles one-dimensional arrays
		// property.
		
		if (cl.isArray())
			return "array of " + cl.getComponentType().getName();
		
		return cl.getName();
	}
	
	public String getExploredValue()
	{
		Object explored = getExploredObject();
		
		return (explored == null) ? "<null>" : explored.toString();
	}
	
	public void exploreComponent(IRequestCycle cycle)
	{
		Inspector inspector = (Inspector)page;
		
		inspector.setExplorePath(null);
	}
	
	public void selectExplorePath(String[] context, IRequestCycle cycle)
	{
		Inspector inspector = (Inspector)page;
		
		inspector.setExplorePath(context[0]);
	}
	
	public void exploreProperty(String[] context, IRequestCycle cycle)
	{
		Inspector inspector = (Inspector)page;
		
		String propertyName = context[0];
		
		String explorePath = inspector.getExplorePath();
		
		if (explorePath == null)
			explorePath = propertyName;
		else
			explorePath = explorePath + PropertyHelper.PATH_SEPERATOR + propertyName;
		
		inspector.setExplorePath(explorePath);	
	}
		
	public List getAccessors()
	{
		Object explored = getExploredObject();
		if (explored == null)
			return null;
		
		List result = new ArrayList();
		PropertyHelper helper = PropertyHelper.forInstance(explored);
		
		Collection accessors = helper.getAccessors(explored);
		
		Iterator i = accessors.iterator();
		while (i.hasNext())
		{
			IPropertyAccessor ac = (IPropertyAccessor)i.next();
			
			if (ac.isReadable())
				result.add(ac);
		}
		
		Collections.sort(result, new AccessorSorter());
		
		return result;
	}
	
	public IPropertyAccessor getAccessor()
	{
		return accessor;
	}
	
	public void setAccessor(IPropertyAccessor value)
	{
		accessor = value;
	}

	public String getAccessorTypeName()
	{
		return convertClassToName(accessor.getType());
	}
	
}
