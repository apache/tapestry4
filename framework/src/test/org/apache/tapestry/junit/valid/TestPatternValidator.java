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

package org.apache.tapestry.junit.valid;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.PatternDelegate;
import org.apache.tapestry.valid.PatternValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Test cases for PatternValidator.
 * 
 * @author Harish Krishnaswamy
 * @since 3.0
 */
public class TestPatternValidator extends BaseValidatorTestCase
{
    PatternValidator pv = new PatternValidator();

    private void positiveTest(IFormComponent field, String input) throws ValidatorException
    {
        Object result = pv.toObject(field, input);
        assertEquals(input, result);
    }

    public void testFulfillingPatterns() throws ValidatorException
    {
        IFormComponent field = newField();

        pv.setPatternString("foo|foot");
        positiveTest(field, "xfooty");

        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");
        positiveTest(field, "06514");
        positiveTest(field, "06514-3149");
    }

    private void assertValidatorException(ValidatorException e)
    {
        assertEquals(ValidationConstraint.PATTERN_MISMATCH, e.getConstraint());
        assertEquals("PatternField does not fulfill the required pattern " + pv.getPatternString()
                + ".", e.getMessage());
    }

    private void negativeTest(String input)
    {
        IFormComponent field = newField("PatternField");

        replayControls();

        try
        {
            pv.toObject(field, input);
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertValidatorException(e);
        }

        verifyControls();
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
        Location l = fabricateLocation(11);

        MockControl control = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) control.getMock();

        field.getDisplayName();
        control.setReturnValue("badPattern");

        field.getLocation();
        control.setReturnValue(l);

        replayControls();

        pv.setPatternString("^(\\d{5}(-\\d{4})?$");

        try
        {
            pv.toObject(field, "06514");
            unreachable();
        }
        catch (ApplicationRuntimeException e)
        {
            checkException(e, "Unable to match pattern " + pv.getPatternString()
                    + " for field badPattern.");
            assertSame(l, e.getLocation());
        }

        verifyControls();
    }

    public void testOverridePatternNotMatchedMessage()
    {
        IFormComponent field = newField("PatternField");
        replayControls();

        pv.setPatternNotMatchedMessage("Field: {0}, Pattern: {1}, you figure!");
        pv.setPatternString("^(\\d{5}(-\\d{4})?)$");

        try
        {
            pv.toObject(field, "xyz");
            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, e.getConstraint());
            assertEquals("Field: PatternField, Pattern: ^(\\d{5}(-\\d{4})?)$, you figure!", e
                    .getMessage());
        }

        verifyControls();
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
        assertEquals("\\^\\(\\\\d\\{5\\}\\(\\-\\\\d\\{4\\}\\)\\?\\)\\$", pv
                .getEscapedPatternString());
    }

}