// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Builds a method that accesses component messages.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class MessageAnnotationWorker implements MethodAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        if (!method.getReturnType().equals(String.class))
            throw new ApplicationRuntimeException(AnnotationMessages.returnStringOnly(method
                    .getReturnType()));

        Message message = method.getAnnotation(Message.class);

        String keyName = message.value();

        if (keyName.equals(""))
            keyName = convertMethodNameToKeyName(method.getName());

        BodyBuilder builder = new BodyBuilder();

        builder.begin();

        Class[] parameterTypes = method.getParameterTypes();
        int paramCount = Tapestry.size(parameterTypes);

        if (paramCount > 0)
        {
            builder.addln("java.lang.Object[] params = new java.lang.Object[{0}];", paramCount);
            for (int i = 0; i < paramCount; i++)
            {
                builder.add("params[{0}] = ", i);

                if (parameterTypes[i].isPrimitive())
                    builder.add("($w) ");

                // Parameter $0 is this, so $1 is the first real parameter.

                builder.addln("${0};", i + 1);
            }
        }

        builder.add("return getMessages().");

        if (paramCount == 0)
            builder.addln("getMessage(\"{0}\");", keyName);
        else
            builder.addln("format(\"{0}\", params);", keyName);

        builder.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(method), builder.toString(), location);

        if (isGetter(method))
            op.claimReadonlyProperty(AnnotationUtils.getPropertyName(method));
    }

    boolean isGetter(Method method)
    {
        // We already know the return type is String

        return method.getName().startsWith("get") && method.getParameterTypes().length == 0;
    }

    String convertMethodNameToKeyName(String methodName)
    {
        StringBuffer buffer = new StringBuffer();

        int cursorx = methodName.startsWith("get") ? 3 : 0;
        int length = methodName.length();
        boolean atStart = true;

        while (cursorx < length)
        {
            char ch = methodName.charAt(cursorx);

            if (Character.isUpperCase(ch))
            {
                if (!atStart)
                    buffer.append('-');
                buffer.append(Character.toLowerCase(ch));
            }
            else
                buffer.append(ch);

            atStart = false;

            cursorx++;
        }

        return buffer.toString();
    }
}
