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


package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import java.util.*;
import com.primix.tapestry.parse.*;

/**
 *  Component of the {@link Inspector} page used to display
 *  the ids and types of all embedded components.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowTemplate
extends BaseComponent
implements IDirect
{

	public boolean getHasTemplate()
	{
		Inspector inspector;
		
		inspector = (Inspector)page;
		
		// Components that inherit from BaseComponent have templates,
		// others do not.
		
		return inspector.getInspectedComponent() instanceof BaseComponent;
	}
	
	public IRender getTemplateDelegate()
	{
		return new IRender()
		{
			public void render(IResponseWriter writer, IRequestCycle cycle)
			throws RequestCycleException
			{
				writeTemplate(writer, cycle);
			}
		};
	}
	
	/**
	 *  Writes the HTML template for the component.  When &lt;jwc&gt; tags are
	 *  written, the id is made a link (that selects the named component).  We
	 *  use some magic to accomplish this, creating links as if we were a
	 *  {@link Direct} component, and attributing those links
	 *  to the captive {@link Direct} component embedded here.
	 *
	 */
	  
	private void writeTemplate(IResponseWriter writer, IRequestCycle cycle)
	{
		String templateName;
		ITemplateSource source;
		ComponentTemplate template;
		int count;
		char[] data;
		TemplateToken token;
		int i;
		String[] context = null;
		IEngineService service = null;
		String URL;
		String id;
		IComponent inspectedComponent;
		IComponent embedded;
		
		inspectedComponent = ((Inspector)page).getInspectedComponent();

		source = page.getEngine().getTemplateSource();

		try
		{
			template = source.getTemplate(inspectedComponent);
		}
		catch (ResourceUnavailableException e)
		{
			return;
		}

		writer.begin("pre");

		count = template.getTokenCount();
		data = template.getTemplateData();

		for (i = 0; i < count; i++)
		{
			token = template.getToken(i);

			if (token.getType() == TokenType.TEXT)
			{
				int start;
				int end;

				start = token.getStartIndex();
				end = token.getEndIndex();

				// Print the section of the template ... print() will
				// escape and invalid characters as HTML entities.  Also,
				// we show the full stretch of text, not the trimmed version.
				
				writer.print(data, start, end - start + 1);

				continue;
			}

			if (token.getType() == TokenType.CLOSE)
			{
				writer.begin("span");
				writer.attribute("class", "jwc-tag");
				
				writer.print("</jwc>");
				
				writer.end(); // <span>
				
				continue;
			}

			// Only other type is OPEN
			
			if (service == null)
			{
				service = cycle.getEngine().getService(IEngineService.DIRECT_SERVICE);
				context = new String[1];
			}
			
			// Each id references a component embedded in the inspected component.
			// Get that component.

			id = token.getId();
			embedded = inspectedComponent.getComponent(id);
			context[0] = embedded.getIdPath();
			
			// Build a URL to select that component, as if by the captive
			// component itself (it's a Direct).
			
			URL = service.buildURL(cycle, this, context);

			writer.begin("span");
			writer.attribute("class", "jwc-tag");
			
			writer.print("<jwc id=\"");

			writer.begin("span");
			writer.attribute("class", "jwc-id");
			
			writer.begin("a");
			writer.attribute("href", URL);
			writer.print(id);
			
			writer.end();  // <a>
			writer.end();  // <span>
			writer.print('"');

			// Collapse an open & close down to a single tag.

			if (i + 1 < count &&
				template.getToken(i + 1).getType() == TokenType.CLOSE)
			{
				writer.print('/');
				i++;
			}

			writer.print('>');
			writer.end();  // <span>
		}

		writer.end(); // <pre>
	}

    /**
     *  Invoked when a component id is clicked.
     *
     */

    public void trigger(IRequestCycle cycle, String[] context)
    {
        Inspector inspector = (Inspector)page;

        inspector.setInspectedIdPath(context[0]);
    }

}
