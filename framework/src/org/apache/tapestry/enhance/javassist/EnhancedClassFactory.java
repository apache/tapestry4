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
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.commons.hivemind.ClassResolver;
import org.apache.tapestry.enhance.CodeGenerationException;
import org.apache.tapestry.enhance.EnhancedClassLoader;
import org.apache.tapestry.enhance.IEnhancedClass;
import org.apache.tapestry.enhance.IEnhancedClassFactory;

/**
 *  This class defines the factory for creation of new Javassist enhanced classes. 
 *  There is typically only one object of this class in the system. 
 *  Common functionality objects for Javassist enhancement are stored here.
 * 
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class EnhancedClassFactory implements IEnhancedClassFactory
{
    private ClassResolver _resourceResolver;
    private EnhancedClassLoader _enhancedClassLoader;
    private ClassPool _classPool;

    private ClassMapping _typeMap = null;

    public EnhancedClassFactory(ClassResolver resourceResolver)
    {
        _resourceResolver = resourceResolver;
        ClassLoader loader = _resourceResolver.getClassLoader();
        
        _enhancedClassLoader = new EnhancedClassLoader(loader);

        // get the default ClassPool and make sure it uses the application resource resolver 
        _classPool = ClassPool.getDefault();
        _classPool.insertClassPath(new LoaderClassPath(loader));

    }

    /**
     * @see org.apache.tapestry.enhance.IEnhancedClassFactory#createEnhancedClass(java.lang.String, java.lang.Class)
     */
    public IEnhancedClass createEnhancedClass(String className, Class parentClass)
    {
        return new EnhancedClass(className, parentClass, this);
    }

    public ClassPool getClassPool()
    {
        return _classPool;
    }

    public ClassMapping getClassMapping()
    {
        if (_typeMap == null)
            _typeMap = new ClassMapping(_classPool);
        return _typeMap;
    }

    /**
     *  Given the java class, returns the equivalent {@link Type}.  In addition,
     *  knows about scalar types, arrays of scalar types, java.lang.Object[] and
     *  java.lang.String[].
     * 
     **/

    public CtClass getObjectType(String type)
    {
        CtClass result = getClassMapping().getType(type);

        if (result == null)
        {
            try
            {
                result = _classPool.get(type);
                getClassMapping().recordType(type, result);
            }
            catch (NotFoundException e)
            {
                throw new CodeGenerationException(e);
            }
        }

        return result;
    }


    /**
     * @return
     */
    public EnhancedClassLoader getEnhancedClassLoader()
    {
        return _enhancedClassLoader;
    }

}
