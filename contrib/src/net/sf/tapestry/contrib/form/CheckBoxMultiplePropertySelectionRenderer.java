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

package net.sf.tapestry.contrib.form;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  Implementation of {@link IMultiplePropertySelectionRenderer} that
 *  produces a table of checkbox (&lt;input type=checkbox&gt;) elements.
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 **/

public class CheckBoxMultiplePropertySelectionRenderer
    implements IMultiplePropertySelectionRenderer
{

    /**
     *  Writes the &lt;table&gt; element.
     *
     **/

    public void beginRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.begin("table");
        writer.attribute("border", 0);
        writer.attribute("cellpadding", 0);
        writer.attribute("cellspacing", 2);
    }

    /**
     *  Closes the &lt;table&gt; element.
     *
     **/

    public void endRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        writer.end(); // <table>
    }

    /**
     *  Writes a row of the table.  The table contains two cells; the first is the checkbox,
     *  the second is the label for the checkbox.
     *
     **/

    public void renderOption(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected)
        throws RequestCycleException
    {
        writer.begin("tr");
        writer.begin("td");

        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");
        writer.attribute("name", component.getName());
        writer.attribute("value", model.getValue(index));

        if (component.isDisabled())
            writer.attribute("disabled");

        if (selected)
            writer.attribute("checked");

        writer.end(); // <td>

        writer.println();

        writer.begin("td");
        writer.print(model.getLabel(index));
        writer.end(); // <td>
        writer.end(); // <tr>

        writer.println();
    }
}