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

package org.apache.tapestry.spec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.prop.OgnlUtils;

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
    extends LocatablePropertyHolder
    implements IExtensionSpecification
{
    private static final Log LOG = LogFactory.getLog(ExtensionSpecification.class);

    private String _className;
    protected Map _configuration = new HashMap();
    private boolean _immediate;

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
                Tapestry.format(
                    "ExtensionSpecification.duplicate-property",
                    this,
                    propertyName));

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
        if (LOG.isDebugEnabled())
            LOG.debug("Instantiating extension class " + _className + ".");
        Class extensionClass = null;
        Object result = null;

        try
        {
            extensionClass = resolver.findClass(_className);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ExtensionSpecification.bad-class", _className),
                getLocation(),
                ex);
        }

        result = instantiateInstance(extensionClass, result);

        initializeProperties(resolver, result);

        return result;
    }

    private void initializeProperties(IResourceResolver resolver, Object extension)
    {
        if (_configuration.isEmpty())
            return;

        Iterator i = _configuration.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String propertyName = (String) entry.getKey();

            OgnlUtils.set(propertyName, resolver, extension, entry.getValue());
        }
    }

    private Object instantiateInstance(Class extensionClass, Object result)
    {
        try
        {
            result = extensionClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
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

    /**
     *  Returns true if the extensions should be instantiated
     *  immediately after the containing 
     *  {@link org.apache.tapestry.spec.LibrarySpecification}
     *  if parsed.  Non-immediate extensions are instantiated
     *  only as needed.
     * 
     **/

    public boolean isImmediate()
    {
        return _immediate;
    }

    public void setImmediate(boolean immediate)
    {
        _immediate = immediate;
    }

}
