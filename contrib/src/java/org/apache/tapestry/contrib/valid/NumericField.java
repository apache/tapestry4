// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.contrib.valid;

import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.ValidField;

/**
 * Backwards compatible version of the 1.0.7 NumericField component. <table border=1>
 * <tr>
 * <td>Parameter</td>
 * <td>Type</td>
 * <td>Read / Write</td>
 * <td>Required</td>
 * <td>Default</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>value</td>
 * <td>{@link Number}</td>
 * <td>R / W</td>
 * <td>yes</td>
 * <td>&nbsp;</td>
 * <td>The value to be updated.
 * <p>
 * When the form is submitted, this parameter is only updated if the value is valid.
 * <p>
 * When rendering, a null value will render as the empty string. A value of zero will render
 * normally.
 * <p>
 * When the form is submitted, the type of the binding is used to determine what kind of object to
 * convert the string to.</td>
 * </tr>
 * <tr>
 * <td>minimum</td>
 * <td>{@link Number}</td>
 * <td>R</td>
 * <td>no</td>
 * <td>&nbsp;</td>
 * <td>The minimum value accepted for the field.</td>
 * </tr>
 * <tr>
 * <td>maximum</td>
 * <td>{@link Number}</td>
 * <td>R</td>
 * <td>no</td>
 * <td>&nbsp;</td>
 * <td>The maximum value accepted for the field.</td>
 * </tr>
 * <tr>
 * <td>required</td>
 * <td>boolean</td>
 * <td>R</td>
 * <td>no</td>
 * <td>false</td>
 * <td>If true, then a non-null value must be provided. If the field is not required, and a null
 * (all whitespace) value is supplied in the field, then the value parameter is <em>not</em>
 * updated.</td>
 * </tr>
 * <tr>
 * <td>displayName</td>
 * <td>String</td>
 * <td>R</td>
 * <td>yes</td>
 * <td>&nbsp;</td>
 * <td>A textual name for the field that is used when formulating error messages.</td>
 * </tr>
 * <tr>
 * <td>type</td>
 * <td>String</td>
 * <td>R</td>
 * <td>yes</td>
 * <td>&nbsp;</td>
 * <td>The class name used to convert the value entered. See
 * {@link NumberValidator#setValueType(String)}</td>
 * </tr>
 * </table>
 * <p>
 * May not contain a body. May have informal parameters.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.8
 * @see ValidField
 */

public abstract class NumericField extends ValidField
{
    public abstract Number getMinimum();

    public abstract Number getMaximum();

    public abstract boolean isRequired();

    public abstract String getType();

    /**
     * Overrides {@link ValidField#getValidator()}to construct a validator on the fly.
     */

    public IValidator getValidator()
    {
        NumberValidator validator = new NumberValidator();

        if (isParameterBound("minimum"))
            validator.setMinimum(getMinimum());

        if (isParameterBound("maximum"))
            validator.setMaximum(getMaximum());

        if (isParameterBound("required"))
            validator.setRequired(isRequired());

        if (isParameterBound("type"))
            validator.setValueType(getType());

        return validator;
    }
}