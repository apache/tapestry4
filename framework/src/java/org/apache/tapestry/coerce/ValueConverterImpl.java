// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.coerce;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Defense;
import org.apache.hivemind.util.ConstructorUtils;

/**
 * Implementation of {@link org.apache.tapestry.coerce.ValueConverter}. Selects an appropriate type
 * converter and delegates to it.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ValueConverterImpl implements ValueConverter
{
    private Map _converterMap = new HashMap();

    /** List of {@link org.apache.tapestry.coerce.TypeConverterContribution}. */

    public List _contributions;

    private Map _primitiveToWrapper = new HashMap();

    private Map _wrapperToPrimitive = new HashMap();

    {
        store(boolean.class, Boolean.class);
        store(byte.class, Byte.class);
        store(short.class, Short.class);
        store(char.class, Character.class);
        store(int.class, Integer.class);
        store(long.class, Long.class);
        store(float.class, Float.class);
        store(double.class, Double.class);
    }

    private void store(Class primitive, Class wrapper)
    {
        _primitiveToWrapper.put(primitive, wrapper);

        _wrapperToPrimitive.put(wrapper, primitive);
    }

    public void initializeService()
    {
        Iterator i = _contributions.iterator();
        while (i.hasNext())
        {
            TypeConverterContribution c = (TypeConverterContribution) i.next();

            _converterMap.put(c.getSubjectClass(), c.getConverter());
        }
    }

    public Object coerceValue(Object value, Class desiredType)
    {
        Defense.notNull(desiredType, "desiredType");

        Class effectiveType = convertType(desiredType);

        // Already the correct type? Go no further!

        if (value != null && effectiveType.isAssignableFrom(value.getClass()))
            return value;
        
        Object result = convertNumberToNumber(value, effectiveType);

        if (result != null)
            return result;

        result = convertUsingPropertyEditor(value, effectiveType);

        if (result != null)
            return result;

        TypeConverter converter = (TypeConverter) _converterMap.get(effectiveType);

        // null value and no converter for the given type? Just return null.

        if (value == null && converter == null)
            return null;

        if (converter == null)
            throw new ApplicationRuntimeException(CoerceMessages.noConverter(effectiveType));

        return converter.convertValue(value);
    }

    /**
     * Attempts to use {@link java.beans.PropertyEditor}to perform a conversion from a string to a
     * numeric type. Returns null if no property editor can be found.
     * 
     * @param value
     *            The value to convert
     * @param targetType
     *            The type to convert to (must be a wrapper type, not a primitive type)
     */

    private Number convertUsingPropertyEditor(Object value, Class targetType)
    {
        // Convert from wrapper type back to primitive type, because
        // PropertyEditorManager expects primitive types not
        // wrapper types.

        if (value == null || value.getClass() != String.class
                || !Number.class.isAssignableFrom(targetType))
            return null;

        Class primitiveType = (Class) _wrapperToPrimitive.get(targetType);

        // Note a primitive type.

        if (primitiveType == null)
            return null;

        // Looks like a conversion from String to Number, let's see.

        PropertyEditor editor = PropertyEditorManager.findEditor(primitiveType);

        // This should not happen, since we've filtered down to just the
        // primitive types that do have property editors.

        if (editor == null)
            return null;

        String text = (String) value;

        try
        {
            editor.setAsText(text);

            return (Number) editor.getValue();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(CoerceMessages.stringToNumberConversionError(
                    text,
                    targetType,
                    ex), ex);
        }

    }

    private Number convertNumberToNumber(Object value, Class targetType)
    {
        if (value == null || !Number.class.isAssignableFrom(value.getClass())
                || !Number.class.isAssignableFrom(targetType))
            return null;

        String valueAsString = value.toString();

        return (Number) ConstructorUtils.invokeConstructor(targetType, new Object[]
        { valueAsString });
    }

    private Class convertType(Class possiblePrimitiveType)
    {
        Class wrapperType = (Class) _primitiveToWrapper.get(possiblePrimitiveType);

        return wrapperType == null ? possiblePrimitiveType : wrapperType;
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }
}