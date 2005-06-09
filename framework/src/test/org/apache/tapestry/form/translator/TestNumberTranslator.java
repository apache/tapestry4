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

import java.util.Locale;

import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

public class TestNumberTranslator extends TranslatorTestCase
{
    private NumberTranslator _translator = new NumberTranslator();
    
    public void testDefaultFormat()
    {
        testFormat(new Integer(10), "10");
    }
    
    public void testCustomFormat()
    {
        _translator.setPattern("$#0.00");
        
        testFormat(new Integer(10), "$10.00");
    }
    
    public void testFormat(Number number, String expected)
    {
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        replay();
        
        String result = _translator.format(_component, number);
        
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
        testParse("0.1", new Double(0.1));
    }
    
    public void testCustomParse()
    {
        _translator.setPattern("#%");
        
        testParse("10%", new Double(0.1));
    }
    
    public void testTrimmedParse()
    {
        _translator.setTrim(true);
        
        testParse(" 100 ", new Long(100));
    }

    private void testParse(String number, Number expected)
    {
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        replay();
        
        try
        {
            Number result = (Number) _translator.parse(_component, number);

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
        testFailedParse("Field Name must be a numeric value.");
    }
    
    public void testFailedParseCustomMessage()
    {
        String message = "Field Name is an invalid number.";
        
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
            System.out.println(_translator.parse(_component, "Bad-Number"));
            
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(message, e.getMessage());
            assertEquals(ValidationConstraint.NUMBER_FORMAT, e.getConstraint());
        }
        finally
        {
            verify();
        }
    }
    
    public void testRenderContribution()
    {
        addScript("/org/apache/tapestry/form/translator/NumberTranslator.js");
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _form.getName();
        _formControl.setReturnValue("formName");
        
        _component.getName();
        _componentControl.setReturnValue("fieldName");
        
        _form.addEventHandler(FormEventType.SUBMIT, "validate_number(document.formName.fieldName,'Field Label must be a numeric value.')");
        _formControl.setVoidCallable();
        
        replay();
        
        _translator.renderContribution(null, _cycle, _component);
        
        verify();
    }
    
    public void testMessageRenderContribution()
    {
        _translator.setMessage("You entered a bunk value for {0}. I should look like {1}.");
        
        addScript("/org/apache/tapestry/form/translator/NumberTranslator.js");
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _form.getName();
        _formControl.setReturnValue("formName");
        
        _component.getName();
        _componentControl.setReturnValue("fieldName");
        
        _form.addEventHandler(FormEventType.SUBMIT, "validate_number(document.formName.fieldName,'You entered a bunk value for Field Label. I should look like #.')");
        _formControl.setVoidCallable();
        
        replay();
        
        _translator.renderContribution(null, _cycle, _component);
        
        verify();
    }
    
    public void testTrimRenderContribution()
    {
        _translator.setTrim(true);
        trim();
        
        addScript("/org/apache/tapestry/form/translator/NumberTranslator.js");
        
        _component.getPage();
        _componentControl.setReturnValue(_page);
        
        _page.getLocale();
        _pageControl.setReturnValue(Locale.US);
        
        _component.getDisplayName();
        _componentControl.setReturnValue("Field Label");
        
        _component.getForm();
        _componentControl.setReturnValue(_form);
        
        _form.getName();
        _formControl.setReturnValue("formName");
        
        _component.getName();
        _componentControl.setReturnValue("fieldName");
        
        _form.addEventHandler(FormEventType.SUBMIT, "validate_number(document.formName.fieldName,'Field Label must be a numeric value.')");
        _formControl.setVoidCallable();
        
        replay();
        
        _translator.renderContribution(null, _cycle, _component);
        
        verify();
    }
}
