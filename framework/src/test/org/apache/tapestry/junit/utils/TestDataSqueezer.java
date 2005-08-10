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

package org.apache.tapestry.junit.utils;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.ComponentAddress;
import org.apache.tapestry.util.io.DataSqueezerImpl;
import org.apache.tapestry.util.io.DataSqueezerUtil;
import org.apache.tapestry.util.io.SqueezeAdaptor;

/**
 * A series of tests for {@link DataSqueezerImpl}&nbsp;and friends.
 * 
 * @author Howard Lewis Ship
 */

public class TestDataSqueezer extends TestCase
{
    private DataSqueezerImpl ds = DataSqueezerUtil.createUnitTestSqueezer();

    public TestDataSqueezer(String name)
    {
        super(name);
    }

    private void attempt(Object input, String expectedEncoding)
    {
        attempt(input, expectedEncoding, ds);
    }

    private void attempt(Object input, String expectedEncoding, DataSqueezer squeezer)

    {
        String encoding = squeezer.squeeze(input);

        assertEquals("String encoding.", expectedEncoding, encoding);

        Object output = squeezer.unsqueeze(encoding);

        assertEquals("Decoded object.", input, output);
    }

    public void testBoolean()
    {
        attempt(Boolean.TRUE, "T");
        attempt(Boolean.FALSE, "F");
    }

    public void testNull()
    {
        attempt(null, "X");
    }

    public void testByte()
    {
        attempt(new Byte((byte) 0), "b0");
        attempt(new Byte((byte) -5), "b-5");
        attempt(new Byte((byte) 72), "b72");
    }

    public void testFloat()
    {
        attempt(new Float(0), "f0.0");
        attempt(new Float(3.1459), "f3.1459");
        attempt(new Float(-37.23), "f-37.23");
    }

    public void testDouble()
    {
        attempt(new Double(0), "d0.0");
        attempt(new Double(3.1459), "d3.1459");
        attempt(new Double(-37.23), "d-37.23");
    }

    public void testInteger()
    {
        attempt(new Integer(0), "0");
        attempt(new Integer(205), "205");
        attempt(new Integer(-173), "-173");
    }

    public void testLong()
    {
        attempt(new Long(0), "l0");
        attempt(new Long(800400300l), "l800400300");
        attempt(new Long(-987654321l), "l-987654321");
    }

    public void testShort()
    {
        attempt(new Short((short) 0), "s0");
        attempt(new Short((short) -10), "s-10");
        attempt(new Short((short) 57), "s57");
    }

    /** @since 2.2 * */

    public void testCharacter()
    {
        attempt(new Character('a'), "ca");
        attempt(new Character('Z'), "cZ");
    }

    public void testString()
    {
        attempt("Now is the time for all good men ...", "SNow is the time for all good men ...");
        attempt("X marks the spot!", "SX marks the spot!");
        attempt("So long, sucker!", "SSo long, sucker!");
    }

    public void testComponentAddress()
    {
        ComponentAddress objAddress = new ComponentAddress("framework:DirectLink",
                "component.subcomponent");
        attempt(objAddress, "Aframework:DirectLink,component.subcomponent");

        objAddress = new ComponentAddress("framework:DirectLink", null);
        attempt(objAddress, "Aframework:DirectLink,");
    }

    public void testArray()
    {
        Object[] input =
        { new Short((short) -82), "Time to encode an array.", new Long(38383833273789l), null,
                Boolean.TRUE, new Double(22. / 7.) };

        String[] encoded = ds.squeeze(input);

        assertEquals("Encoded array length.", input.length, encoded.length);

        Object[] output = ds.unsqueeze(encoded);

        assertEquals("Output array length.", input.length, output.length);

        for (int i = 0; i < input.length; i++)
        {
            assertEquals(input[i], output[i]);
        }
    }

    public void testNullArray()
    {
        Object[] input = null;

        String[] encoded = ds.squeeze(input);

        assertNull(encoded);

        Object[] output = ds.unsqueeze(encoded);

        assertNull(output);
    }

    private void attempt(Serializable s, DataSqueezer squeezer)
    {
        String encoded = squeezer.squeeze(s);

        Object output = squeezer.unsqueeze(encoded);

        assertEquals(s, output);
    }

