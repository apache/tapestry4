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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Implements a component that manages an HTML &lt;input type=checkbox&gt; form element. [ <a
 * href="../../../../../ComponentReference/Checkbox.html">Component Reference </a>]
 * <p>
 * As of 4.0, this component can be validated.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class Checkbox extends AbstractFormComponent implements ValidatableField
{
    /**
     * @see org.apache.tapestry.form.validator.AbstractRequirableField#renderRequirableFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        renderDelegatePrefix(writer, cycle);
        
        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");

        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (getValue())
            writer.attribute("checked", "checked");

        renderIdAttribute(writer, cycle);

        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        renderInformalParameters(writer, cycle);

        writer.closeTag();
        
        renderDelegateSuffix(writer, cycle);
    }

    /**
     * In traditional HTML, many checkboxes would have the same name but different values. Under
     * Tapestry, it makes more sense to have different names and a fixed value. For a checkbox, we
     * only care about whether the name appears as a request parameter.
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        try
        {
            // This is atypical validation - since this component does not explicitly bind to an object
            getValidatableFieldSupport().validate(this, writer, cycle, value);

            setValue(value != null);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }

    public abstract boolean getValue();

    public abstract void setValue(boolean selected);

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
