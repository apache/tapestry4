// Copyright 2004 The Apache Software Foundation
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
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.ClassFactory;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 * Contains the logic for analyzing and enhancing a single component class. Internally, this class
 * makes use of {@link IEnhancedClassFactory}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class ComponentClassFactory
{
    private static final Log LOG = LogFactory.getLog(ComponentClassFactory.class);

    /**
     * Package prefix used for classes that come out of the java. or javax. packages.
     */
    private static final String ENHANCED_PACKAGE = "org.apache.tapestry.enhanced.";

    /**
     * UID used to generate new class names.
     */
    private static int _uid = 0;

    private EnhancementWorklist _worklist;

    private Map _beanProperties = new HashMap();

    private ClassResolver _resolver;

    private IComponentSpecification _specification;

    private Class _componentClass;

    private ClassFactory _classFactory;

    private JavaClassMapping _classMapping = new JavaClassMapping();

    public ComponentClassFactory(ClassResolver resolver, IComponentSpecification specification,
            Class componentClass, ClassFactory classFactory)
    {
        _resolver = resolver;
        _specification = specification;
        _componentClass = componentClass;
        _classFactory = classFactory;

        buildBeanProperties();
    }

    /**
     * We synchronize this method to, hopefully, prevent multiple threads from introspecting any
     * singe class at one time. The Introspector is not thread safe. Unforunately, PropertyUtils,
     * OGNL, here (and elsewhere?) may attempt to introspect at the same time ... we're safe as long
     * as its not the same class being introspected.
     */
    private synchronized void buildBeanProperties()
    {
        BeanInfo info = null;

        try
        {
            info = Introspector.getBeanInfo(_componentClass);

        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unabelToIntrospectClass(
                    _componentClass,
                    ex), ex);
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
     * Invokes {@link #scanForEnhancements()}to identify any enhancements needed on the class,
     * returning true if there are any enhancements to be performed.
     */

    public boolean needsEnhancement()
    {
        scanForEnhancements();

        return _worklist != null && _worklist.hasModifications();
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
     * Given a class name, returns the corresponding class. In addition, scalar types, arrays of
     * scalar types, java.lang.Object[] and java.lang.String[] are supported.
     * 
     * @param type
     *            to convert to a Class
     * @param location
     *            of the involved specification element (for exception reporting)
     */

    public Class convertPropertyType(String type, Location location)
    {
        Class result = _classMapping.getType(type);

        if (result == null)
        {
            try
            {
                String typeName = JavaTypeUtils.getJVMClassName(type);
                result = _resolver.findClass(typeName);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(EnhanceMessages.badPropertyType(type, ex),
                        location, ex);
            }

            _classMapping.recordType(type, result);
        }

        return result;
    }

    private void checkPropertyType(PropertyDescriptor pd, Class propertyType, Location location)
    {
        if (!pd.getPropertyType().equals(propertyType))
            throw new ApplicationRuntimeException(EnhanceMessages.propertyTypeMismatch(
                    _componentClass,
                    pd.getName(),
                    pd.getPropertyType(),
                    propertyType), location, null);
    }

    /**
     * Checks to see that that class either doesn't provide the property, or does but the
     * accessor(s) are abstract. Returns the name of the read accessor if found, or fabricates an
     * appropriate name (if not). Returns null only if the property does not exist.
     */

    private String findReadMethodName(String propertyName, Class propertyType, Location location)
    {
        PropertyDescriptor d = getPropertyDescriptor(propertyName);

        if (d == null)
            return CreateAccessorUtils.buildMethodName("get", propertyName);

        checkPropertyType(d, propertyType, location);

        Method write = d.getWriteMethod();
        Method read = d.getReadMethod();

        if (isImplemented(write))
            throw new ApplicationRuntimeException(EnhanceMessages.nonAbstractWrite(write
                    .getDeclaringClass(), propertyName), location, null);

        if (isImplemented(read))
            throw new ApplicationRuntimeException(EnhanceMessages.nonAbstractRead(read
                    .getDeclaringClass(), propertyName), location, null);

        if (read != null)
            return read.getName();

        return CreateAccessorUtils.buildMethodName("get", propertyName);
    }

    private boolean isMissingProperty(String propertyName)
    {
        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        return !isImplemented(pd);
    }

    /**
     * Invoked by {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer}to create an
     * enahanced subclass of the component class. This means creating a default constructor, new
     * fields, and new accessor and mutator methods. Properties are created for connected
     * parameters, for all formal parameters (the binding property), and for all specified
     * parameters (which may be transient or persistent).
     */

    public Class createEnhancedSubclass()
    {
        EnhancementWorklist worklist = getWorklist();

        String startClassName = _componentClass.getName();
        String subclassName = worklist.getClassName();

        if (LOG.isDebugEnabled())
            LOG.debug("Enhancing subclass of " + startClassName + " for "
                    + _specification.getSpecificationLocation());

        Class result = worklist.createEnhancedSubclass();

        if (LOG.isDebugEnabled())
            LOG.debug("Finished creating enhanced class " + subclassName);

        return result;
    }

    /**
     * Invoked by {@link #needsEnhancement()}to find any enhancements that may be needed. Should
     * create an {@link org.apache.tapestry.enhance.IEnhancer}for each one, and add it to the
     * queue.
     */

    private void scanForEnhancements()
    {
        scanForParameterEnhancements();
        scanForSpecifiedPropertyEnhancements();
    }

    /**
     * Invoked by {@link #scanForEnhancements()}to locate any enhancements needed for component
     * parameters (this includes binding properties and connected parameter property).
     */

    private void scanForParameterEnhancements()
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

    private void scanForSpecifiedPropertyEnhancements()
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

    private void scanForBindingProperty(String parameterName, IParameterSpecification ps)
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

        IEnhancer enhancer = new CreatePropertyEnhancer(propertyName, IBinding.class);

        addEnhancer(enhancer);
    }

    private void scanForParameterProperty(String parameterName, IParameterSpecification ps)
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

        Location location = ps.getLocation();

        Class propertyType = convertPropertyType(ps.getType(), location);

        String readMethodName = findReadMethodName(propertyName, propertyType, location);

        IEnhancer enhancer = new CreatePropertyEnhancer(propertyName, propertyType, readMethodName,
                false);

        addEnhancer(enhancer);
    }

    private void addAutoParameterEnhancer(String parameterName, IParameterSpecification ps)
    {
        Location location = ps.getLocation();
        String propertyName = ps.getPropertyName();

        if (!ps.isRequired() && ps.getDefaultValue() == null)
            throw new ApplicationRuntimeException(EnhanceMessages.autoMustBeRequired(propertyName),
                    location, null);

        Class propertyType = convertPropertyType(ps.getType(), location);

        String readMethodName = findReadMethodName(propertyName, propertyType, location);

        IEnhancer enhancer = new CreateAutoParameterEnhancer(propertyName, parameterName,
                propertyType, readMethodName);

        addEnhancer(enhancer);
    }

    private void addEnhancer(IEnhancer eh)
    {
        if (_worklist == null)
        {
            String subclassName = constructEnhancedClassName();

            _worklist = new EnhancementWorklistImpl(subclassName, _componentClass, _resolver,
                    _classFactory);
        }

        _worklist.addEnhancer(eh);
    }

    private String constructEnhancedClassName()
    {
        String startClassName = _componentClass.getName();

        int lastdot = startClassName.lastIndexOf('.');

        // Get package name, INCLUDING, seperator dot

        String packageName = lastdot < 1 ? "" : startClassName.substring(0, lastdot + 1);
        String rawName = startClassName.substring(lastdot + 1);

        // If the new class is located in a 'restricted' package,
        // use a neutral package name

        if (packageName.startsWith("java.") || packageName.startsWith("javax."))
            packageName = ENHANCED_PACKAGE;

        return packageName + "$" + rawName + "_" + generateUID();
    }

    private void scanForSpecifiedProperty(IPropertySpecification ps)
    {
        String propertyName = ps.getName();
        Location location = ps.getLocation();
        Class propertyType = convertPropertyType(ps.getType(), location);

        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        if (isImplemented(pd))
        {
            // Make sure the property is at least the right type.

            checkPropertyType(pd, propertyType, location);
            return;
        }

        String readMethodName = findReadMethodName(propertyName, propertyType, location);

        IEnhancer enhancer = new CreatePropertyEnhancer(propertyName, propertyType, readMethodName,
                ps.isPersistent());

        addEnhancer(enhancer);
    }

    public EnhancementWorklist getWorklist()
    {
        return _worklist;
    }

    private static synchronized int generateUID()
    {
        return _uid++;
    }

}