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

package org.apache.tapestry.valid;

import org.apache.tapestry.IRender;
import org.apache.tapestry.form.IFormComponent;

/**
 * Defines the interface for an object that tracks input fields. This interface
 * is now poorly named, in that it tracks errors that may <em>not</em> be
 * associated with a specific field.
 * <p>
 * For each field, a flag is stored indicating if the field is, in fact, in
 * error. The input supplied by the client is stored so that if the form is
 * re-rendered (as is typically done when there are input errors), the value
 * entered by the user can be displayed back to the user. An error message
 * renderer is stored; this is an object that can render the error message (it
 * is usually a {@link org.apache.tapestry.valid.RenderString}&nbsp;wrapper
 * around a simple string).
 * 
 * @author Howard Lewis Ship
 * @since 1.0.8
 */

public interface IFieldTracking
{

    /**
     * Returns true if the field is in error (that is, if it has an error
     * message {@link #getErrorRenderer() renderer}.
     */

    boolean isInError();

    /**
     * Returns the field component. This may return null if the error is not
     * associated with any particular field. Note: may return null after the
     * field tracking object is serialized and deserialized (the underlying
     * component reference is transient); this metehod is primarily used for
     * testing.
     */

    IFormComponent getComponent();

    /**
     * Returns an object that will render the error message. The renderer
     * <em>must</em> implement a simple <code>toString()</code> that does
     * not produce markup, but is a simple message.
     * 
     * @see ValidatorException#ValidatorException(String, IRender,
     *      ValidationConstraint)
     * @since 1.0.9
     */

    IRender getErrorRenderer();

    /**
     * Returns the invalid input recorded for the field. This is stored so that,
     * on a subsequent render, the smae invalid input can be presented to the
     * client to be corrected.
     */

    String getInput();

    /**
     * Returns the name of the field, that is, the name assigned by the form
     * (this will differ from the component's id when any kind of looping
     * operation is in effect).
     */

    String getFieldName();

    /**
     * Returns the validation constraint that was violated by the input. This
     * may be null if the constraint isn't known.
     */

    ValidationConstraint getConstraint();
}
