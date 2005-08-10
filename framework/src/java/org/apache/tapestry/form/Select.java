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

import java.util.HashSet;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Implements a component that manages an HTML &lt;select&gt; form element. The most common
 * situation, using a &lt;select&gt; to set a specific property of some object, is best handled
 * using a {@link PropertySelection}component. [ <a
 * href="../../../../../ComponentReference/Select.html">Component Reference </a>]
 * <p>
 * Otherwise, this component is very similar to {@link RadioGroup}. 
 * <p>
 * As of 4.0, this component can be validated.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class Select extends AbstractFormComponent implements ValidatableField
{
    private boolean _rewinding;

    private boolean _rendering;

    private Set _selections;

    private int _nextOptionId;

    /**
     * Used by the <code>Select</code> to record itself as a {@link IRequestCycle}attribute, so
     * that the {@link Option}components it wraps can have access to it.
     */

    private final static String ATTRIBUTE_NAME = "org.apache.tapestry.active.Select";

    public static Select get(IRequestCycle cycle)
    {
        return (Select) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    public abstract boolean isMultiple();

    public boolean isRewinding()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    public String getNextOptionId()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "nextOptionId");

        // Return it as a hex value.

        return Integer.toString(_nextOptionId++);
    }

    public boolean isSelected(String value)
    {
        if (_selections == null)
            return false;

        return _selections.contains(value);
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#prepareForRender(org.apache.tapestry.IRequestCycle)
     */
    protected void prepareForRender(IRequestCycle cycle)
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(Tapestry.getMessage("Select.may-not-nest"), this,
                    null, null);

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
        _selections = null;
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        _rewinding = false;

        renderDelegatePrefix(writer, cycle);

        writer.begin("select");

        writer.attribute("name", getName());

        if (isMultiple())
            writer.attribute("multiple", "multiple");

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        renderIdAttribute(writer, cycle);

        renderDelegateAttributes(writer, cycle);

        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        renderInformalParameters(writer, cycle);

        renderBody(writer, cycle);

        writer.end();

        renderDelegateSuffix(writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        _selections = null;
        _rewinding = true;

        String[] parameters = cycle.getParameters(getName());

        try
        {
            if (parameters != null)
            {
                int length = parameters.length;
    
                _selections = new HashSet((length > 30) ? 101 : 7);
    
                for (int i = 0; i < length; i++)
                    _selections.add(parameters[i]);
            }
    
            renderBody(writer, cycle);
            
            // This is atypical validation - since this component does not explicitly bind to an object
            getValidatableFieldSupport().validate(this, writer, cycle, parameters);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }

    /**
     * Injected.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
     */
    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }
}