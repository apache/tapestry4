//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.script;

import org.apache.tapestry.ILocation;
import org.apache.tapestry.IRequestCycle;

/**
 * Writes out its child tokens only the first time it executes
 * (with a given tag).  Uses
 * {@link org.apache.tapestry.IRequestCycle#setAttribute(String, Object)}
 * to identify whether a particular block has rendered yet.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */

class UniqueToken extends AbstractToken
{
    public UniqueToken(ILocation location)
    {
        super(location);
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        IRequestCycle cycle = session.getRequestCycle();

        ILocation location = getLocation();
        String tag = "<unique> " + location.toString();

        if (cycle.getAttribute(tag) != null)
            return;

        cycle.setAttribute(tag, Boolean.TRUE);

        writeChildren(buffer, session);
    }

}
