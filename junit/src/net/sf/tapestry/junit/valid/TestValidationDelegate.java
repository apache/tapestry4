/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.junit.valid;

import com.primix.tapestry.IRender;
import com.primix.tapestry.valid.IField;
import com.primix.tapestry.valid.IFieldTracking;
import com.primix.tapestry.valid.RenderString;
import com.primix.tapestry.valid.ValidationConstraint;
import com.primix.tapestry.valid.ValidationDelegate;
import com.primix.tapestry.valid.ValidatorException;
import java.util.List;
import junit.framework.TestCase;

/**
 *  Test the class {@link ValidationDelegate}. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

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
		IField f = new TestingField("testAdd");
		String errorMessage = "Need a bigger one.";

		d.setFormComponent(f);
		d.record(
			new ValidatorException(
				errorMessage,
				ValidationConstraint.TOO_LARGE,
				"Bad Stuff"));

		List fieldTracking = d.getFieldTracking();

		assertEquals(1, fieldTracking.size());

		IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

		assertEquals(f, t.getFormComponent());
		checkRender(errorMessage, t);
		assertEquals("testAdd", t.getFieldName());
		assertEquals("Bad Stuff", t.getInvalidInput());
		assertEquals(ValidationConstraint.TOO_LARGE, t.getConstraint());

		assertTrue(d.getHasErrors());
		assertEquals(errorMessage, ((RenderString)(d.getFirstError())).getString());
	}

	private void checkRender(String errorMessage, IFieldTracking tracking)
	{
		IRender render = tracking.getRenderer();

		assertEquals(errorMessage, ((RenderString) render).getString());
	}

	public void testMultipleInvalidInput()
	{
		IField f1 = new TestingField("input1");
		String e1 = "And now for something completely different.";
		IField f2 = new TestingField("input2");
		String e2 = "A man with three buttocks.";

		d.setFormComponent(f1);
		d.record(new ValidatorException(e1, null, "Monty"));

		d.setFormComponent(f2);
		d.record(new ValidatorException(e2, null, "Python"));

		List fieldTracking = d.getFieldTracking();
		assertEquals(2, fieldTracking.size());

		IFieldTracking t = (IFieldTracking) fieldTracking.get(0);

		assertEquals(f1, t.getFormComponent());
		checkRender(e1, t);

		t = (IFieldTracking) fieldTracking.get(1);
		assertEquals("Python", t.getInvalidInput());
		checkRender(e2, t);
		assertEquals(f2, t.getFormComponent());
	}

	public void testReset()
	{
		IField f1 = new TestingField("input1");
		String e1 = "And now for something completely different.";
		IField f2 = new TestingField("input2");
		String e2 = "A man with three buttocks.";

		d.setFormComponent(f1);
		d.record(new ValidatorException(e1, null, "Monty"));

		d.setFormComponent(f2);
		d.record(new ValidatorException(e2, null, "Python"));

		// Now, wipe out info on f1

		d.setFormComponent(f1);
		d.reset();

		List fieldTracking = d.getFieldTracking();
		assertEquals(1, fieldTracking.size());

		IFieldTracking t = (IFieldTracking) fieldTracking.get(0);
		assertEquals("Python", t.getInvalidInput());
		checkRender(e2, t);
		assertEquals(f2, t.getFormComponent());
	}

	public void testResetAll()
	{
		IField f1 = new TestingField("input1");
		String e1 = "And now for something completely different.";
		IField f2 = new TestingField("input2");
		String e2 = "A man with three buttocks.";

		d.setFormComponent(f1);
		d.record(new ValidatorException(e1, null, "Monty"));

		d.setFormComponent(f2);
		d.record(new ValidatorException(e2, null, "Python"));
		;

		d.setFormComponent(f1);
		d.reset();

		d.setFormComponent(f2);
		d.reset();

		List fieldTracking = d.getFieldTracking();
		assertEquals(0, fieldTracking.size());

		assertEquals(false, d.getHasErrors());
		assertNull(d.getFirstError());
	}
}