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

package org.apache.tapestry.enhance;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 *  Contains the logic for analyzing and enhancing a single component class.
 *  Internally, this class makes use of {@link IEnhancedClassFactory}.
 *
 *  @author Howard Lewis Ship
 *  @since 3.0
 *
 **/

public class ComponentClassFactory
{
    private static final Log LOG = LogFactory.getLog(ComponentClassFactory.class);

    /**
     *  Package prefix to be added if the enhanced object is in a 'sysem' package 
     */
    private static final String PACKAGE_PREFIX = "org.apache.tapestry.";

    /**
     *  UID used to generate new class names.
     **/
    private static int _uid = 0;

    /**
     *  Mapping between a primitive type and its Java VM representation
     *  Used for the encoding of array types
     **/
    private static Map _primitiveTypes = new HashMap();

    static {
        _primitiveTypes.put("boolean", "Z");
        _primitiveTypes.put("short", "S");
        _primitiveTypes.put("int", "I");
        _primitiveTypes.put("long", "J");
        _primitiveTypes.put("float", "F");
        _primitiveTypes.put("double", "D");
        _primitiveTypes.put("char", "C");
        _primitiveTypes.put("byte", "B");
    }

    private IResourceResolver _resolver;

    private IEnhancedClassFactory _enhancedClassFactory;
    private IEnhancedClass _enhancedClass;
    private Map _beanProperties = new HashMap();
    private IComponentSpecification _specification;
    private Class _componentClass;
    private JavaClassMapping _classMapping = new JavaClassMapping();

    public ComponentClassFactory(
        IResourceResolver resolver,
        IComponentSpecification specification,
        Class componentClass,
        IEnhancedClassFactory enhancedClassFactory)
    {
        _resolver = resolver;

        _specification = specification;

        _componentClass = componentClass;

        _enhancedClassFactory = enhancedClassFactory;

        buildBeanProperties();
    }

