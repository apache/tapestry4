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
package net.sf.tapestry.util.prop;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Tapestry;

/**
 *  
 *  Uses {@link java.beans.Introspector} to get bean information
 *  and analyze properties for those beans.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class PropertyFinder
{
    /**
     *  Keyed on bean class value is also a Map.  Inner Map is
     *  keyed on property name, value is a
     *  {@link PropertyInfo}.
     * 
     **/

    private static Map _cache = new HashMap();

    /**
     *  Finds the {@link PropertyInfo} for the specified class and
     *  property.  Returns null if the class does not implement 
     *  such a property.
     * 
     **/
    
    public synchronized static PropertyInfo getPropertyInfo(Class beanClass, String propertyName)
    {
        Map beanClassMap = (Map) _cache.get(beanClass);

        if (beanClassMap == null)
        {
            beanClassMap = buildBeanClassMap(beanClass);

            _cache.put(beanClass, beanClassMap);
        }

        return (PropertyInfo) beanClassMap.get(propertyName);
    }

    private static Map buildBeanClassMap(Class beanClass)
    {
        Map result = new HashMap();
        BeanInfo bi = null;

        try
        {
            bi = Introspector.getBeanInfo(beanClass);
        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("PropertyFinder.unable-to-introspect-class", beanClass.getName()),
                ex);
        }

        PropertyDescriptor[] pd = bi.getPropertyDescriptors();

        for (int i = 0; i < pd.length; i++)
        {
            PropertyDescriptor d = pd[i];

            PropertyInfo info =
                new PropertyInfo(
                    d.getName(),
                    d.getPropertyType(),
                    d.getReadMethod() != null,
                    d.getWriteMethod() != null);

            result.put(d.getName(), info);
        }

        return result;
    }
}
