/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.bean;

import java.lang.reflect.Field;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IBeanProvider;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;

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

    public FieldBeanInitializer(String propertyName, String fieldName)
    {
        super(propertyName);

        _fieldName = fieldName;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IResourceResolver resolver = provider.getResourceResolver();
        
        if (!_fieldResolved)
            resolveField(resolver);

        setBeanProperty(resolver, bean, _fieldValue);
    }

    private synchronized void resolveField(IResourceResolver resolver)
    {
        if (_fieldResolved)
            return;

        // This is all copied out of of FieldBinding!!

        int dotx = _fieldName.lastIndexOf('.');

        if (dotx < 0)
            throw new ApplicationRuntimeException(Tapestry.getString("invalid-field-name", _fieldName));

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
            throw new ApplicationRuntimeException(Tapestry.getString("unable-to-resolve-class", className), t);
        }

        Field field = null;

        try
        {
            field = targetClass.getField(simpleFieldName);
        }
        catch (NoSuchFieldException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.getString("field-not-defined", _fieldName), ex);
        }

        // Get the value of the field.  null means look for it as a static
        // variable.

        try
        {
            _fieldValue = field.get(null);
        }
        catch (IllegalAccessException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.getString("illegal-field-access", _fieldName), ex);
        }
        catch (NullPointerException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.getString("field-is-instance", _fieldName), ex);
        }

        _fieldResolved = true;
    }

}