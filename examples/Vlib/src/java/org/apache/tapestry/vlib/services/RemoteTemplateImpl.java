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

/**
 * Handles remote exceptions and retries.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RemoteTemplateImpl implements RemoteTemplate
{
    private final static int MAX_ATTEMPTS = 2;

    private RemoteExceptionCoordinator _coordinator;

    public Object doRemote(RemoteCallback callback, String errorMessage)
    {
        int attempt = 1;

        while (true)
        {
            try
            {
                return callback.remoteCallback();
            }
            catch (RemoteException ex)
            {
                _coordinator.fireRemoteExceptionDidOccur(callback, ex);

                if (attempt++ < MAX_ATTEMPTS)
                    continue;

                throw new ApplicationRuntimeException(errorMessage, ex);
            }

        }
    }

    public void setCoordinator(RemoteExceptionCoordinator coordinator)
    {
        _coordinator = coordinator;
    }
}
