/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
 *  @version $Id$
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
     *  Returns true if the {@link PropertyDescriptor} is null, or
     *  (if non-null), if either accessor method is abstract (or missing).
     * 
     **/

    public boolean isAbstract(PropertyDescriptor pd)
    {
        if (pd == null)
            return true;

        return isAbstract(pd.getReadMethod()) || isAbstract(pd.getWriteMethod());
    }

    /**
     *  Returns true if the method is null, or is abstract.
     * 
     **/

    public boolean isAbstract(Method m)
    {
        if (m == null)
            return true;

        return Modifier.isAbstract(m.getModifiers());
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
                String typeName = translateClassName(type, true);
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

    // this method and its static Map should go into a utility class somewhere  
    protected String translateClassName(String type, boolean toplevel)
    {
        // if it is an array name reformat it in the way Java expects it
        if (type.endsWith("[]")) {
            String subtype = type.substring(0, type.length() - 2);
            return "[" + translateClassName(subtype, false); 
        }
        
        // if not an array and at the top level of the recursion, 
        // return the type as it is  
        if (toplevel) 
            return type;

        // test for a primitive type
        String primitiveIdentifier = (String) _primitiveTypes.get(type); 
        if (primitiveIdentifier != null)
            return primitiveIdentifier;
        
        // a normal java class 
        return "L" + type + ";";
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

        if (!isAbstract(write))
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ComponentClassFactory.non-abstract-write",
                    write.getDeclaringClass().getName(),
                    propertyName),
                location,
                null);

        if (!isAbstract(read))
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

        return isAbstract(pd);
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

        Class result;
        result = enhancedClass.createEnhancedSubclass();

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

        if (!isAbstract(pd))
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

        if (!isAbstract(pd))
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
            String subclassName = startClassName + "$Enhance_" + _uid++;

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


}