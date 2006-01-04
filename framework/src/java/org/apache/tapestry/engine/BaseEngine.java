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

import java.util.Collection;
import java.util.Collections;

/**
 * Concrete implementation of {@link org.apache.tapestry.IEngine} used for ordinary applications.
 * All page state information is maintained in the {@link javax.servlet.http.HttpSession} using
 * instances of {@link org.apache.tapestry.record.SessionPageRecorder}.
 * 
 * @author Howard Lewis Ship
 */

public class BaseEngine extends AbstractEngine
{
    /**
     * Returns an unmodifiable {@link Collection} of the page names for which {@link IPageRecorder}
     * instances exist. Note: Starting in 4.0, this method is deprecated and returns an empty list.
     * 
     * @deprecated
     */

    public Collection getActivePageNames()
    {
        return Collections.EMPTY_LIST;

    }
}