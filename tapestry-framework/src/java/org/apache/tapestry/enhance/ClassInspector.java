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


/**
 * Service that provides a common interface to other lower level reflection services
 * to provide a common set of functionality for reflection when using a 1.4 or >= 1.5 jre.
 */
public interface ClassInspector
{

    /**
     * Gets a compatible method signature for the specific generic {@link Method} object.
     * 
     * @param implementing
     *          The class implmeneting the specific method.
     * @param m
     *      The method object to generate a signature for.
     * @return A signature for the method.
     */
    MethodSignature getMethodSignature(Class implementing, Method m);
    
    /**
     * Gets the first available method for the specified property. It may be a write or read method, but
     * read will be preferred to write. 
     * 
     * @param type
     *          The base class to introspect for the property accessor.
     * @param propertyName
     *          The name of the javabeans style property to look for. (ie "value" would be "getValue" )
     *          
     * @return A compatible method signature, or null if none could be found.
     */
    MethodSignature getPropertyAccessor(Class type, String propertyName);
}
