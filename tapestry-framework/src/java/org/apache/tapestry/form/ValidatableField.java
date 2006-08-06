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

package org.apache.tapestry.form;


/**
 * Implemented by form components that can need to be translated and validated. During render the
 * translator is used to translated the value to a string. During rewind, the submitted value is
 * translated back into an object by the translator and then validated.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface ValidatableField extends IFormComponent
{
    /**
     * Coerced into an Iterator of Validators.
     */
    Object getValidators();
}
