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
import net.sf.tapestry.RenderOnlyPropertyException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  A special type of form component that is used to contain {@link Radio}
 *  components.  The Radio and {@link Radio} group components work together to
 *  update a property of some other object, much like a more flexible
 *  version of a {@link PropertySelection}.
 *
 *  [<a href="../../../../../ComponentReference/RadioGroup.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class RadioGroup extends AbstractFormComponent
{
    private IBinding _selectedBinding;
  
  	private boolean _disabled;
  	
    // Cached copy of the value from the selectedBinding
    private Object _selection;

    // The value from the HTTP request indicating which
    // Radio was selected by the user.
    private int _selectedOption;

    // The HTML field name used for this group (i.e., by all Radio buttons
    // within this group).

    private String _name;

    private boolean _rewinding;
    private boolean _rendering;
    private int _nextOptionId;

    /**
     *  A <code>RadioGroup</code> places itself into the {@link IRequestCycle} as
     *  an attribute, so that its wrapped {@link Radio} components can identify thier
     *  state.
     *
     **/

    private static final String ATTRIBUTE_NAME = "net.sf.tapestry.active.RadioGroup";

    public static RadioGroup get(IRequestCycle cycle)
    {
        return (RadioGroup) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public IBinding getSelectedBinding()
    {
        return _selectedBinding;
    }

    public void setSelectedBinding(IBinding value)
    {
        _selectedBinding = value;
    }


    public String getName()
    {
        return _name;
    }

    public int getNextOptionId()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "nextOptionId");

        return _nextOptionId++;
    }

    /**
     *  Used by {@link Radio} components wrapped by this <code>RadioGroup</code> to see
     *  if the group as a whole is disabled.
     *
     **/

    public boolean isDisabled()
    {
        return _disabled;
    }

    public boolean isRewinding()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    /**
     *  Returns true if the value is equal to the current selection for the
     *  group.  This is invoked by a {@link Radio} during rendering
     *  to determine if it should be marked 'checked'.
     *
     **/

    public boolean isSelection(Object value)
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "selection");

        if (_selection == value)
            return true;

        if (_selection == null || value == null)
            return false;

        return _selection.equals(value);
    }

    /**
    *  Invoked by the {@link Radio} which is selected to update the 
    *  property bound to the selected parameter.
    *
    **/

    public void updateSelection(Object value)
    {
        _selectedBinding.setObject(value);
    }

    /**
    *  Used by {@link Radio} components when rewinding to see if their value was submitted.
    *
    **/

    public boolean isSelected(int option)
    {
        return _selectedOption == option;
    }

    /**
     * Doesn't actual render an HTML element as there is no direct equivalent for
     * an HTML element.  A <code>RadioGroup</code> component exists to organize the
     * {@link Radio} components it wraps (directly or indirectly).
     *
     * A {@link Radio} can finds its {@link RadioGroup} as a {@link IRequestCycle} attribute.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("RadioGroup.may-not-nest"), this);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        _rewinding = form.isRewinding();

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        // When rewinding, find out which (if any) radio was selected by
        // the user.

        if (_rewinding)
        {
            String value = cycle.getRequestContext().getParameter(_name);
            if (value == null)
                _selectedOption = -1;
            else
                _selectedOption = Integer.parseInt(value);
        }

        try
        {
            _rendering = true;
            _nextOptionId = 0;

            // For rendering, the Radio components need to know what the current
            // selection is, so that the correct one can mark itself 'checked'.

            if (!_rewinding)
                _selection = _selectedBinding.getObject();

            renderWrapped(writer, cycle);
        }
        finally
        {
            _rendering = false;
            _selection = null;
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

}