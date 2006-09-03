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

package org.apache.tapestry.contrib.link;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.ILinkRenderer;

/**
 * An implementation of {@link ILinkRenderer} 
 * that only prints out the link's url, i.e. the value of the href 
 * attribute.
 * This renderer can be useful when combining javascript fragments
 * with {@link ILinkComponent}s. 
 * 
 * @author Andreas Andreou
 * @since 4.1.1
 */
public class RawURLLinkRenderer implements ILinkRenderer
{

    /**
     * A singleton for the raw-url linkRendered.
     */

    public static final ILinkRenderer SHARED_INSTANCE = new RawURLLinkRenderer();

    /** 
     * {@inheritDoc}
     */
    public void renderLink(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent linkComponent)
    {
        writer.print(linkComponent.getLink(cycle).getAbsoluteURL(), true);        
    }
}
