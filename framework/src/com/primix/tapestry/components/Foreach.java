package com.primix.tapestry.components;

import com.primix.foundation.*;
import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  <td>java.util.Iterator
 *		<br>java.util.Collection
 *      <br>java.lang.Object[]
 *  </td>
 *  <td>R</th>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>The source of objects to be iterated, which may be a Collection,
 *  an Iterator or an array of Objects.  If unbound, or the binding
 *  is null, then no iteration takes place.</td>
 * </tr>
 *
 * <tr>
 *  <td>currentValue</td>
 *  <td>java.lang.Object</td>
 *  <td>W</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>Used to update the current value on each iteration.</td>
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
 *  <p>Allows special behavior when processing the very first value.
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
 *  Allows special handling of the last value.
 *  </td>
 * </tr>
 *
 * </table>
 *
 * <p>The default abbreviation <b>foreach</b> may be used as the component type.
 * Informal parameters are not allowed.
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

	public Foreach(IPage page, IComponent container, String id, 
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

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
	*  <p>Returns null if the binding value is not an {@link Iterator},
	*  {@link Collection} or {@link Object}[].
	*
	*/

	protected Iterator getSourceData()
	throws RequestCycleException
	{
		Object rawValue = null;

		if (sourceBinding != null)
			rawValue = sourceBinding.getValue();

		if (rawValue == null)
			return Collections.EMPTY_LIST.iterator();


		if (rawValue instanceof Iterator)
			return (Iterator)rawValue;

		if (rawValue instanceof Collection)
		{
			Collection collection;

			collection = (Collection)rawValue;

			return collection.iterator();
		}


		if (rawValue instanceof Object[])
		{
			Object[] array;

			array = (Object[])rawValue;

			return Arrays.asList(array).iterator();
		}                        

		// Not a value value, return null to signal so.

		return null;
	}
	
    public IBinding getValueBinding()
	{
		return valueBinding;
	}

	/**
	*  Gets the source binding and iterates through
	*  its values.  For each, it updates the value binding and render's its wrapped elements.
	*
	*  <table border=1>
	*  <tr> <th colspan=4>Binding access</th> </tr>
	*  <tr> <th colspan=2> Render cycle </th> <th colspan=2> Rewind cycle </th> </tr>
	*  <tr> <th> Binding </th> <th> R/W </th> <th> Binding </th> <th> R/W </th> </tr>
	*  <tr> <td> source </td> <td>R</td> <td> source </td> <td>R</td> </tr>
	*	<tr> <td> first </td> <td>W</td> <td> first </td> <td>W</td> </tr>
	*  <tr> <td> last </td> <td>W</td>  <td> last </td> <td>W</td>  </tr>
	*  <tr> <td> value </td> </td>W</td> <td> value </td> <td>W</td> </tr>
	*  </table>

	*/

	public void render(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
		Iterator dataSource;
		int i = 0;
		Object value;
		boolean hasNext;

		dataSource = getSourceData();
		if (dataSource == null)
			throw new RequestCycleException(
				"Parameter source is not convertable to type java.util.Iterator.",
				this, cycle);

		if (valueBinding == null)
			throw new RequiredParameterException(this, "value", cycle);

		hasNext = dataSource.hasNext();

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
				if (firstBinding != null)
					firstBinding.setValue(Boolean.TRUE);

				if (hasNext && lastBinding != null)
					lastBinding.setValue(Boolean.FALSE);
			}
			else if (i == 1 && firstBinding != null)
				firstBinding.setValue(Boolean.FALSE);

			if (!hasNext && lastBinding != null)
				lastBinding.setValue(Boolean.TRUE);

			valueBinding.setValue(value);

			renderWrapped(writer, cycle);


			i++;
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
}

