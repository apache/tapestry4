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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Validates that an enhanced class is correct; checks that all inherited abstract methods are, in
 * fact, implemented in the class.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EnhancedClassValidatorImpl implements EnhancedClassValidator
{
    private ErrorLog _errorLog;

    public void validate(Class baseClass, Class enhancedClass, IComponentSpecification specification)
    {
        Set implementedMethods = new HashSet();
        List interfaceQueue = new ArrayList();
        Location location = specification.getLocation();

        Class current = enhancedClass;

        while (true)
        {
            Method[] methods = current.getDeclaredMethods();

            for (int i = 0; i < methods.length; i++)
            {
                Method m = methods[i];

                boolean isAbstract = Modifier.isAbstract(m.getModifiers());

                MethodSignature s = new MethodSignature(m);

                if (isAbstract)
                {
                    if (implementedMethods.contains(s))
                        continue;

                    _errorLog.error(EnhanceMessages.noImplForAbstractMethod(
                            m,
                            current,
                            baseClass,
                            enhancedClass), location, null);
                }

                implementedMethods.add(s);
            }

            interfaceQueue.addAll(Arrays.asList(current.getInterfaces()));

            // Did an earlier JDK include methods from interfaces in
            // getDeclaredMethods()? The old code here seemed to indicate
            // that was the case, but it certainly is no longer, that's why
            // we add all the interfaces to a queue to check after the rest.

            current = current.getSuperclass();

            // We need to run straight to the top, to find all the implemented methods.

            if (current == null)
                break;
        }

        while (!interfaceQueue.isEmpty())
        {
            Class thisInterface = (Class) interfaceQueue.remove(0);

            checkAllInterfaceMethodsImplemented(
                    thisInterface,
                    baseClass,
                    enhancedClass,
                    implementedMethods,
                    location);
        }

    }

    private void checkAllInterfaceMethodsImplemented(Class interfaceClass, Class baseClass,
            Class enhancedClass, Set implementedMethods, Location location)
    {
        // Get all methods defined by the interface, or its super-interfaces

        Method[] methods = interfaceClass.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            MethodSignature sig = new MethodSignature(methods[i]);

            if (!implementedMethods.contains(sig))
                _errorLog.error(EnhanceMessages.unimplementedInterfaceMethod(
                        methods[i],
                        baseClass,
                        enhancedClass), location, null);
        }
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}