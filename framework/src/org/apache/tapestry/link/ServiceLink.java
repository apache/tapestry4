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

package org.apache.tapestry.link;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;

/**
 *  A component for creating a link for an arbitrary {@link org.apache.tapestry.engine.IEngineService
 *  engine service}.  A ServiceLink component can emulate an {@link ActionLink},
 *  {@link PageLink} or {@link DirectLink} component, but is most often used in
 *  conjunction with an application-specific service.  
 *
 *  [<a href="../../../../../ComponentReference/ServiceLink.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class ServiceLink extends AbstractLinkComponent
{
    public ILink getLink(IRequestCycle cycle)
    {
        Object[] parameters = DirectLink.constructServiceParameters(getParameters());

        return getLink(cycle, getService(), parameters);
    }

    public abstract String getService();

    public abstract Object getParameters();
}