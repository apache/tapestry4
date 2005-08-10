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

package org.apache.tapestry.html;

import java.util.Date;
import java.util.Iterator;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Component for creating a standard 'shell' for a page, which comprises the &lt;html&gt; and
 * &lt;head&gt; portions of the page. [ <a
 * href="../../../../../ComponentReference/Shell.html">Component Reference </a>]
 * <p>
 * Specifically does <em>not</em> provide a &lt;body&gt; tag, that is usually accomplished using a
 * {@link Body}&nbsp; component.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Shell extends AbstractComponent
{
    private static final String generatorContent = "Tapestry Application Framework, version "
            + Tapestry.VERSION;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        long startTime = 0;

        boolean rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            startTime = System.currentTimeMillis();

            writeDocType(writer, cycle);

            IPage page = getPage();

            writer.comment("Application: " + getApplicationSpecification().getName());

            writer.comment("Page: " + page.getPageName());
            writer.comment("Generated: " + new Date());

            writer.begin("html");
            writer.println();
            writer.begin("head");
            writer.println();

            writeMetaTag(writer, "name", "generator", generatorContent);

            if (getRenderContentType())
                writeMetaTag(writer, "http-equiv", "Content-Type", writer.getContentType());

            getBaseTagWriter().render(writer, cycle);

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

            Iterator i = (Iterator) getValueConverter().coerceValue(
                    getStylesheets(),
                    Iterator.class);

            while (i.hasNext())
            {
                stylesheet = (IAsset) i.next();

                writeStylesheetLink(writer, cycle, stylesheet);
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
        // This is the real code
        String doctype = getDoctype();
        if (HiveMind.isNonBlank(doctype))
        {
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

        IEngineService pageService = getPageService();
        String pageName = getPage().getPageName();

        ILink link = pageService.getLink(cycle, false, pageName);

        StringBuffer buffer = new StringBuffer();
        buffer.append(refresh);
        buffer.append("; URL=");
        buffer.append(link.getAbsoluteURL());

        writeMetaTag(writer, "http-equiv", "Refresh", buffer.toString());
    }

    private void writeMetaTag(IMarkupWriter writer, String key, String value, String content)
    {
        writer.beginEmpty("meta");
        writer.attribute(key, value);
        writer.attribute("content", content);
        writer.println();
    }

    public abstract IRender getDelegate();

    public abstract int getRefresh();

    public abstract IAsset getStylesheet();

    public abstract Object getStylesheets();

    public abstract String getTitle();

    public abstract String getDoctype();

    public abstract boolean getRenderContentType();

    /** @since 4.0 */
    public abstract ValueConverter getValueConverter();

    /** @since 4.0 */

    public abstract IEngineService getPageService();

    /** @since 4.0 */

    public abstract IApplicationSpecification getApplicationSpecification();

    /** @since 4.0 */

    public abstract IRender getBaseTagWriter();
}