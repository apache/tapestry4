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

package org.apache.tapestry.form;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Implements a component that manages an HTML &lt;input type=button&gt; form element.
 *
 *  [<a href="../../../../../ComponentReference/Button.html">Component Reference</a>]
 *  
 *  <p>This component is useful for attaching JavaScript onclick event handlers.
 *
 *  @author Howard Lewis Ship
 *  @author Paul Geerts
 *  @author Malcolm Edgar
 *  @version $Id$
 **/

public abstract class Button extends AbstractFormComponent
{
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);

        if (rewinding)
        {
            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "button");
        writer.attribute("name", name);

        if (isDisabled())
        {
            writer.attribute("disabled", "disabled");
        }

        String label = getLabel();

        if (label != null)
        {
            writer.attribute("value", label);
        }

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    public abstract String getLabel();

    public abstract boolean isDisabled();
}
