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

package org.apache.tapestry.form.validator;

import java.util.Locale;

import org.apache.tapestry.form.FormEventType;

/**
 * Test case for {@link EmailValidator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestEmailValidator extends TestRegExValidator
{
    protected RegExValidator createValidator()
    {
        return new EmailValidator();
    }
    
    public void testValidate()
    {
        testValidate("pferraro@apache.org");
    }
    
    public void testInvalidValidate()
    {
        testInvalidValidate("Invalid email format for Field Label.  Format is user@hostname.", "pferraro");
    }

    public void testCustomInvalidValidate()
    {
        _validator.setMessage("{0} is not a valid email address.");
        
        testInvalidValidate("Field Label is not a valid email address.", "pferraro");
    }

    public void testRenderContribution()
    {
        testRenderContribution("validate_regex(event, document.formName.fieldName,'\\^\\\\w\\[\\-\\._\\\\w\\]\\*\\\\w\\@\\\\w\\[\\-\\._\\\\w\\]\\*\\\\w\\\\\\.\\\\w\\{2\\,3\\}\\$','Invalid email format for Field Label.  Format is user@hostname.')");
    }
    
    public void testCustomRenderContribution()
    {
        _validator.setMessage("{0} is not a valid email address.");
        
        testRenderContribution("validate_regex(event, document.formName.fieldName,'\\^\\\\w\\[\\-\\._\\\\w\\]\\*\\\\w\\@\\\\w\\[\\-\\._\\\\w\\]\\*\\\\w\\\\\\.\\\\w\\{2\\,3\\}\\$','Field Label is not a valid email address.')");
    }
    
    private void testRenderContribution(String handler)
    {
        addScript("/org/apache/tapestry/form/validator/RegExValidator.js");
        
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _form.getName();
        _formControl.setReturnValue("formName");
        
        _component.getName();
        _componentControl.setReturnValue("fieldName");
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        _form.addEventHandler(FormEventType.SUBMIT, handler);
        _formControl.setVoidCallable();
        
        replay();
        
        _validator.renderContribution(null, _cycle, _component);
        
        verify();
    }
}
