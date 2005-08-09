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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Implements the logic used by {@link RequiredField}s for required field checking on rewind and
 * generating client-side logic during render.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface ValidatableFieldSupport
{
    /**
     * Called during render of the specified component. Determines form element value used to render
     * element.
     */
    public void render(ValidatableField field, IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Called during render of the specified component. Renders any contributions from translator
     * and validators.
     */
    public void renderContributions(ValidatableField field, IMarkupWriter writer,
            IRequestCycle cycle);

    /**
     * Called during rewind of the specified component. Specified value is translated via the
     * component's translator then validated using the component's validators.
     * 
     * @throws ValidatorException
     *             if translation or validation fails
     */
    public void bind(ValidatableField field, IMarkupWriter writer, IRequestCycle cycle, String value);

    /**
     * Returns true if this component is required. This usually entails a search of the component's
     * validators.
     */

    public boolean isRequired(ValidatableField field);
}