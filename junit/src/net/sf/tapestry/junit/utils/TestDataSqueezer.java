/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship 
 *
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */
package net.sf.tapestry.junit.utils;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.BooleanHolder;


import net.sf.tapestry.spec.*;
import net.sf.tapestry.spec.BeanLifecycle;
import net.sf.tapestry.util.io.*;
import net.sf.tapestry.util.io.DataSqueezer;
import net.sf.tapestry.util.io.ISqueezeAdaptor;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 *  A series of tests for {@link DataSqueezer} and friends.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 */

public class TestDataSqueezer extends TestCase
{
	private DataSqueezer s = new DataSqueezer();

	public TestDataSqueezer(String name)
	{
		super(name);
	}

	private void attempt(Object input, String expectedEncoding) throws IOException
	{
		attempt(input, expectedEncoding, s);
	}

	private void attempt(Object input, String expectedEncoding, DataSqueezer ds) throws IOException
	{
		String encoding = ds.squeeze(input);

		assertEquals("String encoding.", expectedEncoding, encoding);

		Object output = ds.unsqueeze(encoding);

		assertEquals("Decoded object.", input, output);
	}

	public void testBoolean() throws IOException
	{
		attempt(Boolean.TRUE, "T");
		attempt(Boolean.FALSE, "F");
	}

	public void testNull() throws IOException
	{
		attempt(null, "X");
	}

	public void testByte() throws IOException
	{
		attempt(new Byte((byte) 0), "b0");
		attempt(new Byte((byte) - 5), "b-5");
		attempt(new Byte((byte) 72), "b72");
	}

	public void testFloat() throws IOException
	{
		attempt(new Float(0), "f0.0");
		attempt(new Float(3.1459), "f3.1459");
		attempt(new Float(-37.23), "f-37.23");
	}

	public void testDouble() throws IOException
	{
		attempt(new Double(0), "d0.0");
		attempt(new Double(3.1459), "d3.1459");
		attempt(new Double(-37.23), "d-37.23");
	}

	public void testInteger() throws IOException
	{
		attempt(new Integer(0), "0");
		attempt(new Integer(205), "205");
		attempt(new Integer(-173), "-173");
	}

	public void testLong() throws IOException
	{
		attempt(new Long(0), "l0");
		attempt(new Long(800400300l), "l800400300");
		attempt(new Long(-987654321l), "l-987654321");
	}

	public void testShort() throws IOException
	{
		attempt(new Short((short) 0), "s0");
		attempt(new Short((short) - 10), "s-10");
		attempt(new Short((short) 57), "s57");
	}

	public void testString() throws IOException
	{
		attempt("Now is the time for all good men ...", "Now is the time for all good men ...");
		attempt("X marks the spot!", "SX marks the spot!");
		attempt("So long, sucker!", "SSo long, sucker!");
	}

	public void testArray() throws IOException
	{
		Object[] input =
			{
				new Short((short) - 82),
				"Time to encode an array.",
				new Long(38383833273789l),
				null,
				Boolean.TRUE,
				new Double(22. / 7.)};

		String[] encoded = s.squeeze(input);

		assertEquals("Encoded array length.", input.length, encoded.length);

		Object[] output = s.unsqueeze(encoded);

		assertEquals("Output array length.", input.length, output.length);

		for (int i = 0; i < input.length; i++)
		{
			assertEquals(input[i], output[i]);
		}
	}

	public void testNullArray() throws IOException
	{
		Object[] input = null;

		String[] encoded = s.squeeze(input);

		assertNull(encoded);

		Object[] output = s.unsqueeze(encoded);

		assertNull(output);
	}

	private void attempt(Serializable s, DataSqueezer ds) throws IOException
	{
		String encoded = ds.squeeze(s);

		Object output = ds.unsqueeze(encoded);

		assertEquals(s, output);
	}

	public void testSerializable() throws IOException
	{
		attempt(BeanLifecycle.PAGE, s);
		attempt(BeanLifecycle.REQUEST, s);

		Map map = new HashMap();

		map.put("alpha", Boolean.TRUE);
		map.put("beta", BeanLifecycle.NONE);
		map.put("gamma", new BigDecimal("2590742358742358972.234592348957230948578975248972390857490725"));

		attempt((Serializable) map, s);
	}

	public static class BooleanHolder
	{
		private boolean value;

		public BooleanHolder()
		{
		}

		public BooleanHolder(boolean value)
		{
			this.value = value;
		}

		public boolean getValue()
		{
			return value;
		}

		public void setValue(boolean value)
		{
			this.value = value;
		}

		public boolean equals(Object other)
		{
			if (other == null)
				return false;

			if (this == other)
				return true;

			if (!(other instanceof BooleanHolder))
				return false;

			BooleanHolder otherHolder = (BooleanHolder) other;

			return value == otherHolder.value;
		}
	}

	public static class BHSqueezer implements ISqueezeAdaptor
	{
		private static final String PREFIX = "B";

		private static final String TRUE = "BT";
		private static final String FALSE = "BF";

		public void register(DataSqueezer squeezer)
		{
			squeezer.register(PREFIX, BooleanHolder.class, this);
		}

		public String squeeze(DataSqueezer squeezer, Object data) throws IOException
		{
			BooleanHolder h = (BooleanHolder) data;

			return h.getValue() ? TRUE : FALSE;

		}

		public Object unsqueeze(DataSqueezer squeezer, String string) throws IOException
		{
			if (string.equals(TRUE))
				return new BooleanHolder(true);

			if (string.equals(FALSE))
				return new BooleanHolder(false);

			throw new IOException("Unexpected value.");
		}

	}

	public void testCustom() throws IOException
	{
		DataSqueezer ds = new DataSqueezer(new ISqueezeAdaptor[] { new BHSqueezer()});

		attempt(new BooleanHolder(true), "BT", ds);
		attempt(new BooleanHolder(false), "BF", ds);

		attempt("BooleanHolder", "SBooleanHolder", ds);
	}

	public void testRegisterShortPrefix()
	{
		try
		{
			s.register("", BooleanHolder.class, new BHSqueezer());

			throw new AssertionFailedError("Null prefix should be invalid.");
		}
		catch (IllegalArgumentException ex)
		{
		}
	}

	public void testRegisterInvalidPrefix()
	{
		try
		{
			s.register("\n", BooleanHolder.class, new BHSqueezer());

			throw new AssertionFailedError("Prefix should be invalid.");
		}
		catch (IllegalArgumentException ex)
		{
		}
	}

	public void testRegisterDupePrefix()
	{
		try
		{
			s.register("b", BooleanHolder.class, new BHSqueezer());

			throw new AssertionFailedError("Duplicate prefix should be invalid.");
		}
		catch (IllegalArgumentException ex)
		{
		}
	}

	public void testRegisterNullClass()
	{
		try
		{
			s.register("B", null, new BHSqueezer());

			throw new AssertionFailedError("Null data class should be invalid.");
		}
		catch (IllegalArgumentException ex)
		{
		}
	}

	public void testRegisterNullSqueezer()
	{
		try
		{
			s.register("B", BooleanHolder.class, null);

			throw new AssertionFailedError("Null squeezer should be invalid.");
		}
		catch (IllegalArgumentException ex)
		{
		}
	}
}