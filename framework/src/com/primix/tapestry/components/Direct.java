package com.primix.tapestry.components;

import com.primix.tapestry.*;
import java.util.*;

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
 *  A component for creating a link using the direct service; used for actions that
 *  are not dependant on dynamic page state.
 *
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> 
 * <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>listener</td> <td>{@link IDirectListener}</td>
 *  <td>R</td>
 *  <td>yes</td> <td>&nbsp;</td>
 *  <td>Specifies an object that is notified when the link is clicked.</td> </tr>
 *
 *  <tr>
 *  <td>context</td>
 *  <td>String[] <br> List (of String) <br> String <br>Object</td>
 *  <td>R</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>An array of Strings to be encoded into the URL.  These parameters will
 *  be decoded when the link is triggered.
 *  <p>If the context is simply an Object, then <code>toString()</code> is invoked on
 it.  It is assumed that the listener will be able to convert it back.
 *  <p>In a web application built onto of Enterprise JavaBeans, the context is
 *  often the primary key of some Entity bean; typically such keys are Strings or
 *  Integers (which can be freely converted from String to Integer by the listener).</td>
 * </tr>
 *
 * <tr>
 *   <td>enabled</td> <td>boolean</td> <td>R</td> <td>No</td> <td>true</td>
 *   <td>Controls whether the link is produced.  If disabled, the portion of the template
 *  the link surrounds is still rendered, but not the link itself.
 *  </td></tr>
 *
 *
 * <tr>
 *		<td>anchor</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The name of an anchor or element to link to.  The final URL will have '#'
 *   and the anchor appended to it.
 * </td> </tr>
 *
 * </table>
 *
 * <p>Informal parameters are allowed.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Direct
extends AbstractServiceLink
implements IDirect
{
	private IBinding listenerBinding;
	private IBinding contextBinding;

	public void setContextBinding(IBinding value)
	{
		contextBinding = value;
	}

	public IBinding getContextBinding()
	{
		return contextBinding;
	}

	/**
	*  Returns {@link IApplicationService#DIRECT_SERVICE}.
	*/

	protected String getServiceName(IRequestCycle cycle)
	{
		return IApplicationService.DIRECT_SERVICE;
	}

	protected String[] getContext(IRequestCycle cycle)
	{
		return getContext(contextBinding);
	}
	
	/**
	 *  Converts a binding to a context (an array of Strings).
	 *  This is used by the {@link Direct} and {@link Service}
	 *  components.
	 *
	 */
	 
	public static String[] getContext(IBinding binding)
	{
		Object raw;
		String[] context;
		Vector v;

		if (binding == null)
			return null;

		raw = binding.getValue();

		if (raw == null)
			return null;

		if (raw instanceof String[])
			return (String[])raw;

		if (raw instanceof String)
		{
			context = new String[1];
			context[0] = (String)raw;

			return context;
		}

		if (raw instanceof List)
		{
			List list = (List)raw;
            
			context = new String[list.size()];
	
    		return (String[])list.toArray(context);
		}
		
		// Allow simply Object ... use toString() to make it a string.
		// The listener should be able to convert it back.  For example,
		// if the real type is java.lang.Integer, it's easy to convert
		// it to an int or java.lang.Integer.

		context = new String[1];
		context[0] = raw.toString();
		
		return context;
	}

	/**
	*  Invoked by the direct service to trigger the application-specific
	*  action by notifying the {@link IDirectListener listener}.
	*
	*/

	public void trigger(IRequestCycle cycle, String[] context)
	throws RequestCycleException
	{
		IDirectListener listener;

		listener = getListener(cycle);

		listener.directTriggered(this, context, cycle);
	}

	public IBinding getListenerBinding()
	{
		return listenerBinding;
	}

	public void setListenerBinding(IBinding value)
	{
		listenerBinding = value;
	}

	private IDirectListener getListener(IRequestCycle cycle)
	throws RequestCycleException
	{
		IDirectListener result;

		try
		{
			result = (IDirectListener)listenerBinding.getValue();

			if (result == null)
				throw new RequiredParameterException(this, "listener", listenerBinding, cycle);
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException(
				"Parameter listener must be type IDirectListener.",
				this, cycle, e);
		}

		return result;
	}

}

