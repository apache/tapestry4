package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import java.util.*;
import java.text.*;

/**
 * 
 *  A {@link Form} component that can be used to
 *  create a text field that validates that the user
 *  has entered an integer value (if required) and that the
 *  value is within a particular range.
 *
 *  <p>When the form is submitted, the result is filtered:  If it fails
 *  validation, then no update (through the value binding) is performed.  Instead,
 *  an error flag is set.  Also, the invalid text is kept so that it can
 *  be the default value for the form element when the page is rendered.
 *
 *  <p>Doesn't work inside a {@link Foreach} ... it maintains a little bit
 *  of state (invalid text, error flag) 
 *  that will get confused if the component is re-used on the page.
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
 *    <td>int</td>
 *    <td>R / W</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The integer value to be updated. This value will only be
 *  read once per request cycle (don't use an IntegerField inside
 *  a {@link Foreach}).
 *
 *  <p>When the form is submitted, this parameter is only updated if the value
 *  is valid.</td>
 *	</tr>
 *
 *  <tr>
 *      <td>minimum</td>
 *      <td>int</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>The minimum value accepted for the field.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>maximum</td>
 *      <td>int</td>
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
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Applied to the underlying {@link TextField}.</td>
 *	</tr>
 *
 *  <tr>
 * 		<td>hidden</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Applied to the underlying {@link TextField}.</td>
 *	</tr>
 *
 *	<tr>
 *		<td>displayWidth</td>
 *		<td>int</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Applied to the underlying {@link TextField}.</tr>
 *
 *	<tr>
 *		<td>maximumLength</td>
 *		<td>int</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Applied to the underlying {@link TextField}.</td> </tr>
 *
 *	</table>
 *
 *  <p>May not contain a body, or have informal parameters.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class IntegerField
extends AbstractValidatingTextField
implements ILifecycle
{
    private IBinding valueBinding;

    private IBinding minimumBinding;
    private boolean staticMinimum;
    private int minimumValue;

    private IBinding maximumBinding;
    private boolean staticMaximum;
    private int maximumValue;

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

        staticMinimum = value.isStatic();
        if (staticMinimum)
            minimumValue = value.getInt();
    }

    public IBinding getMaximumBinding()
    {
        return maximumBinding;
    }

    public void setMaximumBinding(IBinding value)
    {
        maximumBinding = value;

        staticMaximum = value.isStatic();
        if (staticMaximum)
            maximumValue = value.getInt();
    }

    /**
     *  Reads the current value of the text parameter.
     *
     */

    protected String read()
    {
        // Get the value binding and convert it from an Integer to a String.

        return valueBinding.getString();
    }



    protected void update(String value)
    {
        int minimumLength = 0;
        int length;
        String errorMessage;
        int scalarValue;
        int minimum;
        int maximum;

        length = value.length();

        if (length == 0)
        {
            if (isRequired())
            {
                errorMessage =
                    getString("field-is-required", getDisplayName());

                notifyDelegate(ValidationConstraint.REQUIRED,
                    errorMessage);

                return;
            }

            // Null field, not required, don't update anything.

            return;
        }

        try
        {
            scalarValue = Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            errorMessage = getString("invalid-int-format", getDisplayName());
    
            notifyDelegate(ValidationConstraint.NUMBER_FORMAT,
                        errorMessage);

            return;
        }

        // Check it within the given range.

        if (minimumBinding != null)
        {
            if (staticMinimum)
                minimum = minimumValue;
            else
                minimum = minimumBinding.getInt();

            if (scalarValue < minimum)
            {
                errorMessage = getString("number-too-small", getDisplayName(), 
                    new Integer(minimum));

                notifyDelegate(ValidationConstraint.TOO_SMALL,
                        errorMessage);
            }
        }

        if (maximumBinding != null)
        {
            if (staticMaximum)
                maximum = maximumValue;
            else
                maximum = maximumBinding.getInt();

            if (scalarValue > maximum)
            {
                errorMessage = getString("number-too-large", getDisplayName(),
                    new Integer(maximum));

                notifyDelegate(ValidationConstraint.TOO_LARGE,
                        errorMessage);
            }
        }


        // Valid!  Update through our binding.

        valueBinding.setInt(scalarValue);

    }

}










