package com.primix.tapestry.components;

import com.primix.tapestry.spec.ComponentSpecification;
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
 *  A component for creating a link for an arbitrary service that uses
 *  no parameters.  
 *
 * <table border=1>

 *
 * <tr>
 *		<td>service</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The name of the service.</td>  </tr>
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


public class Service extends AbstractServiceLink
{
	private IBinding serviceBinding;
	private String serviceValue;

	public Service(IPage page, IComponent container, String name,
		ComponentSpecification specification)
	{
		super(page, container, name, specification);
	}

	public IBinding getServiceBinding()
	{
		return serviceBinding;
	}

	/**
	*  Returns name of the service specified by the service parameter.
	*/

	protected String getServiceName(IRequestCycle cycle)
	{
		if (serviceValue != null)
			return serviceValue;

		return serviceBinding.getString();
	}

	public void setServiceBinding(IBinding value)
	{
		serviceBinding = value;

		if (value.isStatic())
			serviceValue = value.getString();
	}
}

