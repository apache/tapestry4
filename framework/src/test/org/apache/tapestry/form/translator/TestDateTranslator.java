//Copyright 2004, 2005 The Apache Software Foundation
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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Test case for {@link DateTranslator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestDateTranslator extends TranslatorTestCase
{
    private DateTranslator _translator = new DateTranslator();
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
    
    public void testDefaultFormat()
    {
        testFormat(buildDate(1976, Calendar.OCTOBER, 29), "10/29/1976");
    }
    
    public void testCustomFormat()
    {
        _translator.setPattern("yyyy-MM-dd");
        
        testFormat(buildDate(1976, Calendar.OCTOBER, 29), "1976-10-29");
    }
    
    public void testFormat(Date date, String expected)
    {
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        replay();
        
        String result = _translator.format(_component, date);
        
        assertEquals(expected, result);

        verify();
    }

    public void testNullFormat()
    {
        replay();
        
        String result = _translator.format(_component, null);
        
        assertEquals("", result);

        verify();
    }

    public void testDefaultParse()
    {
        testParse("10/29/1976", buildDate(1976, Calendar.OCTOBER, 29));
    }
    
    public void testCustomParse()
    {
        _translator.setPattern("yyyy-MM-dd");
        
        testParse("1976-10-29", buildDate(1976, Calendar.OCTOBER, 29));
    }
    
    public void testTrimmedParse()
    {
        _translator.setTrim(true);
        
        testParse(" 10/29/1976 ", buildDate(1976, Calendar.OCTOBER, 29));
    }
    
    public void testEmptyParse()
    {
        replay();
        
        try
        {
            Date result = (Date) _translator.parse(_component, "");

            assertEquals(null, result);
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

    private void testParse(String date, Date expected)
    {
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        replay();
        
        try
        {
            Date result = (Date) _translator.parse(_component, date);

            assertEquals(expected, result);
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
    
    public void testFailedParseDefaultMessage()
    {
        testFailedParse("Invalid date format for Field Name.  Format is MM/DD/YYYY.");
    }
    
    public void testFailedParseCustomMessage()
    {
        String message = "Field Name is an invalid date.";
        
        _translator.setMessage(message);
        
        testFailedParse(message);
    }

    private void testFailedParse(String message)
    {
        _component.getPage();
        _componentControl.setReturnValue(_page);

        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getPage();
        _componentControl.setReturnValue(_page);

        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Name");
        
        replay();
        
        try
        {
            System.out.println(_translator.parse(_component, "Bad-Date"));
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.DATE_FORMAT, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }
    
    public void testContributeFormEvents()
    {
        replay();
        
        _translator.renderContribution(null, _cycle, _component);
        
        verify();
    }
    
    public void testTrimContributeFormEvents()
    {
        _translator.setTrim(true);
        trim();
        
        replay();
        
        _translator.renderContribution(null, _cycle, _component);
        
        verify();
    }
}
