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
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

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
            public Object doRemote() throws RemoteException
            {
                return "flintstone";
            }
        };

        Object actual = rt.execute(callback, "my error message");

        assertEquals("flintstone", actual);
    }

    public void testSuccessAfterRetry() throws Exception
    {
        RemoteCallback callback = newCallback();
        RemoteExceptionCoordinator coordinator = newCoordinator();

        Throwable t = new RemoteException();

        trainDoRemote(callback, t);

        coordinator.fireRemoteExceptionDidOccur(callback, t);

        callback.doRemote();
        setReturnValue(callback, "rubble");

        replayControls();

        RemoteTemplateImpl rt = new RemoteTemplateImpl();

        rt.setCoordinator(coordinator);

        assertEquals("rubble", rt.execute(callback, "my error message"));

        verifyControls();
    }

    public void testFailure() throws Exception
    {
        RemoteCallback callback = newCallback();
        RemoteExceptionCoordinator coordinator = newCoordinator();

        Throwable t1 = new RemoteException();
        Throwable t2 = new RemoteException();

        trainDoRemote(callback, t1);

        coordinator.fireRemoteExceptionDidOccur(callback, t1);

        trainDoRemote(callback, t2);

        coordinator.fireRemoteExceptionDidOccur(callback, t2);

        replayControls();

        RemoteTemplateImpl rt = new RemoteTemplateImpl();

        rt.setCoordinator(coordinator);

        try
        {
            rt.execute(callback, "error message");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("error message", ex.getMessage());
            assertSame(t2, ex.getRootCause());
        }

        verifyControls();
    }

    public void testGetPerson() throws Exception
    {
        Integer personId = new Integer(33);

        IOperations operations = newOperations();
        Person person = new Person(new Object[Person.N_COLUMNS]);

        operations.getPerson(personId);
        setReturnValue(operations, person);

        replayControls();

        RemoteTemplateImpl template = new RemoteTemplateImpl();
        template.setOperations(operations);

        assertSame(person, template.getPerson(personId));

        verifyControls();
    }

    public void testGetPersons() throws Exception
    {
        IOperations operations = newOperations();
        Person[] persons = new Person[0];

        operations.getPersons();
        setReturnValue(operations, persons);

        replayControls();

        RemoteTemplateImpl template = new RemoteTemplateImpl();
        template.setOperations(operations);

        assertSame(persons, template.getPersons());

        verifyControls();
    }

    public void testGetBook() throws Exception
    {
        Integer bookId = new Integer(33);

        IOperations operations = newOperations();
        Book book = new Book(new Object[Book.N_COLUMNS]);

        operations.getBook(bookId);
        setReturnValue(operations, book);

        replayControls();

        RemoteTemplateImpl template = new RemoteTemplateImpl();
        template.setOperations(operations);

        assertSame(book, template.getBook(bookId));

        verifyControls();
    }

    private IOperations newOperations()
    {
        return (IOperations) newMock(IOperations.class);
    }

    private void trainDoRemote(RemoteCallback callback, Throwable t) throws RemoteException
    {
        callback.doRemote();
        setThrowable(callback, t);
    }

    private RemoteExceptionCoordinator newCoordinator()
    {
        return (RemoteExceptionCoordinator) newMock(RemoteExceptionCoordinator.class);
    }

    private RemoteCallback newCallback()
    {
        return (RemoteCallback) newMock(RemoteCallback.class);
    }

}
