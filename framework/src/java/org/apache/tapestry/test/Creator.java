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

package org.apache.tapestry.test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javassist.CtClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.service.impl.ClassFabImpl;
import org.apache.hivemind.service.impl.CtClassSource;
import org.apache.hivemind.service.impl.HiveMindClassPool;

/**
 * A utility class that is used to instantiate abstract Tapestry pages and
 * components. It creates, at runtime, a subclass where all abstract properties
 * are filled in (complete with instance variable, accessor and mutator methods).
 * This isn't the same as how the class is enhanced at runtime, but is sufficient
 * to unit test the class, especially listener methods.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class Creator
{
    private static final Log LOG = LogFactory.getLog(Creator.class);

    /**
     * Keyed on Class, value is another class (fully enhanced).
     */
    private Map _classes = new HashMap();
    private final CtClassSource _classSource;

    public Creator()
    {
        this(Thread.currentThread().getContextClassLoader());
    }

    public Creator(ClassLoader loader)
    {
        HiveMindClassPool pool = new HiveMindClassPool();

        pool.appendClassLoader(loader);

        _classSource = new CtClassSource(pool);
    }

    private void addAccessorMethod(ClassFab classFab, PropertyDescriptor pd, String attributeName)
    {
        String methodName = getMethodName(pd.getReadMethod(), "get", pd.getName());

        MethodSignature sig = new MethodSignature(pd.getPropertyType(), methodName, null, null);

        classFab.addMethod(Modifier.PUBLIC, sig, "return " + attributeName + ";");
    }

    private void addField(ClassFab classFab, String fieldName, Class fieldType)
    {
        classFab.addField(fieldName, fieldType);
    }

    private void addMissingProperties(ClassFab classFab, BeanInfo info)
    {
        PropertyDescriptor[] pd = info.getPropertyDescriptors();

        for (int i = 0; i < pd.length; i++)
            addMissingProperty(classFab, pd[i]);
    }

    private void addMissingProperty(ClassFab classFab, PropertyDescriptor pd)
    {
        Method readMethod = pd.getReadMethod();
        Method writeMethod = pd.getWriteMethod();

        boolean abstractRead = isAbstract(readMethod);
        boolean abstractWrite = isAbstract(writeMethod);

        if (!(abstractRead || abstractWrite))
            return;

        String attributeName = "_$" + pd.getName();
        Class propertyType = pd.getPropertyType();

        addField(classFab, attributeName, propertyType);

        if (abstractRead)
            addAccessorMethod(classFab, pd, attributeName);

        if (abstractWrite)
            addMutatorMethod(classFab, pd, attributeName);

    }

    private void addMutatorMethod(ClassFab classFab, PropertyDescriptor pd, String attributeName)
    {
        String methodName = getMethodName(pd.getWriteMethod(), "set", pd.getName());

        MethodSignature sig =
            new MethodSignature(void.class, methodName, new Class[] { pd.getPropertyType()}, null);

        classFab.addMethod(Modifier.PUBLIC, sig, attributeName + " = $1;");
    }

    private Class createEnhancedClass(Class inputClass)
    {
        if (inputClass.isInterface() || inputClass.isPrimitive() || inputClass.isArray())
            throw new IllegalArgumentException(ScriptMessages.wrongTypeForEnhancement(inputClass));

        if (!Modifier.isAbstract(inputClass.getModifiers()))
        {
            LOG.error(ScriptMessages.classNotAbstract(inputClass));
            return inputClass;
        }

        BeanInfo info = null;

        try
        {
            info = Introspector.getBeanInfo(inputClass, Object.class);
        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(
                ScriptMessages.unableToIntrospect(inputClass, ex));
        }

        String name = ClassFabUtils.generateClassName("Enhance");

        CtClass newClass = _classSource.newClass(name, inputClass);

        ClassFab classFab = new ClassFabImpl(_classSource, newClass);

        addMissingProperties(classFab, info);

        return classFab.createClass();
    }

    public Class getEnhancedClass(Class inputClass)
    {
        Class result = (Class) _classes.get(inputClass);

        if (result == null)
        {
            result = createEnhancedClass(inputClass);

            _classes.put(inputClass, result);
        }

        return result;
    }

    /**
     * Given a particular abstract class; will create an instance of that class. A subclass
     * is created with all abstract properties filled in with ordinary implementations.
     */
    public Object getInstance(Class abstractClass)
    {
        Class enhancedClass = getEnhancedClass(abstractClass);

        try
        {
            return enhancedClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                ScriptMessages.unableToInstantiate(abstractClass, ex));
        }

    }

    private String getMethodName(Method m, String prefix, String propertyName)
    {
        if (m != null)
            return m.getName();

        StringBuffer buffer = new StringBuffer(prefix);

        buffer.append(propertyName.substring(0, 1).toUpperCase());
        buffer.append(propertyName.substring(1));

        return buffer.toString();
    }

    private boolean isAbstract(Method m)
    {
        return m == null || Modifier.isAbstract(m.getModifiers());
    }
}
