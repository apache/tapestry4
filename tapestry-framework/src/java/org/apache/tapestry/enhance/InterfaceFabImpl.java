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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.InterfaceFab;
import org.apache.hivemind.service.MethodSignature;

/**
 * @author Howard M. Lewis Ship
 */
public class InterfaceFabImpl extends AbstractFab implements InterfaceFab
{
    private List _methods = new ArrayList();

    public InterfaceFabImpl(CtClassSource source, CtClass ctClass)
    {
        super(source, ctClass);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("InterfaceFabImpl[\npublic interface ");

        CtClass ctClass = getCtClass();

        buffer.append(ctClass.getName());

        try
        {
            CtClass[] interfaces = ctClass.getInterfaces();

            for (int i = 0; i < interfaces.length; i++)
            {
                buffer.append(i == 0 ? " extends " : ", ");
                buffer.append(interfaces[i].getName());
            }

        }
        catch (Exception ex)
        {
            buffer.append("<Exception: " + ex + ">");
        }

        Iterator i = _methods.iterator();

        while (i.hasNext())
        {
            MethodSignature sig = (MethodSignature) i.next();

            buffer.append("\n\npublic ");
            buffer.append(sig);
            buffer.append(";");
        }

        buffer.append("\n]");

        return buffer.toString();
    }

    public void addMethod(MethodSignature ms)
    {
        CtClass ctReturnType = convertClass(ms.getReturnType());
        CtClass[] ctParameters = convertClasses(ms.getParameterTypes());
        CtClass[] ctExceptions = convertClasses(ms.getExceptionTypes());

        CtMethod method = new CtMethod(ctReturnType, ms.getName(), ctParameters, getCtClass());

        try
        {
            method.setModifiers(Modifier.PUBLIC | Modifier.ABSTRACT);
            method.setExceptionTypes(ctExceptions);

            getCtClass().addMethod(method);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.unableToAddMethod(
                    ms,
                    getCtClass(),
                    ex), ex);
        }

        _methods.add(ms);
    }

    public Class createInterface()
    {
        return createClass();
    }

}
