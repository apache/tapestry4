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

import net.sf.tapestry.valid.StringValidator;
import net.sf.tapestry.valid.ValidatorException;
import net.sf.tapestry.valid.ValidationConstraint;
import junit.framework.TestCase;

/**
 *  Tests the {@link StringValidator} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

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
			Object result = v.toObject(new TestingField("requiredField"), "");

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