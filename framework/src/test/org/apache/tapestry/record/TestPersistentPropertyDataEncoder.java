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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.record.PersistentPropertyDataEncoderImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPersistentPropertyDataEncoder extends HiveMindTestCase
{
    /**
     * Test pushing minimal amounts of data, which should favor the non-GZipped version of the
     * output stream.
     */

    public void testRoundTripShort() throws Exception
    {
        PropertyChange pc = new PropertyChangeImpl(null, "property", "foo");
        List input = Collections.singletonList(pc);

        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

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

        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

        String encoded = encoder.encodePageChanges(input);

        assertEquals("Z", encoded.substring(0, 1));

        List output = encoder.decodePageChanges(encoded);

        assertEquals(input, output);
    }

    public void testEmptyEncoding()
    {
        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

        assertEquals("", encoder.encodePageChanges(Collections.EMPTY_LIST));

        assertEquals(0, encoder.decodePageChanges("").size());
    }

    public void testEncodeNonSerializable()
    {
        PropertyChange pc = new PropertyChangeImpl(null, "property", new Object());
        List l = Collections.singletonList(pc);

        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

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
        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

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
        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

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
}