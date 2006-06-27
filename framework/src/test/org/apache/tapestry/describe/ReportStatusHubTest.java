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

package org.apache.tapestry.describe;

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.describe.ReportStatusHubImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class ReportStatusHubTest extends BaseDescribeTestCase
{
    public RootDescriptionReceiverFactory newReceiverFactory(IMarkupWriter writer,
            RootDescriptionReciever receiver)
    {
        RootDescriptionReceiverFactory factory = newReceiverFactory();

        expect(factory.newRootDescriptionReceiver(writer)).andReturn(receiver);

        return factory;
    }

    protected RootDescriptionReceiverFactory newReceiverFactory()
    {
        return newMock(RootDescriptionReceiverFactory.class);
    }

    public static class ListenerFixture implements ReportStatusListener
    {

        public void reportStatus(ReportStatusEvent event)
        {
            event.title("Listener Invoked");
        }

    }

    public void testAddAndFire()
    {
        IMarkupWriter writer = newWriter();
        RootDescriptionReciever receiver = newRootReceiver();
        RootDescriptionReceiverFactory factory = newReceiverFactory(writer, receiver);

        receiver.title("Listener Invoked");

        receiver.finishUp();

        replay();

        ReportStatusHubImpl hub = new ReportStatusHubImpl();

        hub.setReceiverFactory(factory);

        hub.addReportStatusListener(new ListenerFixture());

        hub.fireReportStatus(writer);

        verify();
    }

    public void testRemove()
    {
        IMarkupWriter writer = newWriter();
        RootDescriptionReceiverFactory factory = newReceiverFactory();

        replay();

        ReportStatusHubImpl hub = new ReportStatusHubImpl();

        hub.setReceiverFactory(factory);

        ListenerFixture listener = new ListenerFixture();
        hub.addReportStatusListener(listener);
        hub.removeReportStatusListener(listener);

        hub.fireReportStatus(writer);

        verify();

    }

    protected RootDescriptionReciever newRootReceiver()
    {
        return newMock(RootDescriptionReciever.class);
    }
}
