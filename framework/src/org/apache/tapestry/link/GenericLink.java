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
 *  An implementation of {@link org.apache.tapestry.components.ILinkComponent} 
 *  that allows
 *  the exact HREF to be specified, usually used for client side
 *  scripting.  
 * 
 *  [<a href="../../../../../ComponentReference/GenericLink.html">Component Reference</a>]
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.2
 * 
 **/

public abstract class GenericLink extends AbstractLinkComponent
{
    public abstract String getHref();

    public ILink getLink(IRequestCycle cycle)
    {
        return new StaticLink(getHref());
    }
}