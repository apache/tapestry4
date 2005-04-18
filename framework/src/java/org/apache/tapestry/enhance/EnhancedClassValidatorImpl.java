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
import java.util.HashSet;
import java.util.Set;

import org.apache.hivemind.ErrorLog;
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

                    _errorLog.error(EnhanceMessages.noImplForAbstractMethod(m, current, baseClass
                            .getName(), enhancedClass), specification.getLocation(), null);
                }

                implementedMethods.add(s);
            }

            // An earlier version of this code walked the interfaces directly,
            // but it appears that implementing an interface actually
            // puts abstract method declarations into the class
            // (at least, in terms of what getDeclaredMethods() returns).

            // March up to the super class.

            current = current.getSuperclass();

            // Once advanced up to a concrete class, we trust that
            // the compiler did its checking. Alternately, if
            // we started on java.lang.Object for some reason, current
            // will be null and we can stop.S

            if (current == null || !Modifier.isAbstract(current.getModifiers()))
                break;
        }

    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}