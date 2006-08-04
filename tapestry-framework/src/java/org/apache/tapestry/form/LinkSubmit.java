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

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;

/**
 * Implements a component that submits its enclosing form via a JavaScript link. [ <a
 * href="../../../../../ComponentReference/LinkSubmit.html">Component Reference </a>]
 * 
 * @author Richard Lewis-Shell
 */

public abstract class LinkSubmit extends AbstractSubmit
{

    /**
     * The name of an {@link org.apache.tapestry.IRequestCycle} attribute in which the current
     * submit link is stored. LinkSubmits do not nest.
     */

    public static final String ATTRIBUTE_NAME = "org.apache.tapestry.form.LinkSubmit";

    /**
     * Checks the submit name ({@link FormConstants#SUBMIT_NAME_PARAMETER}) to see if it matches
     * this LinkSubmit's assigned element name.
     */
    protected boolean isClicked(IRequestCycle cycle, String name)
    {
        String value = cycle.getParameter(FormConstants.SUBMIT_NAME_PARAMETER);

        return name.equals(value);
    }

    public abstract IScript getScript();

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean disabled = isDisabled();

        IForm form = getForm();
        String name = getName();

        if (!disabled)
        {
            PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);

            Map symbols = new HashMap();
            symbols.put("form", form);
            symbols.put("name", name);

            getScript().execute(this, cycle, pageRenderSupport, symbols);

            writer.begin("a");
            writer.attribute("href", (String) symbols.get("href"));

            renderIdAttribute(writer, cycle);

            renderInformalParameters(writer, cycle);
        }

        renderBody(writer, cycle);

        if (!disabled)
            writer.end();

    }

    /**
     * @see org.apache.tapestry.AbstractComponent#prepareForRender(org.apache.tapestry.IRequestCycle)
     */
    protected void prepareForRender(IRequestCycle cycle)
    {
        IComponent outer = (IComponent) cycle.getAttribute(ATTRIBUTE_NAME);

        if (outer != null)
            throw new ApplicationRuntimeException(FormMessages.linkSubmitMayNotNest(this, outer),
                    this, getLocation(), null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#cleanupAfterRender(org.apache.tapestry.IRequestCycle)
     */
    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    /**
     * Links can not take focus, ever.
     */
    protected boolean getCanTakeFocus()
    {
        return false;
    }

    /**
     * Returns true; the LinkSubmit's body should render during a rewind, even if the component is
     * itself disabled.
     */
    protected boolean getRenderBodyOnRewind()
    {
        return true;
    }

}
