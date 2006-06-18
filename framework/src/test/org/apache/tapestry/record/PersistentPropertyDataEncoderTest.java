// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.record;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.record.PersistentPropertyDataEncoderImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PersistentPropertyDataEncoderTest extends HiveMindTestCase
{
    /**
     * Test pushing minimal amounts of data, which should favor the non-GZipped version of the
     * output stream.
     */

    public void testRoundTripShort() throws Exception
    {
        PropertyChange pc = new PropertyChangeImpl(null, "property", "foo");
        List input = Collections.singletonList(pc);

        PersistentPropertyDataEncoder encoder = newEncoder();

        String encoded = encoder.encodePageChanges(input);

        System.out.println(encoded);

        List output = encoder.decodePageChanges(encoded);

        assertEquals(input, output);

    }

    /**
     * Test pushing a lot of data, which should trigger the GZip encoding option.
     */

    public void testRoundTripLong() throws Exception
    {
        Random r = new Random();

        List input = new ArrayList();

        for (int i = 0; i < 20; i++)
        {
            PropertyChange pc = new PropertyChangeImpl(i % 2 == 0 ? null : "componentId",
                    "property" + i, new Long(r.nextLong()));

            input.add(pc);
        }

        PersistentPropertyDataEncoder encoder = newEncoder();

        String encoded = encoder.encodePageChanges(input);

        assertEquals("Z", encoded.substring(0, 1));

        List output = encoder.decodePageChanges(encoded);

        assertEquals(input, output);
    }

    private PersistentPropertyDataEncoder newEncoder()
    {
        return newEncoder(getClassResolver());
    }

    private PersistentPropertyDataEncoder newEncoder(ClassResolver resolver)
    {
        PersistentPropertyDataEncoderImpl encoder = new PersistentPropertyDataEncoderImpl();

        encoder.setClassResolver(resolver);

        return encoder;
    }

    public void testEmptyEncoding()
    {
        PersistentPropertyDataEncoder encoder = newEncoder();

        assertEquals("", encoder.encodePageChanges(Collections.EMPTY_LIST));

        assertEquals(0, encoder.decodePageChanges("").size());
    }

    public void testEncodeNonSerializable()
    {
        PropertyChange pc = new PropertyChangeImpl(null, "property", new Object());
        List l = Collections.singletonList(pc);

        PersistentPropertyDataEncoder encoder = newEncoder();

        try
        {
            encoder.encodePageChanges(l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "An exception occured encoding the data stream into MIME format: java.lang.Object",
                    ex.getMessage());
        }
    }

    public void testDecodeInvalid()
    {
        PersistentPropertyDataEncoder encoder = newEncoder();

        try
        {
            encoder.decodePageChanges("ZZZZZZZZZZZZZZZZZZZ");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "An exception occured decoding the MIME data stream: Not in GZIP format",
                    ex.getMessage());
        }
    }

    public void testDecodeUnknownPrefix()
    {
        PersistentPropertyDataEncoder encoder = newEncoder();

        try
        {
            encoder.decodePageChanges("QQQQQQQQQQQ");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "The prefix of the MIME encoded data stream was 'Q', it should be 'B' or 'Z'.",
                    ex.getMessage());
        }

    }

    /**
     * Test encoding and decoding a class that's only visible through a non-default class loader. We
     * have to use a lot of reflection on this one.
     * 
     * @see org.apache.tapestry.junit.utils.TestDataSqueezer#testClassLoader()
     */
    /*
    public void testEncodeDecodeCustomClass() throws Exception
    {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File projectRoot = new File(tempDir, "jakarta-tapestry");
        File springJAR = new File(projectRoot,
                "tapestry/target/module-lib/test-subject/spring/spring-1.1.jar");

        if (!springJAR.exists())
            throw new RuntimeException("File " + springJAR
                    + " does not exist; this should have been downloaded by the Ant build scripts.");

        ClassResolver resolver1 = newClassResolver(springJAR);

        Class propertyValueClass = resolver1.findClass("org.springframework.beans.PropertyValue");
        Constructor constructor = propertyValueClass.getConstructor(new Class[]
        { String.class, Object.class });

        Serializable instance = (Serializable) constructor.newInstance(new Object[]
        { "fred", "flintstone" });

        assertEquals("fred", PropertyUtils.read(instance, "name"));
        assertEquals("flintstone", PropertyUtils.read(instance, "value"));

        PersistentPropertyDataEncoder encoder1 = newEncoder(resolver1);

        PropertyChange pc = new PropertyChangeImpl("foo.bar", "property", instance);
        List changes = Collections.singletonList(pc);

        String encoded = encoder1.encodePageChanges(changes);

        // OK, to be 100% sure, we create a NEW encoder to decode the string in.
        ClassResolver resolver2 = newClassResolver(springJAR);
        PersistentPropertyDataEncoder encoder2 = newEncoder(resolver2);

        changes = encoder2.decodePageChanges(encoded);

        assertEquals(1, changes.size());

        pc = (PropertyChange) changes.get(0);

        assertEquals("property", pc.getPropertyName());
        assertEquals("foo.bar", pc.getComponentPath());

        Object instance2 = pc.getNewValue();

        assertNotSame(instance, instance2);

        assertEquals("fred", PropertyUtils.read(instance2, "name"));
        assertEquals("flintstone", PropertyUtils.read(instance2, "value"));
    }
*/
    private ClassResolver newClassResolver(File jarFile) throws Exception
    {
        URLClassLoader classLoader = new URLClassLoader(new URL[]
        { jarFile.toURL() });

        return new DefaultClassResolver(classLoader);

    }
}