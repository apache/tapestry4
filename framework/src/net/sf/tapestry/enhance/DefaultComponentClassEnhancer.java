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
package net.sf.tapestry.enhance;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponentClassEnhancer;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.Direction;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.spec.PropertySpecification;
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
 *  Default implementation of {@link net.sf.tapestry.IComponentClassEnhancer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public class DefaultComponentClassEnhancer implements IComponentClassEnhancer
{
    private static final Log LOG = LogFactory.getLog(DefaultComponentClassEnhancer.class);

    /**
     *  UID used to generate new class names.
     * 
     **/

    private int _uid = 0;

    /**
     *  Map of Class, keyed on ComponentSpecification.
     * 
     **/

    private Map _cachedClasses = new HashMap();
    private IResourceResolver _resolver;
    private EnhanceClassLoader _classLoader;

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

    public DefaultComponentClassEnhancer(IResourceResolver resolver)
    {
        _resolver = resolver;
        _classLoader = new EnhanceClassLoader(resolver.getClassLoader());

        initializeTypeMaps();
    }

    public synchronized void reset()
    {
        _cachedClasses.clear();
        initializeTypeMaps();
    }

    private void initializeTypeMaps()
    {
        _typeMap.clear();
        _objectTypeMap.clear();

        recordType("boolean", boolean.class, Type.BOOLEAN);
        recordType("short", short.class, Type.SHORT);
        recordType("int", int.class, Type.INT);
        recordType("long", long.class, Type.LONG);
        recordType("float", float.class, Type.FLOAT);
        recordType("double", double.class, Type.DOUBLE);
        recordType("char", char.class, Type.CHAR);
        recordType("byte", byte.class, Type.BYTE);
    }

    private void recordType(String name, Class type, Type objectType)
    {
        _typeMap.put(name, type);
        _objectTypeMap.put(name, objectType);
    }

    public synchronized Class getEnhancedClass(
        ComponentSpecification specification,
        String className)
    {
        Class result = (Class) _cachedClasses.get(specification);

        if (result == null)
        {
            result = constructComponentClass(specification, className);

            _cachedClasses.put(specification, result);
        }

        return result;
    }

    /**
     *  Returns the class to be used for the component, which is either
     *  the class with the given name, or an enhanced subclass.
     * 
     **/

    protected Class constructComponentClass(ComponentSpecification specification, String className)
    {
        Class result = _resolver.findClass(className);

        if (needsEnhancement(result, specification))
            result = createEnhancedSubclass(result, specification);

        return result;
    }

    /**
     *  Examines the specification, identifies if any enhancements will be needed.
     *  This implementation looks for the presence of any
     *  {@link net.sf.tapestry.spec.PropertySpecification}s, or any
     *  connected parameters where the property is missing or abstract.
     * 
     **/

    protected boolean needsEnhancement(Class componentClass, ComponentSpecification specification)
    {
        if (!specification.getPropertySpecificationNames().isEmpty())
            return true;

        if (checkConnectedParameters(componentClass, specification))
            return true;

        return false;
    }

    private boolean checkConnectedParameters(
        Class componentClass,
        ComponentSpecification specification)
    {
        List names = specification.getParameterNames();
        int count = names.size();

        Map beanProperties = getBeanProperties(componentClass);

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);
            ParameterSpecification pspec = specification.getParameter(name);

            if (checkConnectedParameter(beanProperties, pspec))
                return true;
        }

        return false;
    }

    /**
     *  Returns true if the parameter is connected (has a non-custom direction) and the
     *  corresponding property is either missing or abstract.
     * 
     **/

    private boolean checkConnectedParameter(Map beanProperties, ParameterSpecification spec)
    {
        Direction direction = spec.getDirection();

        if (direction == Direction.CUSTOM)
            return false;

        String propertyName = spec.getPropertyName();

        PropertyDescriptor d = (PropertyDescriptor) beanProperties.get(propertyName);

        // If the property is entirely missing, then
        // assume it needs creation.

        if (d == null)
            return true;

        // No existing property matches, so we'll return true to
        // create an enhanced class with the property.

        boolean readAbstract = isAbstract(d.getReadMethod());

        boolean writeAbstract = isAbstract(d.getWriteMethod());

        return readAbstract && writeAbstract;
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

    private Class createEnhancedSubclass(Class startClass, ComponentSpecification specification)
    {
        if (LOG.isDebugEnabled())
            LOG.debug(
                "Enhancing class "
                    + startClass.getName()
                    + " for "
                    + specification.getSpecificationLocation());

        Class result = null;

        String startClassName = startClass.getName();
        String enhancedName = startClassName + "$Enhance_" + _uid++;

        Map beanProperties = getBeanProperties(startClass);

        ClassFabricator cf = new ClassFabricator(enhancedName, startClassName);

        cf.addDefaultConstructor();

        List names = specification.getPropertySpecificationNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            PropertySpecification ps = specification.getPropertySpecification(name);

            createSpecifiedProperty(cf, startClass, beanProperties, ps);
        }

        names = specification.getParameterNames();
        count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            ParameterSpecification ps = specification.getParameter(name);

            createParameterProperty(cf, startClass, beanProperties, ps);
        }

        JavaClass jc = cf.commit();

        result = _classLoader.defineClass(jc);

        if (LOG.isDebugEnabled())
            LOG.debug("Finished creating enhanced class " + result.getName());

        return result;
    }

    /**
     *  Invoked to create the specified property. 
     * 
     **/

    protected void createSpecifiedProperty(
        ClassFabricator cf,
        Class beanClass,
        Map beanProperties,
        PropertySpecification ps)
    {
        String propertyName = ps.getName();

        if (LOG.isDebugEnabled())
            LOG.debug("Establishing specified property " + propertyName);

        createProperty(
            cf,
            beanClass,
            beanProperties,
            propertyName,
            ps.getType(),
            ps.isPersistent());
    }

    /**
     *  Creates a property from a parameter specdification.
     * 
     **/

    protected void createParameterProperty(
        ClassFabricator cf,
        Class beanClass,
        Map beanProperties,
        ParameterSpecification ps)
    {
        if (ps.getDirection() == Direction.CUSTOM)
            return;

        String propertyName = ps.getPropertyName();

        // Yes, but does it *need* a property created?

        if (!isMissingParameterProperty(beanClass, beanProperties, propertyName))
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Establishing parameter property " + propertyName);

        createProperty(cf, beanClass, beanProperties, propertyName, ps.getType(), false);
    }

    protected boolean isMissingParameterProperty(
        Class beanClass,
        Map beanProperties,
        String propertyName)
    {
        PropertyDescriptor pd = (PropertyDescriptor) beanProperties.get(propertyName);

        if (pd == null)
            return true;

        return isAbstract(pd.getReadMethod()) && isAbstract(pd.getWriteMethod());

        // Degenerate case:  one accessor abstract, the other real!  Not handled
        // here.
    }

    /**
     *  Checks that the superclass provides
     *  either abstract accessors or none at all.  Creates the file, creates 
     *  the accessors, creates initialization code.
     * 
     **/

    protected void createProperty(
        ClassFabricator cf,
        Class beanClass,
        Map beanProperties,
        String propertyName,
        String type,
        boolean persistent)
    {
        Class propertyType = convertPropertyType(type);

        String readMethodName =
            checkAccessors(beanClass, beanProperties, propertyName, propertyType);

        String fieldName = "_$" + propertyName;

        Type fieldType = getObjectType(type);

        cf.addField(fieldType, fieldName);

        createAccessor(cf, fieldType, fieldName, propertyName, readMethodName);
        createMutator(cf, fieldType, fieldName, propertyName, persistent);
    }

    protected void createAccessor(
        ClassFabricator cf,
        Type fieldType,
        String fieldName,
        String propertyName,
        String readMethodName)
    {
        String methodName =
            readMethodName == null ? buildMethodName("get", propertyName) : readMethodName;

        MethodFabricator mf = cf.createMethod(Constants.ACC_PUBLIC, fieldType, methodName);

        InstructionList il = mf.getInstructionList();
        InstructionFactory factory = cf.getInstructionFactory();

        il.append(factory.createThis());
        il.append(factory.createGetField(cf.getClassName(), fieldName, fieldType));
        il.append(factory.createReturn(fieldType));

        mf.commit();
    }

    protected void createMutator(
        ClassFabricator cf,
        Type fieldType,
        String fieldName,
        String propertyName,
        boolean isPersistent)
    {
        String methodName = buildMethodName("set", propertyName);

        MethodFabricator mf = cf.createMethod(methodName);
        mf.addArgument(fieldType, propertyName);

        InstructionList il = mf.getInstructionList();
        InstructionFactory factory = cf.getInstructionFactory();

        il.append(factory.createThis());
        il.append(factory.createLoad(fieldType, 1));
        il.append(factory.createPutField(cf.getClassName(), fieldName, fieldType));

        // Persistent properties must invoke fireObservedChange

        if (isPersistent)
        {
            il.append(factory.createThis());
            il.append(new PUSH(cf.getConstantPool(), propertyName));
            il.append(factory.createLoad(fieldType, 1));

            Type argumentType = convertToArgumentType(fieldType);

            il.append(
                factory.createInvoke(
                    cf.getClassName(),
                    "fireObservedChange",
                    Type.VOID,
                    new Type[] { Type.STRING, argumentType },
                    Constants.INVOKEVIRTUAL));
        }

        il.append(InstructionConstants.RETURN);

        mf.commit();
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

    protected String buildMethodName(String prefix, String propertyName)
    {
        StringBuffer result = new StringBuffer(prefix);

        char ch = propertyName.charAt(0);

        result.append(Character.toUpperCase(ch));

        result.append(propertyName.substring(1));

        return result.toString();
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

    /**
     *  Checks to see that that class either doesn't provide the property, or does
     *  but the accessor(s) are abstract.  Returns the name of the read accessor,
     *  or null if there is no such accessor (this is helpful if the beanClass
     *  defines a boolean property, where the name of the accessor may be isXXX or
     *  getXXX).
     * 
     **/

    protected String checkAccessors(
        Class beanClass,
        Map beanProperties,
        String propertyName,
        Class propertyType)
    {
        PropertyDescriptor d = (PropertyDescriptor) beanProperties.get(propertyName);

        if (d == null)
            return null;

        if (!d.getPropertyType().equals(propertyType))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultComponentClassEnhancer.property-type-mismatch",
                    new Object[] {
                        beanClass.getName(),
                        propertyName,
                        d.getPropertyType().getName(),
                        propertyType.getName()}));

        Method m = d.getWriteMethod();

        if (!isAbstract(m))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultComponentClassEnhancer.non-abstract-write",
                    m.getDeclaringClass().getName(),
                    propertyName));

        m = d.getReadMethod();

        if (!isAbstract(m))
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultComponentClassEnhancer.non-abstract-read",
                    m.getDeclaringClass().getName(),
                    propertyName));

        return m == null ? null : m.getName();
    }

    protected BeanInfo getBeanInfo(Class beanClass)
    {
        BeanInfo result = null;

        try
        {
            result = Introspector.getBeanInfo(beanClass);

        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultComponentClassEnhancer.unable-to-introspect-class",
                    beanClass.getName()),
                ex);
        }

        return result;
    }

    /**
     *  Examines the specified class and returns a Map, keyed on propertyName
     *  of {@link java.beans.PropertyDescriptor}.
     * 
     **/

    protected Map getBeanProperties(Class beanClass)
    {
        BeanInfo info = getBeanInfo(beanClass);

        Map result = new HashMap();

        PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

        for (int i = 0; i < descriptors.length; i++)
        {
            result.put(descriptors[i].getName(), descriptors[i]);
        }

        return result;
    }
}