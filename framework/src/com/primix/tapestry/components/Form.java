package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

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
 * Component which contains form element components.  Forms use the
 * action service to handle the form submission.
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
 *    <td>method</td>
 *    <td>java.lang.String</td>
 *    <td>R</td>
 *   	<td>no</td>
 *		<td>post</td>
 *		<td>The value to use for the method attribute of the &lt;form&gt; tag.</td>
 *	</tr>
 *
 *
 *  <tr>
 *    <td>listener</td>
 *    <td>{@link IActionListener}</td>
 * 	  <td>R</td>
 * 	  <td>yes</td>
 *	  <td>&nbsp;</td>
 *	  <td>The listener, informed <em>after</em> the wrapped components of the form
 *	      have had a chance to absorb the request.</td>
 *	</tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Form extends AbstractFormComponent
{
	private IBinding methodBinding;
	private String methodValue;

		protected boolean rewinding;

	private static final String[] reservedNames = { "action" };

	/**
	*  Attribute name used with the request cycle.
	*
	*/

		private static final String ATTRIBUTE_NAME = "com.primix.tapestry.components.Form";

	public Form(IPage page, IComponent container, String id, 
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	/**
	*  Returns the currently active <code>Form</code>, or null if no <code>Form</code> is
	*  active.
	*
	*/

		public static Form get(IRequestCycle cycle)
	{
		return (Form)cycle.getAttribute(ATTRIBUTE_NAME);
	}

	public IBinding getMethodBinding()
	{
		return methodBinding;
	}

	/**
	*  Indicates to any wrapped form components that they should respond to the form
	*  submission.
	*
	*/

	public boolean isRewinding()
	{
		return rewinding;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		String method = "post";
		boolean rewound;
		boolean renderring;
		String URL;
		IApplicationService service;
		String actionId;
		IActionListener listener;

		if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
			throw new RequestCycleException("Forms may not be nested.", this, cycle);

		cycle.setAttribute(ATTRIBUTE_NAME, this);

		actionId = cycle.getNextActionId();

			renderring = !cycle.isRewinding();
		rewound = cycle.isRewound();

		rewinding = rewound;

		if (renderring)
		{
			if (methodValue != null)
				method = methodValue;
			else if (methodBinding != null)
				method = methodBinding.getString();

			writer.begin("form");
			writer.attribute("method", method);

			// Forms are processed using the 'action' service.

			service = cycle.getApplication().
			getService(IApplicationService.ACTION_SERVICE);

			URL = service.buildURL(cycle, this, new String[]
				{ actionId 
			});

			writer.attribute("action", cycle.encodeURL(URL));

			generateAttributes(cycle, writer, reservedNames);
		}

		renderWrapped(writer, cycle);

		if (renderring)
		{
			writer.end("form");
		}

		if (rewound)
		{
			listener = getListener(cycle);

			if (listener == null)
				throw new RequiredParameterException(this, "listener", cycle);

			listener.actionTriggered(this, cycle);

			// Abort the rewind render.

			throw new RenderRewoundException(this, cycle);
		}

		cycle.removeAttribute(ATTRIBUTE_NAME);
	}

	public void setMethodBinding(IBinding value)
	{
		methodBinding = value;

		if (value.isStatic())
			methodValue = value.getString();
	}
}

