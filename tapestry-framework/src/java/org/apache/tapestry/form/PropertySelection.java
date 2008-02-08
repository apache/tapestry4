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
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.valid.ValidatorException;

/**
 * A component used to render a drop-down list of options that the user may select. [ <a
 * href="../../../../../components/form/propertyselection.html">Component Reference </a>]
 * <p>
 * Earlier versions of PropertySelection (through release 2.2) were more flexible, they included a
 * <b>renderer </b> property that controlled how the selection was rendered. Ultimately, this proved
 * of little value and this portion of functionality was deprecated in 2.3 and will be removed in
 * 2.3.
 * <p>
 * Typically, the values available to be selected are defined using an
 * {@link java.lang.Enum}. A PropertySelection is dependent on an
 * {@link IPropertySelectionModel} to provide the list of possible values.
 * <p>
 * Often, this is used to select a particular {@link java.lang.Enum} to assign to
 * a property; the {@link org.apache.tapestry.form.EnumPropertySelectionModel} class simplifies this.
 * <p>
 * Often, a drop-down list will contain an initial option that serves both as a label and to represent 
 * that nothing is selected. This can behavior can easily be achieved by decorating an existing 
 * {@link IPropertySelectionModel} with a {@link LabeledPropertySelectionModel}.
 * <p>
 * 
 */
public abstract class PropertySelection extends AbstractFormComponent implements ValidatableField
{   
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        renderDelegatePrefix(writer, cycle);
        
        writer.begin("select");
        writer.attribute("name", getName());
        
        if (isDisabled())
            writer.attribute("disabled", "disabled");
        
        renderIdAttribute(writer, cycle);
        
        renderDelegateAttributes(writer, cycle);
        
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        // Apply informal attributes.
        renderInformalParameters(writer, cycle);
        
        writer.println();
        
        IPropertySelectionModel model = getModel();
        
        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");
        
        getOptionRenderer().renderOptions(writer, cycle, model, getValue());
        
        writer.end(); // <select>

        renderDelegateSuffix(writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        Object object = getModel().translateValue(value);
        
        try
        {
            getValidatableFieldSupport().validate(this, writer, cycle, object);
            
            setValue(object);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }
    
    public abstract IPropertySelectionModel getModel();
    
    /** @since 2.2 * */
    public abstract Object getValue();

    /** @since 2.2 * */
    public abstract void setValue(Object value);
    
    /** Responsible for rendering individual options. */
    public abstract IOptionRenderer getOptionRenderer();
    
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
