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

import java.util.Collections;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.record.PersistentPropertyData}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPersistentPropertyData extends HiveMindTestCase
{

    public void testStoreChange()
    {
        Object newObject1 = new Object();
        Object newObject2 = new Object();

        PersistentPropertyDataEncoder encoder = (PersistentPropertyDataEncoder) newMock(PersistentPropertyDataEncoder.class);

        replayControls();

        PersistentPropertyData ppd = new PersistentPropertyData(encoder);

        ppd.store("foo", "bar", newObject1);

        assertListsEqual(new Object[]
        { new PropertyChangeImpl("foo", "bar", newObject1) }, ppd.getPageChanges());

        // Check for overwriting.

        ppd.store("foo", "bar", newObject2);

        assertListsEqual(new Object[]
        { new PropertyChangeImpl("foo", "bar", newObject2) }, ppd.getPageChanges());

        // We only add the one value, because the output order
        // is indeterminate.

        verifyControls();
    }

    public void testDecode()
    {
        Object newObject = new Object();
        String encoded = "ENCODED";
        List decoded = Collections.singletonList(new PropertyChangeImpl("foo", "bar", newObject));

        MockControl control = newControl(PersistentPropertyDataEncoder.class);
        PersistentPropertyDataEncoder encoder = (PersistentPropertyDataEncoder) control.getMock();

        encoder.decodePageChanges(encoded);
        control.setReturnValue(decoded);

        replayControls();

        PersistentPropertyData ppd = new PersistentPropertyData(encoder);

        ppd.storeEncoded(encoded);

        List l1 = ppd.getPageChanges();

        assertListsEqual(new Object[]
        { new PropertyChangeImpl("foo", "bar", newObject) }, l1);

        List l2 = ppd.getPageChanges();

        assertNotSame(l1, l2);

        assertListsEqual(new Object[]
        { new PropertyChangeImpl("foo", "bar", newObject) }, l2);

        verifyControls();
    }

    public void testEncode()
    {
        String encoded = "ENCODED";
        Object newObject = new Object();
        List changes = Collections.singletonList(new PropertyChangeImpl("foo", "bar", newObject));

        MockControl control = newControl(PersistentPropertyDataEncoder.class);
        PersistentPropertyDataEncoder encoder = (PersistentPropertyDataEncoder) control.getMock();

        encoder.encodePageChanges(changes);
        control.setReturnValue(encoded);

        replayControls();

        PersistentPropertyData ppd = new PersistentPropertyData(encoder);

        ppd.store("foo", "bar", newObject);

        assertSame(encoded, ppd.getEncoded());
        assertSame(encoded, ppd.getEncoded());

        verifyControls();
    }
}