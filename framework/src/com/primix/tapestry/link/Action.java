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

package com.primix.tapestry.link;

import com.primix.tapestry.*;

/**
 *  A component for creating a link that is handled using the action service.
 *
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>listener</td> <td>{@link IActionListener}</td>
 *  <td>R</td>
 *  <td>no</td> <td>&nbsp;</td>
 *  <td>Specifies an object that is notified when the link is clicked, for services that
 *  require a listener.</td> </tr>
 *
 * <tr>
 *   <td>disabled</td> <td>boolean</td> <td>R</td> <td>No</td> <td>true</td>
 *   <td>Controls whether the link is produced.  If disabled, the portion of the template
 *  the link surrounds is still rendered, but not the link itself.
 *  </td></tr>
 *
 * <tr>
 *	<td>stateful</td>
 *  <td>boolean</td>
 *	<td>R</td>
 *	<td>no</td>
 *	<td>true</td>
 *	<td>If true (the default), then the component requires an active (i.e., non-new)
 * {@link HttpSession) when triggered.  Failing that, it throws a {@link StaleLinkException}.
 *  If false, then no check is necessary.  The latter works well with links that
 * encode all necessary state inside the URL itself.</td>
 * </tr>
 *
 * <tr>
 *		<td>scheme</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If specified, then a longer URL (including scheme, server and possibly port)
 * is generated using the specified scheme. Server is determined fromt he incoming request,
 * and port is deterimined from the port paramter or the incoming request.
 *  </td>
 * </tr>
 *
 * <tr>
 *		<td>port</td>
 *		<td>int</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If specified, then a longer URL (including scheme, server and port)
 *  is generated using the specified port.  The server is determined from the incoming
 *  request, the scheme from the scheme paramter or the incoming request.
 * </td>
 * </tr>
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
 * <p>Informal  parameters are allowed.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Action 
	extends AbstractServiceLink
	implements IAction
{
	private IBinding listenerBinding;
	private IBinding statefulBinding;
	private boolean staticStateful;
	private boolean statefulValue;
	
	// Each instance gets its own context array.
	
	private String[] context;
	
	public IBinding getListenerBinding()
	{
		return listenerBinding;
	}
	
	public void setListenerBinding(IBinding value)
	{
		listenerBinding = value;
	}
	
	private IActionListener getListener(IRequestCycle cycle)
		throws RequestCycleException
	{
		IActionListener result;
		
		try
		{
			result = (IActionListener)listenerBinding.getObject("listener", IActionListener.class);
		}
		catch (BindingException ex)
		{
			throw new RequestCycleException(this, ex);
		}
		
		if (result == null)
			throw new RequiredParameterException(this, "listener", listenerBinding);
		
		return result;
	}
	
	public void setStatefulBinding(IBinding value)
	{
		statefulBinding = value;
		
		staticStateful = value.isStatic();
		if (staticStateful)
			statefulValue = value.getBoolean();
	}
	
	public IBinding getStatefulBinding()
	{
		return statefulBinding;
	}
	
	/**
	 *  Returns true if the stateful parameter is bound to
	 *  a true value.  If stateful is not bound, also returns
	 *  the default, true.
	 *
	 */
	
	public boolean getRequiresSession()
	{
		if (staticStateful)
			return statefulValue;
		
		if (statefulBinding != null)
			return statefulBinding.getBoolean();
		
		return true;
	}
	/**
	 *  Returns {@link IEngineService#ACTION_SERVICE}.
	 */
	
	protected String getServiceName(IRequestCycle cycle)
	{
		return IEngineService.ACTION_SERVICE;
	}
	
	
	protected String[] getContext(IRequestCycle cycle)
		throws RequestCycleException
	{
		String actionId;
		IActionListener listener;
		
		actionId = cycle.getNextActionId();
		
		if (cycle.isRewound(this))
		{
			listener = getListener(cycle);
			
			listener.actionTriggered(this, cycle);
			
			throw new RenderRewoundException(this);
		}
		
		if (context == null)
			context = new String[1];
		
		context[0] = actionId;
		
		return context;
	}
}
