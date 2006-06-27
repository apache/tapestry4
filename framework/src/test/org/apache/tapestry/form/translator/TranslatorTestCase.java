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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.form.FormComponentContributorTestCase;
import org.apache.tapestry.form.FormEventType;
import org.testng.annotations.Test;

/**
 * Abstract test case for {@link Translator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
@Test
public abstract class TranslatorTestCase extends FormComponentContributorTestCase
{
    protected void trim()
    {
        expect(_component.getForm()).andReturn(_form);
        
        expect(_form.getName()).andReturn("formName");
        
        expect(_component.getName()).andReturn("fieldName");
        
        _form.addEventHandler(FormEventType.SUBMIT, "trim(document.formName.fieldName)");
    }
}
