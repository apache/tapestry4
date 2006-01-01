// Copyright 2004, 2005, 2006 The Apache Software Foundation
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
        // Set of MethodSignatures for methods that have a non-abstract implementation
        // The Set is built working from the deepest subclass up to (and including) java.lang.Object

        Set implementedMethods = new HashSet();
        // Key is MethodSignature, value is Method
        // Tracks which methods come from interfaces
        Map interfaceMethods = new HashMap();

        Location location = specification.getLocation();

        Class current = enhancedClass;

        while (true)
        {
            addInterfaceMethods(current, interfaceMethods);

            // Inside Eclipse, for abstract classes, getDeclaredMethods() does NOT report methods
            // inherited from interfaces. For Sun JDK and abstract classes, getDeclaredMethods()
            // DOES report interface methods
            // (as if they were declared by the class itself). This code is needlessly complex so
            // that the checks work in both
            // situations. Basically, I think Eclipse is right and Sun JDK is wrong and we're using
            // the interfaceMethods map as a filter to ignore methods that Sun JDK is attributing
            // to the class.

            Method[] methods = current.getDeclaredMethods();

            for (int i = 0; i < methods.length; i++)
            {
                Method m = methods[i];

                MethodSignature s = new MethodSignature(m);

                boolean isAbstract = Modifier.isAbstract(m.getModifiers());

                if (isAbstract)
                {
                    if (interfaceMethods.containsKey(s))
                        continue;

                    // If a superclass defines an abstract method that a subclass implements, then
                    // all's OK.

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

            current = current.getSuperclass();

            // No need to check Object.class; it is concrete and doesn't implement any interfaces,
            // or provide any methods
            // that might be declared in an interface.

            if (current == null || current == Object.class)
                break;
        }

        Iterator i = interfaceMethods.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            MethodSignature sig = (MethodSignature) entry.getKey();

            if (implementedMethods.contains(sig))
                continue;

            Method method = (Method) entry.getValue();

            _errorLog.error(EnhanceMessages.unimplementedInterfaceMethod(
                    method,
                    baseClass,
                    enhancedClass), location, null);
        }

    }

    private void addInterfaceMethods(Class current, Map interfaceMethods)
    {
        Class[] interfaces = current.getInterfaces();

        for (int i = 0; i < interfaces.length; i++)
            addMethodsFromInterface(interfaces[i], interfaceMethods);
    }

    private void addMethodsFromInterface(Class interfaceClass, Map interfaceMethods)
    {
        Method[] methods = interfaceClass.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            MethodSignature sig = new MethodSignature(methods[i]);

            if (interfaceMethods.containsKey(sig))
                continue;

            interfaceMethods.put(sig, methods[i]);
        }
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}