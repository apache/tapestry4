/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
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
 *  <li>disabled</li>
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

	// A number of characters to add to the URL to get the initial size
	// of the StringBuffer used to assemble the complete URL.

	private static final int URL_PAD = 50;

	private IBinding disabledBinding;
	private boolean staticDisabled;
	private boolean disabledValue;

	private boolean disabled;

	private IBinding anchorBinding;
	private String anchorValue;

	private IBinding schemeBinding;
	private String schemeValue;

	private IBinding portBinding;
	private int portValue;

	private boolean rendering;

	private Body body;

	private static final int MAP_SIZE = 3;

	private Map eventHandlers;

	/**
	*  Invoked by {@link #render(IResponseWriter, IRequestCycle)} if the
	*  component is enabled.  The default implementation returns null; other
	*  implementations can provide appropriate parameters as needed.
	*  
	*/

	protected String[] getContext(IRequestCycle cycle) throws RequestCycleException
	{
		return null;
	}

	protected String buildURL(IRequestCycle cycle, String[] context)
		throws RequestCycleException
	{
		String anchor = null;
		StringBuffer buffer = null;
		String scheme = null;
		int port = 0;

		String serviceName = getServiceName(cycle);
		IEngineService service = cycle.getEngine().getService(serviceName);

		if (service == null)
			throw new RequestCycleException(
				"No engine service named " + serviceName + ".",
				this);

		// Perform the major work of building the URL.

		Gesture g = service.buildGesture(cycle, this, context);

		String url = g.getFullURL(cycle);

		// Now, dress up the URL with scheme, server port and anchor,
		// as necessary.

		if (anchorValue != null)
			anchor = anchorValue;
		else if (anchorBinding != null)
			anchor = anchorBinding.getString();

		if (schemeValue != null)
			scheme = schemeValue;
		else if (schemeBinding != null)
			scheme = schemeBinding.getString();

		if (portValue != 0)
			port = portValue;
		else if (portBinding != null)
			port = portBinding.getInt();

		// If nothing to add to the URL, then simply return it.

		if (anchor == null && scheme == null && port == 0)
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

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
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
	 *  Returns true if the link is disabled, false otherwise.  If not otherwise
	 *  specified, the link will be enabled (and this method will return false).
	 *
	 *  @throws RenderOnlyPropertyException if the component is not currently rendering.
	 *
	 */

	public boolean isDisabled()
	{
		if (!rendering)
			throw new RenderOnlyPropertyException(this, "disabled");

		return disabled;
	}

	public void setAnchorBinding(IBinding value)
	{
		anchorBinding = value;

		if (value.isStatic())
			anchorValue = value.getString();
	}

	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;

		staticDisabled = value.isStatic();

		if (staticDisabled)
			disabledValue = value.getBoolean();
	}

	/**
	*  Invoked from {@link #render(IResponseWriter, IRequestCycle)}, 
	*  this is responsible for
	*  setting the enabled property.
	*
	*/

	protected void setup(IRequestCycle cycle)
	{
		if (staticDisabled)
			disabled = disabledValue;
		else if (disabledBinding == null)
			disabled = false;
		else
			disabled = disabledBinding.getBoolean();
	}

	/**
	 *  Adds an event handler (typically, from a wrapped component such
	 *  as a {@link Rollover}).
	 *
	 */

	public void addEventHandler(
		ServiceLinkEventType eventType,
		String functionName)
	{
		Object currentValue;

		if (eventHandlers == null)
			eventHandlers = new HashMap(MAP_SIZE);

		currentValue = eventHandlers.get(eventType);

		// The first value is added as a String

		if (currentValue == null)
		{
			eventHandlers.put(eventType, functionName);
			return;
		}

		// When adding the second value, convert to a List

		if (currentValue instanceof String)
		{
			List list = new ArrayList();
			list.add(currentValue);
			list.add(functionName);

			eventHandlers.put(eventType, list);
			return;
		}

		// For the third and up, add the new function to the List

		List list = (List) currentValue;
		list.add(functionName);
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

		if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
			throw new RequestCycleException(
				"IServiceLink components may not be nested.",
				this);

		try
		{
			rendering = true;

			body = Body.get(cycle);

			cycle.setAttribute(ATTRIBUTE_NAME, this);

			setup(cycle);

			boolean disabled = isDisabled();

			if (!disabled)
			{
				String[] context = getContext(cycle);
				String href = buildURL(cycle, context);

				writer.begin("a");
				writer.attribute("href", href);

				// Allow the wrapped components a chance to render.
				// Along the way, they may interact with this component
				// and cause the name variable to get set.

				wrappedWriter = writer.getNestedWriter();
			}
			else
				wrappedWriter = writer;

			renderWrapped(wrappedWriter, cycle);

			if (!disabled)
			{
				// Write any attributes specified by wrapped components.

				writeEventHandlers(writer);

				// Generate additional attributes from informal parameters.

				generateAttributes(writer, cycle);

				// Dump in HTML provided by wrapped components

				wrappedWriter.close();

				// Close the <a> tag

				writer.end();
			}

			cycle.removeAttribute(ATTRIBUTE_NAME);
		}
		finally
		{
			rendering = false;
			eventHandlers = null;
			// May be possible to keep body from cycle to cycle, but
			// let's just be safe.
			body = null;
		}
	}

	private void writeEventHandlers(IResponseWriter writer)
		throws RequestCycleException
	{
		String name = null;

		if (eventHandlers == null)
			return;

		Iterator i = eventHandlers.entrySet().iterator();

		while (i.hasNext())
		{
			Map.Entry entry = (Map.Entry) i.next();
			ServiceLinkEventType type = (ServiceLinkEventType) entry.getKey();

			name =
				writeEventHandler(writer, name, type.getAttributeName(), entry.getValue());
		}

	}

	private String writeEventHandler(
		IResponseWriter writer,
		String name,
		String attributeName,
		Object value)
		throws RequestCycleException
	{
		String wrapperFunctionName;

		if (value instanceof String)
		{
			wrapperFunctionName = (String) value;
		}
		else
		{
			if (body == null)
				throw new RequestCycleException(
					"A link component with multiple functions for a single event type must be wrapped by a Body.",
					this,
					null);

			if (name == null)
				name = "Link" + body.getUniqueId();

			wrapperFunctionName = attributeName + "_" + name;

			StringBuffer buffer = new StringBuffer();

			buffer.append("function ");
			buffer.append(wrapperFunctionName);
			buffer.append(" ()\n{\n");

			Iterator i = ((List) value).iterator();
			while (i.hasNext())
			{
				String functionName = (String) i.next();
				buffer.append("  ");
				buffer.append(functionName);
				buffer.append("();\n");
			}

			buffer.append("}\n\n");

			body.addOtherScript(buffer.toString());
		}

		writer.attribute(attributeName, "javascript:" + wrapperFunctionName + "();");

		return name;

	}
}