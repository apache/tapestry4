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

package org.apache.tapestry.engine.state;

/**
 * Interface for an object that manages a single application state object. Represents the named
 * intersection of a {@link org.apache.tapestry.engine.state.StateObjectPersistenceManager}for
 * storing the state object between request cycles, and a
 * {@link org.apache.tapestry.engine.state.StateObjectFactory}to create the object in the first
 * place.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface StateObjectManager
{
    /**
     * Returns true if the application state object exists (i.e., a value has been stored).
     */
    public boolean exists();

    /**
     * Gets or creates the application state object.
     */

    public Object get();

    /**
     * Stores (if necessary) the object back into persistent storage. This is not application to all
     * storage scopes.
     * 
     * @param stateObject
     *            the application state object previously obtained from {@link #get()}.
     */

    public void store(Object stateObject);
}