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

package org.apache.tapestry.valid;

import org.apache.tapestry.form.IFormComponent;

/**
 * Base class for a number of implementations of {@link org.apache.tapestry.valid.IValidator},
 * meant to replace the awkward {@link org.apache.tapestry.valid.NumberValidator}.
 * 
 * @author Howard M. Lewis Ship
 */
public abstract class AbstractNumericValidator extends BaseValidator
{
    private boolean _zeroIsNull;

    public AbstractNumericValidator()
    {
        super();
    }

    public AbstractNumericValidator(String initializer)
    {
        super(initializer);
    }

    public AbstractNumericValidator(boolean required)
    {
        super(required);
    }

    /**
     * If true, then when rendering, a zero is treated as a non-value, and null is returned. If
     * false, the default, then zero is rendered as zero.
     */

    public boolean getZeroIsNull()
    {
        return _zeroIsNull;
    }

    public void setZeroIsNull(boolean zeroIsNull)
    {
        _zeroIsNull = zeroIsNull;
    }

    private String _scriptPath = 
        getDefaultScriptPath();

    
    private String _invalidNumericFormatMessage;

    private String _invalidIntegerFormatMessage;

    private String _numberTooSmallMessage;

    private String _numberTooLargeMessage;

    private String _numberRangeMessage;

    /**
     * @since 2.2
     */
    public String getScriptPath()
    {
        return _scriptPath;
    }

    /**
     * Allows a developer to use the existing validation logic with a different client-side script.
     * This is often sufficient to allow application-specific error presentation (perhaps by using
     * DHTML to update the content of a &lt;span&gt; tag, or to use a more sophisticated pop-up
     * window than <code>window.alert()</code>).
     * 
     * @since 2.2
     */
    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    /** @since 3.0 */
    public String getInvalidNumericFormatMessage()
    {
        return _invalidNumericFormatMessage;
    }

    /** @since 3.0 */
    public String getInvalidIntegerFormatMessage()
    {
        return _invalidIntegerFormatMessage;
    }

    /** @since 3.0 */
    public String getNumberRangeMessage()
    {
        return _numberRangeMessage;
    }

    /** @since 3.0 */
    public String getNumberTooLargeMessage()
    {
        return _numberTooLargeMessage;
    }

    /** @since 3.0 */
    public String getNumberTooSmallMessage()
    {
        return _numberTooSmallMessage;
    }

    /**
     * Overrides the <code>invalid-numeric-format</code> bundle key. Parameter {0} is the display
     * name of the field.
     * 
     * @since 3.0
     */
    public void setInvalidNumericFormatMessage(String string)
    {
        _invalidNumericFormatMessage = string;
    }

    /**
     * Overrides the <code>invalid-int-format</code> bundle key. Parameter {0} is the display name
     * of the field.
     * 
     * @since 3.0
     */
    public void setInvalidIntegerFormatMessage(String string)
    {
        _invalidIntegerFormatMessage = string;
    }

    /**
     * Overrides the <code>number-range</code> bundle key. Parameter [0} is the display name of
     * the field. Parameter {1} is the minimum value. Parameter {2} is the maximum value.
     * 
     * @since 3.0
     */
    public void setNumberRangeMessage(String string)
    {
        _numberRangeMessage = string;
    }

    /**
     * Overrides the <code>number-too-large</code> bundle key. Parameter {0} is the display name
     * of the field. Parameter {1} is the maximum allowed value.
     * 
     * @since 3.0
     */
    public void setNumberTooLargeMessage(String string)
    {
        _numberTooLargeMessage = string;
    }

    /**
     * Overrides the <code>number-too-small</code> bundle key. Parameter {0} is the display name
     * of the field. Parameter {1} is the minimum allowed value.
     * 
     * @since 3.0
     */
    public void setNumberTooSmallMessage(String string)
    {
        _numberTooSmallMessage = string;
    }

    /** @since 3.0 */
    protected String buildInvalidNumericFormatMessage(IFormComponent field)
    {
        String pattern = getPattern(
                getInvalidNumericFormatMessage(),
                "invalid-numeric-format",
                field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName());
    }

    protected String buildNumberTooSmallMessage(IFormComponent field, Number minimum)
    {
        String pattern = getPattern(getNumberTooSmallMessage(), "number-too-small", field.getPage()
                .getLocale());

        return formatString(pattern, field.getDisplayName(), minimum);
    }

    /** @since 3.0 */
    protected String buildInvalidIntegerFormatMessage(IFormComponent field)
    {
        String pattern = getPattern(getInvalidIntegerFormatMessage(), "invalid-int-format", field
                .getPage().getLocale());
    
        return formatString(pattern, field.getDisplayName());
    }

    /**
     * @since 3.0
     */
    protected String buildNumberTooLargeMessage(IFormComponent field, Number maximum)
    {
        String pattern = getPattern(getNumberTooLargeMessage(), "number-too-large", field.getPage()
                .getLocale());
    
        return formatString(pattern, field.getDisplayName(), maximum);
    }

    protected String buildNumberRangeMessage(IFormComponent field, Number mininum, Number maximum)
    {
        String pattern = getPattern(getNumberRangeMessage(), "number-range", field.getPage()
                .getLocale());
    
        return formatString(pattern, new Object[]
        { field.getDisplayName(), mininum, maximum });
    }

    protected String buildRangeMessage(IFormComponent field, Number minimum, Number maximum)
    {
        if (minimum != null && maximum != null)
            return buildNumberRangeMessage(field, minimum, maximum);
    
        if (maximum != null)
            return buildNumberTooLargeMessage(field, maximum);
    
        return buildNumberTooSmallMessage(field, minimum);
    }
    
    protected abstract String getDefaultScriptPath();
}