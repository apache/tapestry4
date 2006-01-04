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

package org.apache.tapestry.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Implements a component that manages an HTML &lt;input type=submit&gt; form element. [ <a
 * href="../../../../../ComponentReference/Submit.html">Component Reference </a>]
 * <p>
 * This component is generally only used when the form has multiple submit buttons, and it is
 * important for the application to know which one was pressed. You may also want to use
 * {@link ImageSubmit}which accomplishes much the same thing, but uses a graphic image instead.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Submit extends AbstractSubmit
{
    protected boolean isClicked(IRequestCycle cycle, String name)
    {
        // How to know which Submit button was actually
        // clicked? When submitted, it produces a request parameter
        // with its name and value (the value serves double duty as both
        // the label on the button, and the parameter value).

        // If the value isn't there, then this button wasn't
        // selected.
        return cycle.getParameter(name) != null;
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        String label = getLabel();

        if (label != null)
            writer.attribute("value", label);

        renderIdAttribute(writer, cycle);

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    /** parameter */
    public abstract String getLabel();

}