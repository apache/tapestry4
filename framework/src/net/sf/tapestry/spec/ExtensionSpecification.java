//
// Tapestry Web Application Framework
// Copyright (c) 2002 by Howard Lewis Ship
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

package net.sf.tapestry.spec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Category;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Defines an "extension", which is much like a helper bean, but 
 *  is part of a library or application specification (and has the same
 *  lifecycle as the application).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class ExtensionSpecification
{
    private static final Category CAT = Category.getInstance(ExtensionSpecification.class);

    private String _className;
    private Map _configuration = new HashMap();

    public String getClassName()
    {
        return _className;
    }

    public void setClassName(String className)
    {
        _className = className;
    }

    public void addConfiguration(String propertyName, Object value)
    {
        if (_configuration.containsKey(propertyName))
            throw new IllegalArgumentException(
                Tapestry.getString("ExtensionSpecification.duplicate-property", this, propertyName));

        _configuration.put(propertyName, value);
    }

    /**
     *  Returns an immutable Map of the configuration; keyed on property name,
     *  with values as properties to assign.
     * 
     **/

    public Map getConfiguration()
    {
        return Collections.unmodifiableMap(_configuration);
    }

    /**
     *  Invoked to instantiate an instance of the extension and return it.
     *  It also configures properties of the extension.
     * 
     **/

    public Object instantiateExtension(IResourceResolver resolver)
    {
        CAT.debug("Instantiating extension class " + _className + ".");

        Class extensionClass = resolver.findClass(_className);

        Object result = null;

        result = instantiateInstance(extensionClass, result);

        initializeProperties(result);

        return result;
    }

    private void initializeProperties(Object extension)
    {
        if (_configuration.isEmpty())
            return;
            
        PropertyHelper helper = PropertyHelper.forInstance(extension);

        Iterator i = _configuration.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String propertyNane = (String) entry.getKey();

            helper.set(extension, propertyNane, entry.getValue());
        }
    }

    private Object instantiateInstance(Class extensionClass, Object result)
    {
        try
        {
            result = extensionClass.newInstance();
        }
        catch (IllegalAccessException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        catch (InstantiationException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        
        return result;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("ExtensionSpecification@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append(_className);

        if (_configuration != null)
        {
            buffer.append(' ');
            buffer.append(_configuration);
        }

        buffer.append(']');

        return buffer.toString();
    }

}
