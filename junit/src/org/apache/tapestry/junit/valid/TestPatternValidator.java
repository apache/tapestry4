/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
    IFormComponent pf = new TestingField("PatternField");

    public TestPatternValidator(String name)
    {
        super(name);
    }

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
            Object result = pv.toObject(pf, input);
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
            Object result = pv.toObject(pf, "06514");
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
            Object result = pv.toObject(pf, "xyz");
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
