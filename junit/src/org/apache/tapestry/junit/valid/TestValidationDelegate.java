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

import java.util.List;

import junit.framework.TestCase;
import org.apache.tapestry.IRender;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.RenderString;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

/**
 *  Test the class {@link ValidationDelegate}. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @since 1.0.8
 *
 **/

public class TestValidationDelegate extends TestCase
{
    private ValidationDelegate d = new ValidationDelegate();

    public TestValidationDelegate(String name)
    {
        super(name);
    }

    public void testHasErrorsEmpty()
    {
        assertEquals(false, d.getHasErrors());
    }

    public void testFirstErrorEmpty()
    {
        assertNull(d.getFirstError());
    }

    public void testInvalidInput()
    {
        IFormComponent f = new MockField("testAdd");
        String errorMessage = "Need a bigger one.";

        d.setFormComponent(f);
        d.recordFieldInputValue("Bad Stuff");
        d.record(new ValidatorException(errorMessage, ValidationConstraint.TOO_LARGE));

        List fieldTracking = d.getFieldTracking();

        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertEquals(f, t.getComponent());
        checkRender(errorMessage, t);
        assertEquals("testAdd", t.getFieldName());
        assertEquals("Bad Stuff", t.getInput());
        assertEquals(ValidationConstraint.TOO_LARGE, t.getConstraint());

        assertTrue(d.getHasErrors());
        assertEquals(errorMessage, ((RenderString) (d.getFirstError())).getString());
    }

    public void testValidatorErrorRenderer()
    {
        IFormComponent f = new MockField("testValidatorErrorRenderer");

        IRender errorRenderer = new RenderString("Just don't like it.");

        d.setFormComponent(f);
        d.recordFieldInputValue("Bad Stuff");
        d.record(
            new ValidatorException(
                "Just don't like it.",
                errorRenderer,
                ValidationConstraint.CONSISTENCY));

        List fieldTracking = d.getFieldTracking();

        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertEquals(f, t.getComponent());
        assertEquals(errorRenderer, t.getErrorRenderer());
        assertEquals("testValidatorErrorRenderer", t.getFieldName());
        assertEquals("Bad Stuff", t.getInput());
        assertEquals(ValidationConstraint.CONSISTENCY, t.getConstraint());

        assertTrue(d.getHasErrors());
        assertEquals(errorRenderer, d.getFirstError());
    }

    public void testNoError()
    {
        IFormComponent f = new MockField("testNoError");

        d.setFormComponent(f);
        d.recordFieldInputValue("Futurama");

        List fieldTracking = d.getFieldTracking();
        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertEquals(f, t.getComponent());
        assertEquals(null, t.getErrorRenderer());
        assertEquals(false, t.isInError());
        assertEquals("Futurama", t.getInput());
        assertEquals(null, t.getConstraint());

        assertEquals(false, d.getHasErrors());
        assertNull(d.getFirstError());
    }

    public void testUnassociatedErrors()
    {
        IFormComponent f = new MockField("testUnassociatedErrors");

        d.setFormComponent(f);
        d.recordFieldInputValue("Bender");

        d.setFormComponent(null);
        d.record("Overload!", ValidationConstraint.CONSISTENCY);

        assertEquals(true, d.getHasErrors());

        List fieldTracking = d.getFieldTracking();
        assertEquals(2, fieldTracking.size());

        IFieldTracking t0 = (IFieldTracking) fieldTracking.get(0);
        assertEquals(false, t0.isInError());
        assertEquals(f, t0.getComponent());

        IFieldTracking t1 = (IFieldTracking) fieldTracking.get(1);
        assertEquals(null, t1.getComponent());
        assertEquals(true, t1.isInError());
        checkRender("Overload!", t1);

        checkRender("Overload!", d.getFirstError());

        List trackings = d.getUnassociatedTrackings();
        assertEquals(1, trackings.size());
        assertEquals(t1, trackings.get(0));

        trackings = d.getAssociatedTrackings();
        assertEquals(1, trackings.size());
        assertEquals(t0, trackings.get(0));
    }

