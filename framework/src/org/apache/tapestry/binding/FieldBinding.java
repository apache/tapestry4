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

import java.lang.reflect.Field;

import org.apache.tapestry.BindingException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceResolver;
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
 *  <p>The {@link org.apache.tapestry.engine.IPageSource} maintains a cache of FieldBindings.  This means that
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
    private IResourceResolver resolver;

    public FieldBinding(IResourceResolver resolver, String fieldName, ILocation location)
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