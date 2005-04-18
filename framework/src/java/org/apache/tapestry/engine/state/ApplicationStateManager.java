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
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ApplicationStateManager
{
    /**
     * Checks to see if the named object exists.
     * 
     * @param objectName
     *            the name of the application state object
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if no such object is declared
     */
    public boolean exists(String objectName);

    /**
     * Gets the named application state object, creating it if necessary.
     * 
     * @param objectName
     *            the name of the application state object
     * @return the object
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if no such object is declared
     */
    public Object get(String objectName);
    
    /**
     * Stores a new state object, replacing the old one. The
     * new object may be null.
     * 
     * @param objectName the name of the object to store
     * @param stateObject the new object, possibly null
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if no such object is declared
     */
    
    public void store(String objectName, Object stateObject);

    /**
     * Asks each {@link StateObjectManager}to store each object obtained.
     */

    public void flush();
}