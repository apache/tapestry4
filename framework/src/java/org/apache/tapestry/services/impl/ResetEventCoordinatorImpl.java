// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import java.util.Iterator;

import org.apache.hivemind.util.EventListenerList;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ResetEventCoordinator;

/**
 * Implementation of the <code>tapestry.ResetEventCoordinator</code>
 * service.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ResetEventCoordinatorImpl implements ResetEventCoordinator
{
    private EventListenerList _listeners = new EventListenerList();

    public void addResetEventListener(ResetEventListener l)
    {
        _listeners.addListener(l);
    }

    public void removeResetEventListener(ResetEventListener l)
    {
        _listeners.removeListener(l);
    }

    public void fireResetEvent()
    {
        Iterator i = _listeners.getListeners();

        while (i.hasNext())
        {
            ResetEventListener l = (ResetEventListener) i.next();

            l.resetEventDidOccur();
        }
    }
}
