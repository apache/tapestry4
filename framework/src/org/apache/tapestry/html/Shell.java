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

package org.apache.tapestry.html;

import java.util.Date;
import java.util.Iterator;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 *  Component for creating a standard 'shell' for a page, which comprises
 *  the &lt;html&gt; and &lt;head&gt; portions of the page.
 * 
 *  [<a href="../../../../../ComponentReference/Shell.html">Component Reference</a>]
 *
 *  <p>Specifically does <em>not</em> provide a &lt;body&gt; tag, that is
 *  usually accomplished using a {@link Body} component.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public abstract class Shell extends AbstractComponent
{

    private static final String generatorContent =
        "Tapestry Application Framework, version " + Tapestry.VERSION;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        long startTime = 0;

        boolean rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            startTime = System.currentTimeMillis();

            writeDocType(writer, cycle);

            IPage page = getPage();

            writer.comment("Application: " + page.getEngine().getSpecification().getName());

            writer.comment("Page: " + page.getPageName());
            writer.comment("Generated: " + new Date());

            writer.begin("html");
            renderInformalParameters(writer, cycle);
            writer.println();
            writer.begin("head");
            writer.println();

            writer.beginEmpty("meta");
            writer.attribute("name", "generator");
            writer.attribute("content", generatorContent);
            writer.println();

            if (getRenderContentType()) {
                // This should not be necessary (the HTTP content type should be sufficient), 
                // but some browsers require it for some reason
                writer.beginEmpty("meta");
                writer.attribute("http-equiv", "Content-Type");
                writer.attribute("content", writer.getContentType());
                writer.println();
            }

            writer.begin("title");

            writer.print(getTitle());
            writer.end(); // title
            writer.println();

            IRender delegate = getDelegate();

            if (delegate != null)
                delegate.render(writer, cycle);

            IAsset stylesheet = getStylesheet();

            if (stylesheet != null)
                writeStylesheetLink(writer, cycle, stylesheet);

            Iterator i = Tapestry.coerceToIterator(getStylesheets());

            if (i != null)
            {
                while (i.hasNext())
                {
                    stylesheet = (IAsset) i.next();

                    writeStylesheetLink(writer, cycle, stylesheet);
                }
            }

            writeRefresh(writer, cycle);

            writer.end(); // head
        }

        // Render the body, the actual page content

        renderBody(writer, cycle);

        if (!rewinding)
        {
            writer.end(); // html
            writer.println();

            long endTime = System.currentTimeMillis();

            writer.comment("Render time: ~ " + (endTime - startTime) + " ms");
        }

    }

    private void writeDocType(IMarkupWriter writer, IRequestCycle cycle)
    {
        // This code is deprecated and is here only for backward compatibility
        String DTD = getDTD();
        if (Tapestry.isNonBlank(DTD)) {
            writer.printRaw("<!DOCTYPE HTML PUBLIC \"" + DTD + "\">");
            writer.println();
            return;
        }

        // This is the real code
        String doctype = getDoctype();
        if (Tapestry.isNonBlank(doctype)) {
            writer.printRaw("<!DOCTYPE " + doctype + ">");
            writer.println();
        }
    }

    private void writeStylesheetLink(IMarkupWriter writer, IRequestCycle cycle, IAsset stylesheet)
    {
        writer.beginEmpty("link");
        writer.attribute("rel", "stylesheet");
        writer.attribute("type", "text/css");
        writer.attribute("href", stylesheet.buildURL(cycle));
        writer.println();
    }

    private void writeRefresh(IMarkupWriter writer, IRequestCycle cycle)
    {
        int refresh = getRefresh();

        if (refresh <= 0)
            return;

        // Here comes the tricky part ... have to assemble a complete URL
        // for the current page.

        IEngineService pageService = cycle.getEngine().getService(Tapestry.PAGE_SERVICE);
        String pageName = getPage().getPageName();

        ILink link = pageService.getLink(cycle, null, new String[] { pageName });

        StringBuffer buffer = new StringBuffer();
        buffer.append(refresh);
        buffer.append("; URL=");
        buffer.append(link.getAbsoluteURL());

        // Write out the <meta> tag

        writer.beginEmpty("meta");
        writer.attribute("http-equiv", "Refresh");
        writer.attribute("content", buffer.toString());
    }

    public abstract IRender getDelegate();

    public abstract int getRefresh();

    public abstract IAsset getStylesheet();

    public abstract String getTitle();

    public abstract String getDoctype();

    public abstract String getDTD();

    public abstract Object getStylesheets();

    public abstract boolean getRenderContentType();
    
}