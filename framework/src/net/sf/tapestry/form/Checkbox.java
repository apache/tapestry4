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
 *  [<a href="../../../../../ComponentReference/Checkbox.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Checkbox extends AbstractFormComponent
{
    private boolean _disabled;

    /**  @since 2.2 **/
    private boolean _selected;

    private String _name;

    public String getName()
    {
        return _name;
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
        IForm form = getForm(cycle);

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        if (form.isRewinding())
        {
            String value = cycle.getRequestContext().getParameter(_name);

            _selected = (value != null);

            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");

        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        if (_selected)
            writer.attribute("checked");

        generateAttributes(writer, cycle);

        writer.closeTag();
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /** @since 2.2 **/

    public boolean isSelected()
    {
        return _selected;
    }

    /** @since 2.2 **/

    public void setSelected(boolean selected)
    {
        _selected = selected;
    }

}