/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship
 *
 * http://sourceforge.net/projects/tapestry
 * mailto:hship@sourceforge.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.valid;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.util.*;
import java.util.*;
import java.text.*;
import java.math.*;

/**
 *
 *  A {@link Form} component that can be used to
 *  create a text field that validates that the user
 *  has entered a numeric value (if required) and that the
 *  value is within a particular range.
 *
 *  <p>When the form is submitted, the result is filtered:  If it fails
 *  validation, then no update (through the value binding) is performed.  Instead,
 *  an error flag is set.  Also, the invalid text is kept so that it can
 *  be the default value for the form element when the page is rendered.
 *
 *  <p>This component doesn't work properly inside a {@link Foreach} ...
 *  it maintains a little bit of state (invalid text, error flag)
 *  that will get confused if the component is re-used on the page.
 *
 * <p>This single component can be used with <em>any</em> numeric type, it can
 * convert a {@link String} to any of the subclasses of {@link Number}.  It determines
 *  the class to convert the String to from the bnding.  If the binding is
 *  linked to a property of type double (or {@link Double}), then the string
 *  is converted to {@link Double} before being assigned through the binding.
 *
 *  <p>The earlier component, {@link IntegerField}, exists for backwards compatibility.
 *  It also is a bit more efficient.
 *
 * <table border=1>
 * <tr>
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td>
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>value</td>
 *    <td>{@link Number}</td>
 *    <td>R / W</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The value to be updated. This value will only be
 *  read once per request cycle (don't use a NumericField inside
 *  a {@link Foreach}).
 *
 *  <p>When the form is submitted, this parameter is only updated if the value
 *  is valid.
 *
 *  <p>When rendering, a null value will render as the empty string.  A value
 *  of zero will render normally.
 *
 *  <p>When the form is submitted, the {@link IBinding#getType() type} of the binding
 *  is used to determine what kind of object to convert the string to.
 *
 * </td>
 *	</tr>
 *
 *  <tr>
 *      <td>minimum</td>
 *      <td>{@link Number}</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>The minimum value accepted for the field.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>maximum</td>
 *      <td>{@link Number}</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>The maximum value accepted for the field.</td>
 * </tr>
 *
 *  <tr>
 *      <td>required</td>
 *      <td>boolean</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>false</td>
 *      <td>If true, then a non-null value must be provided. If the field is not
 * required, and a null (all whitespace) value is supplied in the field, then the
 * value parameter is <em>not</em> updated.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>displayName</td>
 *      <td>String</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>A textual name for the field that is used when formulating error messages.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *      <td>delegate</td>
 *      <td>{@link IValidationDelegate}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Object used to assist in error reporting.</td>
 *  </tr>
 *
 *	</table>
 *
 *  <p>May not contain a body.  May have informal parameters,
 *  which are applied to the underlying {@link TextField}.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class NumericField
extends AbstractValidatingTextField
{
    private IBinding valueBinding;
    private IBinding minimumBinding;
    private IBinding maximumBinding;

	/**
	 *  Private interface used to handle parsing Strings to different
	 *  numeric types.
	 *
	 */
	
	private interface NumberAdaptor
	{
		/**
		 *  Parses a non-empty {@link String} into the correct subclass of
		 *  {@link Number}.
		 *
		 *  @throws NumberFormatException if the String can not be parsed.
		 */
		
		public Number parse(String value);
		
		/**
		 *  Compares two instances of the adapted class (the subclass of
		 * {@link Number} handled by the adaptor).  Casts the left and
		 * right parameters to the correct values and invokes
		 * {@link Comparable#compareTo(Object)}.
		 *
		 */
		
		public int compare(Number left, Number right);
	}

	private static Decorator numberAdaptors = new Decorator();
	
	private static abstract class AbstractAdaptor
		implements NumberAdaptor
	{
		public int compare(Number left, Number right)
		{
			Comparable lc = (Comparable)left;
			
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
	
	static
	{
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
		numberAdaptors.register(Float.class, floatAdaptor);
		numberAdaptors.register(Float.TYPE, floatAdaptor);
		numberAdaptors.register(Double.class, doubleAdaptor);
		numberAdaptors.register(Double.TYPE, doubleAdaptor);
		
		numberAdaptors.register(BigDecimal.class, new BigDecimalAdaptor());
		numberAdaptors.register(BigInteger.class, new BigIntegerAdaptor());
	}
	
    public IBinding getValueBinding()
    {
        return valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        valueBinding = value;
    }

    public IBinding getMinimumBinding()
    {
        return minimumBinding;
    }

    public void setMinimumBinding(IBinding value)
    {
        minimumBinding = value;
    }

    public IBinding getMaximumBinding()
    {
        return maximumBinding;
    }

    public void setMaximumBinding(IBinding value)
    {
        maximumBinding = value;
    }

    /**
     *  Reads the current value of the text parameter.
     *
     */

    protected String read()
    {
		Number value = (Number)valueBinding.getObject("value", Number.class);
		
		if (value == null)
			return "";

		return value.toString();
    }


	/**
	 *  Takes the String and "{@link IBinding#getType() sniffs}"
	 *  the value binding to see its type.  It then converts the string
	 *  to that type, does range checks and assigns the
	 *  value back through the value binding.
	 *
	 */

    protected void update(String value)
    {
		Number objectValue = null;
		Number minimum = null;
		Number maximum = null;
		NumberAdaptor adaptor = null;
		
        if (value.length() == 0)
        {
            if (isRequired())
            {
                String errorMessage =
                    getString("field-is-required", getDisplayName());

                notifyDelegate(ValidationConstraint.REQUIRED,
                    errorMessage);

                return;
            }

            // Null field, not required, don't update anything.

            return;
        }
		
		Class valueClass = valueBinding.getType();
		
        try
        {
			adaptor = (NumberAdaptor)numberAdaptors.getAdaptor(valueClass);
			
			if (adaptor == null)
				throw new ApplicationRuntimeException(
					"No adaptor to parse String to " + valueClass.getName() + ".");
			
			objectValue = adaptor.parse(value);
        }
        catch (NumberFormatException ex)
        {
            String errorMessage = getString("invalid-numeric-format", getDisplayName());
    
            notifyDelegate(ValidationConstraint.NUMBER_FORMAT,
                        errorMessage);

            return;
        }

        // Check if within the given range.

		if (minimumBinding != null)
			minimum = (Number)minimumBinding.getObject("minimum", Number.class);
		
		if (minimum != null)
		{
			
			if (adaptor.compare(objectValue, minimum) < 0)
            {
                String errorMessage = getString("number-too-small", getDisplayName(),
                    minimum);

                notifyDelegate(ValidationConstraint.TOO_SMALL,
                        errorMessage);
				
				return;
            }
        }
		
		if (maximumBinding != null)
			maximum = (Number)maximumBinding.getObject("maximum", Number.class);
		
		if (maximum != null)
		{
			if (adaptor.compare(objectValue, maximum) > 0)
			{
				String errorMessage = getString("number-too-large", getDisplayName(),
						maximum);
				
				notifyDelegate(ValidationConstraint.TOO_LARGE, errorMessage);
				return;
			}
		}
		
		// Survived ALL the tests, safe to assign!
		
		valueBinding.setObject(objectValue);
    }

}










