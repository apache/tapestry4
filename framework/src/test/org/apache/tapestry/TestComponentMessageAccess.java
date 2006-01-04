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

package org.apache.tapestry;

import org.apache.hivemind.Messages;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * Tests support for several deprecated methods on {@link org.apache.tapestry.AbstractComponent}
 * related to accessing localized messages. This test case may be removed in 4.1, when the
 * corresponding methods are removed.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestComponentMessageAccess extends HiveMindTestCase
{
    private AbstractComponent newComponent(Messages messages)
    {
        Creator c = new Creator();

        return (AbstractComponent) c.newInstance(AbstractComponent.class, new Object[]
        { "messages", messages });
    }

    public void testGetMessage()
    {
        MockControl control = newControl(Messages.class);
        Messages m = (Messages) control.getMock();

        m.getMessage("fred");
        control.setReturnValue("flintstone");

        AbstractComponent ac = newComponent(m);

        replayControls();

        assertEquals("flintstone", ac.getMessage("fred"));

        verifyControls();
    }

    public void testFormat()
    {
        MockControl control = newControl(Messages.class);
        Messages m = (Messages) control.getMock();

        m.format("fred", "flintstone");
        control.setReturnValue("Fred Flintstone");

        AbstractComponent ac = newComponent(m);

        replayControls();

        assertEquals("Fred Flintstone", ac.format("fred", "flintstone"));

        verifyControls();

        m.format("fred", "wilma", "dino");
        control.setReturnValue("flintstone family");

        replayControls();

        assertEquals("flintstone family", ac.format("fred", "wilma", "dino"));

        verifyControls();

        m.format("fred", "wilma", "dino", "pebbles");
        control.setReturnValue("flintstone family 2");

        replayControls();

        assertEquals("flintstone family 2", ac.format("fred", "wilma", "dino", "pebbles"));

        verifyControls();

        Object[] arguments = new String[]
        { "flinstone" };

        m.format("fred", arguments);
        control.setReturnValue("flintstone family 3");

        replayControls();

        assertEquals("flintstone family 3", ac.format("fred", arguments));

        verifyControls();

    }
}
