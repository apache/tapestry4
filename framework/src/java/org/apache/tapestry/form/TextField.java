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
 * Implements a component that manages an HTML &lt;input type=text&gt; or &lt;input
 * type=password&gt; form element. [ <a
 * href="../../../../../ComponentReference/TextField.html">Component Reference </a>]
 * <p>
 * As of 4.0, this component can be configurably translated and validated.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class TextField extends AbstractFormComponent implements TranslatedField
{
    public abstract boolean isHidden();

    public abstract Object getValue();
    public abstract void setValue(Object value);

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = getTranslatedFieldSupport().format(this, getValue());
        
        renderDelegatePrefix(writer, cycle);

        writer.beginEmpty("input");

        writer.attribute("type", isHidden() ? "password" : "text");

        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (value != null)
            writer.attribute("value", value);

        renderIdAttribute(writer, cycle);

        renderDelegateAttributes(writer, cycle);

        getTranslatedFieldSupport().renderContributions(this, writer, cycle);
        getValidatableFieldSupport().renderContributions(this, writer, cycle);

        renderInformalParameters(writer, cycle);

        writer.closeTag();

        renderDelegateSuffix(writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        try
        {
            Object object = getTranslatedFieldSupport().parse(this, value);
            
            setValue(object);
            
            getValidatableFieldSupport().validate(this, writer, cycle, object);
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
     * Injected.
     */
    public abstract TranslatedFieldSupport getTranslatedFieldSupport();

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
     */
    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }
}