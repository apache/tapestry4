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
 * Implements a component that manages an HTML &lt;input type=button&gt; form element. [ <a
 * href="../../../../../ComponentReference/Button.html">Component Reference </a>]
 * <p>
 * This component is useful for attaching JavaScript onclick event handlers.
 * 
 * @author Howard Lewis Ship
 * @author Paul Geerts
 * @author Malcolm Edgar
 * @author Paul Ferraro
 */
public abstract class Button extends AbstractFormComponent
{
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.begin("button");
        writer.attribute("type", "button");
        writer.attribute("name", getName());

        if (isDisabled())
        {
            writer.attribute("disabled", "disabled");
        }

        renderIdAttribute(writer, cycle);

        renderInformalParameters(writer, cycle);

        String label = getLabel();

        if (label != null)
            writer.print(label);
        else
            renderBody(writer, cycle);

        writer.end();
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // Do nothing
    }

    /**
     * @see org.apache.tapestry.form.IFormComponent#getClientId()
     */
    public String getClientId()
    {
        return null;
    }

    /**
     * @see org.apache.tapestry.form.IFormComponent#getDisplayName()
     */
    public String getDisplayName()
    {
        return null;
    }

    /**
     * @see org.apache.tapestry.form.IFormComponent#isDisabled()
     */
    public boolean isDisabled()
    {
        return false;
    }

    public abstract String getLabel();
}
