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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IComponentClassEnhancer;
import org.apache.tapestry.enhance.javassist.EnhancedClassFactory;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Default implementation of {@link IComponentClassEnhancer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public class DefaultComponentClassEnhancer implements IComponentClassEnhancer
{
    private static final Log LOG = LogFactory.getLog(DefaultComponentClassEnhancer.class);

    /**
     *  Map of Class, keyed on IComponentSpecification.
     * 
     **/

    private Map _cachedClasses;
    private ClassResolver _resolver;
    private IEnhancedClassFactory _factory;
    private boolean _disableValidation;

    /**
     * @param resolver resource resolver used to locate classes
     * @param disableValidation if true, then validation (of unimplemented abstract methods)
     * is skipped
     */
    public DefaultComponentClassEnhancer(ClassResolver resolver, boolean disableValidation)
    {
        _cachedClasses = Collections.synchronizedMap(new HashMap());
        _resolver = resolver;
        _factory = createEnhancedClassFactory();
        _disableValidation = disableValidation;
    }

    protected IEnhancedClassFactory createEnhancedClassFactory()
    {
        return new EnhancedClassFactory(getResourceResolver());
    }

    public synchronized void reset()
    {
        _cachedClasses.clear();
        _factory.reset();
    }

    public ClassResolver getResourceResolver()
    {
        return _resolver;
    }

    public Class getEnhancedClass(IComponentSpecification specification, String className)
    {
        Class result = getCachedClass(specification);

        if (result == null)
        {
            synchronized (this)
            {
                result = getCachedClass(specification);
                if (result == null)
                {
                    result = constructComponentClass(specification, className);
                    storeCachedClass(specification, result);
                }
            }
        }

        return result;
    }

    protected void storeCachedClass(IComponentSpecification specification, Class cachedClass)
    {
        _cachedClasses.put(specification, cachedClass);
    }

    protected Class getCachedClass(IComponentSpecification specification)
    {
        return (Class) _cachedClasses.get(specification);
    }

    /**
     *  Returns the class to be used for the component, which is either
     *  the class with the given name, or an enhanced subclass.
     * 
     **/

    protected Class constructComponentClass(
        IComponentSpecification specification,
        String className)
    {
        Class result = null;

        try
        {
            result = _resolver.findClass(className);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), specification.getLocation(), ex);
        }

        try
        {
            ComponentClassFactory factory = createComponentClassFactory(specification, result);

            if (factory.needsEnhancement())
            {
                result = factory.createEnhancedSubclass();

                if (!_disableValidation)
                    validateEnhancedClass(result, className, specification);
            }
        }
        catch (CodeGenerationException e)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ComponentClassFactory.code-generation-error", className),
                e);
        }

        return result;
    }

    /**
     *  Constructs a new factory for enhancing the specified class. Advanced users
     *  may want to provide thier own enhancements to classes and this method
     *  is the hook that allows them to provide a subclass of
     *  {@link org.apache.tapestry.enhance.ComponentClassFactory} adding those
     *  enhancements.
     * 
     **/

    protected ComponentClassFactory createComponentClassFactory(
        IComponentSpecification specification,
        Class componentClass)
    {
        return new ComponentClassFactory(_resolver, specification, componentClass, _factory);
    }

    /**
     *  Invoked to validate that an enhanced class is acceptible.  Primarily, this is to ensure
     *  that the class contains no unimplemented abstract methods or fields.  Normally,
     *  this kind of checking is done at compile time, but for generated
     *  classes, there is no compile time check (!) and you can get runtime
     *  errors when accessing unimplemented abstract methods.
     * 
     *
     **/

    protected void validateEnhancedClass(
        Class subject,
        String className,
        IComponentSpecification specification)
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug("Validating " + subject);

        Set implementedMethods = new HashSet();
        Class current = subject;

        while (true)
        {
            Method m = checkForAbstractMethods(current, implementedMethods);

            if (m != null)
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "DefaultComponentClassEnhancer.no-impl-for-abstract-method",
                        new Object[] { m, current, className, subject.getName()}),
                    specification.getLocation(),
                    null);

            // An earlier version of this code walked the interfaces directly,
            // but it appears that implementing an interface actually
            // puts abstract method declarations into the class
            // (at least, in terms of what getDeclaredMethods() returns).

            // March up to the super class.

            current = current.getSuperclass();

            // Once advanced up to a concrete class, we trust that
            // the compiler did its checking.

            if (!Modifier.isAbstract(current.getModifiers()))
                break;
        }

    }

    /**
     *  Searches the class for abstract methods, returning the first found.
     *  Records non-abstract methods in the implementedMethods set.
     * 
     **/

    private Method checkForAbstractMethods(Class current, Set implementedMethods)
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug("Searching for abstract methods in " + current);

        Method[] methods = current.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++)
        {
            Method m = methods[i];

            if (debug)
                LOG.debug("Checking " + m);

            boolean isAbstract = Modifier.isAbstract(m.getModifiers());

            MethodSignature s = new MethodSignature(m);

            if (isAbstract)
            {
                if (implementedMethods.contains(s))
                    continue;

                return m;
            }

            implementedMethods.add(s);
        }

        return null;
    }

}