package net.sf.tapestry.util.prop;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ognl.ObjectPropertyAccessor;
import ognl.OgnlException;

/**
 *  Allow access to the public instance variables of
 *  a class which implements {@link net.sf.tapestry.util.prop.IPublicBean}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class PublicBeanPropertyAccessor extends ObjectPropertyAccessor
{
    // For 1.2.2 compatibility -- Collections.EMPTY_MAP is not defined there
    private final static Map EMPTY_MAP = new HashMap();

    /**
     *  Map of Map.  Outer map is keyed on class.  Inner map is keyed
     *  on field name and value is {@link java.lang.reflect.Field}.
     * 
     **/

    private Map _cache = new HashMap();
    
    private synchronized Field findField(Class beanClass, String fieldName)
    {
        Map classMap = (Map) _cache.get(beanClass);

        if (classMap == null)
        {
            classMap = constructClassMap(beanClass);
            _cache.put(beanClass, classMap);
        }

        return (Field) classMap.get(fieldName);
    }

    private Map constructClassMap(Class beanClass)
    {
        Field fields[] = beanClass.getFields();

        if (fields.length == 0)
            return EMPTY_MAP;
            //return Collections.EMPTY_MAP;
            

        Map result = new HashMap();

        for (int i = 0; i < fields.length; i++)
        {
            result.put(fields[i].getName(), fields[i]);
        }

        return result;
    }

    public Object getProperty(Map context, Object target, Object name) throws OgnlException
    {
        Field f = findField(target.getClass(), (String) name);

        if (f != null)
        {
            try
            {
                return f.get(target);
            }
            catch (IllegalAccessException ex)
            {
                throw new OgnlException(ex.getMessage(), ex);
            }
        }

        return super.getProperty(context, target, name);
    }

    public boolean hasGetProperty(Map context, Object target, Object oname) throws OgnlException
    {
        Field f = findField(target.getClass(), (String) oname);

        if (f != null)
            return true;

        return super.hasGetProperty(context, target, oname);
    }

    public boolean hasSetProperty(Map context, Object target, Object oname) throws OgnlException
    {
        Field f = findField(target.getClass(), (String) oname);

        if (f != null)
            return true;

        return super.hasSetProperty(context, target, oname);
    }

    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException
    {
        Field f = findField(target.getClass(), (String) name);

        if (f != null)
        {
            try
            {
                f.set(target, value);
            }
            catch (IllegalAccessException ex)
            {
                throw new OgnlException(ex.getMessage(), ex);
            }
        }

        super.setProperty(context, target, name, value);
    }

}
