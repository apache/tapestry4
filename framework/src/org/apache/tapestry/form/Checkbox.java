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
 *  Implements a component that manages an HTML &lt;input type=checkbox&gt;
 *  form element.
 *
 *  [<a href="../../../../../ComponentReference/Checkbox.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Checkbox extends AbstractFormComponent
{
    /**
     *  Renders the form elements, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *  <p>In traditional HTML, many checkboxes would have the same name but different values.
     *  Under Tapestry, it makes more sense to have different names and a fixed value.
     *  For a checkbox, we only care about whether the name appears as a request parameter.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);
		
        // Used whether rewinding or not.

        String name = form.getElementId(this);

        if (form.isRewinding())
        {
            String value = cycle.getRequestContext().getParameter(name);

            setSelected((value != null));

            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");

        writer.attribute("name", name);

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (isSelected())
            writer.attribute("checked", "checked");

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    public abstract boolean isDisabled();

    /** @since 2.2 **/

    public abstract boolean isSelected();

    /** @since 2.2 **/

    public abstract void setSelected(boolean selected);

}