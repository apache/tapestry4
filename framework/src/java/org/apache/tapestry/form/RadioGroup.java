// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.form;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * A special type of form component that is used to contain {@link Radio}components. The Radio and
 * {@link Radio}group components work together to update a property of some other object, much like
 * a more flexible version of a {@link PropertySelection}. [ <a
 * href="../../../../../ComponentReference/RadioGroup.html">Component Reference </a>]
 * 
 * As of 4.0, RadioGroup can indicate that it is required.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class RadioGroup extends AbstractRequirableField
{
    // Cached copy of the value from the selectedBinding
    private Object _selection;

    // The value from the HTTP request indicating which
    // Radio was selected by the user.
    private int _selectedOption;

    private boolean _rewinding;

    private boolean _rendering;

    private int _nextOptionId;

    /**
     * A <code>RadioGroup</code> places itself into the {@link IRequestCycle}as an attribute, so
     * that its wrapped {@link Radio}components can identify thier state.
     */

    private static final String ATTRIBUTE_NAME = "org.apache.tapestry.active.RadioGroup";

    public static RadioGroup get(IRequestCycle cycle)
    {
        return (RadioGroup) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public int getNextOptionId()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "nextOptionId");

        return _nextOptionId++;
    }

    public boolean isRewinding()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    /**
     * Returns true if the value is equal to the current selection for the group. This is invoked by
     * a {@link Radio}during rendering to determine if it should be marked 'checked'.
     */

    public boolean isSelection(Object value)
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "selection");

        if (_selection == value)
            return true;

        if (_selection == null || value == null)
            return false;

        return _selection.equals(value);
    }

    /**
     * Invoked by the {@link Radio}which is selected to update the property bound to the selected
     * parameter.
     */

    public void updateSelection(Object value)
    {
        getBinding("selected").setObject(value);
    }

    /**
     * Used by {@link Radio}components when rewinding to see if their value was submitted.
     */

    public boolean isSelected(int option)
    {
        return _selectedOption == option;
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#prepareForRender(org.apache.tapestry.IRequestCycle)
     */
    protected void prepareForRender(IRequestCycle cycle)
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(Tapestry.getMessage("RadioGroup.may-not-nest"),
                    this, null, null);
        
        cycle.setAttribute(ATTRIBUTE_NAME, this);

        _rendering = true;
        _nextOptionId = 0;
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#cleanupAfterRender(org.apache.tapestry.IRequestCycle)
     */
    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _rendering = false;
        _selection = null;
        
        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#finishLoad()
     */
    protected void finishLoad()
    {
        setRequiredMessage(ValidationStrings.getMessagePattern(ValidationStrings.REQUIRED_SELECT_FIELD, getPage().getLocale()));
    }

    /**
     * @see org.apache.tapestry.form.AbstractRequirableField#bind(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void bind(IMarkupWriter writer, IRequestCycle cycle) throws ValidatorException
    {
        String value = getSubmittedValue(cycle);
        
        if (value == null)
            _selectedOption = -1;
        else
            _selectedOption = Integer.parseInt(value);
        
        _rewinding = true;
        
        renderBody(writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.AbstractRequirableField#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderFormComponent(writer, cycle);

        _rewinding = false;
        
        // For rendering, the Radio components need to know what the current
        // selection is, so that the correct one can mark itself 'checked'.
        _selection = getBinding("selected").getObject();
        
        renderBody(writer, cycle);
    }
}