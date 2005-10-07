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

package org.apache.tapestry.vlib.services;

import java.rmi.RemoteException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.lib.RemoteExceptionCoordinator;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.vlib.services.RemoteTemplateImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RemoteTemplateTest extends HiveMindTestCase
{
    public void testSuccess()
    {
        RemoteTemplate rt = new RemoteTemplateImpl();
        RemoteCallback callback = new RemoteCallback()
        {
            public Object remoteCallback() throws RemoteException
            {
                return "flintstone";
            }
        };

        Object actual = rt.doRemote(callback, "my error message");

        assertEquals("flintstone", actual);
    }

    public void testSuccessAfterRetry() throws Exception
    {
        RemoteCallback callback = (RemoteCallback) newMock(RemoteCallback.class);
        RemoteExceptionCoordinator coordinator = (RemoteExceptionCoordinator) newMock(RemoteExceptionCoordinator.class);

        Throwable t = new RemoteException();

        callback.remoteCallback();
        setThrowable(callback, t);

        coordinator.fireRemoteExceptionDidOccur(callback, t);

        callback.remoteCallback();
        setReturnValue(callback, "rubble");

        replayControls();

        RemoteTemplateImpl rt = new RemoteTemplateImpl();

        rt.setCoordinator(coordinator);

        assertEquals("rubble", rt.doRemote(callback, "my error message"));

        verifyControls();
    }

    public void testFailure() throws Exception
    {
        RemoteCallback callback = (RemoteCallback) newMock(RemoteCallback.class);
        RemoteExceptionCoordinator coordinator = (RemoteExceptionCoordinator) newMock(RemoteExceptionCoordinator.class);

        Throwable t1 = new RemoteException();
        Throwable t2 = new RemoteException();

        callback.remoteCallback();
        setThrowable(callback, t1);

        coordinator.fireRemoteExceptionDidOccur(callback, t1);

        callback.remoteCallback();
        setThrowable(callback, t2);

        coordinator.fireRemoteExceptionDidOccur(callback, t2);

        replayControls();

        RemoteTemplateImpl rt = new RemoteTemplateImpl();

        rt.setCoordinator(coordinator);

        try
        {
            rt.doRemote(callback, "error message");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("error message", ex.getMessage());
            assertSame(t2, ex.getRootCause());
        }

        verifyControls();
    }
}
