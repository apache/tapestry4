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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;


/**
 * Implemenation of {@link ClassInspector} that is compatible with 
 * 1.4 jres.
 */
public class ClassInspectorImpl implements ClassInspector
{

    /**
     * {@inheritDoc}
     */
    public MethodSignature getMethodSignature(Class implementing, Method m)
    {
        return new MethodSignatureImpl(m);
    }
    
    /**
     * {@inheritDoc}
     */
    public MethodSignature getPropertyAccessor(Class type, String propertyName)
    {
        try {
            BeanInfo info = Introspector.getBeanInfo(type);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            
            for (int i=0; i < props.length; i++) {
                
                if (!propertyName.equals(props[i].getName())) {
                    continue;
                }
                
                return new MethodSignatureImpl(props[i].getReadMethod() != null ? props[i].getReadMethod() : props[i].getWriteMethod());
            } 
            
        } catch (Throwable t) {
            
            throw new ApplicationRuntimeException("Error reading property " + propertyName + " from base class : " + type, t);
        }
        
        return null;
    }

}
