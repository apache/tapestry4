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

package org.apache.tapestry.enhance.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.hivemind.ClassResolver;
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

        reset();
    }
    
    protected ClassPool createClassPool()
    {
        ClassLoader loader = _resourceResolver.getClassLoader();
        
        // create a new ClassPool and make sure it uses the application resource resolver 
        ClassPool classPool = new ClassPool(null);
        classPool.insertClassPath(new LoaderClassPath(loader));
        
        return classPool;
    }
    
    /**
     * @see org.apache.tapestry.enhance.IEnhancedClassFactory#reset()
     */
    public synchronized void reset()
    {
        // create a new class pool and discard the previous one
        _classPool = createClassPool();
        _typeMap = new ClassMapping(_classPool);

        ClassLoader loader = _resourceResolver.getClassLoader();
        _enhancedClassLoader = new EnhancedClassLoader(loader);
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
        return _typeMap;
    }

    /**
     *  Given the java class, returns the equivalent {@link CtClass type}.  In addition,
     *  knows about scalar types, arrays of scalar types, java.lang.Object[] and
     *  java.lang.String[].
     * 
     **/

    public CtClass getObjectType(String type)
    {
        CtClass result = getClassMapping().getType(type);

        if (result == null)
        {
            synchronized (this) {
                result = getClassMapping().getType(type);

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
            }
        }

        return result;
    }


    /**
     * @return The class loader to be used to create the enhanced class
     */
    public EnhancedClassLoader getEnhancedClassLoader()
    {
        return _enhancedClassLoader;
    }

}
