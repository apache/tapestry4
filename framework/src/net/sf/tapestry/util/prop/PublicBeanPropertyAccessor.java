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
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.*;

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
    /**
     *  Map of Map.  Outer map is keyed on class.  Inner map is keyed
     *  on field name and value is {@link java.lang.reflect.Field}.
     * 
     **/

    private Map _cache;

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
            return Collections.EMPTY_MAP;

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
