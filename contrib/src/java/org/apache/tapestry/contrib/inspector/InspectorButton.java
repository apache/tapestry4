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

package org.apache.tapestry.contrib.inspector;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;

/**
 * Component that can be placed into application pages that will launch the inspector in a new
 * window. [ <a href="../../../../../../ComponentReference/InspectorButton.html">Component Reference
 * </a>]
 * <p>
 * Because the InspectorButton component is implemented using a
 * {@link org.apache.tapestry.html.Rollover}, the containing page must use a {@link Body} component
 * instead of a &lt;body&gt; tag.
 * 
 * @author Howard Lewis Ship
 */

public abstract class InspectorButton extends BaseComponent
{
    /**
     * Gets the listener for the link component.
     * 
     * @since 1.0.5
     */

    public void trigger(IRequestCycle cycle)
    {
        String name = getNamespace().constructQualifiedName("Inspector");

        Inspector inspector = (Inspector) cycle.getPage(name);

        inspector.inspect(getPage().getPageName(), cycle);
    }

}