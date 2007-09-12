// Copyright Aug 6, 2006 The Apache Software Foundation
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
import org.apache.tapestry.form.validator.Validator;


/**
 * Marker interface for components that would like to override the default
 * logic used to render validation contributions made for client side form
 * validation.
 * 
 * @author jkuhnert
 */
public interface ValidatableFieldExtension extends ValidatableField
{

    /**Called during render of the specified component. Renders any contributions from validators.
     * Check to see if this field would like to override the default
     * contributions made by the specified {@link Validator} with it's own
     * contributions.
     * 
     * @param validator The validator to check if component wants to override. 
     * @param cycle The current request cycle.
     * 
     * @return True if this field wants to override default, false otherwise.
     */
    boolean overrideValidator(Validator validator, IRequestCycle cycle);
    
    /**
     * Very similar to the method signature used by {@link ValidatableFieldSupport#renderContributions(ValidatableField, IMarkupWriter, IRequestCycle)}, 
     * with the additional parameter being the {@link Validator} that this component has chosen to override.
     * 
     * <p>
     * This method will only be called if {@link #overrideValidator(Validator, IRequestCycle)} returns true
     * for the specified {@link Validator}.
     * </p>
     * 
     * @param validator The original {@link Validator} that component opted to override.
     * @param context The context object used by validation contributors.
     * @param writer The markup writer to send content to. (if any is needed)
     * @param cycle The current request cycle.
     */
    void overrideContributions(Validator validator, FormComponentContributorContext context, 
            IMarkupWriter writer, IRequestCycle cycle);
}
