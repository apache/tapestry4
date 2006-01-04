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

package org.apache.tapestry.event;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.describe.DescriptionReceiver;

/**
 * Tests for {@link org.apache.tapestry.event.ReportStatusEvent}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ReportStatusEventTest extends HiveMindTestCase
{
    protected DescriptionReceiver newReceiver()
    {
        return (DescriptionReceiver) newMock(DescriptionReceiver.class);
    }

    public void testConstructor()
    {
        DescriptionReceiver receiver = newReceiver();

        replayControls();

        ReportStatusEvent event = new ReportStatusEvent(this, receiver);

        assertSame(this, event.getSource());

        verifyControls();
    }

    public void testDelegation()
    {
        Object[] values = new Object[]
        { 1, 2, 3 };
        Object alternate = new Object();

        DescriptionReceiver receiver = newReceiver();

        receiver.array("array", values);
        receiver.describeAlternate(alternate);
        receiver.property("boolean-true", true);
        receiver.property("boolean-false", false);
        receiver.property("byte", (byte) 37);
        receiver.property("char", 'z');
        receiver.property("double", (double) 3.14);
        receiver.property("float", (float) 9.99);
        receiver.property("int", -373);
        receiver.property("long", 373737l);
        receiver.property("object", this);
        receiver.property("short", (short) 99);
        receiver.section("Section");
        receiver.title("Title");

        replayControls();

        ReportStatusEvent event = new ReportStatusEvent(this, receiver);

        event.array("array", values);
        event.describeAlternate(alternate);
        event.property("boolean-true", true);
        event.property("boolean-false", false);
        event.property("byte", (byte) 37);
        event.property("char", 'z');
        event.property("double", (double) 3.14);
        event.property("float", (float) 9.99);
        event.property("int", -373);
        event.property("long", 373737l);
        event.property("object", this);
        event.property("short", (short) 99);
        event.section("Section");
        event.title("Title");

        verifyControls();

    }
}
