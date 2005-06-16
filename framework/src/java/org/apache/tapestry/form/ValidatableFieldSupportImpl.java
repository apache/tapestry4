// Copyright 2005 The Apache Software Foundation
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

import java.util.Iterator;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.form.validator.Validator;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Default {@link VadidatableFieldSupport} implementation.  This implementation generates
 * calls to a static javascript function during render if client-side validation is enabled.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class ValidatableFieldSupportImpl implements ValidatableFieldSupport
{
    private ValueConverter _converter;
    
    public ValueConverter getValueConverter()
    {
        return _converter;
    }
    
    public void setValueConverter(ValueConverter converter)
    {
        _converter = converter;
    }
    
    protected Iterator getValidatorsIterator(ValidatableField component)
    {
        return (Iterator) _converter.coerceValue(component.getValidators(), Iterator.class);
    }
    
    /**
     * @see org.apache.tapestry.form.ValidatableFieldSupport#render(org.apache.tapestry.form.ValidatableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void render(ValidatableField component, IMarkupWriter writer, IRequestCycle cycle)
    {
        IValidationDelegate delegate = component.getForm().getDelegate();
        
        String value = delegate.isInError() ? delegate.getFieldInputValue() : getTranslatedValue(component);
        
        component.render(writer, cycle, value);
    }
    
    /**
     * @see org.apache.tapestry.form.ValidatableFieldSupport#renderContributions(org.apache.tapestry.form.ValidatableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void renderContributions(ValidatableField component, IMarkupWriter writer, IRequestCycle cycle)
    {
        if (component.getForm().isClientValidationEnabled())
        {
            component.getTranslator().renderContribution(writer, cycle, component);
            
            Iterator validators = getValidatorsIterator(component);
            
            while (validators.hasNext())
            {
                Validator validator = (Validator) validators.next();
                
                validator.renderContribution(writer, cycle, component);
            }
        }
    }

    protected String getTranslatedValue(ValidatableField component)
    {
        Object value = component.readValue();
        
        return (value != null) ? component.getTranslator().format(component, value) : "";
    }
    
    /**
     * @see org.apache.tapestry.form.ValidatableFieldSupport#bind(org.apache.tapestry.form.ValidatableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle, java.lang.String)
     */
    public void bind(ValidatableField component, IMarkupWriter writer, IRequestCycle cycle, String value) throws ValidatorException
    {
        IValidationDelegate delegate = component.getForm().getDelegate();
        
        delegate.recordFieldInputValue(value);
        
        Object object = component.getTranslator().parse(component, value);

        component.writeValue(object);
        
        if (object != null)
        {
            Iterator validators = getValidatorsIterator(component);
            
            while (validators.hasNext())
            {
                Validator validator = (Validator) validators.next();
                
                validator.validate(component, object);
            }
        }
    }
}
