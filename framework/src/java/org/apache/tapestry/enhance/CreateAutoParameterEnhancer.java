// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.Tapestry;

/**
 *  Creates a synthetic property for a
 *  {@link org.apache.tapestry.spec.Direction#AUTO}
 *  parameter.
 *
 *  @author Mindbridge
 *  @since 3.0
 *
 */

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

    private String _propertyName;
    private String _parameterName;
    private Class _type;
    private String _readMethodName;

    public CreateAutoParameterEnhancer(
        String propertyName,
        String parameterName,
        Class type,
        String readMethodName)
    {
        Tapestry.notNull(propertyName, "propertyName");
        Tapestry.notNull(parameterName, "parameterName");
        Tapestry.notNull(type, "type");
        Tapestry.notNull(readMethodName, "readMethodName");

        _propertyName = propertyName;
        _parameterName = parameterName;
        _type = type;
        _readMethodName = readMethodName;
    }

    public void performEnhancement(ClassFab classFab)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating auto property: " + _propertyName);

        String readBindingMethodName =
            CreateAccessorUtils.buildMethodName(
                "get",
                _parameterName + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX);

        createReadMethod(classFab, readBindingMethodName);
        createWriteMethod(classFab, readBindingMethodName);
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

    private void createReadMethod(ClassFab classFab, String readBindingMethodName)
    {
        String castToType = "";
        String bindingValueAccessor = "getObject";

        String specialBindingType = getSpecialBindingType();

        if (specialBindingType != null)
        {
            bindingValueAccessor = CreateAccessorUtils.buildMethodName("get", specialBindingType);
        }
        else
        {
            castToType = "($r)";
        }

        String body =
            MessageFormat.format(
                PARAMETER_ACCESSOR_TEMPLATE,
                new Object[] { readBindingMethodName, bindingValueAccessor, castToType });

        MethodSignature sig = new MethodSignature(_type, _readMethodName, null, null);

        classFab.addMethod(Modifier.PUBLIC | Modifier.FINAL, sig, body);
    }

    private void createWriteMethod(ClassFab classFab, String readBindingMethodName)
    {
        String bindingValueAccessor = "setObject";
        String valueCast = "";

        String specialBindingType = getSpecialBindingType();

        if (specialBindingType != null)
        {
            bindingValueAccessor = CreateAccessorUtils.buildMethodName("set", specialBindingType);
        }
        else
        {
            String castForType = getValueCastType();

            if (castForType != null)
                valueCast = castForType;
        }

        String body =
            MessageFormat.format(
                PARAMETER_MUTATOR_TEMPLATE,
                new Object[] { readBindingMethodName, bindingValueAccessor, valueCast });

        String methodName = CreateAccessorUtils.buildMethodName("set", _propertyName);

        MethodSignature sig =
            new MethodSignature(void.class, methodName, new Class[] { _type }, null);

        classFab.addMethod(Modifier.PUBLIC | Modifier.FINAL, sig, body);
    }
}
