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

package org.apache.tapestry.bean;

import java.lang.reflect.Field;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;

/**
 *  Initializes a bean with the value of a public static field.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class FieldBeanInitializer extends AbstractBeanInitializer
{
    protected String _fieldName;
    protected Object _fieldValue;
    private boolean _fieldResolved = false;

    public synchronized void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IResourceResolver resolver = provider.getResourceResolver();

        if (!_fieldResolved)
            resolveField(resolver);

        setBeanProperty(resolver, bean, _fieldValue);
    }

    private void resolveField(IResourceResolver resolver)
    {
        if (_fieldResolved)
            return;

        // This is all copied out of of FieldBinding!!

        int dotx = _fieldName.lastIndexOf('.');

        if (dotx < 0)
            throw new ApplicationRuntimeException(
                Tapestry.format("invalid-field-name", _fieldName));

        String className = _fieldName.substring(0, dotx);
        String simpleFieldName = _fieldName.substring(dotx + 1);

        // Simple class names are assumed to be in the java.lang package.

        if (className.indexOf('.') < 0)
            className = "java.lang." + className;

        Class targetClass = null;

        try
        {
            targetClass = resolver.findClass(className);
        }
        catch (Throwable t)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("unable-to-resolve-class", className),
                t);
        }

        Field field = null;

        try
        {
            field = targetClass.getField(simpleFieldName);
        }
        catch (NoSuchFieldException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("field-not-defined", _fieldName),
                ex);
        }

        // Get the value of the field.  null means look for it as a static
        // variable.

        try
        {
            _fieldValue = field.get(null);
        }
        catch (IllegalAccessException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("illegal-field-access", _fieldName),
                ex);
        }
        catch (NullPointerException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("field-is-instance", _fieldName),
                ex);
        }

        _fieldResolved = true;
    }

}