package net.sf.tapestry.junit.valid;

import java.util.List;

import junit.framework.TestCase;
import net.sf.tapestry.IRender;
import net.sf.tapestry.valid.IField;
import net.sf.tapestry.valid.IFieldTracking;
import net.sf.tapestry.valid.RenderString;
import net.sf.tapestry.valid.ValidationConstraint;
import net.sf.tapestry.valid.ValidationDelegate;
import net.sf.tapestry.valid.ValidatorException;

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