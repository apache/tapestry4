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

import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.Person;

/**
 * Used to execute a {@link org.apache.tapestry.vlib.services.RemoteCallback}. In addition,
 * provides a few very common operations.
 * 
 * @author Howard M. Lewis Ship
 */
public interface RemoteTemplate
{
    /**
     * Executes the callback within a retry loop; consumes any RemoteExceptions.
     * 
     * @param callback
     *            the callback to execute
     * @param errorMessage
     *            used when the callback fails
     * @return the return value of the callback
     */
    <T> T execute(RemoteCallback<T> callback, String errorMessage);

    /**
     * Reads a person, which is expected to exist.
     * 
     * @param personId
     *            unique id for the person
     * @return the Person
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the person does not exist, or if there are any other errors
     */
    Person getPerson(Integer personId);

    /**
     * Returns all persons, sorted by last name, then by first name.
     */

    Person[] getPersons();

    /**
     * Reads a book, which must exist.
     * 
     * @param bookId
     *            unique id for the book
     * @return the book
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the book does not exist, or if there are any other errors
     */

    Book getBook(Integer bookId);
}
