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

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implements a hidden field within a {@link Form}.
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
 *		<td>value</td>
 *		<td>java.lang.String</td>
 *		<td>R / W</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The value to be stored in the the hidden field.  The parameter is read
 *  when the HTML response is generated, and then written when the form is submitted.
 *	</tr>
 *
 * <tr>
 *		<td>listener</td>
 *		<td>{@link IActionListener}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>A listener that is informed after the value parameter is updated.  This
 * allows the data set operated on by the rest of the {@link Form} components
 * to be synchronized.
 *
 *  <p>A typical use is to encode the primary key of an entity as a Hidden; when the
 *  form is submitted, the Hidden's listener re-reads the corresponding entity
 *  from the database.</td>
 *  </tr>
 * 
 *	</table>
 *
 * <p>Does not allow informal parameters, and may not contain a body.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Hidden extends AbstractFormComponent
{
    private IBinding valueBinding;
    private IActionListener listener;

    private String name;

    public String getName()
    {
        return name;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);
        boolean formRewound = form.isRewinding();

        name = form.getElementId(this);

        // If the form containing the Hidden isn't rewound, then render.

        if (!formRewound)
        {
            // Optimiziation: if the page is rewinding (some other action or
            // form was submitted), then don't bother rendering.

            if (cycle.isRewinding())
                return;

            String value = valueBinding.getString();

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", name);
            writer.attribute("value", value);

            return;
        }

        String value = cycle.getRequestContext().getParameter(name);

        // A listener is not always necessary ... it's easy to code
        // the synchronization as a side-effect of the accessor method.

        valueBinding.setString(value);

        if (listener != null)
            listener.actionTriggered(this, cycle);
    }

    public IActionListener getListener()
    {
        return listener;
    }

    public void setListener(IActionListener listener)
    {
        this.listener = listener;
    }

    public IBinding getValueBinding()
    {
        return valueBinding;
    }

    public void setValueBinding(IBinding valueBinding)
    {
        this.valueBinding = valueBinding;
    }

}