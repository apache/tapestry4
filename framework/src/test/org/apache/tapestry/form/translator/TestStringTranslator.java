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

import org.apache.tapestry.valid.ValidatorException;

/**
 * Test case for {@link StringTranslator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestStringTranslator extends TranslatorTestCase
{
    private StringTranslator _translator = new StringTranslator();

    public void testFormat()
    {
        replay();
        
        String result = _translator.format(_component, "Test this");
        
        assertEquals("Test this", result);

        verify();
    }

    public void testNullFormat()
    {
        replay();
        
        String result = _translator.format(_component, null);
        
        assertEquals("", result);

        verify();
    }

    public void testParse()
    {
        replay();
        
        try
        {
            String result = (String) _translator.parse(_component, "Test this");

            assertEquals("Test this", result);
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

    public void testTrimmedParse()
    {
        _translator.setTrim(true);
        
        replay();
        
        try
        {
            String result = (String) _translator.parse(_component, " Test this ");

            assertEquals("Test this", result);
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

    public void testEmptyParse()
    {
        replay();
        
        try
        {
            String result = (String) _translator.parse(_component, "");

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

    public void testCustomEmptyParse()
    {
        _translator.setEmpty("");
        
        replay();
        
        try
        {
            String result = (String) _translator.parse(_component, "");

            assertEquals("", result);
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
    
    public void testRenderContribution()
    {
        replay();
        
        _translator.renderContribution(null, _cycle, null, _component);
        
        verify();
    }
    
    public void testTrimRenderContribution()
    {
        _translator.setTrim(true);
        trim();
        
        replay();
        
        _translator.renderContribution(null, _cycle, null, _component);
        
        verify();
    }
}
