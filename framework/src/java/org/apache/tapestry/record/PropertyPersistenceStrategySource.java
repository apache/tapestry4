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
 * A source for {@link org.apache.tapestry.record.PropertyPersistenceStrategy}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface PropertyPersistenceStrategySource
{
    /**
     * Returns the name strategy.
     * 
     * @param name
     *            the name of the strategy to retrieve.
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if no such strategy exists.
     */
    public PropertyPersistenceStrategy getStrategy(String name);

    /**
     * Returns all changes ({@link IPageChange}) collected by any persistence strategy for the
     * page. May return an empty list.
     * 
     * @see PropertyPersistenceStrategy#getStoredChanges(String, IRequestCycle)
     */

    public Collection getAllStoredChanges(String pageName, IRequestCycle cycle);

    /**
     * Discards any stored property changes for the named page.
     */

    public void discardAllStoredChanged(String pageName, IRequestCycle cycle);
}