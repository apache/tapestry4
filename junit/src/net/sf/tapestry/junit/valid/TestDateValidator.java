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

import net.sf.tapestry.valid.DateValidator;
import net.sf.tapestry.valid.ValidatorException;
import net.sf.tapestry.valid.ValidationConstraint;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import junit.framework.TestCase;

/**
 *  
 * Tests the {@link DateValidator} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

public class TestDateValidator extends TestCase
{
	private Calendar calendar = new GregorianCalendar();
	
	private DateValidator v = new DateValidator();

	private Date buildDate(int month, int day, int year)
	{
		calendar.clear();
		
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.YEAR, year);

		
		return calendar.getTime();
	}

	public TestDateValidator(String name)
	{
		super(name);
	}

	public void testToStringNull()
	{
		String out = v.toString(null, null);
		
		assertNull(out);
	}
	
	public void testToStringValid()
	{
		String out = v.toString(null, buildDate(Calendar.DECEMBER, 8, 2001));
		
		assertEquals("Result.", "12/08/2001", out);
	}
	
	public void testToStringFormat()
	{
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
	
		v.setFormat(format);
		
		String out = v.toString(null, buildDate(Calendar.DECEMBER, 8, 2001));
		
		assertEquals("Result.", "08.12.01", out);
	}
	
	public void testToObjectNull() throws ValidatorException
	{
		Object out = v.toObject(new TestingField("toObjectNull"), null);
		
		assertNull(out);
	}
	
	public void testToObjectEmpty() throws ValidatorException
	{
		Object out = v.toObject(new TestingField("toObjectNull"), "");
		
		assertNull(out);
	}	
	
	public void testToObjectInvalid()
	{
		try
		{
			v.toObject(new TestingField("toObjectInvalid"), "frankenhooker");
			
			fail("Exception expected.");
		}
		catch (ValidatorException ex)
		{
			assertEquals(ValidationConstraint.DATE_FORMAT, ex.getConstraint());
		}
	}
	
	public void testToObjectFormat()
	throws ValidatorException
	{
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
	
		v.setFormat(format);
		
		Object out = v.toObject(null, 	"08.12.01");
		
		assertEquals("Result.", buildDate(Calendar.DECEMBER, 8, 2001)
				,		 out);
	}
	
	public void testToObjectMinimum()
	{
		v.setMinimum(buildDate(Calendar.DECEMBER, 24, 2001));
		
		try
		{
			v.toObject(new TestingField("toObjectMinimum"),
						"12/8/2001");
			fail("Exception expected.");			
		}
		catch (ValidatorException ex)
		{
			assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
		}
	}
	
	public void testToObjectMinimumNull()
	throws ValidatorException
	{
		v.setMinimum(buildDate(Calendar.DECEMBER, 24, 2001));
		
		Object out = v.toObject(null, null);
		
		assertNull(out);
	}

	public void testToObjectMaximum()
	{
		v.setMaximum(buildDate(Calendar.DECEMBER, 24, 2001));
		
		try
		{
			v.toObject(new TestingField("toObjectMinimum"),
						"12/8/2002");
			fail("Exception expected.");			
		}
		catch (ValidatorException ex)
		{
			assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
		}
	}
	
	public void testToObjectMaximumNull()
	throws ValidatorException
	{
		v.setMaximum(buildDate(Calendar.DECEMBER, 24, 2001));
		
		Object out = v.toObject(null, null);
		
		assertNull(out);
	}

}

