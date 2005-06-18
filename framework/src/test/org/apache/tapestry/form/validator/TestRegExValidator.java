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

import org.apache.tapestry.form.FormComponentContributorTestCase;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Test case for {@link RegExValidator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestRegExValidator extends FormComponentContributorTestCase
{
    protected RegExValidator _validator = createValidator();
    
    protected RegExValidator createValidator()
    {
        return new RegExValidator();
    }
    
    public void testValidate()
    {
        _validator.setExpression("\\w+");
        
        testValidate("test");
    }
    
    protected void testValidate(String value)
    {
        replay();
        
        try
        {
            _validator.validate(_component, null, value);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        finally
        {
            verify();
        }
    }
    
    public void testInvalidValidate()
    {
        _validator.setExpression("^\\w+$");
        
        testInvalidValidate("Field Label is invalid.", "regex does not allow spaces");
    }

    public void testCustomInvalidValidate()
    {
        _validator.setExpression("^\\w+$");
        _validator.setMessage("{0} does not match the regular expression: {1}");
        
        testInvalidValidate("Field Label does not match the regular expression: ^\\w+$", "regex does not allow spaces");
    }
    
    protected void testInvalidValidate(String message, String value)
    {
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, null, value);
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }

    public void testRenderContribution()
    {
        _validator.setExpression("^\\w+$");
        
        testRenderContribution("validate_regex(event, document.formName.fieldName,'\\^\\\\w\\+\\$','Field Label is invalid.')");
    }
    
    public void testCustomRenderContribution()
    {
        _validator.setExpression("^\\w+$");
        _validator.setMessage("{0} does not match the regular expression: {1}");
        
        testRenderContribution("validate_regex(event, document.formName.fieldName,'\\^\\\\w\\+\\$','Field Label does not match the regular expression: ^\\w+$')");
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
        
        _validator.renderContribution(null, _cycle, null, _component);
        
        verify();
    }
}
