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

package net.sf.tapestry.components;

import java.text.Format;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Used to insert some text (from a parameter) into the HTML.
 *
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th>
 * <th>Read / Write</th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>value</td> <td>Object</td> <td>R</td>
 *  <td>no</td> <td>&nbsp;</td>
 *  <td>The value to be inserted.  If the binding is null, then nothing is inserted.
 *  Any object may be inserted, the <code>toString()</code> method is used
 *  to convert it to a printable value.</td> </tr>
 *
 * <tr>
 *	<td>format</td>
 *	<td>{@link Format}</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>An optional format object used to convert the value parameter for
 *  insertion into the HTML response. </td> </tr>
 *
 *  <tr>
 *      <td>raw</td>
 *      <td>boolean</td>
 *      <td>no</td>
 *      <td>false</td>
 *      <td>If true, then the method {@link IMarkupWriter#printRaw(String)} is used,
 *  		rather than {@link IMarkupWriter#print(String)}.
 *      </td>
 *  </tr>
 *
 *  <tr>
 * 		<td>class</td>
 * 		<td>{@link String}</td>
 * 		<td>no</td>
 * 		<td>&nbsp;</td>
 * 		<td>
 * 	If specified, then the output is wrapped in an HTML &lt;span&gt; tag, using the value
 *  specified as the CSS class.
 * 		</td>
 * 	</tr>
 * 
 * </table>
 *
 * <p>Informal parameters are not allowed.  The component must not have a body.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class Insert extends AbstractComponent
{
    private Object value;
    private Format format;
    private String styleClass;
    private boolean raw;

    /**
     *  Prints its value parameter, possibly formatted by its format parameter.
     *  Notes:
     *  <ul>
     *  <li>If the cycle is rewinding, then this method does nothing.
     *  <li>If both the value is null, then this method does nothing
     *  <li>If the format is non-null, then {@link Format#format(Object)} is invoked and
     *  the resulting String is what's inserted.
     *  <li>The method will use either {@link IMarkupWriter#print(String)} or
     *  {@link IMarkupWriter#printRaw(String)}, depending on the value
     *  of the raw parameter.
     *  </ul>
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;

        String insert = null;

        if (format == null)
        {
            if (value == null)
            	return;
            	
            insert = value.toString();
        }
        else
        {
            try
            {
                insert = format.format(value);
            }
            catch (Exception ex)
            {
                throw new RequestCycleException(
                    Tapestry.getString("Insert.unable-to-format", value),
                    this,
                    ex);
            }
        }

        if (styleClass != null)
        {
            writer.begin("span");
            writer.attribute("class", styleClass);
        }

        if (raw)
            writer.printRaw(insert);
        else
            writer.print(insert);

        if (styleClass != null)
            writer.end(); // <span>
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public Format getFormat()
    {
        return format;
    }

    public void setFormat(Format format)
    {
        this.format = format;
    }

    public String getStyleClass()
    {
        return styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    public boolean getRaw()
    {
        return raw;
    }

    public void setRaw(boolean raw)
    {
        this.raw = raw;
    }

}