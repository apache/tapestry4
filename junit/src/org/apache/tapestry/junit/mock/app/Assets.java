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

import java.util.Locale;

import org.apache.tapestry.*;
import org.apache.tapestry.html.BasePage;

/**
 *  Provides functionality to switch the locale to French.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class Assets extends BasePage
{
    public void selectFrench(IRequestCycle cycle)
    {
        IEngine engine = cycle.getEngine();
        
        engine.setLocale(Locale.FRENCH);
        
        // Currently, when you change locale there is no way to
        // reload the current page.
        
        cycle.activate("Home");
    }
}
