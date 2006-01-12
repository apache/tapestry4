// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.HomeService;

/**
 * Special version of the home service used to reset the visit tab when
 * re-entering the Tapestry application from a static HTML page.
 * 
 * @author Howard Lewis Ship
 * @see Redirect
 */

public class WorkbenchHomeService extends HomeService
{

    public void service(IRequestCycle cycle)
        throws IOException
    {
        Visit visit = (Visit)cycle.getEngine().getVisit();

        if (visit != null) visit.setActiveTabName("Home");

        super.service(cycle);
    }

}
