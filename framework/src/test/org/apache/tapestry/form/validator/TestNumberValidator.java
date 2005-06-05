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
 * Test case for {@link NumberValidator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestNumberValidator extends FormComponentContributorTestCase
{
    private NumberValidator _validator = new NumberValidator();
    
    public void testValidate()
    {
        _validator.setMin(new Integer(0));
        _validator.setMax(new Integer(10));
        
        replay();
        
        try
        {
            _validator.validate(_component, new Integer(5));
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
    
    public void testTooSmallValidate()
    {
        testTooSmallValidate("Field Label must not be smaller than 10.");
    }
    
    public void testCustomTooSmallValidate()
    {
        _validator.setTooSmallMessage("{0} must be greater than or equal to {1}.");
        
        testTooSmallValidate("Field Label must be greater than or equal to 10.");
    }
    
    private void testTooSmallValidate(String message)
    {
        _validator.setMin(new Integer(10));
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, new Integer(5));
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.TOO_SMALL, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }
    
    public void testTooLargeValidate()
    {
        testTooLargeValidate("Field Label must not be larger than 10.");
    }
    
    public void testCustomTooLargeValidate()
    {
        _validator.setTooLargeMessage("{0} must be less than or equal to {1}.");
        
        testTooLargeValidate("Field Label must be less than or equal to 10.");
    }
    
    private void testTooLargeValidate(String message)
    {
        _validator.setMax(new Integer(10));
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, new Integer(20));
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }

    public void testMinRenderContribution()
    {
        _validator.setMin(new Integer(10));
        
        testRenderContribution(new String[] { "validate_min_number(document.formName.fieldName,10,'Field Label must not be smaller than 10.')" });
    }

    public void testCustomMinRenderContribution()
    {
        _validator.setMin(new Integer(5));
        _validator.setTooSmallMessage("{0} must be greater than or equal to {1}.");
        
        testRenderContribution(new String[] { "validate_min_number(document.formName.fieldName,5,'Field Label must be greater than or equal to 5.')" });
    }

    public void testMaxRenderContribution()
    {
        _validator.setMax(new Integer(100));
        
        testRenderContribution(new String[] { "validate_max_number(document.formName.fieldName,100,'Field Label must not be larger than 100.')" });
    }

    public void testCustomMaxRenderContribution()
    {
        _validator.setMax(new Integer(50));
        _validator.setTooLargeMessage("{0} must be less than or equal to {1}.");
        
        testRenderContribution(new String[] { "validate_max_number(document.formName.fieldName,50,'Field Label must be less than or equal to 50.')" });
    }

    public void testMinMaxRenderContribution()
    {
        _validator.setMin(new Integer(10));
        _validator.setMax(new Integer(100));
        
        String[] handlers = new String[] {
            "validate_min_number(document.formName.fieldName,10,'Field Label must not be smaller than 10.')",
            "validate_max_number(document.formName.fieldName,100,'Field Label must not be larger than 100.')"
        };
        
        testRenderContribution(handlers);
    }

    public void testCustomMinMaxRenderContribution()
    {
        _validator.setMin(new Integer(5));
        _validator.setMax(new Integer(50));
        _validator.setTooSmallMessage("{0} must be greater than or equal to {1}.");
        _validator.setTooLargeMessage("{0} must be less than or equal to {1}.");
        
        String[] handlers = new String[] {
            "validate_min_number(document.formName.fieldName,5,'Field Label must be greater than or equal to 5.')",
            "validate_max_number(document.formName.fieldName,50,'Field Label must be less than or equal to 50.')"
        };
        
        testRenderContribution(handlers);
    }
    
    public void testRenderContribution(String[] handlers)
    {
        addScript("/org/apache/tapestry/form/validator/NumberValidator.js");
        
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
