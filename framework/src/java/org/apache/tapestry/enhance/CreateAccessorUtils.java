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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.MethodSignature;

/**
 * Static methods for creating new accessor and mutator methods.
 * 
 * @author Mindbridge
 * @since 3.0
 */
public class CreateAccessorUtils
{
    private static final Log LOG = LogFactory.getLog(CreateAccessorUtils.class);

    /**
     * The code template for the standard property accessor method.
     * <p>
     * Legend: <br>
     * {0} = property field name <br>
     */
    private static final String PROPERTY_ACCESSOR_TEMPLATE = "" + "'{'" + "  return {0}; " + "'}'";

    /**
     * The code template for the standard property mutator method.
     * <p>
     * Legend: <br>
     * {0} = property field name <br>
     */
    private static final String PROPERTY_MUTATOR_TEMPLATE = "" + "'{'" + "  {0} = $1; " + "'}'";

    /**
     * The code template for the standard persistent property mutator method.
     * <p>
     * Legend: <br>
     * {0} = property field name <br>
     * {1} = property name <br>
     */
    private static final String PERSISTENT_PROPERTY_MUTATOR_TEMPLATE = "'{' {0} = $1;  org.apache.tapestry.Tapestry#fireObservedChange(this, \"{1}\", ($w) {0}); '}'";

    /**
     * Constructs an accessor method name.
     */

    public static String buildMethodName(String prefix, String propertyName)
    {
        StringBuffer result = new StringBuffer(prefix);

        char ch = propertyName.charAt(0);

        result.append(Character.toUpperCase(ch));

        result.append(propertyName.substring(1));

        return result.toString();
    }

    /**
     * Creates an accessor (getter) method for the property.
     * 
     * @param fieldType
     *            the return type for the method
     * @param fieldName
     *            the name of the field (not the name of the property)
     * @param propertyName
     *            the name of the property (used to build the name of the method)
     * @param readMethodName
     *            if not null, the name of the method to use
     */

    public static void createPropertyAccessor(ClassFab classFab, Class fieldType, String fieldName,
            String propertyName, String methodName)
    {
        String body = MessageFormat.format(PROPERTY_ACCESSOR_TEMPLATE, new Object[]
        { fieldName, propertyName });

        MethodSignature sig = new MethodSignature(fieldType, methodName, null, null);

        classFab.addMethod(Modifier.PUBLIC | Modifier.FINAL, sig, body);
    }

    /**
     * Creates a mutator (aka "setter") method.
     * 
     * @param fieldType
     *            type of field value (and type of parameter value)
     * @param fieldName
     *            name of field (not property!)
     * @param propertyName
     *            name of property (used to construct method name)
     * @param isPersistent
     *            if true, adds a call to fireObservedChange()
     */

    public static void createPropertyMutator(ClassFab classFab, Class fieldType, String fieldName,
            String propertyName, boolean isPersistent)
    {
        String bodyTemplate = isPersistent ? PERSISTENT_PROPERTY_MUTATOR_TEMPLATE
                : PROPERTY_MUTATOR_TEMPLATE;

        String body = MessageFormat.format(bodyTemplate, new Object[]
        { fieldName, propertyName });
        String methodName = buildMethodName("set", propertyName);

        MethodSignature sig = new MethodSignature(void.class, methodName, new Class[]
        { fieldType }, null);

        classFab.addMethod(Modifier.PUBLIC | Modifier.FINAL, sig, body);
    }
}