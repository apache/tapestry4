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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.enhance.CodeGenerationException;
import org.apache.tapestry.enhance.IEnhancedClass;
import org.apache.tapestry.enhance.IEnhancer;

/**
 *  Creates a synthetic property for a
 *  {@link org.apache.tapestry.spec.Direction#AUTO}
 *  parameter.
 *
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class CreateAutoParameterEnhancer implements IEnhancer
{
    private static final Log LOG = LogFactory.getLog(CreateAutoParameterEnhancer.class);

    /**
     *  The code template for the parameter accessor method.    
     *                                      <p>
     *  Legend:                             <br>
     *      {0} = readBindingMethodName     <br>
     *      {1} = binding value accessor    <br>
     *      {2} = cast (if needed)          <br>
     */
    protected static final String PARAMETER_ACCESSOR_TEMPLATE =
        ""
            + "'{'"
            + "  org.apache.tapestry.IBinding binding = {0}();"
            + "  return {2} binding.{1}(); "
            + "'}'";

    /**
     *  The code template for the parameter mutator method.    
     *                                      <p>
     *  Legend:                             <br>
     *      {0} = readBindingMethodName     <br>
     *      {1} = binding value mutator     <br>
     */
    protected static final String PARAMETER_MUTATOR_TEMPLATE =
        ""
            + "'{'"
            + "  org.apache.tapestry.IBinding binding = {0}();"
            + "  binding.{1}($1); "
            + "'}'";

    /**
     *  The list of types that have accessors and mutators 
     *  other than getObject()/setObject.
     *  The key in the Map is the type, the value is the property name in IBinding 
     */
    private static final Map SPECIAL_BINDING_TYPES = new HashMap();

    {
        SPECIAL_BINDING_TYPES.put("boolean", "boolean");
        SPECIAL_BINDING_TYPES.put("int", "int");
        SPECIAL_BINDING_TYPES.put("double", "double");
        SPECIAL_BINDING_TYPES.put("java.lang.String", "string");
    }

    private EnhancedClass _enhancedClass;
    private String _propertyName;
    private String _parameterName;
    private CtClass _type;
    private String _readMethodName;

    public CreateAutoParameterEnhancer(
        EnhancedClass enhancedClass,
        String propertyName,
        String parameterName,
        CtClass type,
        String readMethodName)
    {
        _enhancedClass = enhancedClass;
        _propertyName = propertyName;
        _parameterName = parameterName;
        _type = type;
        _readMethodName = readMethodName;
    }

    public void performEnhancement(IEnhancedClass enhancedClass)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating auto property: " + _propertyName);

        EnhancedClass jaEnhancedClass = (EnhancedClass) enhancedClass;
        ClassFabricator cf = jaEnhancedClass.getClassFabricator();

        String readBindingMethodName =
            cf.buildMethodName("get", _parameterName + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX);

        createReadMethod(cf, readBindingMethodName);
        createWriteMethod(cf, readBindingMethodName);
    }

    private String getSpecialBindingType()
    {
        String typeName = _type.getName();
        return (String) SPECIAL_BINDING_TYPES.get(typeName);
    }

    private void createReadMethod(ClassFabricator cf, String readBindingMethodName)
    {
        String castToType;
        String bindingValueAccessor;

        String specialBindingType = getSpecialBindingType();
        if (specialBindingType != null)
        {
            castToType = "";
            bindingValueAccessor = cf.buildMethodName("get", specialBindingType);
        }
        else
        {
            castToType = "($r)";
            bindingValueAccessor = "getObject";
        }

        String readMethodBody =
            MessageFormat.format(
                PARAMETER_ACCESSOR_TEMPLATE,
                new Object[] { readBindingMethodName, bindingValueAccessor, castToType });

        try
        {
            CtMethod method = cf.createAccessor(_type, _propertyName, _readMethodName);
            method.setBody(readMethodBody);
            cf.addMethod(method);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

    private void createWriteMethod(ClassFabricator cf, String readBindingMethodName)
    {
        String bindingValueAccessor;

        String specialBindingType = getSpecialBindingType();
        if (specialBindingType != null)
        {
            bindingValueAccessor = cf.buildMethodName("set", specialBindingType);
        }
        else
        {
            bindingValueAccessor = "setObject";
        }

        String writeMethodBody =
            MessageFormat.format(
                PARAMETER_MUTATOR_TEMPLATE,
                new Object[] { readBindingMethodName, bindingValueAccessor });

        try
        {
            CtMethod method = cf.createMutator(_type, _propertyName);
            method.setBody(writeMethodBody);
            cf.addMethod(method);
        }
        catch (CannotCompileException e)
        {
            throw new CodeGenerationException(e);
        }
    }

}
