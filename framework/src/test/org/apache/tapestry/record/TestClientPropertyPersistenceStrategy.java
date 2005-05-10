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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.record.ClientPropertyPersistenceStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestClientPropertyPersistenceStrategy extends HiveMindTestCase
{
    public void testInitialize()
    {
        MockControl requestc = newControl(WebRequest.class);
        WebRequest request = (WebRequest) requestc.getMock();

        request.getParameterNames();
        requestc.setReturnValue(Arrays.asList(new Object[]
        { "foo", "state:MyPage" }));

        request.getParameterValue("state:MyPage");
        requestc.setReturnValue("ENCODED");

        MockControl encoderc = newControl(PersistentPropertyDataEncoder.class);
        PersistentPropertyDataEncoder encoder = (PersistentPropertyDataEncoder) encoderc.getMock();

        List changes = Collections.singletonList(new PropertyChangeImpl("foo", "bar", "baz"));

        encoder.decodePageChanges("ENCODED");
        encoderc.setReturnValue(changes);

        replayControls();

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy(encoder);
        strategy.setRequest(request);

        strategy.initializeService();

        assertSame(changes, strategy.getStoredChanges("MyPage", null));

        verifyControls();
    }

    public void testGetChangesUnknownPage()
    {
        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();

        assertTrue(strategy.getStoredChanges("UnknownPage", null).isEmpty());
    }

    public void testStoreAndRetrieve()
    {
        PropertyChange pc = new PropertyChangeImpl("foo", "bar", "baz");

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();

        strategy.store("MyPage", "foo", "bar", "baz");

        assertEquals(Collections.singletonList(pc), strategy.getStoredChanges("MyPage", null));

        strategy.discardStoredChanges("MyPage", null);

        assertEquals(Collections.EMPTY_LIST, strategy.getStoredChanges("MyPage", null));
    }

    public void testAddParametersForPersistentProperties()
    {
        MockControl requestc = newControl(WebRequest.class);
        WebRequest request = (WebRequest) requestc.getMock();

        IRequestCycle cycle = (IRequestCycle) newMock(IRequestCycle.class);
        ServiceEncoding encoding = (ServiceEncoding) newMock(ServiceEncoding.class);

        request.getParameterNames();
        requestc.setReturnValue(Arrays.asList(new Object[]
        { "foo", "state:MyPage" }));

        request.getParameterValue("state:MyPage");
        requestc.setReturnValue("ENCODED");

        encoding.setParameterValue("state:MyPage", "ENCODED");

        replayControls();

        ClientPropertyPersistenceStrategy strategy = new ClientPropertyPersistenceStrategy();
        strategy.setRequest(request);

        strategy.initializeService();

        strategy.addParametersForPersistentProperties(encoding, cycle);

        verifyControls();

    }
}