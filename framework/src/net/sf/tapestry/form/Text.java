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

/**
 *  Implements a component that manages an HTML &lt;textarea&gt; form element.
 *
 *  [<a href="../../../../../ComponentReference/Text.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Text extends AbstractFormComponent
{
	private int _rows;
	private int _columns;
	private boolean _disabled;	
    private IBinding _valueBinding;
    private String _name;

    public String getName()
    {
        return _name;
    }


    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = form.isRewinding();

        // Used whether rewinding or not.

        _name = form.getElementId(this);


        if (rewinding)
        {
            if (!_disabled)
            {
                String value = cycle.getRequestContext().getParameter(_name);

                _valueBinding.setString(value);
            }

            return;
        }

        writer.begin("textarea");

        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        if (_rows != 0)
            writer.attribute("rows", _rows);

        if (_columns != 0)
            writer.attribute("cols", _columns);

        generateAttributes(writer, cycle);

        String value = _valueBinding.getString();
        if (value != null)
            writer.print(value);

        writer.end();

    }

    public int getColumns()
    {
        return _columns;
    }

    public void setColumns(int columns)
    {
        _columns = columns;
    }

    public boolean getDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public int getRows()
    {
        return _rows;
    }

    public void setRows(int rows)
    {
        _rows = rows;
    }

}