//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
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
