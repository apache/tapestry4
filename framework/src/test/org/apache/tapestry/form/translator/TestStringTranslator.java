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

import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
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

        String result = _translator.format(_component, null, "Test this");

        assertEquals("Test this", result);

        verify();
    }

    public void testNullFormat()
    {
        replay();

        String result = _translator.format(_component, null, null);

        assertEquals("", result);

        verify();
    }

    public void testParse()
    {
        replay();

        try
        {
            String result = (String) _translator.parse(_component, null, "Test this");

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
            String result = (String) _translator.parse(_component, null, " Test this ");

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
            String result = (String) _translator.parse(_component, null, "");

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
            String result = (String) _translator.parse(_component, null, "");

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
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);

        IFormComponent field = newFieldWithClientId("myfield");

        trainTrim(context, "myfield");

        replay();

        Translator t = new StringTranslator("trim");

        t.renderContribution(writer, cycle, context, field);

        verify();
    }

}
