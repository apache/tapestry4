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

package org.apache.tapestry.contrib.link;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.link.DefaultLinkRenderer;
import org.apache.tapestry.link.ILinkRenderer;

/**
 * A link renderer that ensures that the generated link uses POST instead of GET request and 
 * is therefore no longer limited in size. 
 * <p>
 * Theoretically, browsers should support very long URLs,
 * but in practice they often start behaving strangely if the URLs are more than 256 characters.
 * This renderer uses JavaScript to generate forms containing the requested link parameters and 
 * then "post" them when the link is selected.  
 * As a result, the data is sent to the server using a POST request with a very short URL 
 * and there is no longer a limitation in the size of the parameters.    
 * <p>
 * In short, simply add the following parameter to your <code>DirectLink</code>, 
 * <code>ExternalLink</code>, or other such link components: 
 * <pre>
 * renderer="ognl: @org.apache.tapestry.contrib.link.FormLinkRenderer@RENDERER"
 * </pre>
 * and they will automatically start using POST rather than GET requests. Their parameters
 * will no longer be limited in size.     
 * 
 * @author mb
 * @since 4.0
 */
public class FormLinkRenderer extends DefaultLinkRenderer
{
	/**
	 * 	A public singleton instance of the <code>FormLinkRenderer</code>.
	 *  <p>
	 *  Since the <code>FormLinkRenderer</code> is stateless, this instance
	 *  can serve all links within your application without interference.
	 */
	public final static ILinkRenderer RENDERER = new FormLinkRenderer();

	public void renderLink(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent linkComponent) {
        IMarkupWriter wrappedWriter = null;

        if (cycle.getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("AbstractLinkComponent.no-nesting"),
                linkComponent,
                null,
                null);

        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, linkComponent);

        String actionId = cycle.getNextActionId();
        String formName = "LinkForm" + actionId;
        
		boolean hasBody = getHasBody();

        boolean disabled = linkComponent.isDisabled();

        if (!disabled && !cycle.isRewinding())
        {
            ILink l = linkComponent.getLink(cycle);
            String anchor = linkComponent.getAnchor();

            Body body = Body.get(cycle);

            if (body == null)
    		    throw new ApplicationRuntimeException(
    		        Tapestry.format("must-be-contained-by-body", "FormLinkRenderer"),
    		        this,
    		        null,
    		        null);

            String function = generateFormFunction(formName, l, anchor);
            body.addBodyScript(function);
            
            if (hasBody)
                writer.begin(getElement());
            else
                writer.beginEmpty(getElement());

            writer.attribute(getUrlAttribute(), "javascript: document." + formName + ".submit();");

            beforeBodyRender(writer, cycle, linkComponent);

            // Allow the wrapped components a chance to render.
            // Along the way, they may interact with this component
            // and cause the name variable to get set.

            wrappedWriter = writer.getNestedWriter();
        }
        else
            wrappedWriter = writer;

        if (hasBody)
            linkComponent.renderBody(wrappedWriter, cycle);

        if (!disabled && !cycle.isRewinding())
        {
            afterBodyRender(writer, cycle, linkComponent);

            linkComponent.renderAdditionalAttributes(writer, cycle);

            if (hasBody)
            {
                wrappedWriter.close();

                // Close the <element> tag

                writer.end();
            }
            else
                writer.closeTag();
        }

        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);
		
	}
	
	private String generateFormFunction(String formName, ILink link, String anchor)
	{
		String[] parameterNames = link.getParameterNames();
		
		StringBuffer buf = new StringBuffer();
		buf.append("function prepare" + formName + "() {\n");

		buf.append("  var html = \"\";\n");
		buf.append("  html += \"<div style='position: absolute'>\";\n");
		
		String url = link.getURL(anchor, false);
		buf.append("  html += \"<form name='" + formName + "' method='post' action='" + url + "'>\";\n");

		for (int i = 0; i < parameterNames.length; i++) {
			String parameter = parameterNames[i];
			String[] values = link.getParameterValues(parameter);
			for (int j = 0; j < values.length; j++) {
				String value = values[j];
				buf.append("  html += \"<input type='hidden' name='" + parameter + "' value='" + value + "'/>\";\n");
			}
		}
		buf.append("  html += \"<\" + \"/form>\";\n");
		buf.append("  html += \"<\" + \"/div>\";\n");
		buf.append("  document.write(html);\n");
		buf.append("}\n");
		
		buf.append("prepare" + formName + "();\n\n");

		return buf.toString();
	}
	
}
