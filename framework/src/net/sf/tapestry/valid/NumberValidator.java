//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.valid;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.util.Decorator;

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
    private boolean zeroIsNull;
    private Number minimum;
    private Number maximum;

    /**
     *  Private interface used to handle parsing Strings to different
     *  numeric types.
     *
     **/

    private interface NumberAdaptor
    {
        /**
         *  Parses a non-empty {@link String} into the correct subclass of
         *  {@link Number}.
         *
         *  @throws NumberFormatException if the String can not be parsed.
         **/

        public Number parse(String value);

        /**
         *  Compares two instances of the adapted class (the subclass of
         *  {@link Number} handled by the adaptor).  Casts the left and
         *  right parameters to the correct values and invokes
         *  {@link Comparable#compareTo(Object)}.
         *
         **/

        public int compare(Number left, Number right);
    }

    private static Decorator numberAdaptors = new Decorator();

    private static abstract class AbstractAdaptor implements NumberAdaptor
    {
        public int compare(Number left, Number right)
        {
            Comparable lc = (Comparable) left;

            return lc.compareTo(right);
        }
    }

    private static class ByteAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new Byte(value);
        }
    }

    private static class ShortAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new Short(value);
        }
    }

    private static class IntAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new Integer(value);
        }
    }

    private static class LongAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new Long(value);
        }
    }

    private static class FloatAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new Float(value);
        }
    }

    private static class DoubleAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new Double(value);
        }
    }

    private static class BigDecimalAdaptor extends AbstractAdaptor
    {
        public Number parse(String value)
        {
            return new BigDecimal(value);
        }
    }

    private static class BigIntegerAdaptor extends AbstractAdaptor
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

        numberAdaptors.register(Byte.class, byteAdaptor);
        numberAdaptors.register(Byte.TYPE, byteAdaptor);
        numberAdaptors.register(Short.class, shortAdaptor);
        numberAdaptors.register(Short.TYPE, shortAdaptor);
        numberAdaptors.register(Integer.class, intAdaptor);
        numberAdaptors.register(Integer.TYPE, intAdaptor);
        numberAdaptors.register(Long.class, longAdaptor);
        numberAdaptors.register(Long.TYPE, longAdaptor);
        numberAdaptors.register(Float.class, floatAdaptor);
        numberAdaptors.register(Float.TYPE, floatAdaptor);
        numberAdaptors.register(Double.class, doubleAdaptor);
        numberAdaptors.register(Double.TYPE, doubleAdaptor);

        numberAdaptors.register(BigDecimal.class, new BigDecimalAdaptor());
        numberAdaptors.register(BigInteger.class, new BigIntegerAdaptor());
    }

    public String toString(IField field, Object value)
    {
        if (value == null)
            return null;

        if (zeroIsNull)
        {
            Number number = (Number) value;

            if (number.doubleValue() == 0.0)
                return null;
        }

        return value.toString();
    }

    public Object toObject(IField field, String value) throws ValidatorException
    {
        if (checkRequired(field, value))
            return null;

        Class valueType = field.getValueType();

        NumberAdaptor adaptor = null;
        Number result = null;

        try
        {
            adaptor = (NumberAdaptor) numberAdaptors.getAdaptor(valueType);

            if (adaptor == null)
                throw new ApplicationRuntimeException(
                    "No adaptor to parse String to " + valueType.getName() + ".");

            result = adaptor.parse(value);
        }
        catch (NumberFormatException ex)
        {
            String errorMessage =
                getString("invalid-numeric-format", field.getPage().getLocale(), field.getDisplayName());

            throw new ValidatorException(
                errorMessage,
                ValidationConstraint.NUMBER_FORMAT,
                value);
        }

        if (minimum != null && adaptor.compare(result, minimum) < 0)
        {
            String errorMessage =
                getString("number-too-small", field.getPage().getLocale(), field.getDisplayName(), minimum);

            throw new ValidatorException(
                errorMessage,
                ValidationConstraint.TOO_SMALL,
                value);
        }

        if (maximum != null && adaptor.compare(result, maximum) > 0)
        {
            String errorMessage =
                getString("number-too-large", field.getPage().getLocale(), field.getDisplayName(), maximum);

            throw new ValidatorException(
                errorMessage,
                ValidationConstraint.TOO_LARGE,
                value);
        }

        return result;
    }

    public Number getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Number maximum)
    {
        this.maximum = maximum;
    }
    public Number getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Number minimum)
    {
        this.minimum = minimum;
    }

    /**
     *  If true, then when rendering, a zero is treated as a non-value, and null is returned.
     *  If false, the default, then zero is rendered as zero.
     * 
     **/

    public boolean getZeroIsNull()
    {
        return zeroIsNull;
    }

    public void setZeroIsNull(boolean zeroIsNull)
    {
        this.zeroIsNull = zeroIsNull;
    }

}