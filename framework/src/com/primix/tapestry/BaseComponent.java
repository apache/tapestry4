package com.primix.tapestry;

import com.primix.tapestry.spec.ComponentSpecification;
import com.primix.tapestry.event.*;
import com.primix.tapestry.parse.*;
import java.util.*;
import org.log4j.*;

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
 * Base implementation for most components that use an HTML template.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class BaseComponent
extends AbstractComponent
{
	protected static final int OUTER_INIT_SIZE = 5;
	protected int outerCount = 0;
	protected IRender[] outer;

	private static final Category CAT  = Category.getInstance(BaseComponent.class.getName());

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
		String id;
		IComponent component;
		IComponent activeComponent = null;
		ComponentTemplate componentTemplate;
		IRender element;
		ITemplateSource templateSource;
		boolean check = true;
		Set seenIds = new HashSet();
	
		if (CAT.isDebugEnabled())
			CAT.debug(this + " reading template");
			
		try
		{
	    	templateSource = getPage().getEngine().getTemplateSource();

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
				id = token.getId();

				try
				{
					component = getComponent(id);

					check = true;
				}
				catch (NoSuchComponentException e)
				{
					throw new RequestCycleException(
						"Template for component " +
						getExtendedId() +
						" references undefined embedded component " +
						id + ".", this, cycle, e);
				}

				// Make sure the template contains each component only once.
				
				if (seenIds.contains(id))
					throw new RequestCycleException(
						"Template for component " + getExtendedId() +
						" contains multiple references to embedded component " +
						id + ".");
						
				seenIds.add(id);

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
					// that this could happen because the <jwc> tag is poorly formatted.

					throw new RequestCycleException(
						"More </jwc> tags than <jwc> tags in template.", this, cycle);
				}
			}
		}

		if (stackx != 0)
			throw new RequestCycleException(
				"Not all <jwc> tags closed in template.",
				this, cycle);

		checkAllComponentsReferenced(seenIds, cycle);
		
		if (CAT.isDebugEnabled())
			CAT.debug(this + " finished reading template");
	}

	private void checkAllComponentsReferenced(Set seenIds, IRequestCycle cycle)
	throws RequestCycleException
	{
		Set ids;
		StringBuffer buffer;
		int count;
		int j = 0;
		Iterator i;
		Map components;
		
		// First, contruct a modifiable copy of the ids of all expected components
		// (that is, components declared in the specification).
		
		components = getComponents();
		
		// Occasionally, a component will have a template but no embedded components.
		
		if (components == null)
			ids = Collections.EMPTY_SET;
		else	
			ids = components.keySet();
		
		// If the seen ids ... ids referenced in the template, matches
		// all the ids in the specification then we're fine.
		
		if (seenIds.containsAll(ids))
			return;
		
		// Create a modifiable copy.  Remove the ids that are referenced in
		// the template.  The remainder are worthy of note.
		
		ids = new HashSet(ids);	
		ids.removeAll(seenIds);
		
		count = ids.size();
		
		buffer = new StringBuffer("Template for component ");
		buffer.append(getExtendedId());
		buffer.append(" does not reference embedded component");
		if (count > 0)
			buffer.append('s');
		
		i = ids.iterator();
		while (i.hasNext())
		{
			j++;
			
			if (j == 1)
				buffer.append(": ");
			else
				if (j == count)
					buffer.append(" and ");
				else
					buffer.append(", ");
					
			buffer.append(i.next());
		}	
		
		buffer.append('.');
		
		throw new RequestCycleException(buffer.toString(), this, cycle);
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
		if (CAT.isDebugEnabled())
			CAT.debug("Begin render " + getExtendedId());
			
		if (outer == null)
			readTemplate(cycle);

		for (int i = 0; i < outerCount; i++)
			outer[i].render(writer, cycle);

		if (CAT.isDebugEnabled())
			CAT.debug("End render " + getExtendedId());
	}
}

