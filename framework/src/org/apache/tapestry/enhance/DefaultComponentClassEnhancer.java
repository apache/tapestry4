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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IComponentClassEnhancer;
import org.apache.tapestry.enhance.javassist.EnhancedClassFactory;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Default implementation of {@link org.apache.tapestry.IComponentClassEnhancer}.
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

    private Map _cachedClasses = new HashMap();
    private IResourceResolver _resolver;
    private IEnhancedClassFactory _factory;

    public DefaultComponentClassEnhancer(IResourceResolver resolver)
    {
        _resolver = resolver;
        _factory = createEnhancedClassFactory();
    }
    
    protected IEnhancedClassFactory createEnhancedClassFactory()
    {
        return new EnhancedClassFactory(getResourceResolver());
    }

    public synchronized void reset()
    {
        _cachedClasses.clear();
    }

    public IResourceResolver getResourceResolver()
    {
        return _resolver;
    }

    public Class getEnhancedClass(IComponentSpecification specification, String className)
    {
        Class result = getCachedClass(specification);

        if (result == null)
        {
            result = constructComponentClass(specification, className);
            storeCachedClass(specification, result);
        }

        return result;
    }

    protected synchronized void storeCachedClass(
        IComponentSpecification specification,
        Class cachedClass)
    {
        _cachedClasses.put(specification, cachedClass);
    }

    protected synchronized Class getCachedClass(IComponentSpecification specification)
    {
        return (Class) _cachedClasses.get(specification);
    }

    /**
     *  Returns the class to be used for the component, which is either
     *  the class with the given name, or an enhanced subclass.
     * 
     **/

    protected Class constructComponentClass(IComponentSpecification specification, String className)
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
            
                validateEnhancedClass(result, className, specification);
            }
        }
        catch (CodeGenerationException e)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "ComponentClassFactory.code-generation-error",
                    className),
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