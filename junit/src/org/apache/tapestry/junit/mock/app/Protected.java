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

package org.apache.tapestry.junit.mock.app;

import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

/**
 *  Part of Mock application.  Works with the {@link Guard} page to ensure
 *  that the Guard page has been visited before displaying.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class Protected extends BasePage implements PageValidateListener
{

    public void pageValidate(PageEvent event)
    {
        Guard guard = (Guard) getRequestCycle().getPage("Guard");

        if (!guard.isVisited())
        {
            ICallback callback = new PageCallback(this);
            guard.setCallback(callback);

            throw new PageRedirectException(guard);
        }
    }

}
