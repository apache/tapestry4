package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
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
 *  Implements a component that manages an HTML &lt;select&gt; form element.
 *  The most common situation, using a &lt;select&gt; to set a specific
 *  property of some object, is best handled using a {@link PropertySelection} component.
 *
 *  <p>Otherwise, this component is very similar to {@link RadioGroup}.
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *		<td>multiple</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>If true, the component allows multiple selection.</td> </tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the select is active or not. 
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *  <tr>
 *    <td>listener</td>
 *    <td>{@link IActionListener}</td>
 * 	  <td>R</td>
 * 	  <td>no</td>
 *	  <td>&nbsp;</td>
 *	  <td>The listener, informed <em>after</em> the {@link Option} components
 *	      have had a chance to absorb the request.</td>
 *	</tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed.
 *
 * <p>When using single selection, it is useful to create a subclass of
 * {@link SelectionAdaptor} to mediate the results for your application.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Select extends AbstractFormComponent
{
	private IBinding multipleBinding;
	private IBinding disabledBinding;

	private boolean disabled;
	private boolean rewinding;

	private Set selections;

	/**
	*  Used by the <code>Select</code> to record itself as a
	*  {@link IRequestCycle} attribute, so that the
	*  {@link Option} components it wraps can have access to it.
	*
	*/

	private final static String ATTRIBUTE_NAME
		= "com.primix.tapestry.components.Select";

	public static Select get(IRequestCycle cycle)
	{
		return (Select)cycle.getAttribute(ATTRIBUTE_NAME);
	}

	private static final String[] reservedNames = { "name"};



	public Select(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}

	public IBinding getMultipleBinding()
	{
		return multipleBinding;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public boolean isRewinding()
	{
		return rewinding;
	}

	public boolean isSelected(String value)
	{
		if (selections == null)
			return false;

		return selections.contains(value);
	}

	/**
	*  Renders the &lt;option&gt; element, or responds when the form containing the element
	*  is submitted (by checking {@link Form#isRewinding()}.
	*
	*  <table border=1>
	*  <tr>  <th>attribute</th>  <th>value</th> </tr>
	*  <tr>  <td>name</td>  <td>from {@link IRequestCycle#getNextActionId()}</td> </tr>
	*  <tr> <td>multiple</td> <td>ommitted, unless the <code>multiple</code>
	*			 property is true </tr> </tr>
	*  <tr>  <td>disabled</td>  <td>ommited, unless the <code>disabled</code> property is
	* 	true.  </td> </tr>
	*  </table>
	**/

	public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		String name;
		IActionListener listener;
		Form form;
		boolean compressed;
		boolean multiple;

		form = getForm(cycle);

		if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
			throw new RequestCycleException(
				"Select components may not be nested.",
				this, cycle);

		// It isn't enough to know whether the cycle in general is rewinding, need to know
		// specifically if the form which contains this component is rewinding.

		rewinding = form.isRewinding();

		// Used whether rewinding or not.

		name = cycle.getNextActionId();

		if (disabledBinding == null)
			disabled = false;
		else
			disabled = disabledBinding.getBoolean();

		cycle.setAttribute(ATTRIBUTE_NAME, this);

		if (rewinding)
		{
			selections = buildSelections(cycle, name);
		}
		else
		{
			if (multipleBinding == null)
				multiple = false;
			else
				multiple = multipleBinding.getBoolean();

			writer.begin("select");

			writer.attribute("name", name);

			if (multiple)
				writer.attribute("multiple");

			if (disabled)
				writer.attribute("disabled");

			generateAttributes(cycle, writer, reservedNames);			
		}

		try
		{
			renderWrapped(writer, cycle);
		}
		finally
		{
			selections = null;
		}

		if (!rewinding)
		{
			writer.end();
		}
		else
		{
			listener = getListener(cycle);
			if (listener != null)
				listener.actionTriggered(this, cycle);
		}

		cycle.removeAttribute(ATTRIBUTE_NAME);

	}

	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}

	public void setMultipleBinding(IBinding value)
	{
		multipleBinding = value;
	}
    
    private Set buildSelections(IRequestCycle cycle, String parameterName)
    {
    	RequestContext context;
        String[] parameters;
        int size = 7;
        int length;
        int i;
        Set result;
        
        context = cycle.getRequestContext();
        
        parameters = context.getParameterValues(parameterName);
        
        if (parameters == null)
        	return null;
        
        length = parameters.length;
        if (parameters.length == 0)
        	return null;
            
        if (parameters.length > 30)
        		size = 101;
                
        result = new HashSet(size);
        
        for (i = 0; i < length; i++)
        	result.add(parameters[i]);
        
        return result;
    }    
}

