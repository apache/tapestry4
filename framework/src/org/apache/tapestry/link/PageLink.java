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

import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;

/**
 *  A component for creating a navigation link to another page, 
 *  using the page service.
 *
 *  [<a href="../../../../../ComponentReference/PageLink.html">Component Reference</a>]
 *
 * @author Howard Ship
 * @version $Id$
 *
 **/

public abstract class PageLink extends AbstractLinkComponent
{
    public ILink getLink(IRequestCycle cycle)
    {
        String parameter = null;
        INamespace namespace = getTargetNamespace();
        String targetPage = getTargetPage();

        if (namespace == null)
            parameter = targetPage;
        else
            parameter = namespace.constructQualifiedName(targetPage);

        return getLink(cycle, Tapestry.PAGE_SERVICE, new String[] { parameter });
    }

    public abstract String getTargetPage();

    /** @since 2.2 **/

    public abstract INamespace getTargetNamespace();
}