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

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Implements a component that manages an HTML &lt;input type=radio&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link RadioGroup} component.
 *
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 * <table border=1>
 * <tr> 
 *    <td>Property</td>
 *    <td>Type</td>
 *	  <td>Direction</td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *
 *  <tr>
 *      <td>value</td>
 *      <td>{@link Object}</td>
 *      <td>in</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The value is used to determine if the radio button is initially selected
 *  (when rendering) and is the value assigned to the selected parameter when the
 *  form is submitted, if the HTML radio button is selected.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>If true, then the <code>Radio</code> is disabled.  It will
 * write a disabled attribute into its tag when rendering, and will not update
 * its selected binding.
 * 		<p>A binding may also be disabled if its containing {@link RadioGroup} is
 *		   disabled. </td> </tr>
 *	</table>
 *
 *  <p>Informal parameters are allowed, but may not contain a body.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Radio extends AbstractComponent
{
 	private Object _value;
 	private boolean _disabled;
 	
    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *
     * <p>If the <code>label</code> property is set, it is inserted after the
     * &lt;input&gt; tag.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {

        RadioGroup group = RadioGroup.get(cycle);
        if (group == null)
            throw new RequestCycleException(Tapestry.getString("Radio.must-be-contained-by-group"), this);

        // The group determines rewinding from the form.

        boolean rewinding = group.isRewinding();

        int option = group.getNextOptionId();

        if (rewinding)
        {
            // If not disabled and this is the selected button within the radio group,
            // then update set the selection from the group to the value for this
            // radio button.  This will update the selected parameter of the RadioGroup.

            if (!_disabled && group.isSelected(option))
                group.updateSelection(_value);
            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", "radio");

        writer.attribute("name", group.getName());

        // As the group if the value for this Radio matches the selection
        // for the group as a whole; if so this is the default radio and is checked.

        if (group.isSelection(_value))
            writer.attribute("checked");

        if (_disabled)
            writer.attribute("disabled");

        // The value for the Radio matches the option number (provided by the RadioGroup).
        // When the form is submitted, the RadioGroup will know which option was,
        // in fact, selected by the user.

        writer.attribute("value", option);

        generateAttributes(writer, cycle);

    }

    public boolean getDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public Object getValue()
    {
        return _value;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

}