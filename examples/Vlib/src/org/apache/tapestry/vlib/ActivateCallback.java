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

package org.apache.tapestry.vlib;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;

/**
 *  Callback implementation for pages which implement
 *  the {@link org.apache.tapestry.vlib.IActivate}
 *  interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ActivateCallback implements ICallback
{
    private String _pageName;

    public ActivateCallback(IActivate page)
    {
        this(page.getPageName());
    }

    public ActivateCallback(String pageName)
    {
        _pageName = pageName;
    }

    public void performCallback(IRequestCycle cycle)
    {
        IActivate page = (IActivate) cycle.getPage(_pageName);

		page.validate(cycle);
        page.activate(cycle);
    }
}