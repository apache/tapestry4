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

package org.apache.tapestry.contrib.inspector;

import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ITemplateSource;
import org.apache.tapestry.parse.CloseToken;
import org.apache.tapestry.parse.ComponentTemplate;
import org.apache.tapestry.parse.LocalizationToken;
import org.apache.tapestry.parse.OpenToken;
import org.apache.tapestry.parse.TemplateAttribute;
import org.apache.tapestry.parse.TemplateToken;
import org.apache.tapestry.parse.TextToken;
import org.apache.tapestry.parse.TokenType;

/**
 *  Component of the {@link Inspector} page used to display
 *  the ids and types of all embedded components.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowTemplate extends BaseComponent implements IDirect
{

    public boolean getHasTemplate()
    {
        Inspector inspector;

        inspector = (Inspector) getPage();

        // Components that inherit from BaseComponent have templates,
        // others do not.

        return inspector.getInspectedComponent() instanceof BaseComponent;
    }

    public IRender getTemplateDelegate()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                writeTemplate(writer, cycle);
            }
        };
    }

    /**
     *  Writes the HTML template for the component.  When &lt;jwc&gt; tags are
     *  written, the id is made a link (that selects the named component).  We
     *  use some magic to accomplish this, creating links as if we were a
     *  {@link DirectLink} component, and attributing those links
     *  to the captive {@link DirectLink} component embedded here.
     *
     **/

    private void writeTemplate(IMarkupWriter writer, IRequestCycle cycle)
    {
        IComponent inspectedComponent = getInspectedComponent();
        ComponentTemplate template = null;
        ITemplateSource source = getPage().getEngine().getTemplateSource();

        try
        {
            template = source.getTemplate(cycle, inspectedComponent);
        }
        catch (Exception ex)
        {
            return;
        }

        writer.begin("pre");

        int count = template.getTokenCount();

        for (int i = 0; i < count; i++)
        {
            TemplateToken token = template.getToken(i);
            TokenType type = token.getType();

            if (type == TokenType.TEXT)
            {
                write(writer, (TextToken) token);
                continue;
            }

            if (type == TokenType.CLOSE)
            {
                write(writer, (CloseToken) token);

                continue;
            }

            if (token.getType() == TokenType.LOCALIZATION)
            {

                write(writer, (LocalizationToken) token);
                continue;
            }

            if (token.getType() == TokenType.OPEN)
            {
                boolean nextIsClose =
                    (i + 1 < count) && (template.getToken(i + 1).getType() == TokenType.CLOSE);

                write(writer, nextIsClose, (OpenToken) token);

                if (nextIsClose)
                    i++;

                continue;
            }

            // That's all the types known at this time.
        }

        writer.end(); // <pre>        
    }

    /** @since 3.0 **/

    private IComponent getInspectedComponent()
    {
        Inspector page = (Inspector) getPage();

        return page.getInspectedComponent();
    }

    /** @since 3.0 **/

    private void write(IMarkupWriter writer, TextToken token)
    {
        int start = token.getStartIndex();
        int end = token.getEndIndex();

        // Print the section of the template ... print() will
        // escape and invalid characters as HTML entities.  Also,
        // we show the full stretch of text, not the trimmed version.

        writer.print(token.getTemplateData(), start, end - start + 1);
    }

    /** @since 3.0 **/

    private void write(IMarkupWriter writer, CloseToken token)
    {
        writer.begin("span");
        writer.attribute("class", "jwc-tag");

        writer.print("</");
        writer.print(token.getTag());
        writer.print(">");

        writer.end(); // <span>
    }

    /** @since 3.0 **/

    private void write(IMarkupWriter writer, LocalizationToken token)
    {
        IComponent component = getInspectedComponent();

        writer.begin("span");
        writer.attribute("class", "jwc-tag");

        writer.print("<span key=\"");
        writer.print(token.getKey());
        writer.print('"');

        Map attributes = token.getAttributes();
        if (attributes != null && !attributes.isEmpty())
        {
            Iterator it = attributes.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();
                String attributeName = (String) entry.getKey();
                String attributeValue = (String) entry.getValue();

                writer.print(' ');
                writer.print(attributeName);
                writer.print("=\"");
                writer.print(attributeValue);
                writer.print('"');

            }
        }

        writer.print('>');
        writer.begin("span");
        writer.attribute("class", "localized-string");

        writer.print(component.getMessages().getMessage(token.getKey()));
        writer.end(); // <span>

        writer.print("</span>");

        writer.end(); // <span>
    }

    /** @since 3.0 **/

    private void write(IMarkupWriter writer, boolean nextIsClose, OpenToken token)
    {
        IComponent component = getInspectedComponent();
        IEngineService service = getPage().getEngine().getService(Tapestry.DIRECT_SERVICE);
        String[] context = new String[1];

        // Each id references a component embedded in the inspected component.
        // Get that component.

        String id = token.getId();
        IComponent embedded = component.getComponent(id);
        context[0] = embedded.getIdPath();

        // Build a URL to select that component, as if by the captive
        // component itself (it's a Direct).

        ILink link = service.getLink(getPage().getRequestCycle(), this, context);

        writer.begin("span");
        writer.attribute("class", "jwc-tag");

        writer.print("<");
        writer.print(token.getTag());

        writer.print(" jwcid=\"");

        writer.begin("span");
        writer.attribute("class", "jwc-id");

        writer.begin("a");
        writer.attribute("href", link.getURL());
        writer.print(id);

        writer.end(); // <a>
        writer.end(); // <span>
        writer.print('"');

        Map attributes = token.getAttributesMap();

        if (attributes != null)
        {
            Iterator ii = attributes.entrySet().iterator();

            while (ii.hasNext())
            {
                Map.Entry e = (Map.Entry) ii.next();
                
                TemplateAttribute attribute = (TemplateAttribute)e.getValue();               
                
                writer.print(' ');
                writer.print(e.getKey().toString());
                writer.print("=\"");
                
                // TODO: Fix this to output something appropriate for each type
                // of attribute (literal, expression, string).
                
                writer.print(attribute.getValue());
                writer.print('"');
            }
        }

        // Collapse an open & close down to a single tag.

        if (nextIsClose)
            writer.print('/');

        writer.print('>');
        writer.end(); // <span>
    }

    /**
     *  Invoked when a component id is clicked.
     *
     **/

    public void trigger(IRequestCycle cycle)
    {
        Inspector inspector = (Inspector) getPage();

        Object[] parameters = cycle.getServiceParameters();

        inspector.selectComponent((String) parameters[0]);

        IComponent newComponent = inspector.getInspectedComponent();

        // If the component is not a BaseComponent then it won't have
        // a template, so switch to the specification view.

        if (!(newComponent instanceof BaseComponent))
            inspector.setView(View.SPECIFICATION);
    }

    /**
     *  Always returns true.
     * 
     *  @since 2.3
     * 
     **/

    public boolean isStateful()
    {
        return true;
    }
}