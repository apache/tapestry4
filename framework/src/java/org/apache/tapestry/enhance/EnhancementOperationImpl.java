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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFactory;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.Defense;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Implementation of {@link org.apache.tapestry.enhance.EnhancementOperation}that knows how to
 * provide a {@link org.apache.tapestry.services.ComponentConstructor}from any enhancements.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class EnhancementOperationImpl implements EnhancementOperation
{
    private ClassResolver _resolver;

    private IComponentSpecification _specification;

    private Class _baseClass;

    private ClassFactory _classFactory;

    private ClassFab _classFab;

    private Set _claimedProperties = new HashSet();

    private JavaClassMapping _javaClassMapping = new JavaClassMapping();

    private List _constructorTypes = new ArrayList();

    private List _constructorArguments = new ArrayList();

    /**
     * Keyed on class to instance variable name.
     */

    private Map _classReferences = new HashMap();

    /**
     * Map of property names to {@link PropertyDescriptor}.
     */

    private Map _properties = new HashMap();

    /**
     * Used to incrementally assemble the constructor for the enhanced class.
     */

    private BodyBuilder _constructorBuilder;

    public EnhancementOperationImpl(ClassResolver classResolver,
            IComponentSpecification specification, Class baseClass, ClassFactory classFactory)
    {
        Defense.notNull(classResolver, "classResolver");
        Defense.notNull(specification, "specification");
        Defense.notNull(baseClass, "baseClass");
        Defense.notNull(classFactory, "classFactory");

        _resolver = classResolver;
        _specification = specification;
        _baseClass = baseClass;
        _classFactory = classFactory;

        introspectBaseClass();
    }

    private void introspectBaseClass()
    {
        // TODO: Need some kind of JVM lock here on Introspection!

        try
        {
            synchronized (HiveMind.INTROSPECTOR_MUTEX)
            {
                BeanInfo bi = Introspector.getBeanInfo(_baseClass);

                PropertyDescriptor[] pds = bi.getPropertyDescriptors();

                for (int i = 0; i < pds.length; i++)
                {
                    PropertyDescriptor pd = pds[i];

                    _properties.put(pd.getName(), pd);
                }
            }
        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unabelToIntrospectClass(
                    _baseClass,
                    ex), ex);
        }

    }

    /**
     * Alternate package private constructor used by the test suite, to bypass the defense checks
     * above.
     */

    EnhancementOperationImpl()
    {
    }

    public IComponentSpecification getSpecification()
    {
        return _specification;
    }

    public void claimProperty(String propertyName)
    {
        Defense.notNull(propertyName, "propertyName");

        if (_claimedProperties.contains(propertyName))
            throw new ApplicationRuntimeException(EnhanceMessages.claimedProperty(propertyName));

        _claimedProperties.add(propertyName);
    }

    public void addField(String name, Class type)
    {
        classFab().addField(name, type);
    }

    public void addField(String name, Class type, Object value)
    {
        classFab().addField(name, type);

        int x = addConstructorParameter(type, value);

        constructorBuilder().addln("{0} = ${1};", name, Integer.toString(x));
    }

    public Class convertTypeName(String type)
    {
        Defense.notNull(type, "type");

        Class result = _javaClassMapping.getType(type);

        if (result == null)
        {
            result = _resolver.findClass(type);

            _javaClassMapping.recordType(type, result);
        }

        return result;
    }

    public Class getPropertyType(String name)
    {
        Defense.notNull(name, "name");

        PropertyDescriptor pd = getPropertyDescriptor(name);

        return pd == null ? null : pd.getPropertyType();
    }

    public void validateProperty(String name, Class expectedType)
    {
        Defense.notNull(name, "name");
        Defense.notNull(expectedType, "expectedType");

        PropertyDescriptor pd = getPropertyDescriptor(name);

        if (pd == null)
            return;

        Class propertyType = pd.getPropertyType();

        if (propertyType.equals(expectedType))
            return;

        throw new ApplicationRuntimeException(EnhanceMessages.propertyTypeMismatch(
                _baseClass,
                name,
                propertyType,
                expectedType));
    }

    private PropertyDescriptor getPropertyDescriptor(String name)
    {
        return (PropertyDescriptor) _properties.get(name);
    }

    public String getAccessorMethodName(String propertyName)
    {
        Defense.notNull(propertyName, "propertyName");

        PropertyDescriptor pd = getPropertyDescriptor(propertyName);

        if (pd != null && pd.getReadMethod() != null)
            return pd.getReadMethod().getName();

        return EnhanceUtils.createAccessorMethodName(propertyName);
    }

    public void addMethod(int modifier, MethodSignature sig, String methodBody)
    {
        classFab().addMethod(modifier, sig, methodBody);
    }

    public Class getBaseClass()
    {
        return _baseClass;
    }

    public String getClassReference(Class clazz)
    {
        Defense.notNull(clazz, "clazz");

        String result = (String) _classReferences.get(clazz);

        if (result == null)
            result = addClassReference(clazz);

        return result;
    }

    private String addClassReference(Class clazz)
    {
        String fieldName = "_class$" + clazz.getName().replace('.', '$');

        addField(fieldName, Class.class, clazz);

        _classReferences.put(clazz, fieldName);

        return fieldName;
    }

    /**
     * Adds a new constructor parameter, returning the new count. This is convienient, because the
     * first element added is accessed as $1, etc.
     */

    private int addConstructorParameter(Class type, Object value)
    {
        _constructorTypes.add(type);
        _constructorArguments.add(value);

        return _constructorArguments.size();
    }

    private BodyBuilder constructorBuilder()
    {
        if (_constructorBuilder == null)
        {
            _constructorBuilder = new BodyBuilder();
            _constructorBuilder.begin();
        }

        return _constructorBuilder;
    }

    public boolean hasEnhancements()
    {
        return _classFab != null;
    }

    public void forceEnhancement()
    {
        classFab();
    }

    /**
     * Returns an object that can be used to construct instances of the enhanced component subclass.
     * This should only be called once.
     */

    public ComponentConstructor getConstructor()
    {
        finalizeEnhancedClass();

        Constructor c = findConstructor();

        Object[] params = _constructorArguments.toArray();

        return new ComponentConstructorImpl(c, params, _specification.getLocation());
    }

    private void finalizeEnhancedClass()
    {
        if (_classFab == null)
            return;

        if (_constructorBuilder != null)
        {
            _constructorBuilder.end();

            Class[] types = (Class[]) _constructorTypes
                    .toArray(new Class[_constructorTypes.size()]);

            _classFab.addConstructor(types, null, _constructorBuilder.toString());
        }
    }

    private Constructor findConstructor()
    {
        if (_classFab == null)
            return findEmptyConstructorInBaseClass();

        Class componentClass = _classFab.createClass();

        // The fabricated base class always has exactly one constructor

        return componentClass.getConstructors()[0];
    }

    public Constructor findEmptyConstructorInBaseClass()
    {
        try
        {
            return _baseClass.getConstructor(new Class[0]);
        }
        catch (NoSuchMethodException ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.missingConstructor(_baseClass),
                    ex);
        }

    }

    private ClassFab classFab()
    {
        if (_classFab == null)
        {
            String name = newClassName();

            _classFab = _classFactory.newClass(name, _baseClass, _resolver.getClassLoader());
        }

        return _classFab;
    }

    static int _uid = 0;

    private String newClassName()
    {
        String baseName = _baseClass.getName();
        int dotx = baseName.lastIndexOf('.');

        return "$" + baseName.substring(dotx + 1) + "_" + _uid++;
    }
}