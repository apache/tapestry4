// Copyright 2004 The Apache Software Foundation
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

import java.beans.PropertyDescriptor;

import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * A process object representing enhancements to a component class. The operation is passed to
 * {@link org.apache.tapestry.enhance.EnhancementWorker}objects that perform enhancements.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface EnhancementOperation
{
    public IComponentSpecification getSpecification();

    /**
     * Claims a property. Most enhancements are concerned with adding properties. Some enhancement
     * workerss exist to fill in defaults, and they need to know what properties have already been
     * spoken for by eariler enhancement works.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the property was previously claimed
     */

    public void claimProperty(String propertyName);

    /**
     * Adds a field to the enhanced class; the field will be private and use the provided name and
     * type.
     */

    public void addField(String name, Class type);

    /**
     * Converts a type name (an object class name, a primtive name, or an array) into the
     * corresponding Class object.
     */

    public Class convertTypeName(String type);

    /**
     * Confirms that the named property either doesn't exist (in the component base class), or that
     * the type of the property exactly matches the indicated type.
     */

    public void validateProperty(String name, Class expectedType);

    /**
     * Returns the name of the accessor method for the given property (if it exists in the component
     * base class), or fabricates a new name if it does not.
     */

    public String getAccessorMethodName(String propertyName);

    /**
     * Adds a method to the enhanced class.
     * 
     * @param modifier
     *            as defined by {@link java.lang.reflect.Modifier}, typically
     *            {@link java.lang.reflect.Modifier#PUBLIC}
     * @param sig
     *            the method signature (defining name, return type, etc.)
     * @param methodBody
     *            a Javassist code snippet for the method body
     */
    public void addMethod(int modifier, MethodSignature sig, String methodBody);

    /**
     * Returns the base component class, as defined in the specification (or defaulted). An enhaced
     * subclass of the component class will usually be created.
     */
    public Class getBaseClass();
}