package net.sf.tapestry.junit.valid;

import junit.framework.TestCase;
import net.sf.tapestry.valid.StringValidator;
import net.sf.tapestry.valid.ValidationConstraint;
import net.sf.tapestry.valid.ValidatorException;

/**
 *  Tests the {@link StringValidator} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class TestStringValidator extends TestCase
{
	private StringValidator v = new StringValidator();

	public TestStringValidator(String name)
	{
		super(name);
	}

	public void testToString()
	{
		String in = "Foo";
		String out = v.toString(new TestingField("myField"), in);

		assertEquals("Result.", in, out);
	}

	public void testToStringNull()
	{
		String out = v.toString(new TestingField("nullField"), null);

		assertNull("Null expected.", out);
	}

	public void testToObjectRequiredFail()
	{
		v.setRequired(true);

		try
		{
			v.toObject(new TestingField("requiredField"), "");

			fail("Exception expected.");
		}
		catch (ValidatorException ex)
		{
			assertEquals(ValidationConstraint.REQUIRED, ex.getConstraint());
		}
	}

	public void testToObjectRequiredPass() throws ValidatorException
	{
		v.setRequired(true);

		Object result = v.toObject(new TestingField("requiredField"), "stuff");

		assertEquals("Result.", "stuff", result);
	}

	public void testToObjectMinimumFail()
	{
		v.setMinimumLength(10);
		
		try
		{
			v.toObject(new TestingField("minimum"), "short");
			
			fail("Exception expected.");
		}
		catch (ValidatorException ex)
		{
			assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
		}
	}
	
	public void testToObjectMinimumPass()
	throws ValidatorException
	{
		v.setMinimumLength(10);
		
		String in = "ambidexterous";
		
		Object out = v.toObject(new TestingField("minimum"), in);
		
		assertEquals("Result", in, out);
	}
	
	/**
	 *  An empty string is not subject to the minimum length constraint.
	 * 
	 **/
	
	public void testToObjectMinimumNull()	throws ValidatorException
	{
		v.setMinimumLength(10);
		
		String in = "";
		
		Object out = v.toObject(new TestingField("minimum"), in);
		
		assertNull("Result", out);
	}	
}