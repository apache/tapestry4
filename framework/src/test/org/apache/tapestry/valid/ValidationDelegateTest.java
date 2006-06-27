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

package org.apache.tapestry.valid;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.apache.tapestry.IRender;
import org.apache.tapestry.form.IFormComponent;

/**
 * Test the class {@link ValidationDelegate}.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.8
 */

public class ValidationDelegateTest extends BaseValidatorTestCase
{
    protected IFormComponent newField(String name, int count)
    {
        IFormComponent fc = newMock(IFormComponent.class);

        expect(fc.getName()).andReturn(name).times(count);

        return fc;
    }

    private ValidationDelegate d = new ValidationDelegate();

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
        IFormComponent field = newField("testAdd", 3);

        replay();

        String errorMessage = "Need a bigger one.";

        d.setFormComponent(field);
        d.recordFieldInputValue("Bad Stuff");
        d.record(new ValidatorException(errorMessage, ValidationConstraint.TOO_LARGE));

        List fieldTracking = d.getFieldTracking();

        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertSame(field, t.getComponent());
        checkRender(errorMessage, t);
        assertEquals("testAdd", t.getFieldName());
        assertEquals("Bad Stuff", t.getInput());
        assertEquals(ValidationConstraint.TOO_LARGE, t.getConstraint());

        assertTrue(d.getHasErrors());
        assertEquals(errorMessage, ((RenderString) (d.getFirstError())).getString());

