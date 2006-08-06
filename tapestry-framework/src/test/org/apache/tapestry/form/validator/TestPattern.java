// Copyright 2005 The Apache Software Foundation
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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Pattern}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestPattern extends BaseValidatorTestCase
{

    public void test_OK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        replay();

        new Pattern("pattern=\\d+").validate(field, messages, "1232");

        verify();
    }

    public void test_Fail()
    {
        String pattern = "\\d+";
        IFormComponent field = newField("My Pattern");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.PATTERN_MISMATCH,
                new Object[]
                { "My Pattern", pattern },
                "default message");

        replay();

        try
        {
            new Pattern("pattern=\\d+").validate(field, messages, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("default message", ex.getMessage());
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, ex.getConstraint());
        }

        verify();
    }

    public void test_Fail_Custom_Message()
    {
        String pattern = "\\d+";
        IFormComponent field = newField("My Pattern");
        ValidationMessages messages = newMessages(
                "custom",
                ValidationStrings.PATTERN_MISMATCH,
                new Object[]
                { "My Pattern", pattern },
                "custom message");
        
        replay();
        
        try
        {
            new Pattern("pattern=\\d+,message=custom").validate(field, messages, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("custom message", ex.getMessage());
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, ex.getConstraint());
        }

        verify();
    }

    public void test_Render_Contribution()
    {
        String rawPattern = "\\d+";
        String pattern = new RegexpMatcher().getEscapedPatternString(rawPattern);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        IFormComponent field = newField("Fred", "myfield");
        
        expect(context.getProfile()).andReturn(json);
        
        trainFormatMessage(context, null, ValidationStrings.PATTERN_MISMATCH, 
                new Object[] { "Fred", rawPattern }, "default message");
        
        replay();
        
        new Pattern("pattern=\\d+").renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"myfield\":[tapestry.form.validation.isValidPattern,\""
                + pattern + "\"]},"
                + "\"myfield\":{\"constraints\":\"default message\"}}",
                json.toString());
    }
    
    public void test_Render_Contribution_CustomMessage()
    {
        String rawPattern = "\\d+";
        String pattern = new RegexpMatcher().getEscapedPatternString(rawPattern);
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        IFormComponent field = newField("Fred", "myfield");
        
        expect(context.getProfile()).andReturn(json);
        
        trainFormatMessage(
                context,
                "custom",
                ValidationStrings.PATTERN_MISMATCH,
                new Object[] { "Fred", rawPattern },
                "custom\\message");
        
        replay();
        
        new Pattern("pattern=\\d+,message=custom").renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"myfield\":[tapestry.form.validation.isValidPattern,\""
                + pattern + "\"]},"
                +"\"myfield\":{\"constraints\":\"custom\\\\message\"}}",
                json.toString());
    }
    
}
