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

import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.apache.tapestry.enhance.CodeGenerationException;

/**
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class ClassMapping
{

    /**
     *  Map of type (as Type), keyed on type name.
     * 
     *  This should be kept in synch with ParameterManager, which maintains
     *  a similar list.
     * 
     **/

    private Map _objectTypeMap = new HashMap();
    private ClassPool _classPool;

    public ClassMapping(ClassPool classPool)
    {
        _classPool = classPool;
        initialize();
    }

    protected void initialize()
    {
        recordType("boolean", CtClass.booleanType);
        recordType("short", CtClass.shortType);
        recordType("int", CtClass.intType);
        recordType("long", CtClass.longType);
        recordType("float", CtClass.floatType);
        recordType("double", CtClass.doubleType);
        recordType("char", CtClass.charType);
        recordType("byte", CtClass.byteType);
        
        try
        {
            loadType("boolean[]");
            loadType("short[]");
            loadType("int[]");
            loadType("long[]");
            loadType("float[]");
            loadType("double[]");
            loadType("char[]");
            loadType("byte[]");

            loadType("java.lang.Object");
            loadType("java.lang.Object[]");

            loadType("java.lang.String");
            loadType("java.lang.String[]");
        }
        catch (NotFoundException e)
        {
            // This exception should not occur since the types above must exist.
            // Nevertheless...
            throw new CodeGenerationException(e);
        }
    }

    public void loadType(String type) throws NotFoundException
    {
        CtClass objectType = _classPool.get(type);
        _objectTypeMap.put(type, objectType);
    }
    
    public void recordType(String type, CtClass objectType)
    {
        _objectTypeMap.put(type, objectType);
    }

    public CtClass getType(String type)
    {
        return (CtClass) _objectTypeMap.get(type);
    }
}
