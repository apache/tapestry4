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
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.ParameterSpecification;
import org.apache.tapestry.spec.PropertySpecification;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayType;
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

    protected Class convertPropertyType(String type, Location location)
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
     *  but the accessor(s) are abstract.