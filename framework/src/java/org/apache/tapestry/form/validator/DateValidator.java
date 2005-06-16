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
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * {@link Validator} implementation that validates a date against max and min values.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class DateValidator extends AbstractFormComponentContributor implements Validator
{
    private Date _min;
    private Date _max;
    
    private String _tooEarlyMessage;
    private String _tooLateMessage;

    /**
     * @see org.apache.tapestry.form.validator.Validator#validate(org.apache.tapestry.form.IFormComponent, java.lang.Object)
     */
    public void validate(IFormComponent field, Object object) throws ValidatorException
    {
        Date date = (Date) object;
        
        if ((_min != null) && date.before(_min))
        {
            throw new ValidatorException(buildTooSmallMessage(field), ValidationConstraint.TOO_SMALL);
        }
        
        if ((_max != null) && date.after(_max))
        {
            throw new ValidatorException(buildTooLargeMessage(field), ValidationConstraint.TOO_LARGE);
        }
    }
    
    protected String buildTooSmallMessage(IFormComponent field)
    {
        return buildMessage(field, _tooEarlyMessage, ValidationStrings.DATE_TOO_EARLY, _min);
    }
    
    protected String buildTooLargeMessage(IFormComponent field)
    {
        return buildMessage(field, _tooLateMessage, ValidationStrings.DATE_TOO_LATE, _max);
    }

    protected String buildMessage(IFormComponent field, String messageOverride, String key, Date date)
    {
        Locale locale = field.getPage().getLocale();
        String message = (messageOverride == null) ? ValidationStrings.getMessagePattern(key, locale) : messageOverride;
        
        return MessageFormat.format(message, new Object[] { field.getDisplayName(), date });
    }
    
    /**
     * @return Returns the max.
     */
    public Date getMax()
    {
        return _max;
    }
    
    /**
     * @param max The max to set.
     */
    public void setMax(Date max)
    {
        _max = max;
    }
    
    /**
     * @return Returns the min.
     */
    public Date getMin()
    {
        return _min;
    }
    
    /**
     * @param min The min to set.
     */
    public void setMin(Date min)
    {
        _min = min;
    }
    
    /**
     * @return Returns the numberTooLargeMessage.
     */
    public String getTooLateMessage()
    {
        return _tooLateMessage;
    }
    
    /**
     * @param message The tooLargeMessage to set.
     */
    public void setTooLateMessage(String message)
    {
        _tooLateMessage = message;
    }
    
    /**
     * @return Returns the tooSmallMessage.
     */
    public String getTooEarlyMessage()
    {
        return _tooEarlyMessage;
    }
    
    /**
     * @param message The tooSmallMessage to set.
     */
    public void setTooEarlyMessage(String message)
    {
        _tooEarlyMessage = message;
    }
}
