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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry.form.FormComponentContributorTestCase;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Test case for {@link NumberValidator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestDateValidator extends FormComponentContributorTestCase
{
    private DateValidator _validator = new DateValidator();
    private Calendar _calendar = Calendar.getInstance();

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        _calendar.clear();
    }

    private Date buildDate(int year, int month, int day)
    {
        _calendar.set(Calendar.YEAR, year);
        _calendar.set(Calendar.MONTH, month);
        _calendar.set(Calendar.DATE, day);
        
        return _calendar.getTime();
    }
    
    public void testValidate()
    {
        _validator.setMin(buildDate(2005, Calendar.JANUARY, 1));
        _validator.setMax(buildDate(2005, Calendar.DECEMBER, 31));
        
        replay();
        
        try
        {
            _validator.validate(_component, null, buildDate(2005, Calendar.JUNE, 30));
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
        testTooSmallValidate("Field Label may not be earlier than Jan 1, 2005.");
    }
    
    public void testCustomTooSmallValidate()
    {
        _validator.setTooEarlyMessage("{0} must be after {1,date}.");
        
        testTooSmallValidate("Field Label must be after Jan 1, 2005.");
    }
    
    private void testTooSmallValidate(String message)
    {
        _validator.setMin(buildDate(2005, Calendar.JANUARY, 1));
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, null, buildDate(2004, Calendar.DECEMBER, 31));
            
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
    
    public void testTooLongValidate()
    {
        testTooLateValidate("Field Label may not be later than Dec 31, 2005.");
    }
    
    public void testCustomTooLongValidate()
    {
        _validator.setTooLateMessage("{0} must be before {1,date}.");
        
        testTooLateValidate("Field Label must be before Dec 31, 2005.");
    }
    
    private void testTooLateValidate(String message)
    {
        _validator.setMax(buildDate(2005, Calendar.DECEMBER, 31));
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        replay();
        
        try
        {
            _validator.validate(_component, null, buildDate(2006, Calendar.JANUARY, 1));
            
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

    public void testRenderContribution()
    {
        replay();
        
        _validator.renderContribution(null, _cycle, null, _component);
        
        verify();
    }
}
