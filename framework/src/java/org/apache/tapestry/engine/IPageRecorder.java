// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import org.apache.tapestry.IPage;
import org.apache.tapestry.event.ChangeObserver;

/**
 * Defines an object that can observe changes to properties of a page and its components, store the
 * state of the page between request cycles, and restore a page's state on a subsequent request
 * cycle.
 * <p>
 * Concrete implementations of this can store the changes in memory, as client-side cookies, in a
 * flat file, or in a database.
 * 
 * @author Howard Lewis Ship
 */

public interface IPageRecorder extends ChangeObserver
{
    /**
     * Persists all changes that have been accumulated. If the recorder saves change incrementally,
     * this should ensure that all changes have been persisted.
     * <p>
     * After commiting, a page recorder automatically locks itself.
     */

    public void commit();

    /**
     * Rolls back the page to the currently persisted state.
     * <p>
     * A page recorder can only rollback changes to properties which have changed at some point.
     * This can cause some minor problems, addressed by
     * {@link org.apache.tapestry.event.PageDetachListener#pageDetached(org.apache.tapestry.event.PageEvent)}.
     */

    public void rollback(IPage page);
}