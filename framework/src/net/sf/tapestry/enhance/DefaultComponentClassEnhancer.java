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

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.IComponentClassEnhancer;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.spec.ComponentSpecification;
import org.apache.bcel.classfile.JavaClass;
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
     *  Map of Class, keyed on ComponentSpecification.
     * 
     **/

    private Map _cachedClasses = new HashMap();
    private IResourceResolver _resolver;
    private EnhanceClassLoader _classLoader;

    public DefaultComponentClassEnhancer(IResourceResolver resolver)
    {
        _resolver = resolver;
        _classLoader = new EnhanceClassLoader(_resolver.getClassLoader());
    }

    public synchronized void reset()
    {
        _cachedClasses.clear();
    }

    public IResourceResolver getResourceResolver()
    {
        return _resolver;
    }

    public Class getEnhancedClass(ComponentSpecification specification, String className)
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
        ComponentSpecification specification,
        Class cachedClass)
    {
        _cachedClasses.put(specification, cachedClass);
    }

    protected synchronized Class getCachedClass(ComponentSpecification specification)
    {
        return(Class) _cachedClasses.get(specification);
    }

    /**
     *  Returns the class to be used for the component, which is either
     *  the class with the given name, or an enhanced subclass.
     * 
     **/

    protected Class constructComponentClass(ComponentSpecification specification, String className)
    {
        Class result = _resolver.findClass(className);

        ComponentClassFactory factory = createComponentClassFactory(specification, result);

        if (factory.needsEnhancement())
        {
            JavaClass javaClass = factory.createEnhancedSubclass();

            result = _classLoader.defineClass(javaClass);
        }

        return result;
    }

    protected ComponentClassFactory createComponentClassFactory(
        ComponentSpecification specification,
        Class componentClass)
    {
        return new ComponentClassFactory(_resolver, specification, componentClass);
    }

}