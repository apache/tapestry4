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

import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;

/**
 * A process object representing enhancements to a component class. The
 * operation is passed to {@link org.apache.tapestry.enhance.EnhancementWorker}objects
 * that perform enhancements.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface EnhancementOperation
{

    /**
     * Claims a property. Most enhancements are concerned with adding
     * properties. Some enhancement workers exist to fill in defaults, and they
     * need to know what properties have already been spoken for by eariler
     * enhancement works.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the property was previously claimed
     */

    void claimProperty(String propertyName);

    /**
     * Claims a property as read-only. This will check to see if the property
     * has an abstract setter method.
     * 
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the property was previously claimed, or if the property
     *             includes an accessor method.
     */

    void claimReadonlyProperty(String propertyName);
    
    /**
     * Checks to see if the specified property can be claimed as read only. 
     * 
     * @param propertyName
     *          The property to check.
     * @return True, if no setter method has been created for the specified property and
     *          the property hasn't been claimed by someone else.
     */
    boolean canClaimAsReadOnlyProperty(String propertyName);
    
    /**
     * Returns a list of the names of existing properties that are not claimed
     * and which have abstract accessor methods.
     */

    List findUnclaimedAbstractProperties();

    /**
     * Adds a field to the enhanced class; the field will be private and use the
     * provided name and type.
     */

    void addField(String name, Class type);

    /**
     * Adds a field containing an initial value, which is injected into the
     * class via its fabricated constructor. This method may be called multiple
     * times with the same value and will return the same variable name (an
     * identity map is kept internally).
     * 
     * @param fieldName
     *            The default name for the field, used if a new field (and
     *            contructor argument) is being created. Only used if a field
     *            for the value doesn't exist.
     * @param fieldType
     *            The type of the field to be created.
     * @param value
     *            the value to be referenced, which may not be null
     * @return the name of the field containing the value. This may or may not
     *         match fieldName. The provided fieldName may be modified to
     *         prevent naming conflicts.
     */

    String addInjectedField(String fieldName, Class fieldType,
            Object value);

    /**
     * Converts a type name (an object class name, a primtive name, or an array)
     * into the corresponding Class object.
     */

    Class convertTypeName(String type);

    /**
     * Confirms that the named property either doesn't exist (in the component
     * base class), or that the type of the property exactly matches the
     * indicated type.
     */

    void validateProperty(String name, Class expectedType);

    /**
     * Returns the name of the accessor method for the given property (if it
     * exists in the component base class), or fabricates a new name if it does
     * not.
     */

    String getAccessorMethodName(String propertyName);

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
     * @param location
     *            a location used to identify "why" the method was added; the
     *            location may later be used to describe conflicts. May not be
     *            null.
     */
    void addMethod(int modifier, MethodSignature sig, String methodBody,
            Location location);

    /**
     * Returns the base component class, as defined in the specification (or
     * defaulted). An enhaced subclass of the component class will usually be
     * created.
     */
    Class getBaseClass();

    /**
     * Returns a reference to a particular class. This will, effectively, by the
     * name of a private field.
     */

    String getClassReference(Class clazz);

    /**
     * Returns the type of an existing property of the base component class. If
     * the property does not exist, then returns null.
     */

    Class getPropertyType(String name);

    /**
     * Allows for a kind of distributed construction of a particular method,
     * within a particular interface. Code can be appended to the method's
     * implementation throughout the course of the enhancement operation. When
     * the enhanced class is finialized, the method is added with whatever
     * contents are in its body. If the base class implements the method, then
     * the method body will include an initial call to that implementation.
     * <p>
     * At this time, this works best for void methods (since there isn't an easy
     * way to ensure code would be inserted before a final return statement).
     * 
     * @param interfaceClass
     *            the interface containing the method. If the base class does
     *            not implement the interface, then the enhanced class will have
     *            the interface added.
     * @param methodSignature
     *            the signature of the method to be added.
     * @param code
     *            the Javassist markup to be added to the body of the method.
     */
    void extendMethodImplementation(Class interfaceClass,
            MethodSignature methodSignature, String code);

    /**
     * Returns true if the class implements the specified interface. Checks the
     * base class (as identified in the specification), but <em>also</em>
     * accounts for any additional interfaces that may be added by
     * {@link #extendMethodImplementation(Class, MethodSignature, String)}.
     */

    boolean implementsInterface(Class interfaceClass);
}
