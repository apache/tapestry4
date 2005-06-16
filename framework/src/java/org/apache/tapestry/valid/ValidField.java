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

package org.apache.tapestry.valid;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.Form;

/**
 * A {@link Form}component that creates a text field that allows for validation of user input and
 * conversion between string and object values. [ <a
 * href="../../../../../ComponentReference/ValidField.html">Component Reference </a>]
 * <p>
 * A ValidatingTextField uses an {@link IValidationDelegate} to track errors and an
 * {@link IValidator}to convert between strings and objects (as well as perform validations). The
 * validation delegate is shared by all validating text fields in a form, the validator may be
 * shared my multiple elements as desired.
 * 
 * @author Howard Lewis Ship
 */

public abstract class ValidField extends AbstractFormComponent
{
    public abstract boolean isHidden();

    public abstract boolean isDisabled();

    public abstract Object getValue();

    public abstract void setValue(Object value);

    /** Parameter */

    public abstract String getDisplayName();

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IValidationDelegate delegate = getForm().getDelegate();

        delegate.writePrefix(writer, cycle, this, null);

        writer.beginEmpty("input");

        writer.attribute("type", isHidden() ? "password" : "text");

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        writer.attribute("name", getName());

        String value = readValue();
        if (value != null)
            writer.attribute("value", value);

        renderInformalParameters(writer, cycle);

        delegate.writeAttributes(writer, cycle, this, null);

        IValidator validator = getValidator();

        if (validator == null)
            throw Tapestry.createRequiredParameterException(this, "validator");

        validator.renderValidatorContribution(this, writer, cycle);

        writer.closeTag();

        delegate.writeSuffix(writer, cycle, this, null);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());

        updateValue(value);
    }

    protected String readValue()
    {
        IValidator validator = getValidator();
        if (validator == null)
            throw Tapestry.createRequiredParameterException(this, "validator");
        
        IValidationDelegate delegate = getForm().getDelegate();

        if (delegate.isInError())
            return delegate.getFieldInputValue();

        Object value = getValue();
        
        String result = validator.toString(this, value);

        return result;
    }

    protected void updateValue(String value)
    {
        Object objectValue = null;

        IValidator validator = getValidator();
        if (validator == null)
            throw Tapestry.createRequiredParameterException(this, "validator");

        IValidationDelegate delegate = getForm().getDelegate();

        delegate.recordFieldInputValue(value);

        try
        {
            objectValue = validator.toObject(this, value);
        }
        catch (ValidatorException ex)
        {
            delegate.record(ex);
            return;
        }

        setValue(objectValue);
    }

    public abstract IValidator getValidator();
}