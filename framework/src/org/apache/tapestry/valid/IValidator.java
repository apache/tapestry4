//  Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;

/**
 *  An object that works with an {@link IFormComponent} to format output
 *  (convert object values to strings values) and to process input
 *  (convert strings to object values and validate them).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public interface IValidator
{
    /**
     *  All validators must implement a required property.  If true,
     *  the client must supply a non-null value.
     *
     **/

    public boolean isRequired();

    /**
     *  Invoked during rendering to convert an object value (which may be null)
     *  to a String.  It is acceptible to return null.  The string will be the
     *  VALUE attribute of the HTML text field.
     *
     **/

    public String toString(IFormComponent field, Object value);

    /**
     *  Converts input, submitted by the client, into an object value.
     *  May return null if the input is null (and the required flag is false).
     *
     *  <p>The input string will already have been trimmed.  It may be null.
     *
     *  @throws ValidatorException if the string cannot be converted into
     *  an object, or the object is
     *  not valid (due to other constraints).
     **/

    public Object toObject(IFormComponent field, String input) throws ValidatorException;

    /**
     *  Invoked by the field after it finishes rendering its tag (but before
     *  the tag is closed) to allow the validator to provide a contribution to the
     *  rendering process.  Validators typically generated client-side JavaScript
     *  to peform validation.
     *
     *  @since 2.2
     *
     **/

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle);

}