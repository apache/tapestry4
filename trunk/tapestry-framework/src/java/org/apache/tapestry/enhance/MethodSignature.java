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


/**
 * A representation of a {@link java.lang.reflect.Method}, identifying the name, return type,
 * parameter types and exception types. Actual Method objects are tied to a particular class, and
 * don't compare well with other otherwise identical Methods from other classes or interface;
 * MethodSignatures are distinct from classes and compare well.
 * <p>
 * Because the intended purpose is to compare methods from interfaces (which are always public and
 * abstract) we don't bother to actually track the modifiers. In addition, at this time,
 * MethodSignature <em>does not distinguish between instance and static
 * methods</em>.
 */
public interface MethodSignature
{
    /**
     * Returns the exceptions for this method. Caution: do not modify the returned array. May return
     * null.
     */
    Class[] getExceptionTypes();
    
    /**
     * The name of the method.
     * @return method name
     */
    String getName();
    
    /**
     * Returns the parameter types for this method. May return null. Caution: do not modify the
     * returned array.
     */
    Class[] getParameterTypes();
    
    /**
     * Method return type.
     * 
     * @return The return type of the method.
     */
    Class getReturnType();
    
    /**
     * Returns a string consisting of the name of the method and its parameter values. This is
     * similar to {@link #toString()}, but omits the return type and information about thrown
     * exceptions. A unique id is used by  MethodIterator to identify overlapping methods
     * (methods with the same name but different thrown exceptions).
     * 
     */
    String getUniqueId();
    
    /**
     * Returns true if this signature has the same return type, name and parameters types as the
     * method signature passed in, and this signatures exceptions "trump" (are the same as, or
     * super-implementations of, all exceptions thrown by the other method signature).
     * 
     */

    boolean isOverridingSignatureOf(MethodSignature ms);
    
    /**
     * If the method definition has generic parameters or return types it
     * is expected that this will return true.
     * 
     * @return True if this is a generics based method, false otherwise.
     */
    boolean isGeneric();
}
