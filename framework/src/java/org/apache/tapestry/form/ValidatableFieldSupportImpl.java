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

import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.form.validator.Validator;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Default {@link VadidatableFieldSupport} implementation. This implementation generates calls to a
 * static javascript function during render if client-side validation is enabled.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class ValidatableFieldSupportImpl implements ValidatableFieldSupport
{
    private ValueConverter _converter;

    private ThreadLocale _threadLocale;

    public void setValueConverter(ValueConverter converter)
    {
        _converter = converter;
    }

    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }

    protected Iterator getValidatorsIterator(ValidatableField component)
    {
        return (Iterator) _converter.coerceValue(component.getValidators(), Iterator.class);
    }

    /**
     * @see org.apache.tapestry.form.ValidatableFieldSupport#renderValidatorContributions(org.apache.tapestry.form.ValidatableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void renderContributions(ValidatableField component, IMarkupWriter writer,
            IRequestCycle cycle)
    {
        if (component.getForm().isClientValidationEnabled())
        {
            FormComponentContributorContext context = new FormComponentContributorContextImpl(
                    _threadLocale.getLocale(), cycle, component);

            Iterator validators = getValidatorsIterator(component);

            while (validators.hasNext())
            {
                Validator validator = (Validator) validators.next();

                validator.renderContribution(writer, cycle, context, component);
            }
        }
    }

    /**
     * @see org.apache.tapestry.form.ValidatableFieldSupport#validate(org.apache.tapestry.form.ValidatableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle, java.lang.Object)
     */
    public void validate(ValidatableField component, IMarkupWriter writer, IRequestCycle cycle, Object object) throws ValidatorException
    {
        boolean isNonNull = (object != null);

        Iterator validators = getValidatorsIterator(component);

        ValidationMessages messages = new ValidationMessagesImpl(component, _threadLocale.getLocale());

        while (validators.hasNext())
        {
            Validator validator = (Validator) validators.next();

            if (isNonNull || validator.getAcceptsNull())
                validator.validate(component, messages, object);
        }
    }

    public boolean isRequired(ValidatableField field)
    {
        Iterator i = getValidatorsIterator(field);

        while (i.hasNext())
        {
            Validator validator = (Validator) i.next();

            if (validator.isRequired())
                return true;
        }

        return false;
    }
}
