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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;

/**
 * A type-specific replacement for {@link org.apache.tapestry.valid.NumberValidator}.
 * 
 * @author Howard M. Lewis Ship
 */
public class IntValidator extends AbstractNumericValidator
{
    private boolean _minimumSet;

    private int _minimum;

    private boolean _maximumSet;

    private int _maximum;

    public IntValidator()
    {
    }

    public IntValidator(String initializer)
    {
        super(initializer);
    }

    public String toString(IFormComponent field, Object value)
    {
        if (value == null)
            return null;

        // Be generous; maybe it isn't quite an int, so
        // treat it as a Number

        Number number = (Number) value;

        if (getZeroIsNull() && number.intValue() == 0)
            return null;

        return number.toString();
    }

    public Object toObject(IFormComponent field, String value) throws ValidatorException
    {
        if (checkRequired(field, value))
            return null;

        try
        {
            int intValue = Integer.parseInt(value);

            if (_minimumSet && intValue < _minimum)
                throw new ValidatorException(buildNumberTooSmallMessage(
                        field,
                        new Integer(_minimum)), ValidationConstraint.TOO_SMALL);

            if (_maximumSet && intValue > _maximum)
                throw new ValidatorException(buildNumberTooLargeMessage(
                        field,
                        new Integer(_maximum)), ValidationConstraint.TOO_LARGE);

            return new Integer(intValue);
        }
        catch (NumberFormatException ex)
        {
            throw new ValidatorException(buildInvalidNumericFormatMessage(field),
                    ValidationConstraint.NUMBER_FORMAT);
        }
    }

    public void renderValidatorContribution(IFormComponent field, IMarkupWriter writer,
            IRequestCycle cycle)
    {
        if (!isClientScriptingEnabled())
            return;

        if (!(isRequired() || _minimumSet || _maximumSet))
            return;

        Map symbols = buildSymbols(field);

        processValidatorScript(getScriptPath(), cycle, field, symbols);
    }

    Map buildSymbols(IFormComponent field)
    {
        Map symbols = new HashMap();

        if (isRequired())
            symbols.put("requiredMessage", buildRequiredMessage(field));

        symbols.put("formatMessage", buildInvalidIntegerFormatMessage(field));

        if (_minimumSet || _maximumSet)
        {
            Number minimum = _minimumSet ? new Integer(_minimum) : null;
            Number maximum = _maximumSet ? new Integer(_maximum) : null;

            symbols.put("minimum", minimum);
            symbols.put("maximum", maximum);

            symbols.put("rangeMessage", buildRangeMessage(field, minimum, maximum));
        }

        return symbols;
    }

    public void setMaximum(int maximum)
    {
        _maximum = maximum;
        _maximumSet = true;
    }

    public void setMinimum(int minimum)
    {
        _minimum = minimum;
        _minimumSet = true;
    }

    protected String getDefaultScriptPath()
    {
        return "/org/apache/tapestry/valid/IntegerValidator.script";
    }
}