//  Copyright 2004 The Apache Software Foundation
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.util.AdaptorRegistry;

/**
 *  Simple validation for standard number classes.  This is probably insufficient
 *  for anything tricky and application specific, such as parsing currency.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class NumberValidator extends BaseValidator
{
    private static final Map TYPES = new HashMap();

    static {
        TYPES.put("boolean", boolean.class);
        TYPES.put("Boolean", Boolean.class);
        TYPES.put("java.lang.Boolean", Boolean.class);
        TYPES.put("char", char.class);
        TYPES.put("Character", Character.class);
        TYPES.put("java.lang.Character", Character.class);
        TYPES.put("short", short.class);
        TYPES.put("Short", Short.class);
        TYPES.put("java.lang.Short", Short.class);
        TYPES.put("int", int.class);
        TYPES.put("Integer", Integer.class);
        TYPES.put("java.lang.Integer", Integer.class);
        TYPES.put("long", long.class);
        TYPES.put("Long", Long.class);
        TYPES.put("java.lang.Long", Long.class);
        TYPES.put("float", float.class);
        TYPES.put("Float", Float.class);
        TYPES.put("java.lang.Float", Float.class);
        TYPES.put("byte", byte.class);
        TYPES.put("Byte", Byte.class);
        TYPES.put("java.lang.Byte", Byte.class);
        TYPES.put("double", double.class);
        TYPES.put("Double", Double.class);
        TYPES.put("java.lang.Double", Double.class);
        TYPES.put("java.math.BigInteger", BigInteger.class);
        TYPES.put("java.math.BigDecimal", BigDecimal.class);
    }

    private static final Set INT_TYPES = new HashSet();

    private Class _valueTypeClass = int.class;

    private boolean _zeroIsNull;
    private Number _minimum;
    private Number _maximum;

    private String _scriptPath = "/org/apache/tapestry/valid/NumberValidator.script";

    private String _invalidNumericFormatMessage;
    private String _invalidIntegerFormatMessage;
    private String _numberTooSmallMessage;
    private String _numberTooLargeMessage;
    private String _numberRangeMessage;

    private static AdaptorRegistry _numberAdaptors = new AdaptorRegistry();

    public final static int NUMBER_TYPE_INTEGER = 0;
	public final static int NUMBER_TYPE_REAL = 1;

	/**
	 * This class is not meant for use outside of NumberValidator; it
	 * is public only to fascilitate some unit testing.
	 * 
	 */
    public static abstract class NumberAdaptor
    {
        /**
         *  Parses a non-empty {@link String} into the correct subclass of
         *  {@link Number}.
         *
         *  @throws NumberFormatException if the String can not be parsed.
         **/

        abstract public Number parse(String value);

        /**
         *  Indicates the type of the number represented -- integer or real.
         *  The information is used to build the client-side validator.  
         *  This method could return a boolean, but returns an int to allow
         *  future extensions of the validator.
         *   
         *  @return one of the predefined number types
         **/
        abstract public int getNumberType();

        public int compare(Number left, Number right)
        {
            if (!left.getClass().equals(right.getClass()))
                right = coerce(right);

            Comparable lc = (Comparable) left;

            return lc.compareTo(right);
        }

        /**
         * Invoked when comparing two Numbers of different types.
         * The number is cooerced from its ordinary type to 
         * the correct type for comparison.
         * 
         * @since 3.0
         */
        protected abstract Number coerce(Number number);
    }

    private static abstract class IntegerNumberAdaptor extends NumberAdaptor
    {
        public int getNumberType()
        {
            return NUMBER_TYPE_INTEGER;
        }
    }

    private static abstract class RealNumberAdaptor extends NumberAdaptor
    {
        public int getNumberType()
        {
            return NUMBER_TYPE_REAL;
        }
    }

    private static class ByteAdaptor extends IntegerNumberAdaptor
    {
        public Number parse(String value)
        {
            return new Byte(value);
        }

        protected Number coerce(Number number)
        {
            return new Byte(number.byteValue());
        }
    }

    private static class ShortAdaptor extends IntegerNumberAdaptor
    {
        public Number parse(String value)
        {
            return new Short(value);
        }

        protected Number coerce(Number number)
        {
            return new Short(number.shortValue());
        }
    }

    private static class IntAdaptor extends IntegerNumberAdaptor
    {
        public Number parse(String value)
        {
            return new Integer(value);
        }

        protected Number coerce(Number number)
        {
            return new Integer(number.intValue());
        }
    }

    private static class LongAdaptor extends IntegerNumberAdaptor
    {
        public Number parse(String value)
        {
            return new Long(value);
        }

        protected Number coerce(Number number)
        {
            return new Long(number.longValue());
        }
    }

    private static class FloatAdaptor extends RealNumberAdaptor
    {
        public Number parse(String value)
        {
            return new Float(value);
        }

        protected Number coerce(Number number)
        {
            return new Float(number.floatValue());
        }
    }

    private static class DoubleAdaptor extends RealNumberAdaptor
    {
        public Number parse(String value)
        {
            return new Double(value);
        }

        protected Number coerce(Number number)
        {
            return new Double(number.doubleValue());
        }
    }

    private static class BigDecimalAdaptor extends RealNumberAdaptor
    {
        public Number parse(String value)
        {
            return new BigDecimal(value);
        }

        protected Number coerce(Number number)
        {
            return new BigDecimal(number.doubleValue());
        }
    }

    private static class BigIntegerAdaptor extends IntegerNumberAdaptor
    {
        public Number parse(String value)
        {
            return new BigInteger(value);
        }

        protected Number coerce(Number number)
        {
            return new BigInteger(number.toString());
        }
    }

    static {
        NumberAdaptor byteAdaptor = new ByteAdaptor();
        NumberAdaptor shortAdaptor = new ShortAdaptor();
        NumberAdaptor intAdaptor = new IntAdaptor();
        NumberAdaptor longAdaptor = new LongAdaptor();
        NumberAdaptor floatAdaptor = new FloatAdaptor();
        NumberAdaptor doubleAdaptor = new DoubleAdaptor();

        _numberAdaptors.register(Byte.class, byteAdaptor);
        _numberAdaptors.register(byte.class, byteAdaptor);
        _numberAdaptors.register(Short.class, shortAdaptor);
        _numberAdaptors.register(short.class, shortAdaptor);
        _numberAdaptors.register(Integer.class, intAdaptor);
        _numberAdaptors.register(int.class, intAdaptor);
        _numberAdaptors.register(Long.class, longAdaptor);
        _numberAdaptors.register(long.class, longAdaptor);
        _numberAdaptors.register(Float.class, floatAdaptor);
        _numberAdaptors.register(float.class, floatAdaptor);
        _numberAdaptors.register(Double.class, doubleAdaptor);
        _numberAdaptors.register(double.class, doubleAdaptor);

        _numberAdaptors.register(BigDecimal.class, new BigDecimalAdaptor());
        _numberAdaptors.register(BigInteger.class, new BigIntegerAdaptor());
    }

    public String toString(IFormComponent field, Object value)
    {
        if (value == null)
            return null;

        if (_zeroIsNull)
        {
            Number number = (Number) value;

            if (number.doubleValue() == 0.0)
                return null;
        }

        return value.toString();
    }

    private NumberAdaptor getAdaptor(IFormComponent field)
    {
        NumberAdaptor result = getAdaptor(_valueTypeClass);

        if (result == null)
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "NumberValidator.no-adaptor-for-field",
                    field,
                    _valueTypeClass.getName()));

        return result;
    }

	/**
	 * Returns an adaptor for the given type.
	 * 
	 * <p>
	 * Note: this method exists only for testing purposes. It is not meant to
	 * be invoked by user code and is subject to change at any time.
	 * 
	 * @param type the type (a Number subclass) for which to return an adaptor
	 * @return the adaptor, or null if no such adaptor may be found
	 * @since 3.0
	 */
    public static NumberAdaptor getAdaptor(Class type)
    {
        return (NumberAdaptor) _numberAdaptors.getAdaptor(type);
    }

    public Object toObject(IFormComponent field, String value) throws ValidatorException
    {
        if (checkRequired(field, value))
            return null;

        NumberAdaptor adaptor = getAdaptor(field);
        Number result = null;

        try
        {
            result = adaptor.parse(value);
        }
        catch (NumberFormatException ex)
        {
            throw new ValidatorException(
                buildInvalidNumericFormatMessage(field),
                ValidationConstraint.NUMBER_FORMAT);
        }

        if (_minimum != null && adaptor.compare(result, _minimum) < 0)
            throw new ValidatorException(
                buildNumberTooSmallMessage(field),
                ValidationConstraint.TOO_SMALL);

        if (_maximum != null && adaptor.compare(result, _maximum) > 0)
            throw new ValidatorException(
                buildNumberTooLargeMessage(field),
                ValidationConstraint.TOO_LARGE);

        return result;
    }

    public Number getMaximum()
    {
        return _maximum;
    }

    public boolean getHasMaximum()
    {
        return _maximum != null;
    }

    public void setMaximum(Number maximum)
    {
        _maximum = maximum;
    }

    public Number getMinimum()
    {
        return _minimum;
    }

    public boolean getHasMinimum()
    {
        return _minimum != null;
    }

    public void setMinimum(Number minimum)
    {
        _minimum = minimum;
    }

    /**
     *  If true, then when rendering, a zero is treated as a non-value, and null is returned.
     *  If false, the default, then zero is rendered as zero.
     * 
     **/

    public boolean getZeroIsNull()
    {
        return _zeroIsNull;
    }

    public void setZeroIsNull(boolean zeroIsNull)
    {
        _zeroIsNull = zeroIsNull;
    }

    /** 
     * 
     *  @since 2.2
     * 
     **/

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (!isClientScriptingEnabled())
            return;

        if (!(isRequired() || _minimum != null || _maximum != null))
            return;

        Map symbols = new HashMap();

        if (isRequired())
            symbols.put("requiredMessage", buildRequiredMessage(field));

        if (isIntegerNumber())
            symbols.put("formatMessage", buildInvalidIntegerFormatMessage(field));
        else
            symbols.put("formatMessage", buildInvalidNumericFormatMessage(field));

        if (_minimum != null || _maximum != null)
            symbols.put("rangeMessage", buildRangeMessage(field));

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }

    private String buildRangeMessage(IFormComponent field)
    {
        if (_minimum != null && _maximum != null)
            return buildNumberRangeMessage(field);

        if (_maximum != null)
            return buildNumberTooLargeMessage(field);

        return buildNumberTooSmallMessage(field);
    }

    /**
     *  @since 2.2
     * 
     **/

    public String getScriptPath()
    {
        return _scriptPath;
    }

    /**
     *  Allows a developer to use the existing validation logic with a different client-side
     *  script.  This is often sufficient to allow application-specific error presentation
     *  (perhaps by using DHTML to update the content of a &lt;span&gt; tag, or to use
     *  a more sophisticated pop-up window than <code>window.alert()</code>).
     * 
     *  @since 2.2
     * 
     **/

    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

    /** Sets the value type from a string type name.  The name may be
     *  a scalar numeric type, a fully qualified class name, or the name
     *  of a numeric wrapper type from java.lang (with the package name omitted).
     * 
     * @since 3.0 
     * 
     **/

    public void setValueType(String typeName)
    {
        Class typeClass = (Class) TYPES.get(typeName);

        if (typeClass == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("NumberValidator.unknown-type", typeName));

        _valueTypeClass = typeClass;
    }

    /** @since 3.0 **/

    public void setValueTypeClass(Class valueTypeClass)
    {
        _valueTypeClass = valueTypeClass;
    }

    /** 
     *  
     *  Returns the value type to convert strings back into.  The default is int.
     * 
     *  @since 3.0 
     * 
     **/

    public Class getValueTypeClass()
    {
        return _valueTypeClass;
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
     * Overrides the <code>invalid-numeric-format</code> bundle key.
     * Parameter {0} is the display name of the field.
     * 
     * @since 3.0
     */

    public void setInvalidNumericFormatMessage(String string)
    {
        _invalidNumericFormatMessage = string;
    }

    /** 
     * Overrides the <code>invalid-int-format</code> bundle key.
     * Parameter {0} is the display name of the field.
     * 
     * @since 3.0
     */

    public void setInvalidIntegerFormatMessage(String string)
    {
        _invalidIntegerFormatMessage = string;
    }

    /** @since 3.0 */

    protected String buildInvalidNumericFormatMessage(IFormComponent field)
    {
        String pattern =
            getPattern(
                getInvalidNumericFormatMessage(),
                "invalid-numeric-format",
                field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName());
    }

    /** @since 3.0 */

    protected String buildInvalidIntegerFormatMessage(IFormComponent field)
    {
        String pattern =
            getPattern(
                getInvalidIntegerFormatMessage(),
                "invalid-int-format",
                field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName());
    }

    /** 
     * Overrides the <code>number-range</code> bundle key.
     * Parameter [0} is the display name of the field.
     * Parameter {1} is the minimum value.
     * Parameter {2} is the maximum value.
     * 
     * @since 3.0
     */

    public void setNumberRangeMessage(String string)
    {
        _numberRangeMessage = string;
    }

    protected String buildNumberRangeMessage(IFormComponent field)
    {
        String pattern =
            getPattern(_numberRangeMessage, "number-range", field.getPage().getLocale());

        return formatString(pattern, new Object[] { field.getDisplayName(), _minimum, _maximum });
    }

    /**
     *  Overrides the <code>number-too-large</code> bundle key.
     *  Parameter {0} is the display name of the field.
     *  Parameter {1} is the maximum allowed value.
     *  
     *  @since 3.0
     */

    public void setNumberTooLargeMessage(String string)
    {
        _numberTooLargeMessage = string;
    }

    /** @since 3.0 */

    protected String buildNumberTooLargeMessage(IFormComponent field)
    {
        String pattern =
            getPattern(_numberTooLargeMessage, "number-too-large", field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName(), _maximum);
    }

    /**
     *  Overrides the <code>number-too-small</code> bundle key.
     *  Parameter {0} is the display name of the field.
     *  Parameter {1} is the minimum allowed value.
     * 
     *  @since 3.0
     * 
     */

    public void setNumberTooSmallMessage(String string)
    {
        _numberTooSmallMessage = string;
    }

    /** @since 3.0 */

    protected String buildNumberTooSmallMessage(IFormComponent field)
    {
        String pattern =
            getPattern(_numberTooSmallMessage, "number-too-small", field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName(), _minimum);
    }

    /** @since 3.0 */

    public boolean isIntegerNumber()
    {
        NumberAdaptor result = (NumberAdaptor) _numberAdaptors.getAdaptor(_valueTypeClass);
        if (result == null)
            return false;

        return result.getNumberType() == NUMBER_TYPE_INTEGER;
    }
}