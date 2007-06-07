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

import org.apache.commons.lang.StringUtils;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.*;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.spec.IApplicationSpecification;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
    public static final String SHELL_ATTRIBUTE = "org.apache.tapestry.html.Shell";
    
    private static final String GENERATOR_CONTENT = "Tapestry Application Framework, version " + Tapestry.VERSION;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        TapestryUtils.storeUniqueAttribute(cycle, SHELL_ATTRIBUTE, this);
        
        long startTime = System.currentTimeMillis();
        boolean rewinding = cycle.isRewinding();
        boolean dynamic = getBuilder().isDynamic();

        if (!rewinding && !dynamic)
        {
            writeDocType(writer, cycle);

            IPage page = getPage();

            if (!isDisableTapestryMeta()) {

                writer.comment("Application: " + getApplicationSpecification().getName());

                writer.comment("Page: " + page.getPageName());
                writer.comment("Generated: " + new Date());
            }

            writer.begin("html");
            writer.println();
            writer.begin("head");
            writer.println();

            if (!isDisableTapestryMeta())
                writeMetaTag(writer, "name", "generator", GENERATOR_CONTENT);

            if (isDisableCaching())
                writeMetaTag(writer, "http-equiv", "content", "no-cache");

            if (getRenderContentType())
                writeMetaTag(writer, "http-equiv", "Content-Type", writer.getContentType());

            writeRefresh(writer, cycle);

            if (getRenderBaseTag())
                getBaseTagWriter().render(writer, cycle);

            writer.begin("title");

            writer.print(getTitle(), getRaw());
            writer.end(); // title
            writer.println();

            IRender delegate = getDelegate();

            if (delegate != null)
                delegate.render(writer, cycle);

            IRender ajaxDelegate = getAjaxDelegate();

            if (ajaxDelegate != null)
                ajaxDelegate.render(writer, cycle);
            
            IAsset stylesheet = getStylesheet();

            if (stylesheet != null)
                writeStylesheetLink(writer, cycle, stylesheet);

            Iterator i = (Iterator) getValueConverter().coerceValue(getStylesheets(), Iterator.class);

            while (i.hasNext())
            {
                stylesheet = (IAsset) i.next();

                writeStylesheetLink(writer, cycle, stylesheet);
            }
        }

        // Render the body, the actual page content

        IMarkupWriter nested = !dynamic ? writer.getNestedWriter() : writer;

        renderBody(nested, cycle);

        if (!rewinding)
        {
            List relations = getRelations();
            if (relations != null)
                writeRelations(writer, relations); 
            
            StringBuffer additionalContent = getContentBuffer();
            if (additionalContent != null)
                writer.printRaw(additionalContent.toString());
            
            writer.end(); // head
        }
        
        if (!dynamic)
            nested.close();
        
        if (!rewinding && !dynamic)
        {
            writer.end(); // html
            writer.println();

            if (!isDisableTapestryMeta()) {
                
                long endTime = System.currentTimeMillis();

                writer.comment("Render time: ~ " + (endTime - startTime) + " ms");     
            }
        }

    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        super.cleanupAfterRender(cycle);

        cycle.removeAttribute(SHELL_ATTRIBUTE);
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
        writer.attribute("href", stylesheet.buildURL());
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

        ILink link = pageService.getLink(false, pageName);

        StringBuffer buffer = new StringBuffer();
        buffer.append(refresh);
        buffer.append("; URL=");
        buffer.append(StringUtils.replace(link.getAbsoluteURL(), "&amp;", "&"));

        writeMetaTag(writer, "http-equiv", "Refresh", buffer.toString());
    }
    
    private void writeMetaTag(IMarkupWriter writer, String key, String value, String content)
    {
        writer.beginEmpty("meta");
        writer.attribute(key, value);
        writer.attribute("content", content);
        writer.println();
    }
    
    private void writeRelations(IMarkupWriter writer, List relations)
    {
        Iterator i = relations.iterator();
        while (i.hasNext())
        {
            RelationBean relationBean = (RelationBean) i.next();
            if (relationBean != null)
                writeRelation(writer, relationBean);
        }
    }
    
    private void writeRelation(IMarkupWriter writer, RelationBean relationBean)
    {
            writer.beginEmpty("link");
            writeAttributeIfNotNull(writer, "rel", relationBean.getRel());
            writeAttributeIfNotNull(writer, "rev", relationBean.getRev());            
            writeAttributeIfNotNull(writer, "type", relationBean.getType());
            writeAttributeIfNotNull(writer, "media", relationBean.getMedia());
            writeAttributeIfNotNull(writer, "title", relationBean.getTitle());
            writeAttributeIfNotNull(writer, "href", relationBean.getHref());
            writer.println();
    }    
    
    private void writeAttributeIfNotNull(IMarkupWriter writer, String name, String value)
    {
        if (value != null)
            writer.attribute(name, value);
    }
    
    /**
     * Retrieves the {@link Shell} that was stored into the request
     * cycle. This allows components wrapped by the {@link Shell} to
     * locate it and access the services it provides.
     * 
     * @since 4.1.1
     */
    public static Shell get(IRequestCycle cycle)
    {
        return (Shell) cycle.getAttribute(SHELL_ATTRIBUTE);
    }    
    
    /**
     * Adds a relation (stylesheets, favicon, e.t.c.) to the page.
     *
     * @since 4.1.1
     */
    public void addRelation(RelationBean relation)
    {
        List relations = getRelations();
        if (relations == null)
            relations = new ArrayList();
        
        if (!relations.contains(relation))
            relations.add(relation);
        
        setRelations(relations);             
    }

    /**
     * Include additional content in the header of a page.
     * 
     * @param content 
     *
     * @since 4.1.1
     */
    public void includeAdditionalContent(String content)
    {
        if (HiveMind.isBlank(content))
            return;
        
        StringBuffer buffer = getContentBuffer();
        
        if (buffer == null)
            buffer = new StringBuffer();
        
        buffer.append(content);
        
        setContentBuffer(buffer);
    }
    
    public abstract boolean isDisableCaching();
    
    public abstract IRender getAjaxDelegate();
    
    public abstract IRender getDelegate();
    
    public abstract int getRefresh();

    public abstract IAsset getStylesheet();

    public abstract Object getStylesheets();

    public abstract String getTitle();

    public abstract String getDoctype();

    public abstract boolean getRenderContentType();

    public abstract boolean isDisableTapestryMeta();

    public abstract ResponseBuilder getBuilder();

    /** @since 4.0 */
    public abstract ValueConverter getValueConverter();

    /** @since 4.0 */

    public abstract IEngineService getPageService();

    /** @since 4.0 */

    public abstract IApplicationSpecification getApplicationSpecification();

    /** @since 4.0 */

    public abstract IRender getBaseTagWriter();
    
    /** @since 4.0.1 */
    
    public abstract boolean getRenderBaseTag();
    
    /** @since 4.0.3 */
    
    public abstract boolean getRaw();
    
    /** @since 4.1.1 */
    
    public abstract List getRelations();
    
    /** @since 4.1.1 */
    
    public abstract void setRelations(List relations);
    
    /** @since 4.1.1 */
    
    public abstract StringBuffer getContentBuffer();
    
    /** @since 4.1.1 */
    
    public abstract void setContentBuffer(StringBuffer buffer);    

}