        verify();
    }

    public void testValidatorErrorRenderer()
    {
        IFormComponent field = newField("testValidatorErrorRenderer", 3);

        replay();

        IRender errorRenderer = new RenderString("Just don't like it.");

        d.setFormComponent(field);
        d.recordFieldInputValue("Bad Stuff");
        d.record(new ValidatorException("Just don't like it.", errorRenderer,
                ValidationConstraint.CONSISTENCY));

        List fieldTracking = d.getFieldTracking();

        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertSame(field, t.getComponent());
        assertSame(errorRenderer, t.getErrorRenderer());
        assertEquals("testValidatorErrorRenderer", t.getFieldName());
        assertEquals("Bad Stuff", t.getInput());
        assertEquals(ValidationConstraint.CONSISTENCY, t.getConstraint());

        assertTrue(d.getHasErrors());
        assertSame(errorRenderer, d.getFirstError());

        verify();
    }

    public void testNoError()
    {
        IFormComponent field = newField("input", 2);

        replay();

        d.setFormComponent(field);
        d.recordFieldInputValue("Futurama");

        List fieldTracking = d.getFieldTracking();
        assertEquals(1, fieldTracking.size());

        IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

        assertSame(field, t.getComponent());
        assertNull(t.getErrorRenderer());
        assertEquals(false, t.isInError());
        assertEquals("Futurama", t.getInput());
        assertNull(t.getConstraint());

        assertEquals(false, d.getHasErrors());
        assertNull(d.getFirstError());

        verify();
    }

    public void testUnassociatedErrors()
    {
        IFormComponent field = newField("input", 2);

        replay();

        d.setFormComponent(field);
        d.recordFieldInputValue("Bender");

        d.setFormComponent(null);
        d.record("Overload!", ValidationConstraint.CONSISTENCY);

        assertEquals(true, d.getHasErrors());

        List fieldTracking = d.getFieldTracking();
        assertEquals(2, fieldTracking.size());

        IFieldTracking t0 = (IFieldTracking) fieldTracking.get(0);
        assertEquals(false, t0.isInError());
        assertSame(field, t0.getComponent());

        IFieldTracking t1 = (IFieldTracking) fieldTracking.get(1);
        assertNull(t1.getComponent());
        assertEquals(true, t1.isInError());
        checkRender("Overload!", t1);

        checkRender("Overload!", d.getFirstError());

        List trackings = d.getUnassociatedTrackings();
        assertEquals(1, trackings.size());
        assertEquals(t1, trackings.get(0));

        trackings = d.getAssociatedTrackings();
        assertEquals(1, trackings.size());
        assertEquals(t0, trackings.get(0));

        verify();
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
        IFormComponent f1 = newField("monty", 3);
        IFormComponent f2 = newField("python", 3);

        replay();

        String e1 = "And now for something completely different.";
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

        assertSame(f1, t.getComponent());
        checkRender(e1, t);

        t = (IFieldTracking) fieldTracking.get(1);
        assertEquals("Python", t.getInput());
        checkRender(e2, t);
        assertSame(f2, t.getComponent());

        verify();
    }

    public void testReset()
    {
        IFormComponent f1 = newField("monty", 4);
        IFormComponent f2 = newField("python", 3);

        replay();

        String e1 = "And now for something completely different.";
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

        verify();
    }

    public void testResetAll()
    {
        IFormComponent f1 = newField("monty", 3);
        IFormComponent f2 = newField("python", 3);

        replay();

        String e1 = "And now for something completely different.";
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

        verify();
    }

    /** @since 4.0 */

    public void testGetErrorRenderers()
    {
        List l = d.getErrorRenderers();

        assertEquals(true, l.isEmpty());

        IFormComponent f1 = newField("monty", 2);
        IFormComponent f2 = newField("python", 3);

        IRender f2ErrorRenderer = (IRender) newMock(IRender.class);

        replay();

        d.setFormComponent(f1);
        d.recordFieldInputValue("f1 input");

        d.setFormComponent(f2);
        d.recordFieldInputValue("f2 input");
        d.record(f2ErrorRenderer, null);

        l = d.getErrorRenderers();
        assertEquals(1, l.size());
        assertSame(f2ErrorRenderer, l.get(0));

        verify();
    }

    public void testClearErrors()
    {
        IFormComponent f = newField("input", 4);

        replay();

        d.setFormComponent(f);
        d.recordFieldInputValue("hello");
        d.record("An error in the input field.", null);

        assertEquals(true, d.getHasErrors());

        assertNotNull(d.getFirstError());

        d.clearErrors();

        assertEquals(false, d.getHasErrors());

        d.setFormComponent(f);

        assertEquals("hello", d.getFieldInputValue());

        verify();
    }

    public void testRegisterForFocus()
    {
        IFormComponent fred = newFieldWithClientId("fred");
        IFormComponent barney = newFieldWithClientId("barney");
        IFormComponent wilma = newField();

        ValidationDelegate vd = new ValidationDelegate();

        replay();

        vd.registerForFocus(fred, ValidationConstants.NORMAL_FIELD);
        vd.registerForFocus(barney, ValidationConstants.REQUIRED_FIELD);
        vd.registerForFocus(wilma, ValidationConstants.NORMAL_FIELD);

        assertEquals("barney", vd.getFocusField());

        verify();
    }

    private IFormComponent newFieldWithClientId(String clientId)
    {
        IFormComponent field = newMock(IFormComponent.class);

        expect(field.getClientId()).andReturn(clientId);

        return field;
    }

    /**
     * Test {@link ValidationDelegate#record(IFormComponent, String)}.
     * 
     * @since 4.0
     */
    public void testSimpleRecord()
    {
        IFormComponent field = newField();

        trainGetName(field, "myField");
        trainGetName(field, "myField");

        replay();

        ValidationDelegate delegate = new ValidationDelegate();

        delegate.record(field, "My Error Message");

        List list = delegate.getFieldTracking();

        assertEquals(1, list.size());

        IFieldTracking ft = (IFieldTracking) list.get(0);

        assertEquals(true, ft.isInError());
        assertSame(field, ft.getComponent());
        assertNull(ft.getConstraint());
        assertEquals("My Error Message", ft.getErrorRenderer().toString());

        verify();
    }

    public void testSimpleRecordUnassociated()
    {
        ValidationDelegate delegate = new ValidationDelegate();

        delegate.record(null, "My Error Message");

        List list = delegate.getUnassociatedTrackings();

        assertEquals(1, list.size());

        IFieldTracking ft = (IFieldTracking) list.get(0);

        assertEquals(true, ft.isInError());
        assertNull(ft.getComponent());
        assertNull(ft.getConstraint());
        assertEquals("My Error Message", ft.getErrorRenderer().toString());

    }

    protected void trainGetName(IFormComponent field, String name)
    {
        expect(field.getName()).andReturn(name);
    }
}
