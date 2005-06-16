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

package org.apache.tapestry.form.validator;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * @author  Paul Ferraro
 * @since   4.0
 */
public class StringValidator extends AbstractFormComponentContributor implements Validator
{
    private int _minLength = 0;
    private int _maxLength = 0;

    private String _tooLongMessage;
    private String _tooShortMessage;

    protected String defaultScript()
    {
        return "/org/apache/tapestry/form/validator/StringValidator.js";
    }
    
    /**
     * @see org.apache.tapestry.form.validator.Validator#validate(java.lang.Object)
     */
    public void validate(IFormComponent field, Object object) throws ValidatorException
    {
        String string = (String) object;
        
        if ((_minLength > 0) && (string.length() < _minLength))
        {
            throw new ValidatorException(buildTooShortMessage(field), ValidationConstraint.MINIMUM_WIDTH);
        }
        
        if ((_maxLength > 0) && (string.length() > _maxLength))
        {
            throw new ValidatorException(buildTooLongMessage(field), ValidationConstraint.MAXIMUM_WIDTH);
        }
    }
    
    public int getMinLength()
    {
        return _minLength;
    }

    public void setMinLength(int minLength)
    {
        _minLength = minLength;
    }

    public int getMaxLength()
    {
        return _maxLength;
    }
    
    public void setMaxLength(int maxLength)
    {
        _maxLength = maxLength;
    }

    public String getTooShortMessage()
    {
        return _tooShortMessage;
    }

    public void setTooShortMessage(String message)
    {
        _tooShortMessage = message;
    }

    public String getTooLongMessage()
    {
        return _tooLongMessage;
    }

    public void setTooLongMessage(String message)
    {
        _tooLongMessage = message;
    }

    protected String buildTooShortMessage(IFormComponent field)
    {
        return buildMessage(field, _tooShortMessage, ValidationStrings.VALUE_TOO_SHORT, _minLength);
    }

    protected String buildTooLongMessage(IFormComponent field)
    {
        return buildMessage(field, _tooLongMessage, ValidationStrings.VALUE_TOO_LONG, _maxLength);
    }
    
    protected String buildMessage(IFormComponent field, String messageOverride, String key, int length)
    {
        Locale locale = field.getPage().getLocale();
        String message = (messageOverride == null) ? ValidationStrings.getMessagePattern(key, locale) : messageOverride;
        
        return MessageFormat.format(message, new Object[] { new Integer(length), field.getDisplayName() });
    }
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponentContributor#renderContribution(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle, org.apache.tapestry.form.IFormComponent)
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle, IFormComponent field)
    {
        super.renderContribution(writer, cycle, field);
        
        IForm form = field.getForm();
        String formName = form.getName();
        String fieldName = field.getName();

        if (_minLength > 0)
        {
            String message = buildTooShortMessage(field);
            String handler = "validate_min_length(document." + formName + "." + fieldName + "," + _minLength + ",'" + message + "')";
            
            super.addSubmitHandler(form, handler);
        }
        
        if (_maxLength > 0)
        {
            String message = buildTooLongMessage(field);
            String handler = "validate_max_length(document." + formName + "." + fieldName + "," + _maxLength + ",'" + message + "')";
            
            super.addSubmitHandler(form, handler);
        }
    }
}