    private void buildBeanProperties()
    {
        BeanInfo info = null;

        try
        {
            info = Introspector.getBeanInfo(_componentClass);

        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ComponentClassFactory.unable-to-introspect-class",
                    _componentClass.getName()),
                ex);
        }

        PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

        for (int i = 0; i < descriptors.length; i++)
        {
            _beanProperties.put(descriptors[i].getName(), descriptors[i]);
        }
    }

    protected PropertyDescriptor getPropertyDescriptor(String name)
    {
        return (PropertyDescriptor) _beanProperties.get(name);
    }

    /**
     *  Invokes {@link #scanForEnhancements()} to identify any
     *  enhancements needed on the class, returning true
     *  if there are any enhancements to be performed. 
     * 
     **/

    public boolean needsEnhancement()
    {
        scanForEnhancements();

        return _enhancedClass != null && _enhancedClass.hasModifications();
    }

    /**
     * @return true if pd is not null and both read/write methods are implemented
     */
    public boolean isImplemented(PropertyDescriptor pd)
    {
        if (pd == null)
            return false;

        return isImplemented(pd.getReadMethod()) && isImplemented(pd.getWriteMethod());
    }

    /**
     * @return true if m is not null and is abstract.
     */
    public boolean isAbstract(Method m)
    {
        if (m == null)
            return false;

        return Modifier.isAbstract(m.getModifiers());
    }

    /**
     * @return true if m is not null and not abstract  
     */
    public boolean isImplemented(Method m)
    {
        if (m == null)
            return false;

        return !Modifier.isAbstract(m.getModifiers());
    }

    /**
     *  Given a class name, returns the corresponding class.  In addition,
     *  scalar types, arrays of scalar types, java.lang.Object[] and
     *  java.lang.String[] are supported.
     * 
     *  @param type to convert to a Class
     *  @param location of the involved specification element (for exception reporting)
     * 
     **/

    public Class convertPropertyType(String type, ILocation location)
    {
        Class result = _classMapping.getType(type);

        if (result == null)
        {
            try
            {
                String typeName = translateClassName(type);
                result = _resolver.findClass(typeName);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.format("ComponentClassFactory.bad-property-type", type),
                    location,
                    ex);
            }

            _classMapping.recordType(type, result);
        }

        return result;
    }

    /**
     *  Translates types from standard Java format to Java VM format.
     *  For example, java.util.Locale remains java.util.Locale, but
     *  int[][] is translated to [[I and java.lang.Object[] to 
     *  [Ljava.lang.Object;   
     *  This method and its static Map should go into a utility class
     */
    protected String translateClassName(String type)
    {
        // if it is not an array, just return the type itself
        if (!type.endsWith("[]"))
            return type;

        // if it is an array, convert it to JavaVM-style format
        StringBuffer javaType = new StringBuffer();
        while (type.endsWith("[]"))
        {
            javaType.append("[");
            type = type.substring(0, type.length() - 2);
        }

        String primitiveIdentifier = (String) _primitiveTypes.get(type);
        if (primitiveIdentifier != null)
            javaType.append(primitiveIdentifier);
        else
            javaType.append("L" + type + ";");

        return javaType.toString();
    }

    protected void checkPropertyType(PropertyDescriptor pd, Class propertyType, ILocation location)
    {
        if (!pd.getPropertyType().equals(propertyType))
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ComponentClassFactory.property-type-mismatch",
                    new Object[] {
                        _componentClass.getName(),
                        pd.getName(),
                        pd.getPropertyType().getName(),
                        propertyType.getName()}),
                location,
                null);
    }

    /**
     *  Checks to see that that class either doesn't provide the property, or does
     *  but the accessor(s) are abstract.  Returns the name of the read accessor,
     *  or null if there is no such accessor (this is helpful if the beanClass
     *  defines a boolean property, where the name of the accessor may be isXXX or
     *  getXXX).
     * 
     **/

    protected String checkAccessors(String propertyName, Class propertyType, ILocation location)
    {
        PropertyDescriptor d = getPropertyDescriptor(propertyName);

        if (d == null)
            return null;

        checkPropertyType(d, propertyType, location);

        Method write = d.getWriteMethod();
        Method read = d.getReadMethod();

        if (isImplemented(write))
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ComponentClassFactory.non-abstract-write",
                    write.getDeclaringClass().getName(),
                    propertyName),
                location,
                null);

        if (isImplemented(read))
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ComponentClassFactory.non-abstract-read",
                    read.getDeclaringClass().getName(),
                    propertyName),
                location,
                null);

        return read == null ? null : read.getName();
    }

    protected boolean isMissingProperty(String propertyName)
    {
        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        return !isImplemented(pd);
    }

    /**
     *  Invoked by {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer} to
     *  create an enahanced
     *  subclass of the component class.  This means creating a default constructor,
     *  new fields, and new accessor and mutator methods.  Properties are created
     *  for connected parameters, for all formal parameters (the binding property),
     *  and for all specified parameters (which may be transient or persistent).
     * 
     **/

    public Class createEnhancedSubclass()
    {
        IEnhancedClass enhancedClass = getEnhancedClass();

        String startClassName = _componentClass.getName();
        String subclassName = enhancedClass.getClassName();

        if (LOG.isDebugEnabled())
            LOG.debug(
                "Enhancing subclass of "
                    + startClassName
                    + " for "
                    + _specification.getSpecificationLocation());

        Class result = enhancedClass.createEnhancedSubclass();

        if (LOG.isDebugEnabled())
            LOG.debug("Finished creating enhanced class " + subclassName);

        return result;
    }

    /**
     *  Invoked by {@link #needsEnhancement()} to find any enhancements
     *  that may be needed.  Should create an {@link org.apache.tapestry.enhance.IEnhancer}
     *  for each one, and add it to the queue.
     * 
     **/

    protected void scanForEnhancements()
    {
        scanForParameterEnhancements();
        scanForSpecifiedPropertyEnhancements();
        scanForAbstractClass();
    }

    protected void scanForAbstractClass()
    {
        if (Modifier.isAbstract(_componentClass.getModifiers()))
            getEnhancedClass().addEnhancer(new NoOpEnhancer());

    }

    /**
     *  Invoked by {@link #scanForEnhancements()} to locate
     *  any enhancements needed for component parameters (this includes
     *  binding properties and connected parameter property).
     * 
     **/

    protected void scanForParameterEnhancements()
    {
        List names = _specification.getParameterNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            IParameterSpecification ps = _specification.getParameter(name);

            scanForBindingProperty(name, ps);

            scanForParameterProperty(name, ps);
        }

    }

    protected void scanForSpecifiedPropertyEnhancements()
    {
        List names = _specification.getPropertySpecificationNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            IPropertySpecification ps = _specification.getPropertySpecification(name);

            scanForSpecifiedProperty(ps);
        }
    }

    protected void scanForBindingProperty(String parameterName, IParameterSpecification ps)
    {
        String propertyName = parameterName + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX;
        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        // only enhance custom parameter binding properties if they are declared abstract
        if (ps.getDirection() == Direction.CUSTOM)
        {
            if (pd == null)
                return;

            if (!(isAbstract(pd.getReadMethod()) || isAbstract(pd.getWriteMethod())))
                return;
        }

        if (isImplemented(pd))
            return;

        // Need to create the property.
        getEnhancedClass().createProperty(propertyName, IBinding.class.getName());
    }

    protected void scanForParameterProperty(String parameterName, IParameterSpecification ps)
    {
        Direction direction = ps.getDirection();

        if (direction == Direction.CUSTOM)
            return;

        if (direction == Direction.AUTO)
        {
            addAutoParameterEnhancer(parameterName, ps);
            return;
        }

        String propertyName = ps.getPropertyName();

        // Yes, but does it *need* a property created?

        if (!isMissingProperty(propertyName))
            return;

        ILocation location = ps.getLocation();

        Class propertyType = convertPropertyType(ps.getType(), location);

        String readMethodName = checkAccessors(propertyName, propertyType, location);

        getEnhancedClass().createProperty(propertyName, ps.getType(), readMethodName, false);
    }

    protected void addAutoParameterEnhancer(String parameterName, IParameterSpecification ps)
    {
        ILocation location = ps.getLocation();
        String propertyName = ps.getPropertyName();

        if (!ps.isRequired() && ps.getDefaultValue() == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("ComponentClassFactory.auto-must-be-required", parameterName),
                location,
                null);

        Class propertyType = convertPropertyType(ps.getType(), location);

        String readMethodName = checkAccessors(propertyName, propertyType, location);

        getEnhancedClass().createAutoParameter(
            propertyName,
            parameterName,
            ps.getType(),
            readMethodName);
    }

    protected void scanForSpecifiedProperty(IPropertySpecification ps)
    {
        String propertyName = ps.getName();
        ILocation location = ps.getLocation();
        Class propertyType = convertPropertyType(ps.getType(), location);

        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        if (isImplemented(pd))
        {
            // Make sure the property is at least the right type.

            checkPropertyType(pd, propertyType, location);
            return;
        }

        String readMethodName = checkAccessors(propertyName, propertyType, location);

        getEnhancedClass().createProperty(
            propertyName,
            ps.getType(),
            readMethodName,
            ps.isPersistent());
    }

    public IEnhancedClass getEnhancedClass()
    {
        if (_enhancedClass == null)
        {
            String startClassName = _componentClass.getName();
            String subclassName = startClassName + "$Enhance_" + generateUID();

            // If the new class is located in a 'restricted' package, 
            // add a neutral package prefix to the name. 
            // The class enhancement will likely fail anyway, since the original object 
            // would not implement IComponent, but we do not know what the enhancement
            // will do in the future -- it might implement that interface automatically. 
            if (subclassName.startsWith("java.") || subclassName.startsWith("javax."))
                subclassName = PACKAGE_PREFIX + subclassName;

            _enhancedClass =
                _enhancedClassFactory.createEnhancedClass(subclassName, _componentClass);
        }
        return _enhancedClass;
    }

    private static synchronized int generateUID()
    {
        return _uid++;
    }

}