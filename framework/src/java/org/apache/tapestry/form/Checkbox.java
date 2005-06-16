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
 * Implements a component that manages an HTML &lt;input type=checkbox&gt; form element. [ <a
 * href="../../../../../ComponentReference/Checkbox.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class Checkbox extends AbstractFormComponent
{
    /**
    * @see org.apache.tapestry.form.validator.AbstractRequirableField#renderRequirableFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");

        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (isSelected())
            writer.attribute("checked", "checked");

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }
    
    /**
     * In traditional HTML, many checkboxes would have the same name but different values.
     * Under Tapestry, it makes more sense to have different names and a fixed value.
     * For a checkbox, we only care about whether the name appears as a request parameter.
     * 
     * @see org.apache.tapestry.form.validator.AbstractRequirableField#bind(org.apache.tapestry.IRequestCycle, java.lang.String)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        setSelected(value != null);
    }
    
    /** @since 2.2 */
    public abstract boolean isSelected();

    /** @since 2.2 */
    public abstract void setSelected(boolean selected);
}