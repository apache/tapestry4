package com.primix.tapestry.components;

import com.primix.tapestry.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Component for creating a standard 'shell' for a page, which comprises
 *  the &lt;html&gt; and &lt;head&gt; portions of the page.
 *
 *  <p>Specifically does <em>not</em> provide a &lt;body&gt; tag, that is
 *  usually accomplished using a {@link Body} component.
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *	  <th>Read / Write </th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 *  <tr>
 *      <td>title</td>
 *      <td>{@link String}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Used to provide the window title for the page.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>stylesheet</td>
 *      <td>{@link IAsset}</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>If given, creates a &lt;link rel=stylesheet&gt; element.</td>
 *  </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed, but a body is (and is virtually required).
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class Shell extends AbstractComponent
{
    private IBinding titleBinding;
    private String titleValue;
    private IBinding stylesheetBinding;

    public void setTitleBinding(IBinding value)
    {
        titleBinding = value;
        if (value.isStatic())
            titleValue = value.getString();
    }

    public IBinding getTitleBinding()
    {
        return titleBinding;
    }

    public void setStylesheetBinding(IBinding value)
    {
        stylesheetBinding = value;
    }

    public IBinding getStylesheetBinding()
    {
        return stylesheetBinding;
    }

    public void render(IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException
    {
        long startTime = 0;
        long endTime = 0;
        String title = titleValue;
        IAsset stylesheet = null;
        IPage page;
        boolean rewinding;

        rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            if (stylesheetBinding != null)
                stylesheet = (IAsset)stylesheetBinding.getValue();

            if (title == null)
                title = titleBinding.getString();

            startTime = System.currentTimeMillis();

            writer.printRaw("<!DOCTYPE HTML PUBLIC \"-//W3C/DTD HTML 4.0//EN\" " +
                            "\"http://www.w3c.org/TR/REC-html40/strict.dtd\">");

            page = getPage();

            writer.comment("Application: " + 
                page.getApplication().getSpecification().getName());

            writer.comment("Page: " + page.getName());
            writer.comment("Generated: " + new Date());

            writer.begin("html");
            writer.begin("head");
            writer.begin("title");

            writer.print(title);
            writer.end();  // title

            if (stylesheet != null)
            {
                writer.beginOrphan("link");
                writer.attribute("rel", "stylesheet");
                writer.attribute("type", "text/css");
                writer.attribute("href", stylesheet.buildURL(cycle));
            }

            writer.beginOrphan("meta");
            writer.attribute("name", "generator");
            writer.attribute("content", "Tapestry Web Application Framework");

            // Refresh will go here.

            writer.end();  // end
        }

        // Render the body, the actual page content

        renderWrapped(writer, cycle);

        if (!rewinding)
        {
            writer.end(); // html

            endTime = System.currentTimeMillis();

            writer.comment("Render time: ~ " + (endTime - startTime) + " ms");
        }

    }
}
