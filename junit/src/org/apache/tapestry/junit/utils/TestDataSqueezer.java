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

package org.apache.tapestry.junit.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.spec.AssetType;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.util.ComponentAddress;
import org.apache.tapestry.util.DefaultResourceResolver;
import org.apache.tapestry.util.io.DataSqueezer;
import org.apache.tapestry.util.io.ISqueezeAdaptor;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  A series of tests for {@link DataSqueezer} and friends.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TestDataSqueezer extends TestCase
{
    private IResourceResolver _resolver = new DefaultResourceResolver();
    private DataSqueezer s = new DataSqueezer(_resolver);

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

    /** @since 2.2 **/

    public void testCharacter() throws IOException
    {
        attempt(new Character('a'), "ca");
        attempt(new Character('Z'), "cZ");
    }

    public void testString() throws IOException
    {
        attempt("Now is the time for all good men ...", "SNow is the time for all good men ...");
        attempt("X marks the spot!", "SX marks the spot!");
        attempt("So long, sucker!", "SSo long, sucker!");
    }

	/** @since 3.0 **/
	
	public void testEnum() throws IOException
	{
		attempt(AssetType.PRIVATE, "Eorg.apache.tapestry.spec.AssetType@PRIVATE");
	}

    public void testComponentAddress() throws IOException 
    {
        ComponentAddress objAddress = new ComponentAddress("framework:DirectLink", "component.subcomponent");
        attempt(objAddress, "Aframework:DirectLink/component.subcomponent");

        objAddress = new ComponentAddress("framework:DirectLink", null);
        attempt(objAddress, "Aframework:DirectLink/");
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
        DataSqueezer ds = new DataSqueezer(_resolver, new ISqueezeAdaptor[] { new BHSqueezer()});

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

    private void unable(String message)
    {
        System.err.println("Unable to run test " + getClass().getName() + " " + getName() + ":");
        System.err.println(message);
        System.err.println("This may be ignored when running tests inside Eclipse.");
    }

    public void testClassLoader() throws Exception
    {

        File cd = new File(System.getProperty("user.dir"));
        File dir = new File(cd.getParentFile(), "examples/Workbench/classes");

        if (!dir.exists())
        {
            unable("Unable to find classes directory " + dir + ".");
            return;
        }

        URL tutorialClassesURL = dir.toURL();

        URLClassLoader classLoader = new URLClassLoader(new URL[] { tutorialClassesURL });

        Class visitClass = classLoader.loadClass("org.apache.tapestry.workbench.Visit");

        Object visit = visitClass.newInstance();

        if (getClass().getClassLoader() == visit.getClass().getClassLoader())
        {
            unable("Unable to setup necessary ClassLoaders for test.");
            return;
        }

        // System.out.println("This classloader = " + getClass().getClassLoader());
        // System.out.println("Visit classloader = " + visit.getClass().getClassLoader());

        IResourceResolver resolver = new DefaultResourceResolver(visit.getClass().getClassLoader());

        String stringValue = Long.toHexString(System.currentTimeMillis());

        OgnlUtils.set("stringValue", resolver, visit, stringValue);

        DataSqueezer squeezer = new DataSqueezer(resolver);

        String squeezed = squeezer.squeeze(visit);

        Object outVisit = squeezer.unsqueeze(squeezed);

        // System.out.println("outVisit classloader = " + outVisit.getClass().getClassLoader());

        assertNotNull(outVisit);
        assertTrue("Input and output objects not same.", visit != outVisit);

        String outStringValue = (String) OgnlUtils.get("stringValue", resolver, outVisit);

        assertEquals("Stored string.", stringValue, outStringValue);
    }

}