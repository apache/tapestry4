package com.primix.tapestry;

import com.primix.tapestry.spec.ComponentSpecification;
import com.primix.tapestry.event.*;
import com.primix.tapestry.parse.*;
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
 * Base implementation for most components that use an HTML template.
 *
 * <p>A better name would be <code>Component</code>, but that causes conflicts
 * with <code>ComponentBeanInfo</code> which confuses the
 * {@link com.primix.foundation.prop.PropertyHelper}
 * class when it attempts to dynamically access properties of a component.
 *
 *
 * @author Howard Ship
 * @version $Id$
 */

public class BaseComponent extends AbstractComponent
{
	protected static final int OUTER_INIT_SIZE = 5;
	protected int outerCount = 0;
	protected IRender[] outer;

	public BaseComponent(IPage page, IComponent container, String id, 
		ComponentSpecification spec)
	{
		super(page, container, id, spec);
	}

	/**
	*  Adds an element as an outer element for the receiver.  Outer
	*  elements are elements that should be directly rendered by the
	*  receiver's <code>render()</code> method.  That is, they are
	*  top-level elements on the HTML template.
	*
	*/

	protected void addOuter(IRender element)
	{
		if (outer == null)
		{
			outer = new IRender[OUTER_INIT_SIZE];
			outer[0] = element;

			outerCount = 1;
			return;
		}

		// No more room?  Make the array bigger.

		if (outerCount == outer.length)
		{
			IRender[] newOuter;

			newOuter = new IRender[outer.length * 2];

			System.arraycopy(outer, 0, newOuter, 0, outerCount);

			outer = newOuter;
		}

		outer[outerCount++] = element;
	}

	/**
	*
	* Reads the receiver's template and figures out which elements wrap with 
	* other elements.
	*
	* <P>This is coded as a single, big, ugly method for efficiency.
	*/

	protected void readTemplate(IRequestCycle cycle)
	throws RequestCycleException
	{
		TemplateToken token;
		TokenType type;
		int i, count;
		IComponent[] componentStack;
		int stackx = 0;
		String name;
		IComponent component;
		IComponent activeComponent = null;
		ComponentTemplate componentTemplate;
		IRender element;
		ITemplateSource templateSource;
		boolean check = true;
	
		try
		{
	    	templateSource = page.getApplication().getTemplateSource();

			componentTemplate = templateSource.getTemplate(this);
		}
		catch (ResourceUnavailableException e)
		{
	    	throw new RequestCycleException(e.getMessage(), this, cycle, e);
		}

		count = componentTemplate.getTokenCount();

		// The stack can never be as large as the number of tokens, so this is safe.

		componentStack = new IComponent[count];
		activeComponent = null;

		for (i = 0; i < count; i++)
		{
			token = componentTemplate.getToken(i);
			type = token.getType();

			if (type == TokenType.TEXT)
			{
				// Get a render for the token.  This allows the token and the render
				// to be shared across sessions.

				element = token.getRender();

				if (activeComponent == null)
					addOuter(element);
				else
				{
					if (check)
					{
						check = false;

						if (!activeComponent.getSpecification().getAllowBody())
							throw new BodylessComponentException(activeComponent, cycle);
					}

					activeComponent.addWrapped(element);
				}

				continue;
			}

			// On an OPEN, we get the name

			if (type == TokenType.OPEN)
			{
				name = token.getId();

				// Could use a sanity check here that we only see a name once.

				try
				{
					component = getComponent(name);

					check = true;
				}
				catch (NoSuchComponentException e)
				{
					throw new RequestCycleException(
						"Error in template: Component " +
						getExtendedId() +
						" does not contain a component '" +
						name + "'.", this, cycle, e);
				}

				if (activeComponent == null)
					addOuter(component);
				else
				{
					if (check)
					{
						check = false;

						if (!activeComponent.getSpecification().getAllowBody())
							throw new BodylessComponentException(activeComponent, cycle);
					}

					activeComponent.addWrapped(component);	
				}	

				componentStack[stackx++] = activeComponent;

				activeComponent = component;

				continue;
			}

			if (type == TokenType.CLOSE)
			{
				try
				{
					activeComponent = componentStack[--stackx];

					check = true;
				}
				catch (IndexOutOfBoundsException e)
				{
					// Actually, the current template parser is easy enough to confuse
					// that this could happend because the <jwc> tag is poorly formatted.

					throw new RequestCycleException(
						"More </jwc> tags than <jwc> tags in template.", this, cycle);
				}
			}
		}

		if (stackx != 0)
			throw new RequestCycleException(
				"Not all <jwc> tags closed in template.",
				this, cycle);

	}

	/**
	*  Renders the top level components contained by the receiver.
	*
	*  <p>Checks to see if the receivers's template has been read yet.  If not, it is
	*  read at this time, using {@link #readTemplate(IRequestCycle)}.
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		int i;

		if (outer == null)
			readTemplate(cycle);

		for (i = 0; i < outerCount; i++)
			outer[i].render(writer, cycle);

	}
}

