//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.valid;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.valid.PatternDelegate;
import org.apache.tapestry.valid.PatternValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Test cases for PatternValidator.
 * 
 * @author  Harish Krishnaswamy
 * @version $Id$
 * @since   3.0
 */
public class TestPatternValidator extends TapestryTestCase
{
    PatternValidator pv = new PatternValidator();
    IFormComponent pf = new MockField("PatternField");

    private void positiveTest(String input) throws ValidatorException
    {
        Object result = pv.toObject(pf, input);
        assertEquals(input, result);
    }

    public void testFulfillingPatterns() throws ValidatorException
    {
        pv.setPatternString("foo|foot");
        positiveTest("xfooty");

        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");
        positiveTest("06514");
        positiveTest("06514-3149");
    }

    private void assertValidatorException(ValidatorException e)
    {
        assertEquals(ValidationConstraint.PATTERN_MISMATCH, e.getConstraint());
        assertEquals(
            "PatternField does not fulfill the required pattern " + pv.getPatternString() + ".",
            e.getMessage());
    }

    private void negativeTest(String input)
    {
        try
        {
            pv.toObject(pf, input);
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertValidatorException(e);
        }
    }

    public void testUnfulfillingPatterns()
    {
        pv.setPatternString("foo|foot");
        negativeTest("fot");

        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");
        negativeTest("065143");
        negativeTest("06514-314");
    }

    public void testMalformedPattern() throws ValidatorException
    {
        pv.setPatternString("^(\\d{5}(-\\d{4})?$");

        try
        {
            pv.toObject(pf, "06514");
            unreachable();
        }
        catch (ApplicationRuntimeException e)
        {
            checkException(
                e,
                "Unable to match pattern "
                    + pv.getPatternString()
                    + " for field "
                    + pf.getDisplayName());
        }
    }

    public void testOverridePatternNotMatchedMessage()
    {
        pv.setPatternNotMatchedMessage("Field: {0}, Pattern: {1}, you figure!");
        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");

        try
        {
            pv.toObject(pf, "xyz");
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, e.getConstraint());
            assertEquals(
                "Field: PatternField, Pattern: ^(\\d{5}(-\\d{4})?)$, you figure!",
                e.getMessage());
        }
    }

    public void testOverridePatternMatcher()
    {
        class MatcherDelegate implements PatternDelegate
        {
            public boolean contains(String value, String patternString)
            {
                return false;
            }

            public String getEscapedPatternString(String patternString)
            {
                return null;
            }
        }

        pv.setPatternDelegate(new MatcherDelegate());
        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");
        negativeTest("06514-3149");

        assertNull(pv.getEscapedPatternString());
    }

    public void testGetEscapedPatternString()
    {
        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");
        assertEquals(
            "\\^\\(\\\\d\\{5\\}\\(\\-\\\\d\\{4\\}\\)\\?\\)\\$",
            pv.getEscapedPatternString());
    }

}
