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

package org.apache.tapestry.form.translator;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.FormComponentContributor;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Interface used by {@link ValidatableField}s to both format an object as text and 
 * translate submitted text into an appropriate object for a given field.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface Translator extends FormComponentContributor
{
    /**
     * Invoked during rendering to format an object (which may be null) into a
     * text value (which should not be null) appropriate for the specified field.
     */
    public String format(IFormComponent field, Object object);
    
    /**
     * Invoked during rewind to parse a submitted input value into an object suitable
     * for the specified component.
     * @throws ValidatorException if the specified text could not be parsed into an object.
     */
    public Object parse(IFormComponent field, String value) throws ValidatorException;
}
