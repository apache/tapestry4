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

package org.apache.tapestry.wml;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormSupport;
import org.apache.tapestry.form.FormSupportFactory;

/**
 * Implementation of {@link FormSupportFactory} that generates 
 * {@link GoFormSupportImpl} instances, suitable for WML content.
 *
 * @since 4.1.1
 */
public class GoFormSupportFactory implements FormSupportFactory
{
    public FormSupport createFormSupport(IMarkupWriter writer, IRequestCycle cycle, IForm form) 
    {
        return new GoFormSupportImpl(writer, cycle, form);
    }    
}
