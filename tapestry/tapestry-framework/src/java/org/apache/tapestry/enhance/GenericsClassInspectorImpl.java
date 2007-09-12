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

import org.apache.hivemind.ApplicationRuntimeException;

import java.beans.Introspector;
import java.lang.reflect.Method;


/**
 * Implementation of {@link ClassInspector} that can properly handle
 * jre 1.5 generic types.
 */
public class GenericsClassInspectorImpl implements ClassInspector
{

    /**
     * {@inheritDoc}
     */
    public MethodSignature getMethodSignature(Class implementing, Method m)
    {
        return new GenericsMethodSignatureImpl(implementing, m);
    }
    
    /**
     * {@inheritDoc}
     */
    public MethodSignature getPropertyAccessor(Class type, String propertyName)
    {
        try {
            Method[] props = type.getMethods();
            Method match = null;
            
            for (int i=0; i < props.length; i++) {

                String propName = getPropertyName(props[i]);

                if (!propertyName.equals(propName)) {
                    continue;
                }

                // store for later retrieval if necessary
                if (match == null)
                    match = props[i];
                
                // try to find read methods before resorting to write
                if (props[i].getReturnType() == void.class) {
                    continue;
                }

                return new GenericsMethodSignatureImpl(type, props[i]);
            } 

            if (match != null)
                return new GenericsMethodSignatureImpl(type, match);

        } catch (Throwable t) {
            
            throw new ApplicationRuntimeException("Error reading property " + propertyName + " from base class : " + type, t);
        }
        
        return null;
    }

    static String getPropertyName(Method m)
    {
        String name = m.getName();

        if (name.startsWith("get"))
            name = name.substring(3);
        else if (name.startsWith("set"))
            name = name.substring(3);
        else if (name.startsWith("is"))
            name = name.substring(2);

        return Introspector.decapitalize(name);
    }
}
