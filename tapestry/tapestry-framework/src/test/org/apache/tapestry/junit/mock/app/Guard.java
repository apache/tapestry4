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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.html.BasePage;

/**
 * Guard page for the Protected page. Protected checks the "visited" property of this page and, if
 * false, redirects to this page.
 * 
 * @author Howard Lewis Ship
 * @since 2.3
 */

public abstract class Guard extends BasePage
{
    public abstract ICallback getCallback();

    public abstract void setCallback(ICallback callback);

    public abstract void setVisited(boolean visited);

    public abstract boolean isVisited();

    public void linkClicked(IRequestCycle cycle)
    {
        setVisited(true);

        ICallback callback = getCallback();

        setCallback(null);

        callback.performCallback(cycle);
    }

}