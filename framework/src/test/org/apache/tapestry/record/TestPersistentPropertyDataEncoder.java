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

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.record.PersistentPropertyDataEncoderImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPersistentPropertyDataEncoder extends HiveMindTestCase
{
    public void testRoundTrip() throws Exception
    {
        Random r = new Random();

        List input = new ArrayList();

        for (int i = 0; i < 20; i++)
        {
            PropertyChange pc = new PropertyChangeImpl(i % 2 == 0 ? null : "componentId", "property" + i,
                    new Long(r.nextLong()));

            input.add(pc);
        }

        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

        String encoded = encoder.encodePageChanges(input);

        // System.out.println(encoded);

        List output = encoder.decodePageChanges(encoded);

        assertEquals(input, output);
    }

    public void testEmptyEncoding()
    {
        PersistentPropertyDataEncoder encoder = new PersistentPropertyDataEncoderImpl();

        assertEquals("", encoder.encodePageChanges(Collections.EMPTY_LIST));

        assertEquals(0, encoder.decodePageChanges("").size());
    }
}