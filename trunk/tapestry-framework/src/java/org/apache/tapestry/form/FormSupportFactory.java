// Copyright 2006 The Apache Software Foundation
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

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Interface for factories that can generate {@link FormSupport} 
 * objects.
 *
 * @since 4.1.1
 */
public interface FormSupportFactory 
{
    
    /**
     * Invoked every time an {@link IForm} is rendering in order to obtain a 
     * {@link FormSupport} instance to support and manage the process.
     * <p/>
     */ 
    FormSupport createFormSupport(IMarkupWriter writer, IRequestCycle cycle, IForm form);
}
