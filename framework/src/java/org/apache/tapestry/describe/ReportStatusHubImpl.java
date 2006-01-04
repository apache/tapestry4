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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;

/**
 * Implementation of the tapestry.describe.ReportStatusHub service.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ReportStatusHubImpl implements ReportStatusHub
{
    private List _listeners = new ArrayList();

    private RootDescriptionReceiverFactory _receiverFactory;

    public synchronized void addReportStatusListener(ReportStatusListener listener)
    {
        Defense.notNull(listener, "listener");

        _listeners.add(listener);
    }

    public synchronized void removeReportStatusListener(ReportStatusListener listener)
    {
        Defense.notNull(listener, "listener");

        _listeners.remove(listener);
    }

    public synchronized void fireReportStatus(IMarkupWriter writer)
    {
        if (_listeners.isEmpty())
            return;

        RootDescriptionReciever receiver = _receiverFactory.newRootDescriptionReceiver(writer);

        ReportStatusEvent event = new ReportStatusEvent(this, receiver);

        Iterator i = _listeners.iterator();

        while (i.hasNext())
        {
            ReportStatusListener listener = (ReportStatusListener) i.next();

            listener.reportStatus(event);

            receiver.finishUp();
        }
    }

    public void setReceiverFactory(RootDescriptionReceiverFactory receiverFactory)
    {
        _receiverFactory = receiverFactory;
    }

}
