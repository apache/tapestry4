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

package org.apache.tapestry.binding;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.Tapestry;

/**
 *  Base class for {@link IBinding} implementations.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class AbstractBinding implements IBinding
{
    /** @since 3.0 **/

    private ILocation _location;

    /**
     *  A mapping from primitive types to wrapper types.
     * 
     **/

    private static final Map PRIMITIVE_TYPES = new HashMap();

    static {
        PRIMITIVE_TYPES.put(boolean.class, Boolean.class);
        PRIMITIVE_TYPES.put(byte.class, Byte.class);
        PRIMITIVE_TYPES.put(char.class, Character.class);
        PRIMITIVE_TYPES.put(short.class, Short.class);
        PRIMITIVE_TYPES.put(int.class, Integer.class);
        PRIMITIVE_TYPES.put(long.class, Long.class);
        PRIMITIVE_TYPES.put(float.class, Float.class);
        PRIMITIVE_TYPES.put(double.class, Double.class);
    }

    /** @since 3.0 **/

    protected AbstractBinding(ILocation location)
    {
        _location = location;
    }

    public ILocation getLocation()
    {
        return _location;
    }

    /**
     *  Cooerces the raw value into a true or false, according to the
     *  rules set by {@link Tapestry#evaluateBoolean(Object)}.
     *
     **/

    public boolean getBoolean()
    {
        return Tapestry.evaluateBoolean(getObject());
    }

    public int getInt()
    {
        Object raw;

        raw = getObject();
        if (raw == null)
            throw Tapestry.createNullBindingException(this);

        if (raw instanceof Number)
        {
            return ((Number) raw).intValue();
        }

        if (raw instanceof Boolean)
        {
            return ((Boolean) raw).booleanValue() ? 1 : 0;
        }

        // Save parsing for last.  This may also throw a number format exception.

        return Integer.parseInt((String) raw);
    }

    public double getDouble()
    {
        Object raw;

        raw = getObject();
        if (raw == null)
            throw Tapestry.createNullBindingException(this);

        if (raw instanceof Number)
        {
            return ((Number) raw).doubleValue();
        }

        if (raw instanceof Boolean)
        {
            return ((Boolean) raw).booleanValue() ? 1 : 0;
        }

        // Save parsing for last.  This may also throw a number format exception.

        return Double.parseDouble((String) raw);
    }

    /**
     *  Gets the value for the binding.  If null, returns null,
     *  otherwise, returns the String (<code>toString()</code>) version of
     *  the value.
     *
     **/

    public String getString()
    {
        Object value;

        value = getObject();
        if (value == null)
            return null;

        return value.toString();
    }

    /**
     *  @throws BindingException always.
     *
     **/

    public void setBoolean(boolean value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws BindingException always.
     *
     **/

    public void setInt(int value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws BindingException always.
     *
     **/

    public void setDouble(double value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws BindingException always.
     *
     **/

    public void setString(String value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws BindingException always.
     *
     **/

    public void setObject(Object value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  Default implementation: returns true.
     * 
     *  @since 2.0.3
     * 
     **/

    public boolean isInvariant()
    {
        return true;
    }

    public Object getObject(String parameterName, Class type)
    {
        Object result = getObject();

        if (result == null)
            return result;

        Class resultClass = result.getClass();

        if (type.isAssignableFrom(resultClass))
            return result;

        if (type.isPrimitive() && isWrapper(type, resultClass))
            return result;

        String key =
            type.isInterface() ? "AbstractBinding.wrong-interface" : "AbstractBinding.wrong-type";

        String message =
            Tapestry.format(
                key,
                new Object[] { parameterName, result, resultClass.getName(), type.getName()});

        throw new BindingException(message, this);
    }

    public boolean isWrapper(Class primitiveType, Class subjectClass)
    {
        return PRIMITIVE_TYPES.get(primitiveType).equals(subjectClass);
    }

    /** @since 3.0 **/

    protected BindingException createReadOnlyBindingException(IBinding binding)
    {
        return new BindingException(
            Tapestry.getMessage("AbstractBinding.read-only-binding"),
            binding);
    }
}