    public void testSerializableShort()
    {
        attempt(new StringHolder("X"), ds);
    }

    public void testSerializableLong()
    {

        Map map = new HashMap();

        map.put("alpha", Boolean.TRUE);
        map.put("beta", new StringHolder("FredFlintstone"));
        map.put("gamma", new BigDecimal(
                "2590742358742358972.234592348957230948578975248972390857490725"));

        attempt((Serializable) map, ds);
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

    public static class BHSqueezer implements SqueezeAdaptor
    {
        private final String prefix_;

        private final Class dataClass_;

        private static final String TRUE = "BT";

        private static final String FALSE = "BF";

        public BHSqueezer()
        {
            this("B", BooleanHolder.class);
        }

        public BHSqueezer(String prefix)
        {
            this(prefix, BooleanHolder.class);
        }

        public BHSqueezer(String prefix, Class dataClass)
        {
            prefix_ = prefix;
            dataClass_ = dataClass;
        }

        public String getPrefix()
        {
            return prefix_;
        }

        public Class getDataClass()
        {
            return dataClass_;
        }

        public String squeeze(DataSqueezer squeezer, Object data)
        {
            BooleanHolder h = (BooleanHolder) data;

            return h.getValue() ? TRUE : FALSE;
        }

        public Object unsqueeze(DataSqueezer squeezer, String string)
        {
            if (string.equals(TRUE))
                return new BooleanHolder(true);

            if (string.equals(FALSE))
                return new BooleanHolder(false);

            throw new ApplicationRuntimeException("Unexpected value.");
        }

    }

    public void testCustom()
    {
        DataSqueezerImpl squeezer = DataSqueezerUtil.createUnitTestSqueezer();
        squeezer.register(new BHSqueezer());

        attempt(new BooleanHolder(true), "BT", squeezer);
        attempt(new BooleanHolder(false), "BF", squeezer);

        attempt("BooleanHolder", "SBooleanHolder", squeezer);
    }

    public void testRegisterShortPrefix()
    {
        try
        {
            ds.register(new BHSqueezer(""));

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
            ds.register(new BHSqueezer("\n"));

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
            ds.register(new BHSqueezer("b"));

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
            ds.register(new BHSqueezer("B", null));

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
            ds.register(null);

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
        // TODO: Change the build to download some JAR file,
        // and change this code to reference a class within that
        // JAR file.

        File dir = new File(System.getProperty("PROJECT_ROOT", ".")
                + "/examples/Workbench/target/classes");

        if (!dir.exists())
        {
            unable("Unable to find classes directory " + dir + ".");
            return;
        }

        URL tutorialClassesURL = dir.toURL();

        URLClassLoader classLoader = new URLClassLoader(new URL[]
        { tutorialClassesURL });

        Class visitClass = classLoader.loadClass("org.apache.tapestry.workbench.Visit");

        Object visit = visitClass.newInstance();

        ClassLoader visitClassLoader = visit.getClass().getClassLoader();

        if (getClass().getClassLoader() == visitClassLoader)
        {
            unable("Unable to setup necessary ClassLoaders for test.");
            return;
        }

        // System.out.println("This classloader = " + getClass().getClassLoader());
        // System.out.println("Visit classloader = " + visit.getClass().getClassLoader());

        String stringValue = Long.toHexString(System.currentTimeMillis());

        PropertyUtils.write(visit, "stringValue", stringValue);

        ClassResolver resolver = new DefaultClassResolver(visitClassLoader);
        DataSqueezer squeezer = DataSqueezerUtil.createUnitTestSqueezer(resolver);

        String squeezed = squeezer.squeeze(visit);

        Object outVisit = squeezer.unsqueeze(squeezed);

        // System.out.println("outVisit classloader = " + outVisit.getClass().getClassLoader());

        assertNotNull(outVisit);
        assertTrue("Input and output objects not same.", visit != outVisit);

        String outStringValue = (String) PropertyUtils.read(outVisit, "stringValue");

        assertEquals("Stored string.", stringValue, outStringValue);
    }

}