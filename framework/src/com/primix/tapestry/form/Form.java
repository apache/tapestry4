package com.primix.tapestry.components.html.form;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

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
 * Component which contains form element components.  Forms use the
 * action service to handle the form submission.  A Form will wrap
 * other components and static HTML, including
 * form component such as {@link Text}, {@link TextField}, {@link Checkbox}, etc.
 *
 * <p>When a form is submitted, it continues through the rewind cycle until
 * <em>after</em> all of its wrapped elements have renderred.  As the form
 * component render (in the rewind cycle), they will be updating
 * properties of the containing page and notifying thier listeners.  Again:
 * each form component is responsible not only for rendering HTML (to present the
 * form), but for handling it's share of the form submission.
 *
 * <p>Only after all that is done will the Form notify its listener.
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


public class Form extends AbstractComponent
{
    private IBinding methodBinding;
    private String methodValue;

    private IBinding listenerBinding;

    private boolean rewinding;
    private int nextElementId = 0;
    private StringBuffer buffer;
    private boolean rendering;
    private String name;

    private static final String[] reservedNames = { "action" };

    /**
    *  Attribute name used with the request cycle.
    *
    */

    private static final String ATTRIBUTE_NAME = "com.primix.tapestry.active.Form";

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

    public void setListenerBinding(IBinding value)
    {
        listenerBinding = value;
    }

    public IBinding getListenerBinding()
    {
        return listenerBinding;
    }

    /**
    *  Indicates to any wrapped form components that they should respond to the form
    *  submission.
    *
    *  @throws RenderOnlyPropertyException if not rendering.
    */

    public boolean isRewinding()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return rewinding;
    }

    /**
     *  Constructs a unique identifier (within the Form) from a prefix and a
     *  unique index.  The prefix typically corresponds to the component Class (i.e.
     *  "Text" or "Checkbox").
     *
     */

    public String getNextElementId(String prefix)
    {
        if (buffer == null)
            buffer = new StringBuffer();
        else
        {
            buffer.setLength(0);
        }

        buffer.append(prefix);
        buffer.append(nextElementId++);

        return buffer.toString();
    }

    /**
    *  Returns the name generated for the form.  This is used to faciliate
    *  components that write JavaScript and need to access the form or
    *  its contents.
    *
    *  <p>This value is generated when the form renders, and is not cleared.
    *  If the Form is inside a {@link Foreach}, this will be the most recently
    *  generated name for the Form.
    *
    *  <p>This property is exposed so that sophisticated applications can write
    *  JavaScript handlers for the form and components within the form.
    *
    *  @see AbstractFormComponent#getName()
    *
    */

    public String getName()
    {
        return name;
    }

    public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        String method = "post";
        boolean rewound;
        String URL;
        IEngineService service;
        String actionId;
        IActionListener listener;
        boolean renderForm;

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException("Forms may not be nested.", this, cycle);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        actionId = cycle.getNextActionId();
        name = "Form" + actionId;

        try
        {
            renderForm = !cycle.isRewinding();
            rewound = cycle.isRewound(this);

            rewinding = rewound;

            if (renderForm)
            {
                if (methodValue != null)
                    method = methodValue;
                else if (methodBinding != null)
                    method = methodBinding.getString();

                writer.begin("form");
                writer.attribute("method", method);
                writer.attribute("name", name);

                // Forms are processed using the 'action' service.

                service = cycle.getEngine().
                getService(IEngineService.ACTION_SERVICE);

                URL = service.buildURL(cycle, this, 
                    new String[]
                    { actionId 
                });

                writer.attribute("action", cycle.encodeURL(URL));

                generateAttributes(cycle, writer, reservedNames);
            }

            nextElementId = 0;

            rendering = true;
            renderWrapped(writer, cycle);

            if (renderForm)
            {
                // What's this for?  It's part of checking for stale links.  We record
                // the next action id into the form.  This ensures that the number
                // of action ids within the form (when the form HTML is rendered)
                // matches the expected number (when the form submission is processed).

                writer.beginOrphan("input");
                writer.attribute("type", "hidden");
                writer.attribute("name", name);
                writer.attribute("value", nextElementId++);

                writer.end("form");
            }

            if (rewound)
            {
                String actual;

                actual = cycle.getRequestContext().getParameter(name);

                if (actual == null ||
                    Integer.parseInt(actual) != nextElementId++)
                    throw new StaleLinkException(
                        "Incorrect number of elements with Form " + getExtendedId() + ".",
                        getPage(), cycle);

                try
                {
                    listener = (IActionListener)listenerBinding.getValue();
                }
                catch (ClassCastException e)
                {
                    throw new RequestCycleException("Parameter listener is not type IActionListener.",
                        this, cycle, e);
                }

                if (listener == null)
                    throw new RequiredParameterException(this, "listener", listenerBinding,
                        cycle);

                listener.actionTriggered(this, cycle);

                // Abort the rewind render.

                throw new RenderRewoundException(this, cycle);
            }


            cycle.removeAttribute(ATTRIBUTE_NAME);
        }
        finally
        {
            rendering = false;
        }
    }

    public void setMethodBinding(IBinding value)
    {
        methodBinding = value;

        if (value.isStatic())
            methodValue = value.getString();
    }
}


