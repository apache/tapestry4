//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.form;

import java.util.HashSet;
import java.util.Set;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderOnlyPropertyException;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

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
 *	  <td>Direction</td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *		<td>multiple</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>If true, the component allows multiple selection.</td> </tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the select is active or not. 
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Select extends AbstractFormComponent
{
    private boolean multiple;
    private boolean disabled;
    private boolean rewinding;
    private boolean rendering;

    private Set selections;
    private int nextOptionId;

    private String name;

    public String getName()
    {
        return name;
    }

    /**
     *  Used by the <code>Select</code> to record itself as a
     *  {@link IRequestCycle} attribute, so that the
     *  {@link Option} components it wraps can have access to it.
     *
     **/

    private final static String ATTRIBUTE_NAME = "net.sf.tapestry.active.Select";

    public static Select get(IRequestCycle cycle)
    {
        return (Select) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public boolean isDisabled()
    {
        return disabled;
    }

		public boolean isMultiple() 
		{
			return multiple;
		}

		public void setMultiple(boolean newValue) 
		{
			multiple = newValue;
		}

    public boolean isRewinding()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return rewinding;
    }

    public String getNextOptionId()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "nextOptionId");

        // Return it as a hex value.

        return Integer.toString(nextOptionId++);
    }

    public boolean isSelected(String value)
    {
        if (selections == null)
            return false;

        return selections.contains(value);
    }

    /**
     *  Renders the &lt;option&gt; element, or responds when the form containing the element
     *  is submitted (by checking {@link IForm#isRewinding()}.
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(
                Tapestry.getString("Select.may-not-nest"),
                this);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        rewinding = form.isRewinding();

        // Used whether rewinding or not.

        name = form.getElementId(this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        if (rewinding)
        {
            selections = buildSelections(cycle, name);
        }
        else
        {
            writer.begin("select");

            writer.attribute("name", name);

            if (multiple)
                writer.attribute("multiple");

            if (disabled)
                writer.attribute("disabled");

            generateAttributes(writer, cycle);
        }

        rendering = true;
        nextOptionId = 0;

        renderWrapped(writer, cycle);

        if (!rewinding)
        {
            writer.end();
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);

    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        rendering = false;
        selections = null;

        super.cleanupAfterRender(cycle);
    }

    /**
     *  Cut-and-paste with {@link RadioGroup}!
     *
     **/

    private Set buildSelections(IRequestCycle cycle, String parameterName)
    {
        RequestContext context;
        String[] parameters;
        int size = 7;
        int length;
        int i;
        Set result;

        context = cycle.getRequestContext();

        parameters = context.getParameters(parameterName);

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