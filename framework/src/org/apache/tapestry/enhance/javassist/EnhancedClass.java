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

    public EnhancedClass(String className, Class parentClass, EnhancedClassFactory classFactory)
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
        if (_classFabricator == null)
        {
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
        IEnhancer enhancer =
            new CreatePropertyEnhancer(
                propertyName,
                getObjectType(propertyType),
                readMethodName,
                persistent);
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
        IEnhancer enhancer =
            new CreateAutoParameterEnhancer(
                this,
                propertyName,
                parameterName,
                getObjectType(typeClassName),
                readMethodName);
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
        return loader.defineClass(
            enhancedClassName,
            enhancedClassBytes,
            _parentClass.getProtectionDomain());
    }

}
