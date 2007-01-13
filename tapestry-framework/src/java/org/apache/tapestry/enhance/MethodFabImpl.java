// Copyright 2004, 2005 The Apache Software Foundation
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

import javassist.CtClass;
import javassist.CtMethod;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.MethodFab;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.service.impl.CtClassSource;

/**
 * Implementation of hivemind {@link MethodFab} utiltity.
 */
public class MethodFabImpl implements MethodFab
{
    private CtClassSource _source;

    private MethodSignature _signature;

    private CtMethod _method;

    private StringBuffer _descriptionBody = new StringBuffer();

    public MethodFabImpl(CtClassSource source, MethodSignature signature, CtMethod method,
            String body)
    {
        _source = source;
        _signature = signature;
        _method = method;

        _descriptionBody.append(body);
    }

    /**
     * Returns a a text representation of the method, parameters and method body.
     */
    public String toString()
    {
        if (_method == null)
            return "";
        
        StringBuffer buffer = new StringBuffer();

        try
        {
            buffer.append(Modifier.toString(_method.getModifiers()));

            buffer.append(" ");
            buffer.append(_method.getReturnType().getName());

            buffer.append(" ");
            buffer.append(_method.getName());
            buffer.append("(");

            CtClass[] params = _method.getParameterTypes();

            for (int i = 0; i < params.length; i++)
            {
                if (i > 0)
                    buffer.append(", ");

                buffer.append(params[i].getName());

                buffer.append(" $");
                buffer.append(i + 1);
            }
            buffer.append(")");

            CtClass[] exceptions = _method.getExceptionTypes();

            for (int i = 0; i < exceptions.length; i++)
            {
                if (i == 0)
                    buffer.append("\n  throws ");
                else
                    buffer.append(", ");

                buffer.append(exceptions[i].getName());
            }

            buffer.append("\n");
            buffer.append(_descriptionBody);
        }
        catch (Exception ex)
        {
            buffer.append(" *** ");
            buffer.append(ex);
        }

        return buffer.toString();
    }

    public void addCatch(Class exceptionClass, String catchBody)
    {
        if (_method == null)
            return;
        
        CtClass ctException = _source.getCtClass(exceptionClass);

        try
        {
            _method.addCatch(catchBody, ctException);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unableToAddCatch(
                    exceptionClass,
                    _method,
                    ex), ex);
        }

        _descriptionBody.append("\ncatch(");
        _descriptionBody.append(exceptionClass.getName());
        _descriptionBody.append(" $e)\n");
        _descriptionBody.append(catchBody);
    }

    public void extend(String body, boolean asFinally)
    {
        if (_method == null)
            return;
        
        try
        {
            _method.insertAfter(body, asFinally);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unableToExtendMethod(
                    _signature,
                    _method.getDeclaringClass().getName(),
                    ex), ex);
        }

        _descriptionBody.append("\n");

        if (asFinally)
            _descriptionBody.append("finally\n");

        _descriptionBody.append(body);
    }


}
