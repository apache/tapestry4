/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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


/**
 *
 *  A type of static {@link IBinding} that gets it value from a public field
 *  (static class variable) of some class or interface.
 *
 *  <p>The binding uses a field name, which consists of a fully qualified class name and
 *  a static field of that class seperated by a dot.  For example: <code>com.foobar.SomeClass.SOME_FIELD</code>.
 *
 * <p>If the class specified is for the <code>java.lang</code> package, then the package may be
 * ommitted.  This allows <code>Boolean.TRUE</code> to be recognized as a valid value.
 *
 * <p>The {@link PageSource} maintains a cache of FieldBindings.  This means that
 * each field will be represented by a single binding ... that means that for any field,
 * the <code>accessValue()</code> method (which obtains the value for the field using
 *  reflection) will only be invoked once.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.tapestry.binding;

import com.primix.tapestry.*;
import com.primix.tapestry.pageload.PageSource;
import java.util.*;
import java.lang.reflect.*;

public class FieldBinding extends AbstractBinding
{
    private String fieldName;
    private boolean accessed;
    private Object value;
    private IResourceResolver resolver;

    public FieldBinding(IResourceResolver resolver, String fieldName)
    {
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
            throw new BindingException("Invalid field name: " + fieldName + ".", this);

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
            throw new BindingException("Unable to resolve class " + className + ".",
                    this, t);
        }

        try
        {
            field = targetClass.getField(simpleFieldName);
        }
        catch (NoSuchFieldException e)
        {
            throw new BindingException("Field " + fieldName + " does not exist.",
            this, e);
        }

        // Get the value of the field.  null means look for it as a static
        // variable.

        try
        {
            value = field.get(null);
        }
        catch (IllegalAccessException e)
        {
            throw new BindingException("Cannot access field " + fieldName + ".",
                    this, e);
        }
        catch (NullPointerException e)
        {
            throw new BindingException("Field " + fieldName + " is an instance variable, not a class variable.",
                    this, e);
        }


        // Don't look for it again, even if the value is itself null.

        accessed = true;
    }
}