//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.html;

import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

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
 *	  <th>Direction</th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 *  <tr>
 *      <td>title</td>
 *      <td>{@link String}</td>
 *      <td>in</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Used to provide the window title for the page.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>stylesheet</td>
 *      <td>{@link IAsset}</td>
 *      <td>in</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>If given, creates a &lt;link rel=stylesheet&gt; element.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>refresh</td>
 *      <td>int</td>
 *      <td>in</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>If provided (and non-zero), then a &lt;meta http-equiv="Refresh"&gt; element is
 *   included in the header.  The refresh interval is the value provided (which is the time to
 *   display the page, in seconds).
 *
 *  <p>The refresh will be the same page (not necessarily the same URL as that which initially
 *   presented the page, since the page will often be initially displayed because of a link
 *   or form submission).
 *
 *   <p>Note that to the &lt;meta&gt; tag, a refresh of zero means refresh immediately.  For this
 *   component, a refresh of zero is the same as unspecified: no automatic refresh.
 *  </td>
 *  </tr>
 *
 *  <tr>
 *		<td>delegate</td>
 *		<td>{@link IRender}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If specified, the delegate is invoked just before the
 * &lt;/head&gt; tag.  This allows the delegate to write additional tags,
 * often meta tags of various types.
 *  </td> </tr>
 *
 *
 * </table>
 *
 *  <p>Informal parameters are not allowed, but a body is (and is virtually required).
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class Shell extends AbstractComponent
{
    private String title;
    private IAsset stylesheet;
    private IRender delegate;
    private int refresh;
    
    private static final String generatorContent =
        "Tapestry Application Framework, version " + Tapestry.VERSION;


    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        long startTime = 0;
  
        boolean rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            startTime = System.currentTimeMillis();

            writer.printRaw(
                "<!DOCTYPE HTML PUBLIC " + "\"-//W3C//DTD HTML 4.0 Transitional//EN\">");
            writer.println();

            page = getPage();

            writer.comment("Application: " + page.getEngine().getSpecification().getName());

            writer.comment("Page: " + page.getName());
            writer.comment("Generated: " + new Date());

            writer.begin("html");
            writer.println();
            writer.begin("head");
            writer.println();

            writer.beginEmpty("meta");
            writer.attribute("name", "generator");
            writer.attribute("content", generatorContent);
            writer.println();

            writer.begin("title");

            writer.print(title);
            writer.end(); // title
            writer.println();

                if (delegate != null)
                    delegate.render(writer, cycle);

            if (stylesheet != null)
            {
                writer.beginEmpty("link");
                writer.attribute("rel", "stylesheet");
                writer.attribute("type", "text/css");
                writer.attribute("href", stylesheet.buildURL(cycle));
                writer.println();
            }

            writeRefresh(writer, cycle);

            writer.end(); // head
        }

        // Render the body, the actual page content

        renderWrapped(writer, cycle);

        if (!rewinding)
        {
            writer.end(); // html
            writer.println();

            long endTime = System.currentTimeMillis();

            writer.comment("Render time: ~ " + (endTime - startTime) + " ms");
        }

    }

    private void writeRefresh(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
         if (refresh <= 0)
            return;

        // Here comes the tricky part ... have to assemble a complete URL
        // for the current page.

        RequestContext context = cycle.getRequestContext();
        IEngineService pageService =
            cycle.getEngine().getService(IEngineService.PAGE_SERVICE);
        String pageName = getPage().getName();

        Gesture g = pageService.buildGesture(cycle, null, new String[] { pageName });

        StringBuffer buffer = new StringBuffer();
        buffer.append(refresh);
        buffer.append("; URL=");
        buffer.append(g.getAbsoluteURL());

        // Write out the <meta> tag

        writer.beginEmpty("meta");
        writer.attribute("http-equiv", "Refresh");
        writer.attribute("content", buffer.toString());
    }
    
    public IRender getDelegate()
    {
        return delegate;
    }

    public void setDelegate(IRender delegate)
    {
        this.delegate = delegate;
    }

    public int getRefresh()
    {
        return refresh;
    }

    public void setRefresh(int refresh)
    {
        this.refresh = refresh;
    }

    public IAsset getStylesheet()
    {
        return stylesheet;
    }

    public void setStylesheet(IAsset stylesheet)
    {
        this.stylesheet = stylesheet;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}