    /**
     * In rare cases, you may add errors even though the page hasn't rendered and that's
     * was causing a NPE.
     */
    public void testComponentNotRecorded()
    {
        // This mock field neaver rendered, so it does not have a Form-assigned name.

        IFormComponent f = new MockField(null);

        d.setFormComponent(f);
        d.record("Never rendered.", ValidationConstraint.CONSISTENCY);

        assertEquals(true, d.getHasErrors());

        List fieldTracking = d.getFieldTracking();
        assertEquals(1, fieldTracking.size());

        List trackings = d.getUnassociatedTrackings();
        assertEquals(1, trackings.size());

        IFieldTracking t = (IFieldTracking) trackings.get(0);

        assertEquals(null, t.getComponent());
        assertEquals(true, t.isInError());
        checkRender("Never rendered.", t);
    }

    private void checkRender(String errorMessage, IFieldTracking tracking)
    {
        IRender render = tracking.getErrorRenderer();

        checkRender(errorMessage, render);
    }

    private void checkRender(String errorMessage, IRender render)
    {
        assertEquals(errorMessage, ((RenderString) render).getString());
    }

    public void testMultipleInvalidInput()
    {
        IFormComponent f1 = new MockField("input1");
        String e1 = "And now for something completely different.";
        IFormComponent f2 = new MockField("input2");
        String e2 = "A man with three buttocks.";

        d.setFormComponent(f1);
        d.recordFieldInputValue("Monty");
        d.record(new ValidatorException(e1, null));

        d.setFormComponent(f2);
        d.recordFieldInputValue("Python");
        d.record(new ValidatorException(e2, null));

        List fieldTracking = d.getFieldTracking();
        assertEquals(2, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertEquals(f1, t.getComponent());
        checkRender(e1, t);

        t = (IFieldTracking) fieldTracking.get(1);
        assertEquals("Python", t.getInput());
        checkRender(e2, t);
        assertEquals(f2, t.getComponent());
    }

    public void testReset()
    {
        IFormComponent f1 = new MockField("input1");
        String e1 = "And now for something completely different.";
        IFormComponent f2 = new MockField("input2");
        String e2 = "A man with three buttocks.";

        d.setFormComponent(f1);
        d.recordFieldInputValue("Monty");
        d.record(new ValidatorException(e1, null));

        d.setFormComponent(f2);
        d.recordFieldInputValue("Python");
        d.record(new ValidatorException(e2, null));

        // Now, wipe out info on f1

        d.setFormComponent(f1);
        d.reset();

        List fieldTracking = d.getFieldTracking();
        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);
        assertEquals("Python", t.getInput());
        checkRender(e2, t);
        assertEquals(f2, t.getComponent());
    }

    public void testResetAll()
    {
        IFormComponent f1 = new MockField("input1");
        String e1 = "And now for something completely different.";
        IFormComponent f2 = new MockField("input2");
        String e2 = "A man with three buttocks.";

        d.setFormComponent(f1);
        d.record(new ValidatorException(e1, null));

        d.setFormComponent(f2);
        d.record(new ValidatorException(e2, null));

        d.setFormComponent(f1);
        d.reset();

        d.setFormComponent(f2);
        d.reset();

        assertEquals(null, d.getFieldTracking());

        assertEquals(false, d.getHasErrors());
        assertNull(d.getFirstError());
    }

    public void testClearErrors()
    {
        IFormComponent f = new MockField("input");

        d.setFormComponent(f);
        d.recordFieldInputValue("hello");
        d.record("An error in the input field.", null);

        assertEquals(true, d.getHasErrors());

        assertNotNull(d.getFirstError());

        d.clearErrors();

        assertEquals(false, d.getHasErrors());

        d.setFormComponent(f);

        assertEquals("hello", d.getFieldInputValue());
    }
}