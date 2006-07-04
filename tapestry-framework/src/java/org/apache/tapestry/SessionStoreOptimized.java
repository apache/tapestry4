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

package org.apache.tapestry;

/**
 * <em>Optional</em> interface implemented by Application State Objects. This interface allows
 * Tapestry to optimize the storage of the objects into the
 * {@link org.apache.tapestry.web.WebSession session}, only storing the objects when they require
 * storage due to a change in internal state.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface SessionStoreOptimized
{
    /**
     * Queried by the {@link org.apache.tapestry.engine.state.StateObjectManager} to see if the
     * object actually needs to be stored. Objects that implement this interface should store an
     * internal flag. The flag should be set when any change to the object's internal state occurs.
     * The flag should be cleared when the object is stored into the session (typically, by
     * implementing {@link javax.servlet.http.HttpSessionBindingListener}.
     * 
     * @return true if the object needs to be stored back into the session, false if the internal
     *         state of the object is unchanged
     */
    boolean isStoreToSessionNeeded();

}
