/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.link;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.html.*;
import java.util.*;
import javax.servlet.http.*;

/**
 *  Abstract super-class for components that generate some form of
 *  &lt;a&gt; hyperlink using an {@link IEngineService}.
 *  Supplies support for the following parameters:
 *
 *  <ul>
 *  <li>scheme</li>
 *  <li>port</li>
 *  <li>anchor</li>
 * </ul>
 *
 * <p>Subclasses usually need only implement {@link #getServiceName(IRequestCycle)}
 * and {@link #getContext(IRequestCycle)}.
 * 
 *                       
 * @author Howard Lewis Ship
 * @version $Id$
 */

public abstract class GestureLink extends AbstractServiceLink
{

	private IBinding anchorBinding;
	private String anchorValue;

	private IBinding schemeBinding;
	private String schemeValue;

	private IBinding portBinding;
	private int portValue;

    /**
     *  Constructs a URL based on the service, context plus scheme, port and anchor.
     * 
     **/
    
	protected String getURL(IRequestCycle cycle) throws RequestCycleException
	{
		return buildURL(cycle, getContext(cycle));
	}

	/**
	 *  Invoked by {@link #getURL()}.
	 *  The default implementation returns null; other
	 *  implementations can provide appropriate parameters as needed.
	 *  
	 **/

	protected String[] getContext(IRequestCycle cycle) throws RequestCycleException
	{
		return null;
	}

	private String buildURL(IRequestCycle cycle, String[] context) throws RequestCycleException
	{
		String anchor = null;
		String scheme = null;
		int port = 0;
		String URL = null;

		String serviceName = getServiceName();
		IEngineService service = cycle.getEngine().getService(serviceName);

		if (service == null)
			throw new RequestCycleException(
				Tapestry.getString("GestureLink.missing-service", serviceName),
				this);

		Gesture g = service.buildGesture(cycle, this, context);

		// Now, dress up the URL with scheme, server port and anchor,
		// as necessary.

		if (schemeValue != null)
			scheme = schemeValue;
		else if (schemeBinding != null)
			scheme = schemeBinding.getString();

		if (portValue != 0)
			port = portValue;
		else if (portBinding != null)
			port = portBinding.getInt();

		if (scheme == null && port == 0)
			URL = g.getURL();
		else
			URL = g.getAbsoluteURL(scheme, null, port);

		if (anchorValue != null)
			anchor = anchorValue;
		else if (anchorBinding != null)
			anchor = anchorBinding.getString();

		if (anchor == null)
			return URL;

		return URL + "#" + anchor;
	}

	public IBinding getAnchorBinding()
	{
		return anchorBinding;
	}

	public IBinding getSchemeBinding()
	{
		return schemeBinding;
	}

	public void setSchemeBinding(IBinding value)
	{
		schemeBinding = value;

		if (value.isStatic())
			schemeValue = value.getString();
	}

	public IBinding getPortBinding()
	{
		return portBinding;
	}

	public void setPortBinding(IBinding value)
	{
		portBinding = value;

		if (value.isStatic())
			portValue = value.getInt();
	}

	/**
	 *  Returns the service used to build URLs.
	 *
	 **/

	protected abstract String getServiceName();

	public void setAnchorBinding(IBinding value)
	{
		anchorBinding = value;

		if (value.isStatic())
			anchorValue = value.getString();
	}

}