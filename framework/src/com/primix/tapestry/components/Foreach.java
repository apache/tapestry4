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

package com.primix.tapestry.components;

import com.primix.tapestry.util.*;
import java.util.*;
import com.primix.tapestry.*;

/**
 *  Repeatedly renders its wrapped contents while iterating through
 *  a list of values.
 *
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *    <th>Read / Write</th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>source</td>
 *  <td>{@link Iterator}
 *		<br>{@link Collection}
 *      <br>java.lang.Object[]
 *  </td>
 *  <td>R</th>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>The source of objects to be iterated, which may be a Collection,
 *  an Iterator or an array of Objects.  If unbound, or the binding
 *  is null, or bound to an unusable value (one not in the list), 
 *  then no iteration takes place.</td>
 * </tr>
 *
 * <tr>
 *  <td>value</td>
 *  <td>java.lang.Object</td>
 *  <td>W</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>Used to update the current value on each iteration.
 *  <p>Alternate, wrapped components may access the value via
 *  the {@link #getValue() value property}.</td>
 *  </tr>
 *
 *  <tr>
 *   <td>first</td>
 *   <td>java.lang.Boolean</td>
 *  <td>W</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>Set to <code>Boolean.TRUE</code> when the first value is processed.
 *  Set to <code>Boolean.FALSE</code> when the second value is processed.
 *
 *  <p>Allows special behavior when processing the very first value.
 *
 *  <p>Wrapped component may alternately access this value via the
 *  {@link #isFirst() first property}.
 *  </td>
 *  </tr>
 *
 *  <tr>
 *  <td>last</td>
 *  <td>java.lang.Boolean</td>
 *  <td>W</td>
 *  <td>no</td>
 * <td>&nbsp;</td>
 * <td>Set to <code>Boolean.FALSE</code> when the first value is processed.
 *  Set to <code>Boolean.TRUE</code> when the last value is processed.
 *
 *  <p>Allows special handling of the last value.
 *
 * <p>Wrapped component may alternately access this value via the
 *  {@link #isLast() last property}.
 *
 *  </td>
 * </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Foreach extends AbstractComponent
{
	private IBinding sourceBinding;
	private IBinding valueBinding;
	private IBinding firstBinding;
	private IBinding lastBinding;
	
	private Object value;
	private boolean first;
	private boolean last;
	private boolean rendering;
	
	public IBinding getFirstBinding()
	{
		return firstBinding;
	}
	
	public IBinding getLastBinding()
	{
		return lastBinding;
	}
	
	public IBinding getSourceBinding()
	{
		return sourceBinding;
	}
	
	/**
	 *  Gets the source binding and returns an {@link Iterator}
	 *  representing
	 *  the values identified by the source.  Returns an empty {@link Iterator}
	 *  if the binding, or the binding value, is null.
	 *
	 *  <p>Invokes {@link Tapestry#coerceToIterator(Object)} to perform
	 *  the actual conversion.
	 *
	 */
	
	protected Iterator getSourceData()
		throws RequestCycleException
	{
		
		if (sourceBinding == null)
			return null;
		
		Object rawValue = sourceBinding.getObject();
		
		return Tapestry.coerceToIterator(rawValue);
	}
	
    public IBinding getValueBinding()
	{
		return valueBinding;
	}
	
	/**
	 *  Gets the source binding and iterates through
	 *  its values.  For each, it updates the value binding and render's its wrapped elements.
	 *
	 */
	
	public void render(IResponseWriter writer, IRequestCycle cycle) 
		throws RequestCycleException
	{
		
		Iterator dataSource = getSourceData();
		
		// The dataSource was either not convertable, or was empty.
		
		if (dataSource == null)
			return;		
		
		try
		{
			rendering = true;
			value = null;
			int i = 0;
			
			boolean hasNext = dataSource.hasNext();
			
			while (hasNext)
			{
				value = dataSource.next();
				hasNext = dataSource.hasNext();
				
				// On the first pass, set the 'first' to true and
				// (usually) the 'last' binding to false
				// On the second pass, set the 'first' binding to false
				// On the last pass, set the 'last' binding to true
				
				if (i == 0)
				{
					setFirst(true);
					
					if (hasNext)
						setLast(false);
				}
				else if (i == 1)
					setFirst(false);
				
				if (!hasNext)
					setLast(true);
				
				if (valueBinding != null)
					valueBinding.setObject(value);
				
				renderWrapped(writer, cycle);
				
				i++;
			}
		}
		finally
		{
			value = null;
			rendering = false;
		}
	}
	
	public void setFirstBinding(IBinding value)
	{
		firstBinding = value;
	}
	
	public void setLastBinding(IBinding value)
	{
		lastBinding = value;
	}
	
	public void setSourceBinding(IBinding value)
	{
		sourceBinding = value;
	}
	
	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}
	
	private void setFirst(boolean value)
	{
		first = value;
		
		if (firstBinding != null)
			firstBinding.setBoolean(value);
	}
	
	/**
	 *  Returns true if the current {@link #getValue() value}
	 *  is the first value extracted from the
	 *  source.
	 *
	 *  @throws RenderOnlyPropertyException is the Foreach is not currently rendering.
	 *
	 */
	
	public boolean isFirst()
	{
		if (!rendering)
			throw new RenderOnlyPropertyException(this, "first");
		
		return first; 
	}
	
	
	private void setLast(boolean value)
	{
		last = value;
		
		if (lastBinding != null)
			lastBinding.setBoolean(value);
	}
	
	/**
	 *  Returns true if the current {@link #getValue() value}
	 *  is the last value extracted from the
	 *  source.
	 *
	 *  @throws RenderOnlyPropertyException is the Foreach is not currently rendering.
	 *
	 */
	
	public boolean isLast()
	{
		if (!rendering)
			throw new RenderOnlyPropertyException(this, "last");
		
		return last;	
	}
	
	/**
	 *  Returns the most recent value extracted from the source parameter.
	 *
	 *  @throws RenderOnlyPropertyException is the Foreach is not currently rendering.
	 *
	 */
	
	public Object getValue()
	{
		if (!rendering)
			throw new RenderOnlyPropertyException(this, "value");
		
		return value;
	}
	
}

