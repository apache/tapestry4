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
 * Test case for {@link StringValidator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestStringValidator extends FormComponentContributorTestCase
{
    private StringValidator _validator = new StringValidator();
    
    public void testValidate()
    {
        _validator.setMinLength(0);
        _validator.setMaxLength(10);
        
        replay();
        
        try
        {
            _validator.validate(_component, "test");
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
    
    public void testTooShortValidate()
    {
        testTooShortValidate("You must enter at least 10 characters for Field Label.");
    }
    
    public void testCustomTooShortValidate()
    {
        _validator.setTooShortMessage("{1} may not be less than {0} characters in length.");
        
        testTooShortValidate("Field Label may not be less than 10 characters in length.");
    }
    
    private void testTooShortValidate(String message)
    {
        _validator.setMinLength(10);
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, "test");
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }
    
    public void testTooLongValidate()
    {
        testTooLongValidate("You must enter no more than 10 characters for Field Label.");
    }
    
    public void testCustomTooLongValidate()
    {
        _validator.setTooLongMessage("{1} may not be more than {0} characters in length.");
        
        testTooLongValidate("Field Label may not be more than 10 characters in length.");
    }
    
    private void testTooLongValidate(String message)
    {
        _validator.setMaxLength(10);
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, "abcdefghijklmnopqrstuvwxyz");
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.MAXIMUM_WIDTH, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }

    public void testMinRenderContribution()
    {
        _validator.setMinLength(10);
        
        testRenderContribution(new String[] { "validate_min_length(event, document.formName.fieldName,10,'You must enter at least 10 characters for Field Label.')" });
    }

    public void testCustomMinRenderContribution()
    {
        _validator.setMinLength(5);
        _validator.setTooShortMessage("{1} should be at least {0} characters in length.");
        
        testRenderContribution(new String[] { "validate_min_length(event, document.formName.fieldName,5,'Field Label should be at least 5 characters in length.')" });
    }

    public void testMaxRenderContribution()
    {
        _validator.setMaxLength(100);
        
        testRenderContribution(new String[] { "validate_max_length(event, document.formName.fieldName,100,'You must enter no more than 100 characters for Field Label.')" });
    }

    public void testCustomMaxRenderContribution()
    {
        _validator.setMaxLength(50);
        _validator.setTooLongMessage("{1} should be no more than {0} characters in length.");
        
        testRenderContribution(new String[] { "validate_max_length(event, document.formName.fieldName,50,'Field Label should be no more than 50 characters in length.')" });
    }

    public void testMinMaxRenderContribution()
    {
        _validator.setMinLength(10);
        _validator.setMaxLength(100);
        
        String[] handlers = new String[] {
            "validate_min_length(event, document.formName.fieldName,10,'You must enter at least 10 characters for Field Label.')",
            "validate_max_length(event, document.formName.fieldName,100,'You must enter no more than 100 characters for Field Label.')"
        };
        
        testRenderContribution(handlers);
    }

    public void testCustomMinMaxRenderContribution()
    {
        _validator.setMinLength(5);
        _validator.setMaxLength(50);
        _validator.setTooShortMessage("{1} should be at least {0} characters in length.");
        _validator.setTooLongMessage("{1} should be no more than {0} characters in length.");
        
        String[] handlers = new String[] {
            "validate_min_length(event, document.formName.fieldName,5,'Field Label should be at least 5 characters in length.')",
            "validate_max_length(event, document.formName.fieldName,50,'Field Label should be no more than 50 characters in length.')"
        };
        
        testRenderContribution(handlers);
    }
    
    public void testRenderContribution(String[] handlers)
    {
        addScript("/org/apache/tapestry/form/validator/StringValidator.js");
        
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _form.getName();
        _formControl.setReturnValue("formName");
        
        _component.getName();
        _componentControl.setReturnValue("fieldName");
        
        for (int i = 0; i < handlers.length; ++i)
        {
            _component.getPage();
            _componentControl.setReturnValue(_page);
            
            _page.getLocale();
            _pageControl.setReturnValue(Locale.US);
            
            _component.getDisplayName();
            _componentControl.setReturnValue("Field Label");
            
            _form.addEventHandler(FormEventType.SUBMIT, handlers[i]);
            _formControl.setVoidCallable();
        }
        
        replay();
        
        _validator.renderContribution(null, _cycle, _component);
        
        verify();
    }
}
