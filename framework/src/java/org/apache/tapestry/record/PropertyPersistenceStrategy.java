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

package org.apache.tapestry.record;

import java.util.Collection;

import org.apache.tapestry.IRequestCycle;

/**
 * Defines how a persistent property is made persistent. The typical implementation is to store the
 * persistent property into the session as a session attribute.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface PropertyPersistenceStrategy
{
    /**
     * Stores the new value.
     * 
     * @param pageName
     *            the name of the page containing the property
     * @param idPath
     *            the path to the component with the property (may be null)
     * @param propertyName
     *            the name of the property to be persisted
     * @param newValue
     *            the new value (which may be null)
     */

    public void store(String pageName, String idPath, String propertyName, Object newValue);

    /**
     * Returns a collection of {@link org.apache.tapestry.record.IPageChange}s. These represent
     * prior changes previously stored. The order is not significant. Must not return null. Does not
     * have to reflect changes made during the current request (this method is typically invoked as
     * part of rolling back a page to a prior state, before any further changes are possible).
     */

    public Collection getStoredChanges(String pageName, IRequestCycle cycle);

    /**
     * Invoked to discard any stored changes for the specified page.
     */
    public void discardStoredChanges(String pageName, IRequestCycle cycle);
}