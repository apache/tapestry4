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

package org.apache.tapestry.enhance;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.ToStringBuilder;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Implementation of {@link org.apache.tapestry.enhance.EnhancementOperation}that knows how to
 * provide a {@link org.apache.tapestry.services.ComponentConstructor}from any enhancements.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EnhancementOperationImpl implements EnhancementOperation
{
    private ClassResolver _resolver;

    private IComponentSpecification _specification;

    private Class _baseClass;

    private ClassFab _classFab;

    private Set _claimedProperties = new HashSet();

    private JavaClassMapping _javaClassMapping = new JavaClassMapping();

    private List _constructorTypes = new ArrayList();

    private List _constructorArguments = new ArrayList();

    /**
     * Set of interfaces added to the enhanced class.
     */

    private Set _addedInterfaces = new HashSet();

    /**
     * Map of {@link BodyBuilder}, keyed on {@link MethodSignature}.
     */

    private Map _incompleteMethods = new HashMap();

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

        introspectBaseClass();

        String name = newClassName();

        _classFab = classFactory.newClass(name, _baseClass);
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("baseClass", _baseClass.getName());
        builder.append("claimedProperties", _claimedProperties);
        builder.append("classFab", _classFab);

        return builder.toString();
    }

    /**
     * We want to find the properties of the class, but in many cases, the class is abstract. Some
     * JDK's (Sun) will include public methods from interfaces implemented by the class in the
     * public declared methods for the class (which is used by the Introspector). Eclipse's built-in
     * compiler does not appear to (this may have to do with compiler options I've been unable to
     * track down). The solution is to augment the information provided directly by the Introspector
     * with additional information compiled by Introspecting the interfaces directly or indirectly
     * implemented by the class.
     */
    private void introspectBaseClass()
    {
        try
        {
            synchronized (HiveMind.INTROSPECTOR_MUTEX)
            {
                addPropertiesDeclaredInBaseClass();
            }
        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unabelToIntrospectClass(
                    _baseClass,
                    ex), ex);
        }

    }

    private void addPropertiesDeclaredInBaseClass() throws IntrospectionException
    {
        Class introspectClass = _baseClass;

        addPropertiesDeclaredInClass(introspectClass);

        List interfaceQueue = new ArrayList();

        while (introspectClass != null)
        {
            addInterfacesToQueue(introspectClass, interfaceQueue);

            introspectClass = introspectClass.getSuperclass();
        }

        while (!interfaceQueue.isEmpty())
        {
            Class interfaceClass = (Class) interfaceQueue.remove(0);

            addPropertiesDeclaredInClass(interfaceClass);

            addInterfacesToQueue(interfaceClass, interfaceQueue);
        }
    }

    private void addInterfacesToQueue(Class introspectClass, List interfaceQueue)
    {
        Class[] interfaces = introspectClass.getInterfaces();

        for (int i = 0; i < interfaces.length; i++)
            interfaceQueue.add(interfaces[i]);
    }

    private void addPropertiesDeclaredInClass(Class introspectClass) throws IntrospectionException
    {
        BeanInfo bi = Introspector.getBeanInfo(introspectClass);

        PropertyDescriptor[] pds = bi.getPropertyDescriptors();

        for (int i = 0; i < pds.length; i++)
        {
            PropertyDescriptor pd = pds[i];

            String name = pd.getName();

            if (!_properties.containsKey(name))
                _properties.put(name, pd);
        }
    }

    /**
     * Alternate package private constructor used by the test suite, to bypass the defense checks
     * above.
     */

    EnhancementOperationImpl()
    {
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
        _classFab.addField(name, type);
    }

    public void addField(String name, Class type, Object value)
    {
        _classFab.addField(name, type);

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
        _classFab.addMethod(modifier, sig, methodBody);
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
        StringBuffer buffer = new StringBuffer("_class$");

        Class c = clazz;

        while (c.isArray())
        {
            buffer.append("array$");
            c = c.getComponentType();
        }

        buffer.append(c.getName().replace('.', '$'));

        String fieldName = buffer.toString();

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
        finalizeIncompleteMethods();

        if (_constructorBuilder != null)
        {
            _constructorBuilder.end();

            Class[] types = (Class[]) _constructorTypes
                    .toArray(new Class[_constructorTypes.size()]);

            _classFab.addConstructor(types, null, _constructorBuilder.toString());
        }
    }

    private void finalizeIncompleteMethods()
    {
        Iterator i = _incompleteMethods.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();
            MethodSignature sig = (MethodSignature) e.getKey();
            BodyBuilder builder = (BodyBuilder) e.getValue();

            // Each BodyBuilder is created and given a begin(), this is
            // the matching end()

            builder.end();

            _classFab.addMethod(Modifier.PUBLIC, sig, builder.toString());
        }
    }

    private Constructor findConstructor()
    {
        Class componentClass = _classFab.createClass();

        // The fabricated base class always has exactly one constructor

        return componentClass.getConstructors()[0];
    }

    static int _uid = 0;

    private String newClassName()
    {
        String baseName = _baseClass.getName();
        int dotx = baseName.lastIndexOf('.');

        return "$" + baseName.substring(dotx + 1) + "_" + _uid++;
    }

    public void extendMethodImplementation(Class interfaceClass, MethodSignature methodSignature,
            String code)
    {
        addInterfaceIfNeeded(interfaceClass);

        BodyBuilder builder = (BodyBuilder) _incompleteMethods.get(methodSignature);

        if (builder == null)
        {
            builder = createIncompleteMethod(methodSignature);

            _incompleteMethods.put(methodSignature, builder);
        }

        builder.addln(code);
    }

    private void addInterfaceIfNeeded(Class interfaceClass)
    {
        if (implementsInterface(interfaceClass))
            return;

        _classFab.addInterface(interfaceClass);
        _addedInterfaces.add(interfaceClass);
    }

    public boolean implementsInterface(Class interfaceClass)
    {
        if (interfaceClass.isAssignableFrom(_baseClass))
            return true;

        Iterator i = _addedInterfaces.iterator();
        while (i.hasNext())
        {
            Class addedInterface = (Class) i.next();

            if (interfaceClass.isAssignableFrom(addedInterface))
                return true;
        }

        return false;
    }

    private BodyBuilder createIncompleteMethod(MethodSignature sig)
    {
        BodyBuilder result = new BodyBuilder();

        // Matched inside finalizeIncompleteMethods()

        result.begin();

        if (existingImplementation(sig))
            result.addln("super.{0}($$);", sig.getName());

        return result;
    }

    /**
     * Returns true if the base class implements the provided method as either a public or a
     * protected method.
     */

    private boolean existingImplementation(MethodSignature sig)
    {
        Method m = findMethod(sig);

        return m != null && !Modifier.isAbstract(m.getModifiers());
    }

    /**
     * Finds a public or protected method in the base class.
     */
    private Method findMethod(MethodSignature sig)
    {
        // Finding a public method is easy:

        try
        {
            return _baseClass.getMethod(sig.getName(), sig.getParameterTypes());

        }
        catch (NoSuchMethodException ex)
        {
            // Good; no super-implementation to invoke.
        }

        Class c = _baseClass;

        while (c != Object.class)
        {
            try
            {
                return c.getDeclaredMethod(sig.getName(), sig.getParameterTypes());
            }
            catch (NoSuchMethodException ex)
            {
                // Ok, continue loop up to next base class.
            }

            c = c.getSuperclass();
        }

        return null;
    }

    public List findUnclaimedAbstractProperties()
    {
        List result = new ArrayList();

        Iterator i = _properties.values().iterator();

        while (i.hasNext())
        {
            PropertyDescriptor pd = (PropertyDescriptor) i.next();

            String name = pd.getName();

            if (_claimedProperties.contains(name))
                continue;

            if (isAbstractProperty(pd))
                result.add(name);
        }

        return result;
    }

    /**
     * A property is abstract if either its read method or it write method is abstract. We could do
     * some additional checking to ensure that both are abstract if either is. Note that in many
     * cases, there will only be one accessor (a reader or a writer).
     */
    private boolean isAbstractProperty(PropertyDescriptor pd)
    {
        return isExistingAbstractMethod(pd.getReadMethod())
                || isExistingAbstractMethod(pd.getWriteMethod());
    }

    private boolean isExistingAbstractMethod(Method m)
    {
        return m != null && Modifier.isAbstract(m.getModifiers());
    }
}