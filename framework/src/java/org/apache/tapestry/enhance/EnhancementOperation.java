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

import java.util.List;

import org.apache.hivemind.service.MethodSignature;

/**
 * A process object representing enhancements to a component class. The operation is passed to
 * {@link org.apache.tapestry.enhance.EnhancementWorker}objects that perform enhancements.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface EnhancementOperation
{
    /**
     * Claims a property. Most enhancements are concerned with adding properties. Some enhancement
     * workers exist to fill in defaults, and they need to know what properties have already been
     * spoken for by eariler enhancement works.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the property was previously claimed
     */

    public void claimProperty(String propertyName);

    /**
     * Returns a list of the names of existing properties that are not claimed and which have
     * abstract accessor methods.
     */

    public List findUnclaimedAbstractProperties();

    /**
     * Adds a field to the enhanced class; the field will be private and use the provided name and
     * type.
     */

    public void addField(String name, Class type);

    /**
     * Adds a field containing an initial value. The EnhancementOperation will ensure that the value
     * is passed into the enhanced class' constructor and assigned.
     */

    public void addField(String name, Class type, Object value);

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

    /**
     * Returns a reference to a particular class. This will, effectively, by the name of a private
     * field.
     */

    public String getClassReference(Class clazz);

    /**
     * Returns the type of an existing property of the base component class. If the property does
     * not exist, then returns null.
     */

    public Class getPropertyType(String name);

    /**
     * Allows for a kind of distributed construction of a particular method, within a particular
     * interface. Code can be appended to the method's implementation throughout the course of the
     * enhancement operation. When the enhanced class is finialized, the method is added with
     * whatever contents are in its body. If the base class implements the method, then the method
     * body will include an initial call to that implementation.
     * <p>
     * At this time, this works best for void methods (since there isn't an easy way to ensure code
     * would be inserted before a final return statement).
     * 
     * @param interfaceClass
     *            the interface containing the method. If the base class does not implement the
     *            interface, then the enhanced class will have the interface added.
     * @param methodSignature
     *            the signature of the method to be added.
     * @param code
     *            the Javassist markup to be added to the body of the method.
     */
    public void extendMethodImplementation(Class interfaceClass, MethodSignature methodSignature,
            String code);
    
    /**
     * Returns true if the class implements the specified interface.  Checks the base class
     * (as identified in the specification), but <em>also</em> accounts for any additional
     * interfaces that may be added by {@link #extendMethodImplementation(Class, MethodSignature, String)}.
     * 
     */
    
    public boolean implementsInterface(Class interfaceClass);
}