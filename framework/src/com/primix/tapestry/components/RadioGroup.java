package com.primix.tapestry.components;

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
 *  A special type of form component that is used to contain {@link Radio}
 *  components.  Roughly the analog of a {@link Select}.
 *
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 *  <p>In most cases, a {@link SelectionAdaptor} will be used with
 *  a {@link Foreach} to drive the options used by the {@link Radio}
 *  components. 
 *
 * <p>TBD:  The similuarities between {@link Select} and <code>RadioGroup</code>
 *  are so great that they probably are they could probably be merged, or at least,
 *  factored with a common base class.
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
 *		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>no</td>
 *		<td>If true, then all contained {@link Radio} components will be
 *		    disabled as well.</td> </tr>
 *
 *	</table>
 *
 * <p>Informal parameters are not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class RadioGroup extends AbstractFormComponent
{
    private IBinding disabledBinding;

    private Set selections;
    private String name;
    private boolean disabled;
    private boolean rewinding;
    private boolean rendering;
    private int nextOptionId;

    /**
    *  A <code>RadioGroup</code> places itself into the {@link IRequestCycle} as
    *  an attribute, so that its wrapped {@link Radio} components can identify thier
    *  state.
    *
    */


    private static final String ATTRIBUTE_NAME =
        "com.primix.tapestry.component.RadioGroup";

    public static RadioGroup get(IRequestCycle cycle)
    {
        return (RadioGroup)cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public IBinding getDisabledBinding()
    {
        return disabledBinding;
    }

    public String getName()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "name");

        return name;
    }

    public String getNextOptionId()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "nextOptionId");

        return Integer.toString(nextOptionId++);
    }	

    /**
    *  Used by {@link Radio} components wrapped by this <code>RadioGroup</code> to see
    *  if the group as a whole is disabled.
    *
    */

    public boolean isDisabled()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "disabled");

        return disabled;
    }

    public boolean isRewinding()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return rewinding;
    }

    /**
    *  Used by {@link Radio} components when rewinding to see if their value was submitted.  They
    *  use this to determine if they, individually, were selected.
    *
    */

    public boolean isSelected(String value)
    {
        if (selections == null)
            return false;

        return selections.contains(value);
    }

    /**
    * Doesn't actual render an HTML element ... there is no direct equivalent for
    * an HTML element.  A <code>RadioGroup</code> component exists to organize the
    * {@link Radio} components it wraps (directly or indirectly).
    *
    * A {@link Radio} can finds its {@link RadioGroup} as a {@link IRequestCycle} attribute.
    *
    */

    public void render(IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException
    {
        Form form;

        form = getForm(cycle);

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException("RadioGroup components may not be nested.",
                this, cycle);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        rewinding = form.isRewinding();

        // Used whether rewinding or not.

        name = form.getNextElementId("RadioGroup");

        if (disabledBinding == null)
            disabled = false;
        else
            disabled = disabledBinding.getBoolean();

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        if (rewinding)
            selections = buildSelections(cycle, name);

        try
        {
            rendering = true;
            nextOptionId = 0;

            renderWrapped(writer, cycle);
        }
        finally
        {
            rendering = false;
            selections = null;
            name = null;
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    public void setDisabledBinding(IBinding value)
    {
        disabledBinding = value;
    }

    /**
    *  Ick!  Cut and paste with {@link Select}.
    *
    */

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



