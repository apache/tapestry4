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

package org.apache.tapestry.workbench;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.html.BasePage;

/**
 *  PageLink to demonstrate redirect from a listener method.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Redirect extends BasePage
{
    public void redirectInternal(IRequestCycle cycle)
    {
        throw new RedirectException("redirect-target.html");
    }

    public void redirectExternal(IRequestCycle cycle)
    {
        throw new RedirectException("http://jakarta.apache.org/tapestry");
    }
}
