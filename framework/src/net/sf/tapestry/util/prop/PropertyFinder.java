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
