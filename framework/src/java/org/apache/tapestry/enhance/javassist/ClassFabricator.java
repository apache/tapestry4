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

import java.io.IOException;
import java.text.MessageFormat;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.enhance.CodeGenerationException;

/**
 *  @author Mindbridge
 *  @since 3.0
 */
public class ClassFabricator
{
    private static final Log LOG = LogFactory.getLog(ClassFabricator.class);

    /**
     *  The code template for the standard property accessor method.    
     *                                      <p>
     *  Legend:                             <br>
     *      {0} = property field name       <br>
     */
    private static final String PROPERTY_ACCESSOR_TEMPLATE = "" +
        "'{'" +
        "  return {0}; " +
        "'}'";
        
    /**
     *  The code template for the standard property mutator method.    
     *                                      <p>
     *  Legend:                             <br>
     *      {0} = property field name       <br>
     */
    private static final String PROPERTY_MUTATOR_TEMPLATE = "" +
        "'{'" +
        "  {0} = $1; " +
        "'}'";
        
    /**
     *  The code template for the standard persistent property mutator method.    
     *                                      <p>
     *  Legend:                             <br>
     *      {0} = property field name       <br>
     *      {1} = property name             <br>
     */
    private static final String PERSISTENT_PROPERTY_MUTATOR_TEMPLATE =
        "" +
        "'{'" +
        "  {0} = $1;" +
        "  fireObservedChange(\"{1}\", {0}); " +
        "'}'";

    private ClassPool _classPool;
    private CtClass _genClass;

    public ClassFabricator(String className, CtClass parentClass, ClassPool classPool)
    {
        _classPool = classPool;
        _genClass = _classPool.makeClass(className, parentClass);
    }

    public CtField getField(String fieldName)
    {
        try
        {
            return _genClass.getField(fieldName);
        }
        catch (NotFoundException e)
        {
            return null;
        }
    }
    
    public void createField(CtClass fieldType, String fieldName)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating field: " + fieldName);

        try
        {
            CtField field = new CtField(fieldType, fieldName, _genClass);
            _genClass.addField(field);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

    public void createField(CtClass fieldType, String fieldName, String init)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating field: " + fieldName + " with initializer: " + init);

        try
        {
            CtField field = new CtField(fieldType, fieldName, _genClass);
            _genClass.addField(field, init);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

    public CtMethod getMethod(String name, String signature)
    {
        try
        {
            return _genClass.getMethod(name, signature);
        }
        catch (NotFoundException e)
        {
            return null;
        }
    }

    public void addMethod(CtMethod method) throws CannotCompileException
    {
        _genClass.addMethod(method);
    }

    /**
     *  Constructs an accessor method name.
     * 
     **/

    public String buildMethodName(String prefix, String propertyName)
    {
        StringBuffer result = new StringBuffer(prefix);

        char ch = propertyName.charAt(0);

        result.append(Character.toUpperCase(ch));

        result.append(propertyName.substring(1));

        return result.toString();
    }

    public CtMethod createMethod(
        CtClass returnType,
        String methodName,
        CtClass[] arguments)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating method: " + methodName);

        CtMethod method = new CtMethod(returnType, methodName, arguments, _genClass);

        return method;
    }

    public CtMethod createAccessor(
        CtClass fieldType,
        String propertyName,
        String readMethodName)
    {
        String methodName =
            readMethodName == null ? buildMethodName("get", propertyName) : readMethodName;

        if (LOG.isDebugEnabled())
            LOG.debug("Creating accessor: " + methodName);

        CtMethod method = new CtMethod(fieldType, methodName, new CtClass[0], _genClass);

        return method;
    }

    /**
     *  Creates an accessor (getter) method for the property.
     * 
     *  @param fieldType the return type for the method
     *  @param fieldName the name of the field (not the name of the property)
     *  @param propertyName the name of the property (used to build the name of the method)
     *  @param readMethodName if not null, the name of the method to use
     * 
     **/

    public void createPropertyAccessor(
        CtClass fieldType,
        String fieldName,
        String propertyName,
        String readMethodName)
    {
        try
        {
            String accessorBody =
                MessageFormat.format(PROPERTY_ACCESSOR_TEMPLATE, new Object[] { fieldName, propertyName });

            CtMethod method = createAccessor(fieldType, propertyName, readMethodName);
            method.setBody(accessorBody);
            _genClass.addMethod(method);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

    public CtMethod createMutator(
        CtClass fieldType,
        String propertyName)
    {
        String methodName = buildMethodName("set", propertyName);

        if (LOG.isDebugEnabled())
            LOG.debug("Creating mutator: " + methodName);

        CtMethod method =
            new CtMethod(CtClass.voidType, methodName, new CtClass[] { fieldType }, _genClass);

        return method;
    }

    /**
     *  Creates a mutator (aka "setter") method.
     * 
     *  @param fieldType type of field value (and type of parameter value)
     *  @param fieldName name of field (not property!)
     *  @param propertyName name of property (used to construct method name)
     *  @param isPersistent if true, adds a call to fireObservedChange()
     * 
     **/

    public void createPropertyMutator(
        CtClass fieldType,
        String fieldName,
        String propertyName,
        boolean isPersistent)
    {
        String bodyTemplate = isPersistent ? PERSISTENT_PROPERTY_MUTATOR_TEMPLATE : PROPERTY_MUTATOR_TEMPLATE;
        String body = MessageFormat.format(bodyTemplate, new Object[] { fieldName, propertyName });

        try
        {
            CtMethod method = createMutator(fieldType, propertyName);
            method.setBody(body);
            _genClass.addMethod(method);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

    
    public void commit()
    {
    }

    public byte[] getByteCode()
    {
        try
        {
            return _genClass.toBytecode();
        }
        catch (NotFoundException e)
        {
            throw new CodeGenerationException(e);
        }
        catch (IOException e)
        {
            throw new CodeGenerationException(e);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

}
