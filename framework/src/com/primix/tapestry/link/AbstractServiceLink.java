package com.primix.tapestry.components.html.link;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.*;
import java.util.*;
import javax.servlet.http.*;

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
 *  Abstract super-class for components that generate some form of
 *  &lt;a&gt; hyperlink using an {@link IEngineService}.
 *  Supplies support for the following parameters:
 *
 *  <ul>
 *  <li>scheme</li>
 *  <li>enabled</li>
 *  <li>anchor</li>
 * </ul>
 *
 * <p>Subclasses usually need only implement {@link #getServiceName(IRequestCycle)}
 * and {@link #getContext(IRequestCycle)}.
 *                       
 * @author Howard Ship
 * @version $Id$
 */


public abstract class AbstractServiceLink
    extends AbstractComponent
    implements IServiceLink
{
	private static final int DEFAULT_HTTP_PORT = 80;
	private static final String[] reservedNames = { "href" };

	// A number of characters to add to the URL to get the initial size
	// of the StringBuffer used to assemble the complete URL.
	
	private static final int URL_PAD = 50;

	private IBinding enabledBinding;
	private boolean staticEnabled;
	private boolean enabledValue;

	private boolean enabled;

	private IBinding anchorBinding;
	private String anchorValue;

	private IBinding schemeBinding;
	private String schemeValue;
	
	private IBinding portBinding;
	private int portValue;
	
	private boolean rendering;

	private static final int MAP_SIZE = 3;
	private Map attributes;

	/**
	*  Invoked by {@link #render(IResponseWriter, IRequestCycle)} if the
	*  component is enabled.  The default implementation returns null; other
	*  implementations can provide appropriate parameters as needed.
	*  
	*/

	protected String[] getContext(IRequestCycle cycle)
	throws RequestCycleException
	{
		return null;
	}

	protected String buildURL(IRequestCycle cycle, String[] context)
	throws RequestCycleException
	{
		IEngineService service;
		String serviceName;
		String url;
		String anchor = null;
		StringBuffer buffer = null;
		String scheme = null;
		int port = 0;

		serviceName = getServiceName(cycle);
		service = cycle.getEngine().getService(serviceName);

		if (service == null)
			throw new RequestCycleException("No engine service named " + 
				serviceName + ".",
				this, cycle);
		
		// Perform the major work of building the URL.
		
		url = service.buildURL(cycle, this, context);

		url = cycle.encodeURL(url);
		
		// Now, dress up the URL with scheme, server port and anchor,
		// as necessary.
		
		if (anchorValue != null)
			anchor = anchorValue;
		else
			if (anchorBinding != null)
			anchor = anchorBinding.getString();

		if (schemeValue != null)
			scheme = schemeValue;
		else
			if (schemeBinding != null)
				scheme = schemeBinding.getString();	
		
		if (portValue != 0)
			port = portValue;
		else
			if (portBinding != null)
				port = portBinding.getInt();
				
		// If nothing to add to the URL, then simply return it.
		
		if (anchor == null &&
			scheme == null &&
			port == 0)
				return url;
			
		buffer = new StringBuffer(url.length() + URL_PAD);
		
		if (scheme != null || port != 0)
		{
			HttpServletRequest request = cycle.getRequestContext().getRequest();
			
			// If just the port is specified, but not the scheme, use the
			// same scheme as the incoming request.
			
			if (scheme == null)
				scheme = request.getScheme();
				
			buffer.append(scheme);
			buffer.append("://");
			buffer.append(request.getServerName());
			
			// If scheme specified but not port, use the same
			// port as the incoming request.
			
			if (port == 0)
				port = request.getServerPort();

			// This is a little shakey .. the scheme may not be 'http', for example.
			// Not sure how to get this information automatically, may be
			// for the application to figure out.
			
			if (port != DEFAULT_HTTP_PORT)
			{
				buffer.append(':');
				buffer.append(port);	
			}		
		}

		buffer.append(url);
		
		if (anchor != null)
		{
			buffer.append('#');
			buffer.append(anchor);
		}
		
		return buffer.toString();
	}

	public IBinding getAnchorBinding()
	{
		return anchorBinding;
	}

	public IBinding getEnabledBinding()
	{
		return enabledBinding;
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
	*  @see #buildURL(IRequestCycle, String[])
	*
	*/

	protected abstract String getServiceName(IRequestCycle cycle);

	/**
	 *  Returns true if the link is enabled, false otherwise.
	 *
	 *  @throws RenderOnlyPropertyException if the component is not currently rendering.
	 *
	 */

	public boolean isEnabled()
	{
		if (!rendering)
			throw new RenderOnlyPropertyException(this, "enabled");

		return enabled;
	}

	public void setAnchorBinding(IBinding value)
	{
		anchorBinding = value;

		if (value.isStatic())
			anchorValue = value.getString();
	}

	public void setEnabledBinding(IBinding value)
	{
		enabledBinding = value;

		staticEnabled = value.isStatic();

		if (staticEnabled)
			enabledValue = value.getBoolean();
	}

	/**
	*  Invoked from {@link #render(IResponseWriter, IRequestCycle)}, 
	*  this is responsible for
	*  setting the enabled property.
	*
	*/


	protected void setup(IRequestCycle cycle)
	{
		if (staticEnabled)
			enabled = enabledValue;
		else
			if (enabledBinding == null)
			enabled = true;
		else
			enabled = enabledBinding.getBoolean();
	}

	/**
	 *  Record an attribute (typically, from a wrapped component such
	 *  as a {@link Rollover}).
	 *
	 */

	public void setAttribute(String attributeName, String attributeValue)
	{
		if (attributes == null)
			attributes = new HashMap(MAP_SIZE);

		attributes.put(attributeName, attributeValue);
	}	

	/**
	 *  Renders the link.  This is somewhat complicated, because a
	 *  nested {@link IResponseWriter response writer} is used
	 *  to render the contents of the link, before the link
	 *  itself actually renders.
	 *
	 *  <p>This is to support components such as {@link Rollover}, which
	 *  must specify some attributes of the service link 
	 *  as they render in order to
	 *  create some client-side JavaScript that works.  Thus the
	 *  service link renders its wrapped components into
	 *  a temporary buffer, then renders its own HTML.
	 *
	 */

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		IResponseWriter wrappedWriter;
		boolean compressed = false;
		String href;
		String[] context;
		boolean enabled;

		if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
			throw new RequestCycleException(
				"IServiceLink components may not be nested.",
			this, cycle);

		try
		{
			rendering = true;

			cycle.setAttribute(ATTRIBUTE_NAME, this);

			setup(cycle);

			enabled = isEnabled();

			if (enabled)
			{		
				context = getContext(cycle);

				writer.begin("a");

				href = buildURL(cycle, context);
				writer.attribute("href", href);

				// Allow the wrapped components a chance to render.
				// Along the way, they may interact with this component
				// and cause the name variable to get set.

				compressed = writer.compress(true);
				wrappedWriter = writer.getNestedWriter();
				wrappedWriter.setCompressed(true);
			}
			else
				wrappedWriter = writer;

			renderWrapped(wrappedWriter, cycle);

			if (enabled)
			{
				// Write any attributes specified by wrapped components.

				writeAttributes(writer);

				// Generate additional attributes from informal parameters.

				generateAttributes(cycle, writer, reservedNames);

				// Dump in HTML provided by wrapped components

				wrappedWriter.close();

				// Close the <a> tag

				writer.end();
				writer.setCompressed(compressed);
			}

			cycle.removeAttribute(ATTRIBUTE_NAME);
		}
		finally
		{
			rendering = false;
			attributes = null;
		}
	}

	private void writeAttributes(IResponseWriter writer)
	{
		Map.Entry entry;
		Iterator i;

		if (attributes == null)
			return;

		i = attributes.entrySet().iterator();

		while (i.hasNext())
		{
			entry = (Map.Entry)i.next();

			writer.attribute((String)entry.getKey(), (String)entry.getValue());
		}

	}
}
