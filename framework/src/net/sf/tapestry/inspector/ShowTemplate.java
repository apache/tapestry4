/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.inspector;

import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.parse.ComponentTemplate;
import net.sf.tapestry.parse.TemplateToken;
import net.sf.tapestry.parse.TokenType;

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
            public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
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
        ComponentTemplate template = null;
        String[] context = null;
        IEngineService service = null;
        IComponent inspectedComponent = ((Inspector) getPage()).getInspectedComponent();
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
         char[] data = template.getTemplateData();

        for (int i = 0; i < count; i++)
        {
            TemplateToken token = template.getToken(i);

            if (token.getType() == TokenType.TEXT)
            {
                int start;
                int end;

                start = token.getStartIndex();
                end = token.getEndIndex();

                // Print the section of the template ... print() will
                // escape and invalid characters as HTML entities.  Also,
                // we show the full stretch of text, not the trimmed version.

                writer.print(data, start, end - start + 1);

                continue;
            }

            if (token.getType() == TokenType.CLOSE)
            {
                writer.begin("span");
                writer.attribute("class", "jwc-tag");

                writer.print("</");
                writer.print(token.getTag());
                writer.print(">");

                writer.end(); // <span>

                continue;
            }

            if (token.getType() == TokenType.LOCALIZATION)
            {
                writer.begin("span");
                writer.attribute("class", "jwc-tag");

                writer.print("<span key=\"");
                writer.print(token.getId());
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
                
                writer.print(inspectedComponent.getString(token.getId()));
                writer.end(); // <span>
                
                writer.print("</span>");

                writer.end(); // <span>

                continue;
            }

            // Only other type is OPEN

            if (service == null)
            {
                service = cycle.getEngine().getService(IEngineService.DIRECT_SERVICE);
                context = new String[1];
            }

            // Each id references a component embedded in the inspected component.
            // Get that component.

            String id = token.getId();
            IComponent embedded = inspectedComponent.getComponent(id);
            context[0] = embedded.getIdPath();

            // Build a URL to select that component, as if by the captive
            // component itself (it's a Direct).

            Gesture g = service.buildGesture(cycle, this, context);

            writer.begin("span");
            writer.attribute("class", "jwc-tag");

            writer.print("<");
            writer.print(token.getTag());

            if (token.getTag().equalsIgnoreCase("jwc"))
                writer.print(" id=\"");
            else
                writer.print(" jwcid=\"");

            writer.begin("span");
            writer.attribute("class", "jwc-id");

            writer.begin("a");
            writer.attribute("href", g.getURL());
            writer.print(id);

            writer.end(); // <a>
            writer.end(); // <span>
            writer.print('"');

            Map attributes = token.getAttributes();

            if (attributes != null)
            {
                Iterator ii = attributes.entrySet().iterator();

                while (ii.hasNext())
                {
                    Map.Entry e = (Map.Entry) ii.next();
                    writer.print(' ');
                    writer.print(e.getKey().toString());
                    writer.print("=\"");
                    writer.print(e.getValue().toString());
                    writer.print('"');
                }
            }

            // Collapse an open & close down to a single tag.

            if (i + 1 < count && template.getToken(i + 1).getType() == TokenType.CLOSE)
            {
                writer.print('/');
                i++;
            }

            writer.print('>');
            writer.end(); // <span>
        }

        writer.end(); // <pre>
    }

    /**
     *  Invoked when a component id is clicked.
     *
     **/

    public void trigger(IRequestCycle cycle)
    {
        Inspector inspector = (Inspector) getPage();

        Object[] parameters = cycle.getServiceParameters();

        inspector.selectComponent((String)parameters[0]);

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