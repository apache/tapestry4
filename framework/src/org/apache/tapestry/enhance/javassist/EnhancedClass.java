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

package org.apache.tapestry.enhance.javassist;

import javassist.ClassPool;
import javassist.CtClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.enhance.BaseEnhancedClass;
import org.apache.tapestry.enhance.EnhancedClassLoader;
import org.apache.tapestry.enhance.IEnhancer;

/**
 *  Represents a class to be enhanced using Javassist. 
 * 
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class EnhancedClass extends BaseEnhancedClass
{
    private static final Log LOG = LogFactory.getLog(EnhancedClass.class);

    private String _className;
    private Class _parentClass;
    private EnhancedClassFactory _classFactory;

    private ClassFabricator _classFabricator = null;
    
    public EnhancedClass(
        String className,
        Class parentClass,
        EnhancedClassFactory classFactory)
    {
        _className = className;
        _parentClass = parentClass;
        _classFactory = classFactory;
    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClass#getClassName()
     */
    public String getClassName()
    {
        return _className;
    }
    
    public CtClass getObjectType(String type)
    {
        return _classFactory.getObjectType(type);
    }

    public ClassFabricator getClassFabricator()
    {
        if (_classFabricator == null) {
            CtClass jaParentClass = getObjectType(_parentClass.getName());
            ClassPool classPool = _classFactory.getClassPool();
            _classFabricator = new ClassFabricator(_className, jaParentClass, classPool);
        }
        return _classFabricator;
    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClass#createProperty(java.lang.String, java.lang.String)
     */
    public void createProperty(String propertyName, String propertyType)
    {
        createProperty(propertyName, propertyType, null, false);
    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClass#createProperty(java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public void createProperty(
        String propertyName,
        String propertyType,
        String readMethodName,
        boolean persistent)
    {
        IEnhancer enhancer = new CreatePropertyEnhancer(propertyName, getObjectType(propertyType), readMethodName, persistent);
        addEnhancer(enhancer);
    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClass#createAutoParameter(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void createAutoParameter(
        String propertyName,
        String parameterName,
        String typeClassName,
        String readMethodName)
    {
        IEnhancer enhancer = new CreateAutoParameterEnhancer(this, propertyName, parameterName, getObjectType(typeClassName), readMethodName);
        addEnhancer(enhancer);
    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClass#createEnhancedSubclass()
     */
    public Class createEnhancedSubclass()
    {
        performEnhancement();

        ClassFabricator cf = getClassFabricator();
        cf.commit();
        
        String enhancedClassName = getClassName();
        byte[] enhancedClassBytes = cf.getByteCode();
        
        EnhancedClassLoader loader = _classFactory.getEnhancedClassLoader();
        return loader.defineClass(enhancedClassName, enhancedClassBytes);
    }

}
