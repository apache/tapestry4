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

package net.sf.tapestry.valid;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.NullValueForBindingException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.form.Form;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Used to label an {@link IFormComponent}.  Because such fields
 *  know their displayName (user-presentable name), there's no reason
 *  to hard code the label in a page's HTML template (this also helps
 *  with localization).
 *
 *  <p>The {@link IValidationDelegate delegate} may
 *  also modify the formatting of the label to match the state of the
 *  field (i.e., if the field is required or in error).
 *
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
 * <tr>
 *  <td>field</td>
 *  <td>{@link IFormComponent}</td>
 *  <td>in</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>The field to be labeled.</td>
 * </tr>
 * 
 *  <tr>
 * 		<td>displayName</td>
 * 		<td>{@link String}</td>
 * 		<td>R</td> <td>no</td> <td>&nbsp;</td>
 * 		<td>
 * 	Allows the display name to be overriden from the value supplied by a {@link IFormComponent}.
 *  Most implementation of {@link IFormComponent} provide a null displayName, and it
 *  is necessary to set one using this parameter.
 * 	</td>
 *  </tr>
 *
 *  </table>
 *
 * <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Lewis Lewis Ship
 *  @version $Id$
 * 
 **/

public class FieldLabel extends AbstractComponent
{
    private IFormComponent field;
    private String displayName;

    /**
     *  Gets the {@link IField} 
     *  and {@link IValidationDelegate delegate},
     *  then renders the label obtained from the field.  Does nothing
     *  when rewinding.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;

        String finalDisplayName = (displayName != null) ? displayName : field.getDisplayName();

        if (finalDisplayName == null)
            throw new RequestCycleException(
                Tapestry.getString("FieldLabel.no-display-name", field.getExtendedId()),
                this);

        IValidationDelegate delegate = Form.get(cycle).getDelegate();

        delegate.writeLabelPrefix(field, writer, cycle);

        writer.print(finalDisplayName);

        delegate.writeLabelSuffix(field, writer, cycle);
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public IFormComponent getField()
    {
        return field;
    }

    public void setField(IFormComponent field)
    {
        this.field = field;
    }

}