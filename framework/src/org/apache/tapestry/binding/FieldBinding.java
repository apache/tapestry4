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

import java.lang.reflect.Field;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.Tapestry;

/**
 *
 *  A type of static {@link org.apache.tapestry.IBinding} that gets it value from a public field
 *  (static class variable) of some class or interface.
 *
 *  <p>The binding uses a field name, which consists of a fully qualified class name and
 *  a static field of that class seperated by a dot.  For example: <code>com.foobar.SomeClass.SOME_FIELD</code>.
 *
 *  <p>If the class specified is for the <code>java.lang</code> package, then the package may be
 *  ommitted.  This allows <code>Boolean.TRUE</code> to be recognized as a valid value.
 *
 *  <p>The {@link org.apache.tapestry.IPageSource} maintains a cache of FieldBindings.  This means that
 *  each field will be represented by a single binding ... that means that for any field,
 *  the <code>accessValue()</code> method (which obtains the value for the field using
 *  reflection) will only be invoked once.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @deprecated To be removed in 2.5 with no replacement.  Can be accomplished using OGNL expressions.
 * 
 **/

public class FieldBinding extends AbstractBinding
{
    private String fieldName;
    private boolean accessed;
    private Object value;
    private ClassResolver resolver;

    public FieldBinding(ClassResolver resolver, String fieldName, Location location)
    {
    	super(location);
    	
        this.resolver = resolver;
        this.fieldName = fieldName;
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("FieldBinding[");
        buffer.append(fieldName);

        if (accessed)
        {
            buffer.append(" (");
            buffer.append(value);
            buffer.append(')');
        }

        buffer.append(']');

        return buffer.toString();
    }

    public Object getObject()
    {
        if (!accessed)
            accessValue();

        return value;
    }

    private void accessValue()
    {
        String className;
        String simpleFieldName;
        int dotx;
        Class targetClass;
        Field field;

        dotx = fieldName.lastIndexOf('.');

        if (dotx < 0)
            throw new BindingException(Tapestry.format("invalid-field-name", fieldName), this);

        // Hm. Should validate that there's a dot!

        className = fieldName.substring(0, dotx);
        simpleFieldName = fieldName.substring(dotx + 1);

        // Simple class names are assumed to be in the java.lang package.

        if (className.indexOf('.') < 0)
            className = "java.lang." + className;

        try
        {
            targetClass = resolver.findClass(className);
        }
        catch (Throwable t)
        {
            throw new BindingException(Tapestry.format("unable-to-resolve-class", className), this, t);
        }

        try
        {
            field = targetClass.getField(simpleFieldName);
        }
        catch (NoSuchFieldException ex)
        {
            throw new BindingException(Tapestry.format("field-not-defined", fieldName), this, ex);
        }

        // Get the value of the field.  null means look for it as a static
        // variable.

        try
        {
            value = field.get(null);
        }
        catch (IllegalAccessException ex)
        {
            throw new BindingException(Tapestry.format("illegal-field-acccess", fieldName), this, ex);
        }
        catch (NullPointerException ex)
        {
            throw new BindingException(Tapestry.format("field-is-instance", fieldName), this, ex);
        }

        // Don't look for it again, even if the value is itself null.

        accessed = true;
    }
}