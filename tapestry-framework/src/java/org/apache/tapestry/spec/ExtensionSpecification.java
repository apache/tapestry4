// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.coerce.ValueConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Defines an "extension", which is much like a helper bean, but is part of a library or application
 * specification (and has the same lifecycle as the application).
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class ExtensionSpecification extends LocatablePropertyHolder implements IExtensionSpecification
{
    private static final Log LOG = LogFactory.getLog(ExtensionSpecification.class);
    
    protected Map _configuration = new HashMap();
    
    private String _className;
    
    private boolean _immediate;

    /** @since 4.0 */
    private ClassResolver _resolver;

    /** @since 4.0 */
    private ValueConverter _converter;

    /**
     * Creates a new instance which will use the specified resolver to resolve classes and converter
     * to coerce values to their desired type.
     *
     * @param resolver
     *          The class resolver used to resolve classes safely in a servlet environment.
     * @param valueConverter
     *          Converter used to coerce values.
     */
    public ExtensionSpecification(ClassResolver resolver, ValueConverter valueConverter)
    {
        _resolver = resolver;
        _converter = valueConverter;
    }

    public String getClassName()
    {
        return _className;
    }

    public void setClassName(String className)
    {
        _className = className;
    }

    public void addConfiguration(String propertyName, String value)
    {
        if (_configuration.containsKey(propertyName))
            throw new IllegalArgumentException(Tapestry.format("ExtensionSpecification.duplicate-property", this, propertyName));

        _configuration.put(propertyName, value);
    }

    /**
     * Returns an immutable Map of the configuration; keyed on property name, with values as
     * properties to assign.
     */

    public Map getConfiguration()
    {
        return Collections.unmodifiableMap(_configuration);
    }

    /**
     * Invoked to instantiate an instance of the extension and return it. It also configures
     * properties of the extension.
     */

    public Object instantiateExtension()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Instantiating extension class " + _className + ".");

        Class extensionClass = null;
        Object result = null;

        try
        {
            extensionClass = _resolver.findClass(_className);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "ExtensionSpecification.bad-class",
                    _className), getLocation(), ex);
        }

        result = instantiateInstance(extensionClass, result);

        initializeProperties(result);

        return result;
    }

    private void initializeProperties(Object extension)
    {

        Iterator i = _configuration.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String propertyName = (String) entry.getKey();
            String textValue = (String) entry.getValue();

            try
            {
                Class propertyType = PropertyUtils.getPropertyType(extension, propertyName);

                Object objectValue = _converter.coerceValue(textValue, propertyType);

                PropertyUtils.write(extension, propertyName, objectValue);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
            }
        }
    }

    private Object instantiateInstance(Class extensionClass, Object result)
    {
        Object returnResult = result;
        try
        {
            returnResult = extensionClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
        }

        return returnResult;
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
     * Returns true if the extensions should be instantiated immediately after the containing
     * {@link org.apache.tapestry.spec.LibrarySpecification}if parsed. Non-immediate extensions are
     * instantiated only as needed.
     */

    public boolean isImmediate()
    {
        return _immediate;
    }

    public void setImmediate(boolean immediate)
    {
        _immediate = immediate;
    }

}
