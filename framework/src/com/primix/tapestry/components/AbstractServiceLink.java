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
 *  Abstract super-class for components that generate some form of
 *  &lt;a&gt; hyperlink using an {@link IApplicationService}.
 *  Supplies support for the following parameters:
 *
 *  <ul>
 *  <li>enabled</li>
 *  <li>anchor</li>
 *  <li>listener</li>
 * </ul>
 *
 * @author Howard Ship
 * @version $Id$
 */


public abstract class AbstractServiceLink
    extends AbstractComponent
    implements IServiceLink
{
	private static final String[] reservedNames = { "href" };

	private IBinding enabledBinding;
	private boolean staticEnabled;
	private boolean enabledValue;

	private boolean enabled;

	private IBinding anchorBinding;
	private String anchorValue;

	private static final int MAP_SIZE = 11;
	private Map attributes;
	
	private boolean rendering;

	public AbstractServiceLink(IPage page, IComponent container, String name,
		ComponentSpecification specification)
	{
		super(page, container, name, specification);
	}

	public void addAttribute(String name, String value)
	{
		if (attributes == null)
			attributes = new HashMap(MAP_SIZE);

		attributes.put(name, value);
	}

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
		IApplicationService service;
		String serviceName;
		String url;
		String anchor = null;

		serviceName = getServiceName(cycle);
		service = cycle.getApplication().getService(serviceName);

		if (service == null)
			throw new RequestCycleException("No application service named " + 
				serviceName + ".",
				this, cycle);

		url = service.buildURL(cycle, this, context);

		url = cycle.encodeURL(url);

		if (anchorValue != null)
			anchor = anchorValue;
		else
			if (anchorBinding != null)
			anchor = anchorBinding.getString();

		if (anchor != null)
			url += "#" + anchor;

		return url;
	}

	public IBinding getAnchorBinding()
	{
		return anchorBinding;
	}

	public IBinding getEnabledBinding()
	{
		return enabledBinding;
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
	*  setting the enabled property and clearing attributes.
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

			attributes = null;
	}

	private void writeAttributes(IResponseWriter writer)
	{
		Iterator i;
		String name;
		String value;
        Map.Entry entry;

		if (attributes != null)
		{
			i = attributes.entrySet().iterator();

			while (i.hasNext())
			{
            	entry = (Map.Entry)i.next();
                
				name = (String)entry.getKey();
                value = (String)entry.getValue();
                
				writer.attribute(name, value);
			}
		}
	}

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

				compressed = writer.compress(true);
				wrappedWriter = writer.getNestedWriter();
				wrappedWriter.setCompressed(true);
			}
			else
				wrappedWriter = writer;

			renderWrapped(wrappedWriter, cycle);

			if (enabled)
			{
				// Write any attributes specified by wrapped components (i.e., Rollover).

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
		}
	}
}
