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

package org.apache.tapestry.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Renders the text and components wrapped by a component.
 *
 *  [<a href="../../../../../ComponentReference/RenderBody.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 * 
 **/

public class RenderBody extends AbstractComponent
{
    /**
     *  Finds this <code>RenderBody</code>'s container, and invokes
     *  {@link IComponent#renderBody(IMarkupWriter, IRequestCycle)}
     *  on it.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IComponent container = getContainer();

        container.renderBody(writer, cycle);
    }
}