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
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.Tapestry;

/**
 *  A component that renders an HTML &lt;option&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link Select} component.
 *
 *  [<a href="../../../../../ComponentReference/Option.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Option extends AbstractComponent
{
    private IBinding _selectedBinding;
	private String _label;


    public IBinding getSelectedBinding()
    {
        return _selectedBinding;
    }

    /**
     *  Renders the &lt;option&gt; element, or responds when the form containing the element 
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *  <table border=1>
     *  <tr>  <th>attribute</th>  <th>value</th> </tr>
     *  <tr>  <td>value</td>  <td>from {@link Select#getNextOptionId()}</td>  </tr>
     *  <tr> <td>selected</td> <td>from selected property</td> </tr>
     *  <tr> <td><i>other</i></td> <td>from extra bindings</td> </tr>
     *  </tr>
     *  </table>
     *
     * <p>If the <code>label</code> property is set, it is inserted after the
     * &lt;option&gt; tag.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        String value;
        Select select;
        boolean rewinding;

        select = Select.get(cycle);
        if (select == null)
            throw new RequestCycleException(Tapestry.getString("Option.must-be-contained-by-select"), this);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        rewinding = select.isRewinding();

        value = select.getNextOptionId();

        if (rewinding)
        {
            if (!select.isDisabled())
                _selectedBinding.setBoolean(select.isSelected(value));
        }
        else
        {
            writer.beginEmpty("option");

            writer.attribute("value", value);

            if (_selectedBinding.getBoolean())
                writer.attribute("selected");

            generateAttributes(writer, cycle);

            if (_label != null) 
                writer.print(_label);

            writer.println();
        }

    }

    public void setSelectedBinding(IBinding value)
    {
        _selectedBinding = value;
    }
    public String getLabel()
    {
        return _label;
    }

    public void setLabel(String label)
    {
        this._label = label;
    }

}