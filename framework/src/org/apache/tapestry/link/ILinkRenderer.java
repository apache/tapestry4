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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.ILinkComponent;

/**
 *  Used by various instances of {@link org.apache.tapestry.components.ILinkComponent} to
 *  actually renderer a link.  Implementations of the interface can manipulate
 *  some of the details of how the link is written.
 * 
 *  <p>
 *  A link rendered may be used in many threads, and must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public interface ILinkRenderer
{
    /**
     *  Renders the link, taking into account whether the link is
     *  {@link org.apache.tapestry.components.ILinkComponent#isDisabled() disabled}.
     *  This is complicated by the fact that the rendering of the body must be done
     *  within a nested writer, since the Link component will not render its tag
     *  until after its body renders (to allow for any wrapped components that need
     *  to write event handlers for the link).
     * 
     *  <p>
     *  The renderer is expected to call back into the link component to handle
     *  any informal parameters, and to handle events output.
     * 
     * 
     **/

    public void renderLink(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent linkComponent);

}
