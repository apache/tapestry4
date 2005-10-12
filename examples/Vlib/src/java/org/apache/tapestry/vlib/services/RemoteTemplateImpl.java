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

import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.lib.RemoteExceptionCoordinator;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

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

    private IOperations _operations;

    public <T> T execute(RemoteCallback<T> callback, String errorMessage)
    {
        int attempt = 1;

        while (true)
        {
            try
            {
                return callback.doRemote();
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

    public Person getPerson(final Integer personId)
    {
        RemoteCallback<Person> callback = new RemoteCallback()
        {
            public Person doRemote() throws RemoteException
            {
                try
                {
                    return _operations.getPerson(personId);
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        return execute(callback, "Unable to read Person #" + personId + ".");
    }

    public Person[] getPersons()
    {
        RemoteCallback<Person[]> callback = new RemoteCallback()
        {
            public Person[] doRemote() throws RemoteException
            {
                return _operations.getPersons();
            }
        };

        return execute(callback, "Error reading application users.");
    }

    public Book getBook(final Integer bookId)
    {
        RemoteCallback<Book> callback = new RemoteCallback()
        {
            public Book doRemote() throws RemoteException
            {
                try
                {
                    return _operations.getBook(bookId);
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        return execute(callback, "Unable to read Book #" + bookId + ".");
    }

    public void setCoordinator(RemoteExceptionCoordinator coordinator)
    {
        _coordinator = coordinator;
    }

    public void setOperations(IOperations operations)
    {
        _operations = operations;
    }
}
