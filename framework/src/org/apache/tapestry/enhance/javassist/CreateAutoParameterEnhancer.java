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
     *      {2} = value cast
     */
    protected static final String PARAMETER_MUTATOR_TEMPLATE =
        ""
            + "'{'"
            + "  org.apache.tapestry.IBinding binding = {0}();"
            + "  binding.{1}({2} $1); "
            + "'}'";

    /**
     *  The list of types that have accessors and mutators 
     *  other than getObject()/setObject.
     *  The key in the Map is the type, the value is the property name in IBinding 
     */
    private static final Map SPECIAL_BINDING_TYPES = new HashMap();

    static {
        SPECIAL_BINDING_TYPES.put("boolean", "boolean");
        SPECIAL_BINDING_TYPES.put("int", "int");
        SPECIAL_BINDING_TYPES.put("double", "double");
        SPECIAL_BINDING_TYPES.put("java.lang.String", "string");
    }

    private static final Map VALUE_CAST_TYPES = new HashMap();

    static {
        VALUE_CAST_TYPES.put("byte", "($w)");
        VALUE_CAST_TYPES.put("long", "($w)");
        VALUE_CAST_TYPES.put("short", "($w)");
        VALUE_CAST_TYPES.put("char", "($w)");
        VALUE_CAST_TYPES.put("float", "($w)");
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

    private String getValueCastType()
    {
        String typeName = _type.getName();

        return (String) VALUE_CAST_TYPES.get(typeName);
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
        String valueCast = "";

        String specialBindingType = getSpecialBindingType();
        if (specialBindingType != null)
        {
            bindingValueAccessor = cf.buildMethodName("set", specialBindingType);
        }
        else
        {
            bindingValueAccessor = "setObject";

            String castForType = getValueCastType();

            if (castForType != null)
                valueCast = castForType;
        }

        String writeMethodBody =
            MessageFormat.format(
                PARAMETER_MUTATOR_TEMPLATE,
                new Object[] { readBindingMethodName, bindingValueAccessor, valueCast });

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
