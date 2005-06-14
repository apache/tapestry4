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

package org.apache.tapestry.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Implemented by form components that can be marked as required.  The form rewind
 * will record validation errors for required fields for which a value was not submitted.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface RequirableField extends IFormComponent
{
    /**
     * Indicates whether or not this field is required.
     * @return true, if this field is required, false otherwise.
     */
    public boolean isRequired();
    
    /**
     * Returns the custom validation message pattern that overrides the default one.
     * This value may include {@link MessageFormat} parameters, the context of which is 
     * specific to this field.
     * @return a custom validation message
     */
    public String getRequiredMessage();
    
    /**
     * Called on the field if the required field check succeeds.
     * @throws ValidatorException if required field check fails.
     */
    public void bind(IMarkupWriter writer, IRequestCycle cycle) throws ValidatorException;
    
    /**
     * Retrieves a simplified view of the value submitted for this component.  Used to during
     * required field checking.
     */
    public String getSubmittedValue(IRequestCycle cycle);
}
