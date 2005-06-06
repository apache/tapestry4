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

/**
 * Implements the logic used by {@link RequiredField}s for required field checking on rewind 
 * and generating client-side logic during render.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public interface RequirableFieldSupport
{
    /**
     * Called by the {@link RequiredField} during render to generate client-side validation logic.
     */
    public void render(RequirableField component, IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Called by the {@link RequiredField} during rewind to perform required field checking.
     */
    public void rewind(RequirableField component, IMarkupWriter writer, IRequestCycle cycle);
}