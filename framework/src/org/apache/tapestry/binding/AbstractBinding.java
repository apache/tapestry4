/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.binding;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.Location;
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

    private Location _location;

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

    protected AbstractBinding(Location location)
    {
        _location = location;
    }

    public Location getLocation()
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
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setBoolean(boolean value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setInt(int value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setDouble(double value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
     *
     **/

    public void setString(String value)
    {
        throw createReadOnlyBindingException(this);
    }

    /**
     *  @throws ReadOnlyBindingException always.
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

        String message = Tapestry.getString(key, parameterName, result, type.getName());

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
            Tapestry.getString("AbstractBinding.read-only-binding"),
            binding);
    }
}