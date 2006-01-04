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

import javax.ejb.CreateException;

import org.apache.hivemind.lib.NameLookup;
import org.apache.hivemind.lib.RemoteExceptionEvent;
import org.apache.hivemind.lib.RemoteExceptionListener;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.IBookQueryHome;

public class BookQuerySourceImpl implements BookQuerySource, RemoteExceptionListener
{
    private NameLookup _nameLookup;

    private IBookQueryHome _home;

    private RemoteTemplate _remoteTemplate;

    public synchronized IBookQuery newQuery()
    {
        RemoteCallback<IBookQuery> callback = new RemoteCallback()
        {
            public IBookQuery doRemote() throws RemoteException
            {
                try
                {
                    return getHome().create();
                }
                catch (CreateException ex)
                {
                    throw new RemoteException(ex.getMessage(), ex);
                }
            }

        };

        return _remoteTemplate.execute(callback, "Creating new book query.");
    }

    private IBookQueryHome getHome()
    {
        if (_home == null)
            _home = (IBookQueryHome) _nameLookup.lookup("vlib/BookQuery", IBookQueryHome.class);

        return _home;
    }

    public void remoteExceptionDidOccur(RemoteExceptionEvent event)
    {
        _home = null;
    }

    public void setNameLookup(NameLookup nameLookup)
    {
        _nameLookup = nameLookup;
    }

    public void setRemoteTemplate(RemoteTemplate remoteTemplate)
    {
        _remoteTemplate = remoteTemplate;
    }

}
