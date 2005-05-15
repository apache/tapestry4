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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
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
     * The name of an {@link org.apache.tapestry.IRequestCycle}attribute in which the current
     * submit link is stored. LinkSubmits do not nest.
     */

    public static final String ATTRIBUTE_NAME = "org.apache.tapestry.form.LinkSubmit";

    /**
     * The name of an {@link org.apache.tapestry.IRequestCycle}attribute in which the link submit
     * component that generates the javascript function is stored. The function is only required
     * once per page (containing a form with a non-disabled LinkSubmit)
     */
    public static final String ATTRIBUTE_FUNCTION_NAME = "org.apache.tapestry.form.LinkSubmit_function";

	protected boolean isClicked(IRequestCycle cycle, String name)
	{
        // How to know which Submit link was actually
        // clicked? When submitted, it sets its elementId into a hidden field
        String value = cycle.getParameter("_linkSubmit");

        // If the value isn't the elementId of this component, then this link wasn't
        // selected.
        return value != null && value.equals(name);
	}
    
	protected void writeTag(IMarkupWriter writer, IRequestCycle cycle, String name)
	{
		boolean disabled = isDisabled();
		
		IMarkupWriter wrappedWriter;
		
		if (!disabled)
		{
	        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(
	                cycle,
	                this);
	
	        // make sure the submit function is on the page (once)
	        if (cycle.getAttribute(ATTRIBUTE_FUNCTION_NAME) == null)
	        {
	            pageRenderSupport
	                    .addBodyScript("function submitLink(form, elementId) { form._linkSubmit.value = elementId; if (form.onsubmit == null || form.onsubmit()) form.submit(); }");
	            cycle.setAttribute(ATTRIBUTE_FUNCTION_NAME, this);
	        }
	
	        IForm form = getForm(cycle);
	        String formName = form.getName();

            // one hidden field per form:
	        String formHiddenFieldAttributeName = ATTRIBUTE_FUNCTION_NAME + formName;
	        if (cycle.getAttribute(formHiddenFieldAttributeName) == null)
	        {
	            writer.beginEmpty("input");
	            writer.attribute("type", "hidden");
	            writer.attribute("name", "_linkSubmit");
	            cycle.setAttribute(formHiddenFieldAttributeName, this);
	        }
		    
		    writer.begin("a");
		    writer.attribute("href", "javascript:submitLink(document." + formName + ",\"" + name
		            + "\");");
		
		    // Allow the wrapped components a chance to render.
		    // Along the way, they may interact with this component
		    // and cause the name variable to get set.
		    wrappedWriter = writer.getNestedWriter();
		}
		else
			wrappedWriter = writer;

        renderBody(wrappedWriter, cycle);

		if (!disabled)
		{
		    // Generate additional attributes from informal parameters.		
		    renderInformalParameters(writer, cycle);
		
		    // Dump in HTML provided by wrapped components
		    wrappedWriter.close();
		
		    // Close the <a> tag
		    writer.end();
		}

	}
	
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(Tapestry.getMessage("LinkSubmit.may-not-nest"),
                    this, null, null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        try 
		{
        	super.renderComponent(writer, cycle);
		}
        finally
		{
            cycle.removeAttribute(ATTRIBUTE_NAME);        	
		}
    }

}