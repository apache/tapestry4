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

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implements a component that manages an HTML &lt;input type=checkbox&gt;
 *  form element.
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
 *    <td>selected</td>
 *    <td>boolean</td>
 *    <td>in-out</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>Indicates whether the checkbox is selected or not.
 *
 *			<p>Corresponds to the <code>checked</code> HTML attribute.</td>
 *	</tr>
 *
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the text field is active or not.  If disabled, then
 *			any value that comes up when the form is submitted is ignored.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *
 *	</table>
 *
 * <p>Informal parameters are allowed.  A body is not allowed.
 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Checkbox extends AbstractFormComponent
{
    private boolean disabled;    
    private IBinding selectedBinding;

    private String name;

    public String getName()
    {
        return name;
    }

    public IBinding getSelectedBinding()
    {
        return selectedBinding;
    }

    /**
     *  Renders the form elements, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *  <p>In traditional HTML, many checkboxes would have the same name but different values.
     *  Under Tapestry, it makes more sense to have different names and a fixed value.
     *  For a checkbox, we only care about whether the name appears as a request parameter.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        String value;
        boolean disabled = false;
        boolean checked;

        IForm form = getForm(cycle);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = form.isRewinding();

        // Used whether rewinding or not.

        name = form.getElementId(this);

        if (rewinding)
        {
            if (!disabled)
            {
                value = cycle.getRequestContext().getParameter(name);

                checked = (value != null);

                selectedBinding.setBoolean(checked);
            }

        }
        else
        {
            checked = selectedBinding.getBoolean();

            writer.beginEmpty("input");
            writer.attribute("type", "checkbox");

            writer.attribute("name", name);

            if (disabled)
                writer.attribute("disabled");

            if (checked)
                writer.attribute("checked");

            generateAttributes(writer, cycle);

            writer.closeTag();
        }

    }

    public void setSelectedBinding(IBinding value)
    {
        selectedBinding = value;
    }
    
    public boolean getDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

}