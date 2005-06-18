// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.form.validator;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.FormComponentContributor;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidatorException;

/**
 * An object that can be "attached" to a {@link org.apache.tapestry.form.IFormComponent} to perform
 * server-side validation ({@link #validate(IFormComponent, ValidationMessages, Object)}) as well
 * as generate cleint-side validation (in the form of JavaScript submit listeners).
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface Validator extends FormComponentContributor
{
    /**
     * Invoked to validate input for the field. A
     * {@link org.apache.tapestry.form.translator.Translator} will have already converted the
     * submitted user input string into an object.
     * 
     * @param field
     *            the form element component being validated, often used to determine the
     *            {@link IFormComponent#getDisplayName() user presentable name} for the field, used
     *            in error messages.
     * @param messages
     *            access to the pre-defined validation messages, in the appropriate locale
     * @param object
     *            the client-side representation of the field's data
     * @throws ValidatorException
     *             if the object violates the constraint represented by this Validator.
     */

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException;
}
