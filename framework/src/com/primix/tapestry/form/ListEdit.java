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
import com.primix.tapestry.util.io.*;
import java.util.*;
import java.io.*;

/**
 *  A specialized {@link Form} component used to edit a list of items
 *  within a form; it is similar to a {@link Foreach} but leverages
 *  hidden inputs within the &lt;form&gt; to store the items in the list.
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 * <tr> <td>source</td>
 *	<td>Object[] or {@link List}</td>
 *	<td>R</td>
 *	<td>yes</td> <td>&nbsp;</td>
 *	<td>The list of values to be editted within the form.  This list is only
 *  read when the component renders; it records hidden input fields in the
 *  form to guide things when the form is submitted.
 *
 *  <p>The source can contain objects of any type, though they should be serializable.
 *  Ideally, the objects should be {@link String} or wrapper types such as {@link Integer}.
 *  In other words, the source should be a list of primary keys of the objects
 *  being editted.
 *	</td> </tr>
 *
 *  <tr>
 *	<td>value</td>
 *	<td>R / W</td>
 *	<td>yes</td>
 *	<td>&nbsp;</td>
 *	<td>The value for each iteration through the list (during render or rewind).
 *	</td> </tr>
 *
 *  <tr>
 *	<td>index</td>
 *	<td>W</td>
 *	<td>no</td> <td>&nbsp;</td>
 *	<td>The index (starting at zero) for each iteration through the list.
 *	</td> </tr>
 *
 * <tr>
 *  <td>element</td>
 *  <td>R</td>
 *  <td>no</td> <td>&nbsp;</td>
 *	<td>If specified, then a tag for the specified element is placed around
 *  the content on each iteration.  This emulates the
 *  {@link com.primix.tapestry.components.Any} component.  Most often
 *  the element specified is "tr" when the ListEdit is part of a table.
 *  Any informal parameters are applied to the element.
 *	</td>
 *
 *  </table>
 *
 *  <p>Informal parameters are allowed.  A body is allowed (and expected).
 *
 * <p>An instance of {@link DataSqueezer} is used to convert arbitrary objects into 
 *  Strings and then back into objects.  However, for best efficiency, the list
 * should be simple Strings or numeric types, typically a primary key or other
 * identifier from which the rest of an object may be retrieved or constructed.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.2
 */


public class ListEdit
	extends AbstractComponent
{
	/**
	 *  Interface that allows a ListEdit to treat an Object[] array or
	 *  a {@link List} identically.
	 *
	 */
	
	private interface ISource
	{
		public int getCount();
		
		public Object get(int index);
	}
	
	private static class ArraySource 
		implements ISource
	{
		Object[] array;
		
		ArraySource(Object[] array)
		{
			this.array = array;
		}
		
		public int getCount()
		{
			return array.length;
		}
		
		public Object get(int index)
		{
			return array[index];
		}
	}
	
	private static class ListSource
		implements ISource
	{
		List list;
		
		ListSource(List list)
		{
			this.list = list;
		}
		
		public int getCount()
		{
			return list.size();
		}
		
		public Object get(int index)
		{
			return list.get(index);
		}
	}
	
	private static class EmptySource
		implements ISource
	{
		public int getCount()
		{
			return 0;
		}
		
		public Object get(int index)
		{
			throw new IndexOutOfBoundsException("ListEdit.EmptySource contains no values.");
		}
	}
	
	private IBinding sourceBinding;
	private IBinding valueBinding;
	private IBinding indexBinding;
	private IBinding elementBinding;
	private String staticElement;
	
	/**
	 *  The squeezer that converts values to and from Strings for storage in
	 *  hidden fields.  DataSqueezer is thread-safe so we can share this
	 *  across all instances.
	 *
	 */
	
	private static final DataSqueezer squeezer = new DataSqueezer();
	
	public void setSourceBinding(IBinding value)
	{
		sourceBinding = value;
	}
	
	public IBinding getSourceBinding()
	{
		return sourceBinding;
	}
	
	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}
	
	public IBinding getValueBinding()
	{
		return valueBinding;
	}
	
	public void setIndexBinding(IBinding value)
	{
		indexBinding = value;
	}
	
	public IBinding getIndexBinding()
	{
		return indexBinding;
	}
	
	/** @since 1.0.4 **/
	
	public void setElementBinding(IBinding value)
	{
		elementBinding = value;
		
		if (value.isStatic())
			staticElement = value.getString();
	}
	
	/** @since 1.0.4 **/
	
	public IBinding getElementBinding()
	{
		return elementBinding;
	}
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		ISource source = null;
		int count;
		RequestContext context = null;
		Object value = null;
		String element = null;
		
		Form form = Form.get(cycle);
		if (form == null)
			throw new RequestCycleException(
				"ListEdit components must be wrapped by a Form.",
				this);
		
		boolean cycleRewinding = cycle.isRewinding();
		boolean formRewinding = form.isRewinding();
		
		if (!cycleRewinding && elementBinding != null)
		{
			if (staticElement == null)
				element = (String)elementBinding.getObject("element", String.class);
			else
				element = staticElement;
		}
			
		// If the cycle is rewinding, but not this particular form,
		// then do nothing (don't even render the body).
		
		if (cycleRewinding && ! form.isRewinding())
			return;
		
		String name = form.getElementId(this);
		
		if (!cycleRewinding)
		{
			source = getSource();
			count = source.getCount();
			
			writer.beginEmpty("input");
			writer.attribute("type", "hidden");
			writer.attribute("name", name);
			writer.attribute("value", count);
			writer.println();
		}
		else
		{
			context = cycle.getRequestContext();
			count = Integer.parseInt(context.getParameter(name));
		}
		
		for (int i = 0; i < count; i++)
		{
			if (indexBinding != null)
				indexBinding.setInt(i);
			
			if (cycleRewinding)
				value = extractValue(context, form.getElementId(this));
			else
			{
				value = source.get(i);
				writeValue(writer, form.getElementId(this), value);
			}
			
			valueBinding.setObject(value);
			
			if (element != null)
			{
				writer.begin(element);
				generateAttributes(cycle, writer, null);
			}
			
			renderWrapped(writer, cycle);
			
			if (element != null)
				writer.end();
		}
	}
	
	private void writeValue(IResponseWriter writer, String name, Object value)
		throws RequestCycleException
	{
		String externalValue;
		
		try
		{
			externalValue = squeezer.squeeze(value);
		}
		catch (IOException ex)
		{
			throw new RequestCycleException(
				"Unable to convert " + value + 
					" to an external String in ListEdit component.", this, ex);
		}
		
		writer.beginEmpty("input");
		writer.attribute("type", "hidden");
		writer.attribute("name", name);
		writer.attribute("value", externalValue);
		writer.println();
	}
	
	private Object extractValue(RequestContext context, String name)
		throws RequestCycleException
	{
		String value = context.getParameter(name);
		
		try
		{
			return squeezer.unsqueeze(value);
		}
		catch (IOException ex)
		{
			throw new RequestCycleException(
				"Unable to convert '" + value + "' back into an object in " +
					"ListEdit component.", this, ex);
		}
	}
	
	private ISource getSource()
		throws RequestCycleException
	{
		Object raw = sourceBinding.getObject();
		
		if (raw == null)
			return new EmptySource();
		
		if (raw instanceof List)
			return new ListSource((List)raw);
		
		if (raw.getClass().isArray())
			return new ArraySource((Object[])raw);
		
		throw new RequestCycleException(
			"Unable to convert " + raw + " to a source for ListEdit component.", 
			this);
	}
}

