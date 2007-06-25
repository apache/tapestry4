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
 * The standard implementation of {@link FormSupportFactory}. It generates 
 * {@link FormSupportImpl} instances.
 *
 * @since 4.1.1
 */
public class FormSupportFactoryImpl implements FormSupportFactory
{
    public FormSupport createFormSupport(IMarkupWriter writer, IRequestCycle cycle, IForm form) 
    {
        return new FormSupportImpl(writer, cycle, form);
    }    
}
