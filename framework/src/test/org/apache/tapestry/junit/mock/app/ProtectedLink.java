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

package org.apache.tapestry.junit.mock.app;

import org.apache.tapestry.IDirect;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.DirectCallback;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.html.BasePage;

/**
 * Tests the ability to "protect" a link with a Guard page. Tests the
 * {@link org.apache.tapestry.callback.DirectCallback}class.
 * 
 * @author Howard Lewis Ship
 * @since 2.3
 */

public class ProtectedLink extends BasePage
{
    public void linkClicked(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();

        Guard guard = (Guard) cycle.getPage("Guard");

        if (!guard.isVisited())
        {
            ICallback callback = new DirectCallback((IDirect) getComponent("link"), parameters);
            guard.setCallback(callback);
            cycle.activate(guard);
            return;
        }

        ProtectedLinkResult page = (ProtectedLinkResult) cycle.getPage("ProtectedLinkResult");
        page.setParameters(parameters);

        cycle.activate(page);
    }
}
