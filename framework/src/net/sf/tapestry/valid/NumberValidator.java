package net.sf.tapestry.valid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.AdaptorRegistry;

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
    private boolean _zeroIsNull;
    private Number _minimum;
    private Number _maximum;

    private String _scriptPath = "/net/sf/tapestry/valid/NumberValidator.script";

    private static AdaptorRegistry _numberAdaptors = new AdaptorRegistry();

    private static abstract class NumberAdaptor
    {
        /**
         *  Parses a non-empty {@link String} into the correct subclass of
         *  {@link Number}.
         *
         *  @throws NumberFormatException if the String can not be parsed.
         **/

        abstract public Number parse(String value);

        public int compare(Number left, Number right)
        {
            Comparable lc = (Comparable) left;

            return lc.compareTo(right);
        }
    }

    private static class ByteAdaptor extends NumberAdaptor
    {
        public Number parse(String value)
        {
            return new Byte(value);
        }
    }

    private static class ShortAdaptor extends NumberAdaptor
    {
        public Number parse(String value)
        {
            return new Short(value);
        }
    }

    private static class IntAdaptor extends NumberAdaptor
    {
        public Number parse(String value)
        {
            return new Integer(value);
        }
    }

    private static class LongAdaptor extends NumberAdaptor
    {
        public Number parse(String value)
        {
            return new Long(value);
        }
    }

    private static class FloatAdaptor extends NumberAdaptor
    {
        public Number parse(String value)
        {
            return new Float(value);
        }      
    }

    private static class DoubleAdaptor extends FloatAdaptor
    {
        public Number parse(String value)
        {
            return new Double(value);
        }
    }

    private static class BigDecimalAdaptor extends FloatAdaptor
    {
        public Number parse(String value)
        {
            return new BigDecimal(value);
        }
    }

    private static class BigIntegerAdaptor extends NumberAdaptor
    {
        public Number parse(String value)
        {
            return new BigInteger(value);
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
        _numberAdaptors.register(Byte.TYPE, byteAdaptor);
        _numberAdaptors.register(Short.class, shortAdaptor);
        _numberAdaptors.register(Short.TYPE, shortAdaptor);
        _numberAdaptors.register(Integer.class, intAdaptor);
        _numberAdaptors.register(Integer.TYPE, intAdaptor);
        _numberAdaptors.register(Long.class, longAdaptor);
        _numberAdaptors.register(Long.TYPE, longAdaptor);
        _numberAdaptors.register(Float.class, floatAdaptor);
        _numberAdaptors.register(Float.TYPE, floatAdaptor);
        _numberAdaptors.register(Double.class, doubleAdaptor);
        _numberAdaptors.register(Double.TYPE, doubleAdaptor);

        _numberAdaptors.register(BigDecimal.class, new BigDecimalAdaptor());
        _numberAdaptors.register(BigInteger.class, new BigIntegerAdaptor());
    }

    public String toString(IField field, Object value)
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

    private NumberAdaptor getAdaptor(IField field)
    {
        Class valueType = field.getValueType();

        NumberAdaptor result = (NumberAdaptor) _numberAdaptors.getAdaptor(valueType);

        if (result == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("NumberValidator.no-adaptor-for-field", field, valueType.getName()));

        return result;
    }

    public Object toObject(IField field, String value) throws ValidatorException
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
            String errorMessage =
                getString("invalid-numeric-format", field.getPage().getLocale(), field.getDisplayName());

            throw new ValidatorException(errorMessage, ValidationConstraint.NUMBER_FORMAT, value);
        }

        if (_minimum != null && adaptor.compare(result, _minimum) < 0)
        {
            String errorMessage =
                getString("number-too-small", field.getPage().getLocale(), field.getDisplayName(), _minimum);

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_SMALL, value);
        }

        if (_maximum != null && adaptor.compare(result, _maximum) > 0)
        {
            String errorMessage =
                getString("number-too-large", field.getPage().getLocale(), field.getDisplayName(), _maximum);

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_LARGE, value);
        }

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

    public void renderValidatorContribution(IField field, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (!isClientScriptingEnabled())
            return;

        if (!(isRequired() || _minimum != null || _maximum != null))
            return;

        Map symbols = new HashMap();

        Locale locale = field.getPage().getLocale();
        String displayName = field.getDisplayName();

        if (isRequired())
            symbols.put("requiredMessage", getString("field-is-required", locale, displayName));

        NumberAdaptor adaptor = getAdaptor(field);

        symbols.put("formatMessage", getString("invalid-numeric-format", locale, displayName));

        if (_minimum != null || _maximum != null)
        {

            symbols.put("rangeMessage", buildRangeMessage(displayName, locale));
        }

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }

    private String buildRangeMessage(String displayName, Locale locale)
    {
        if (_minimum != null && _maximum != null)
            return getString("number-range", locale, new Object[] { displayName, _minimum, _maximum });

        if (_minimum != null)
            return getString("number-too-small", locale, displayName, _minimum);

        return getString("number-too-large", locale, displayName, _maximum);
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

}