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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.ParameterSpecification;
import org.apache.tapestry.spec.PropertySpecification;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Contains the logic for analyzing and enhancing a single component class.
 *  Internally, this class makes use of {@link org.apache.tapestry.enhance.ClassFabricator}
 *  and {@link org.apache.tapestry.enhance.MethodFabricator}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
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
    private ComponentSpecification _specification;
    private Class _componentClass;
    private String _subclassName;

    /**
     *  Map of type (as Class), keyed on type name. 
     * 
     **/

    private Map _typeMap = new HashMap();

    /**
     *  Map of type (as Type), keyed on type name.
     * 
     **/

    private Map _objectTypeMap = new HashMap();

    {
        recordType("boolean", boolean.class, Type.BOOLEAN);
        recordType("short", short.class, Type.SHORT);
        recordType("int", int.class, Type.INT);
        recordType("long", long.class, Type.LONG);
        recordType("float", float.class, Type.FLOAT);
        recordType("double", double.class, Type.DOUBLE);
        recordType("char", char.class, Type.CHAR);
        recordType("byte", byte.class, Type.BYTE);
    }

    public ComponentClassFactory(
        IResourceResolver resolver,
        ComponentSpecification specification,
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

    private PropertyDescriptor getPropertyDescriptor(String name)
    {
        return (PropertyDescriptor) _beanProperties.get(name);
    }

    /**
    *  Examines the specification, identifies if any enhancements will be needed.
    *  This implementation looks for the presence of any
    *  {@link org.apache.tapestry.spec.PropertySpecification}s, or any
    *  connected parameters where the property is missing or abstract.
    * 
    **/

    public boolean needsEnhancement()
    {
        if (!_specification.getPropertySpecificationNames().isEmpty())
            return true;

        if (checkParameters())
            return true;

        return false;
    }

    /**
     *  Checks the formal parameters, returns true if any connected parameter
     *  has a missing or abstract property, or any parameter has a 
     *  missing or abstract binding property.
     * 
     **/

    private boolean checkParameters()
    {
        List names = _specification.getParameterNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);
            ParameterSpecification pspec = _specification.getParameter(name);

            if (checkParameterProperty(name))
                return true;

            if (checkConnectedParameter(pspec))
                return true;
        }

        return false;
    }

    /**
     *  Returns true if the parameter is connected (has a non-custom direction) and the
     *  corresponding property is either missing or abstract.
     * 
     **/

    private boolean checkConnectedParameter(ParameterSpecification spec)
    {
        Direction direction = spec.getDirection();

        if (direction == Direction.CUSTOM)
            return false;

        String propertyName = spec.getPropertyName();

        PropertyDescriptor d = getPropertyDescriptor(propertyName);

        return isAbstract(d);
    }

    /**
     *  Checks for a property that will store the
     *  {@link org.apache.tapestry.IBinding binding} for the (formal)
     *  parameter.
     *  
     *  <p>
     *  Returns false is present, true if missing or abstract.
     * 
     **/

    private boolean checkParameterProperty(String parameterName)
    {
        String propertyName = parameterName + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX;

        PropertyDescriptor d = getPropertyDescriptor(propertyName);

        return isAbstract(d);

    }

    private boolean isAbstract(PropertyDescriptor d)
    {
        if (d == null)
            return true;

        return isAbstract(d.getReadMethod()) && isAbstract(d.getWriteMethod());
    }

    /**
     *  Returns true if the method is null, or is abstract.
     * 
     **/

    private boolean isAbstract(Method m)
    {
        if (m == null)
            return true;

        return Modifier.isAbstract(m.getModifiers());
    }

    protected Class convertPropertyType(String type)
    {
        Class result = (Class) _typeMap.get(type);

        if (result == null)
        {
            result = _resolver.findClass(type);

            _typeMap.put(type, result);
        }

        return result;
    }

    protected Type getObjectType(String type)
    {
        Type result = (Type) _objectTypeMap.get(type);

        if (result == null)
        {
            result = new ObjectType(type);
            _objectTypeMap.put(type, result);
        }

        return result;
    }

    protected String buildMethodName(String prefix, String propertyName)
    {
        StringBuffer result = new StringBuffer(prefix);

        char ch = propertyName.charAt(0);

        result.append(Character.toUpperCase(ch));

        result.append(propertyName.substring(1));

        return result.toString();
    }

    /**
     *  Checks to see that that class either doesn't provide the property, or does
     *  but the accessor(s) are abstract.  Returns the name of the read accessor,
     *  or null if there is no such accessor (this is helpful if the beanClass
     *  defines a boolean property, where the name of the accessor may be isXXX or
     *  getXXX).
     * 
     **/

    protected String checkAccessors(String propertyName, Class propertyType)
    {
        PropertyDescriptor d = getPropertyDescriptor(propertyName);

        if (d == null)
            return null;

        if (!d.getPropertyType().equals(propertyType))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "ComponentClassFactory.property-type-mismatch",
                    new Object[] {
                        _componentClass.getName(),
                        propertyName,
                        d.getPropertyType().getName(),
                        propertyType.getName()}));

        Method m = d.getWriteMethod();

        if (!isAbstract(m))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "ComponentClassFactory.non-abstract-write",
                    m.getDeclaringClass().getName(),
                    propertyName));

        m = d.getReadMethod();

        if (!isAbstract(m))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "ComponentClassFactory.non-abstract-read",
                    m.getDeclaringClass().getName(),
                    propertyName));

        return m == null ? null : m.getName();
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

    protected void createMutator(
        Type fieldType,
        String fieldName,
        String propertyName,
        boolean isPersistent)
    {
        String methodName = buildMethodName("set", propertyName);

        MethodFabricator mf = _classFabricator.createMethod(methodName);
        mf.addArgument(fieldType, propertyName);

        InstructionList il = mf.getInstructionList();
        InstructionFactory factory = _classFabricator.getInstructionFactory();

        il.append(factory.createThis());
        il.append(factory.createLoad(fieldType, 1));
        il.append(factory.createPutField(_subclassName, fieldName, fieldType));

        // Persistent properties must invoke fireObservedChange

        if (isPersistent)
        {
            il.append(factory.createThis());
            il.append(new PUSH(_classFabricator.getConstantPool(), propertyName));
            il.append(factory.createLoad(fieldType, 1));

            Type argumentType = convertToArgumentType(fieldType);

            il.append(
                factory.createInvoke(
                    _subclassName,
                    "fireObservedChange",
                    Type.VOID,
                    new Type[] { Type.STRING, argumentType },
                    Constants.INVOKEVIRTUAL));
        }

        il.append(InstructionConstants.RETURN);

        mf.commit();
    }

    protected void createAccessor(
        Type fieldType,
        String fieldName,
        String propertyName,
        String readMethodName)
    {
        String methodName =
            readMethodName == null ? buildMethodName("get", propertyName) : readMethodName;

        MethodFabricator mf =
            _classFabricator.createMethod(Constants.ACC_PUBLIC, fieldType, methodName);

        InstructionList il = mf.getInstructionList();
        InstructionFactory factory = _classFabricator.getInstructionFactory();

        il.append(factory.createThis());
        il.append(factory.createGetField(_subclassName, fieldName, fieldType));
        il.append(factory.createReturn(fieldType));

        mf.commit();
    }

    /**
       *  Checks that the superclass provides
       *  either abstract accessors or none at all.  Creates the file, creates 
       *  the accessors, creates initialization code.
       * 
       **/

    protected void createProperty(String propertyName, String type, boolean persistent)
    {
        Class propertyType = convertPropertyType(type);

        String readMethodName = checkAccessors(propertyName, propertyType);

        String fieldName = "_$" + propertyName;

        Type fieldType = getObjectType(type);

        _classFabricator.addField(fieldType, fieldName);

        createAccessor(fieldType, fieldName, propertyName, readMethodName);
        createMutator(fieldType, fieldName, propertyName, persistent);
    }

    protected boolean isMissingProperty(String propertyName)
    {
        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        return isAbstract(pd);
    }

    /**
     *  Creates a property for a connected
     *  parameter from a parameter specification.
     * 
     **/

    protected void createConnectedParameterProperty(ParameterSpecification ps)
    {
        if (ps.getDirection() == Direction.CUSTOM)
            return;

        String propertyName = ps.getPropertyName();

        // Yes, but does it *need* a property created?

        if (!isMissingProperty(propertyName))
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Establishing connected parameter property " + propertyName);

        createProperty(propertyName, ps.getType(), false);
    }

    /**
     *  Invoked to create a specified property. 
     * 
     **/

    protected void createSpecifiedProperty(PropertySpecification ps)
    {
        String propertyName = ps.getName();

        if (LOG.isDebugEnabled())
            LOG.debug("Establishing specified property " + propertyName);

        createProperty(propertyName, ps.getType(), ps.isPersistent());
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

        createPropertySpecificationEnhancements();

        createParameterEnhancements();

        JavaClass result = _classFabricator.commit();

        if (LOG.isDebugEnabled())
            LOG.debug("Finished creating enhanced class " + _subclassName);

        return result;
    }

    private void createParameterBindingProperty(String parameterName)
    {
        String propertyName = parameterName + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX;

        if (!isMissingProperty(propertyName))
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Establishing parameter binding property " + propertyName);

        createProperty(propertyName, IBinding.class.getName(), false);
    }

	/**
	 *  Creates any properties related to
	 *  {@link org.apache.tapestry.spec.PropertySpecification property specifications}.
	 * 
	 **/
	
    protected void createPropertySpecificationEnhancements()
    {
        List names = _specification.getPropertySpecificationNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            PropertySpecification ps = _specification.getPropertySpecification(name);

            createSpecifiedProperty(ps);
        }
    }

	/**
	 *  Creates new properties related to formal parameters.  This is one
	 *  property to store the binding, and a second property if the parameter
	 *  is connected.
	 * 
	 **/
	
    protected void createParameterEnhancements()
    {
        List names = _specification.getParameterNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            createParameterBindingProperty(name);

            ParameterSpecification ps = _specification.getParameter(name);

            createConnectedParameterProperty(ps);
        }
    }
}