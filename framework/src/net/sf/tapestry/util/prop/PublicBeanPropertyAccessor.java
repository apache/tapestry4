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
