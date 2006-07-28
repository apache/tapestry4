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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.apache.hivemind.Messages;
import org.apache.tapestry.test.Creator;
import org.testng.annotations.Test;

/**
 * Tests support for several deprecated methods on {@link org.apache.tapestry.AbstractComponent}
 * related to accessing localized messages. This test case may be removed in 4.1, when the
 * corresponding methods are removed.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestComponentMessageAccess extends BaseComponentTestCase
{
    private AbstractComponent newComponent(Messages messages)
    {
        Creator c = new Creator();

        return (AbstractComponent) c.newInstance(AbstractComponent.class, new Object[]
        { "messages", messages });
    }

    @Test
    public void testGetMessage()
    {
        Messages m = newMock(Messages.class);

        expect(m.getMessage("fred")).andReturn("flintstone");

        AbstractComponent ac = newComponent(m);

        replay();

        assertEquals("flintstone", ac.getMessages().getMessage("fred"));

        verify();
    }

    @Test
    public void testFormat()
    {
        Messages m = newMock(Messages.class);

        expect(m.format("fred", "flintstone")).andReturn("Fred Flintstone");

        AbstractComponent ac = newComponent(m);

        replay();

        assertEquals("Fred Flintstone", ac.getMessages().format("fred", "flintstone"));

        verify();

        expect(m.format("fred", "wilma", "dino")).andReturn("flintstone family");

        replay();

        assertEquals("flintstone family", ac.getMessages().format("fred", "wilma", "dino"));

        verify();

        expect(m.format("fred", "wilma", "dino", "pebbles")).andReturn("flintstone family 2");

        replay();

        assertEquals("flintstone family 2", ac.getMessages().format("fred", "wilma", "dino", "pebbles"));

        verify();

        Object[] arguments = new String[]
        { "flinstone" };

        expect(m.format("fred", arguments)).andReturn("flintstone family 3");

        replay();

        assertEquals("flintstone family 3", ac.getMessages().format("fred", arguments));

        verify();

    }
}
