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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 *  Contains the logic for analyzing and enhancing a single component class.
 *  Internally, this class makes use of {@link org.apache.tapestry.enhance.ClassFabricator}
 *  and {@link org.apache.tapestry.enhance.MethodFabricator}.
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
     *  UID used to generate new class names.
     * 
     **/

    private static int _uid = 0;

    private IResourceResolver _resolver;
    private EnhanceClassLoader _classLoader;

    private ClassFabricator _classFabricator;
    private Map _beanProperties = new HashMap();
    private IComponentSpecification _specification;
    private Class _componentClass;
    private String _subclassName;

    /**
     *  List of {@link IEnhancer}.
     * 
     **/

    private List _enhancers;

    /**
     *  Map of type (as Class), keyed on type name. 
     * 
     **/

    private Map _typeMap = new HashMap();

    /**
     *  Map of type (as Type), keyed on type name.
     * 
     *  This should be kept in synch with ParameterManager, which maintains
     *  a similar list.
     * 
     **/

    private Map _objectTypeMap = new HashMap();

    {
        recordType("boolean", boolean.class, Type.BOOLEAN);
        recordType("boolean[]", boolean[].class, new ArrayType(Type.BOOLEAN, 1));

        recordType("short", short.class, Type.SHORT);
        recordType("short[]", short[].class, new ArrayType(Type.SHORT, 1));

        recordType("int", int.class, Type.INT);
        recordType("int[]", int[].class, new ArrayType(Type.INT, 1));

        recordType("long", long.class, Type.LONG);
        recordType("long[]", long[].class, new ArrayType(Type.LONG, 1));

        recordType("float", float.class, Type.FLOAT);
        recordType("float[]", float[].class, new ArrayType(Type.FLOAT, 1));

        recordType("double", double.class, Type.DOUBLE);
        recordType("double[]", double[].class, new ArrayType(Type.DOUBLE, 1));

        recordType("char", char.class, Type.CHAR);
        recordType("char[]", char[].class, new ArrayType(Type.CHAR, 1));

        recordType("byte", byte.class, Type.BYTE);
        recordType("byte[]", byte.class, new ArrayType(Type.BYTE, 1));

        recordType("java.lang.Object", Object.class, Type.OBJECT);
        recordType("java.lang.Object[]", Object[].class, new ArrayType(Type.OBJECT, 1));

        recordType("java.lang.String", String.class, Type.STRING);
        recordType("java.lang.String[]", String[].class, new ArrayType(Type.STRING, 1));
    }

    public ComponentClassFactory(
        IResourceResolver resolver,
        IComponentSpecification specification,
        Class componentClass)
    {
        _resolver = resolver;

        _specification = specification;

        _componentClass = componentClass;

        buildBeanProperties();
    }

    private void recordType(String name, Class type, Type objectType)
    {
        _typeMap.put(name, type);
        _objectTypeMap.put(name, objectType);
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
                Tapestry.getString(
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

        return _enhancers != null;
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

    public Class convertPropertyType(String type, Location location)
    {
        Class result = (Class) _typeMap.get(type);

        if (result == null)
        {
            try
            {

                result = _resolver.findClass(type);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.getString("ComponentClassFactory.bad-property-type", type),
                    location,
                    ex);
            }

            _typeMap.put(type, result);
        }

        return result;
    }

    /**
     *  Given the name of a class, returns the equivalent {@link Type}.  In addition,
     *  knows about scalar types, arrays of scalar types, java.lang.Object[] and
     *  java.lang.String[].
     * 
     **/

    public Type getObjectType(String type)
    {
        Type result = (Type) _objectTypeMap.get(type);

        if (result == null)
        {
            result = new ObjectType(type);
            _objectTypeMap.put(type, result);
        }

        return result;
    }

    /**
     *  Constructs an accessor method name.
     * 
     **/

    public String buildMethodName(String prefix, String propertyName)
    {
        StringBuffer result = new StringBuffer(prefix);

        char ch = propertyName.charAt(0);

        result.append(Character.toUpperCase(ch));

        result.append(propertyName.substring(1));

        return result.toString();
    }

    protected void checkPropertyType(PropertyDescriptor pd, Class propertyType, Location location)
    {
        if (!pd.getPropertyType().equals(propertyType))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
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

    protected String checkAccessors(String propertyName, Class propertyType, Location location)
    {
        PropertyDescriptor d = getPropertyDescriptor(propertyName);

        if (d == null)
            return null;

        checkPropertyType(d, propertyType, location);

        Method write = d.getWriteMethod();
        Method read = d.getReadMethod();

        if (!isAbstract(write))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "ComponentClassFactory.non-abstract-write",
                    write.getDeclaringClass().getName(),
                    propertyName),
                location,
                null);

        if (!isAbstract(read))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "ComponentClassFactory.non-abstract-read",
                    read.getDeclaringClass().getName(),
                    propertyName),
                location,
                null);

        return read == null ? null : read.getName();
    }

    /**
     *  Given an arbitrary type, figures out the correct
     *  argument type (for fireObservedChange()) to use.
     * 
     **/

    protected Type convertToArgumentType(Type type)
    {
        if (type instanceof BasicType)
            return type;

        return Type.OBJECT;
    }

    /**
     *  Creates a mutator (aka "setter") method.
     * 
     *  @param fieldType type of field value (and type of parameter value)
     *  @param fieldName name of field (not property!)
     *  @param propertyName name of property (used to construct method name)
     *  @param isPersistent if true, adds a call to fireObservedChange()
     * 
     **/

    public void createMutator(
        Type fieldType,
        String fieldName,
        String propertyName,
        boolean isPersistent)
    {
        String methodName = buildMethodName("set", propertyName);

        if (LOG.isDebugEnabled())
            LOG.debug("Creating mutator: " + methodName);

        MethodFabricator mf = _classFabricator.createMethod(methodName);
        mf.addArgument(fieldType, propertyName);

        InstructionFactory factory = _classFabricator.getInstructionFactory();

        mf.append(factory.createThis());
        mf.append(factory.createLoad(fieldType, 1));
        mf.append(factory.createPutField(_subclassName, fieldName, fieldType));

        // Persistent properties must invoke fireObservedChange()

        if (isPersistent)
        {
            mf.append(factory.createThis());
            mf.append(new PUSH(_classFabricator.getConstantPool(), propertyName));
            mf.append(factory.createLoad(fieldType, 1));

            Type argumentType = convertToArgumentType(fieldType);

            mf.append(
                factory.createInvoke(
                    _subclassName,
                    "fireObservedChange",
                    Type.VOID,
                    new Type[] { Type.STRING, argumentType },
                    Constants.INVOKEVIRTUAL));
        }

        mf.append(InstructionConstants.RETURN);

        mf.commit();
    }

    /**
     *  Creates an accessor (getter) method for the property.
     * 
     *  @param fieldType the return type for the method
     *  @param fieldName the name of the field (not the name of the property)
     *  @param propertyName the name of the property (used to build the name of the method)
     *  @param readMethodName if not null, the name of the method to use
     * 
     **/

    public void createAccessor(
        Type fieldType,
        String fieldName,
        String propertyName,
        String readMethodName)
    {
        String methodName =
            readMethodName == null ? buildMethodName("get", propertyName) : readMethodName;

        if (LOG.isDebugEnabled())
            LOG.debug("Creating accessor: " + methodName);

        MethodFabricator mf =
            _classFabricator.createMethod(Constants.ACC_PUBLIC, fieldType, methodName);

        InstructionFactory factory = _classFabricator.getInstructionFactory();

        mf.append(factory.createThis());
        mf.append(factory.createGetField(_subclassName, fieldName, fieldType));
        mf.append(factory.createReturn(fieldType));

        mf.commit();
    }

    protected boolean isMissingProperty(String propertyName)
    {
        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        return isAbstract(pd);
    }

    /**
     *  Invoked by {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer} to
     *  create, as a {@link org.apache.bcel.classfile.JavaClass}, an enahanced
     *  subclass of the component class.  This means creating a default constructor,
     *  new fields, and new accessor and mutator methods.  Properties are created
     *  for connected parameters, for all formal parameters (the binding property),
     *  and for all specified parameters (which may be transient or persistent).
     * 
     **/

    public JavaClass createEnhancedSubclass()
    {
        String startClassName = _componentClass.getName();

        if (LOG.isDebugEnabled())
            LOG.debug(
                "Enhancing subclass of "
                    + startClassName
                    + " for "
                    + _specification.getSpecificationLocation());

        _subclassName = startClassName + "$Enhance_" + _uid++;

        _classFabricator = new ClassFabricator(_subclassName, startClassName);

        _classFabricator.addDefaultConstructor();

        int count = _enhancers.size();

        for (int i = 0; i < count; i++)
        {
            IEnhancer enhancer = (IEnhancer) _enhancers.get(i);

            enhancer.performEnhancement(this);
        }

        JavaClass result = _classFabricator.commit();

        if (LOG.isDebugEnabled())
            LOG.debug("Finished creating enhanced class " + _subclassName);

        return result;
    }

    protected void addEnhancer(IEnhancer enhancer)
    {
        if (_enhancers == null)
            _enhancers = new ArrayList();

        _enhancers.add(enhancer);
    }

    /**
     *  Invoked by {@link #needsEnhancement()} to find any enhancements
     *  that may be needed.  Should create an {@link org.apache.tapestry.enhance.IEnhancer}
     *  for each one, and add it to the queue using {@link #addEnhancer(IEnhancer)}.
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

        Type propertyType = getObjectType(IBinding.class.getName());

        IEnhancer enhancer =
            new CreatePropertyEnhancer(propertyName, propertyType, ps.getLocation());

        addEnhancer(enhancer);
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

        Location location = ps.getLocation();

        Class propertyType = convertPropertyType(ps.getType(), location);

        String readMethodName = checkAccessors(propertyName, propertyType, location);

        Type fieldType = getObjectType(ps.getType());

        IEnhancer enhancer =
            new CreatePropertyEnhancer(propertyName, fieldType, readMethodName, false, location);

        addEnhancer(enhancer);
    }

    protected void addAutoParameterEnhancer(String parameterName, IParameterSpecification ps)
    {
        Location location = ps.getLocation();
        String propertyName = ps.getPropertyName();

        if (!ps.isRequired())
            throw new ApplicationRuntimeException(
                Tapestry.getString("ComponentClassFactory.auto-must-be-required", parameterName),
                location,
                null);

        Class propertyType = convertPropertyType(ps.getType(), location);

        String readMethodName = checkAccessors(propertyName, propertyType, location);

        Type fieldType = getObjectType(ps.getType());

        IEnhancer enhancer =
            new CreateAutoParameterEnhancer(
                this,
                propertyName,
                parameterName,
                fieldType,
                ps.getType(),
                readMethodName,
                location);

        addEnhancer(enhancer);
    }

    protected void scanForSpecifiedProperty(IPropertySpecification ps)
    {
        String propertyName = ps.getName();
        Location location = ps.getLocation();
        Class propertyType = convertPropertyType(ps.getType(), location);

        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        if (!isAbstract(pd))
        {
            // Make sure the property is at least the right type.

            checkPropertyType(pd, propertyType, location);
            return;
        }

        String readMethodName = checkAccessors(propertyName, propertyType, location);

        Type fieldType = getObjectType(ps.getType());

        IEnhancer enhancer =
            new CreatePropertyEnhancer(
                propertyName,
                fieldType,
                readMethodName,
                ps.isPersistent(),
                location);

        addEnhancer(enhancer);
    }

    public void createField(Type fieldType, String fieldName)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating field: " + fieldName);

        _classFabricator.addField(fieldType, fieldName);
    }

    public ClassFabricator getClassFabricator()
    {
        return _classFabricator;
    }

}