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

package org.apache.tapestry.util.prop;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;

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
                Tapestry.format("PropertyFinder.unable-to-introspect-class", beanClass.getName()),
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
