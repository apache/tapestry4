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
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.Tapestry;
import org.apache.log4j.Category;

/**
 *  Implements a component that manages an HTML &lt;textarea&gt; form element.
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
 *    <td>value</td>
 *    <td>java.lang.String</td>
 *    <td>in-out</td>
 *   	<td>no</td>
 *		<td>post</td>
 *		<td>The text inside the textarea.  The parameter is only updated
 *			when the the Text component is not disabled.</td>
 *	</tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the textarea is active or not.  If disabled, then
 *			any value that comes up when the form is submitted is ignored.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *	<tr>
 *		<td>columns</td>
 *		<td>integer</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The width of the textarea, in characters.  If zero, or unspecified
 *		    the value is left to the client browser to determine.
 *
 *			<p>Corresponds to the <code>cols</code> HTML attribute.</td> </tr>
 *
 *	<tr>
 *		<td>rows</td>
 *		<td>integer</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The number of rows in the textarea. If
 *			unspecified or zero, then the value is left to the client browser to
 *			determine.
 *
 *			<p>Corresponds to the <code>maxlength</code> HTML attribute.</td> </tr>
 *
 * <tr>
 *	</table>
 *
 * <p>Informal parameters are allowed.  The component may not contain a body.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Text extends AbstractFormComponent
{
	private int rows;
	private int columns;
	private boolean disabled;	
    private IBinding valueBinding;
    private String name;

    public String getName()
    {
        return name;
    }


    public IBinding getValueBinding()
    {
        return valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        valueBinding = value;
    }

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *  <table border=1>
     *  <tr>  <th>attribute</th>  <th>value</th> </tr>
     *  <tr>  <td>name</td>  <td>from {@link IRequestCycle#getNextActionId()}</td> </tr>
     *  <tr>  <td>disabled</td>  <td>ommited, unless the <code>disabled</code> property is
     * 	true.  </td> </tr>
     *  <tr> <td>rows</td> <td>from <code>rows</code> property</td> </tr>
     *  <tr> <td>cols</td> <td>from <code>columns</code> property</td> </tr>
     *  </table>
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
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
                String value = cycle.getRequestContext().getParameter(name);

                valueBinding.setString(value);
            }

            return;
        }

        writer.begin("textarea");

        writer.attribute("name", name);

        if (disabled)
            writer.attribute("disabled");

        if (rows != 0)
            writer.attribute("rows", rows);

        if (columns != 0)
            writer.attribute("cols", columns);

        generateAttributes(writer, cycle);

        String value = valueBinding.getString();
        if (value != null)
            writer.print(value);

        writer.end();

    }

    public int getColumns()
    {
        return columns;
    }

    public void setColumns(int columns)
    {
        this.columns = columns;
    }

    public boolean getDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

}