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

import com.primix.tapestry.valid.IField;
import com.primix.tapestry.valid.NumberValidator;
import com.primix.tapestry.valid.ValidatorException;
import java.math.BigDecimal;
import java.math.BigInteger;
import junit.framework.TestCase;

/**
 *  Test the {@link NumberValidator}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */
public class TestNumberValidator extends TestCase
{
	private NumberValidator v = new NumberValidator();

	public TestNumberValidator(String name)
	{
		super(name);
	}

	private void testPassThru(String displayName, Class valueType, Number input)
		throws ValidatorException
	{
		testPassThru(new TestingField(displayName, valueType), input);
	}

	private void testPassThru(IField field, Number input)
		throws ValidatorException
	{
		String s = v.toString(field, input);

		Object o = v.toObject(field, s);

		assertEquals("Input and output.", input, o);
	}

	public void testShort() throws ValidatorException
	{
		testPassThru("testShort", Short.class, new Short((short)1000));
	}
	
	public void testInteger() throws ValidatorException
	{
		testPassThru("testInteger", Integer.class, new Integer(373));
	}
	
	public void testByte() throws ValidatorException
	{
		testPassThru("testByte", Byte.class, new Byte((byte)131));
	}
	
	public void testFloat() throws ValidatorException
	{
		testPassThru("testFloat", Float.class, new Float(3.1415));
	}
	
	public void testDouble() throws ValidatorException
	{
		testPassThru("testDouble", Double.class, new Double(348348.484854848));
	}
	
	public void testBigInteger() throws ValidatorException
	{
		testPassThru("testBigInteger",
			BigInteger.class,
			new BigInteger("234905873490587234905724908252390487590234759023487523489075"));
	}
	
	public void testBigDecimal() throws ValidatorException
	{
		testPassThru("testBigDecimal",
		BigDecimal.class,
		new BigDecimal("-29574923857342908743.29058734289734907543289752345897234590872349085"));
	}
}