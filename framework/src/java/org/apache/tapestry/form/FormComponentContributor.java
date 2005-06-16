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

/**
 * Interface for objects that contribute client-side events back to a form
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface FormComponentContributor
{
    /**
     * Invoked by a form component after it finishes rendering its tag (but before
     * the tag is closed) to allow this object to contribute to the component's
     * rendering process.  Typically used by Validators and Translators to add
     * javascript methods to the form's submit event handler.
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle, IFormComponent field);
}
