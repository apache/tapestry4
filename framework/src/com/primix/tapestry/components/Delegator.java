package com.primix.tapestry.components;

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
 *  A component which delegates it's behavior to another object.
 *
  * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *	  <th>Read / Write </th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>delegate</td>
 *  <td>{@link IRender}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>The object which will provide the rendering for the component.</td>
 * </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Delegator extends AbstractComponent
{
	private IBinding delegateBinding;

	public void setDelegateBinding(IBinding value)
	{
		delegateBinding = value;
	}

	public IBinding getDelegateBinding()
	{
		return delegateBinding;
	}

	/**
	*  Gets its delegate and invokes {@link IRender#render(IResponseWriter, IRequestCycle)}
	*  on it.
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
		IRender delegate = null;

        try
        {
            delegate = (IRender)delegateBinding.getObject("delegate", IRender.class);
		}
        catch (BindingException ex)
        {
            throw new RequestCycleException(this, ex);
		}
	
		if (delegate == null)
			throw new RequiredParameterException(this, "delegate", delegateBinding);
			
		delegate.render(writer, cycle);
	}
}

