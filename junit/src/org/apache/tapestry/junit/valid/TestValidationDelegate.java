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
 *  @version $Id$
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
}