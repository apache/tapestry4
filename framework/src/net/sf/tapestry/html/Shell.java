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

import javax.servlet.http.HttpServletRequest;
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
 *  [<a href="../../../../../ComponentReference/Shell.html">Component Reference</a>]
 *
 *  <p>Specifically does <em>not</em> provide a &lt;body&gt; tag, that is
 *  usually accomplished using a {@link Body} component.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class Shell extends AbstractComponent
{
    private String _title;
    private IAsset _stylesheet;
    private IRender _delegate;
    private int _refresh;
    private String _DTD = "-//W3C//DTD HTML 4.0 Transitional//EN";

    private static final String generatorContent = "Tapestry Application Framework, version " + Tapestry.VERSION;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        long startTime = 0;

        boolean rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            startTime = System.currentTimeMillis();

            if (!Tapestry.isNull(_DTD))
            {
                writer.printRaw("<!DOCTYPE HTML PUBLIC \"");
                writer.printRaw(_DTD);
                writer.printRaw("\">");
                writer.println();
            }

            IPage page = getPage();

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

            writer.print(_title);
            writer.end(); // title
            writer.println();

            if (_delegate != null)
                _delegate.render(writer, cycle);

            if (_stylesheet != null)
            {
                writer.beginEmpty("link");
                writer.attribute("rel", "stylesheet");
                writer.attribute("type", "text/css");
                writer.attribute("href", _stylesheet.buildURL(cycle));
                writer.println();
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

    private void writeRefresh(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (_refresh <= 0)
            return;

        // Here comes the tricky part ... have to assemble a complete URL
        // for the current page.

        RequestContext context = cycle.getRequestContext();
        IEngineService pageService = cycle.getEngine().getService(IEngineService.PAGE_SERVICE);
        String pageName = getPage().getName();

        Gesture g = pageService.buildGesture(cycle, null, new String[] { pageName });

        StringBuffer buffer = new StringBuffer();
        buffer.append(_refresh);
        buffer.append("; URL=");
        buffer.append(g.getAbsoluteURL());

        // Write out the <meta> tag

        writer.beginEmpty("meta");
        writer.attribute("http-equiv", "Refresh");
        writer.attribute("content", buffer.toString());
    }

    public IRender getDelegate()
    {
        return _delegate;
    }

    public void setDelegate(IRender delegate)
    {
        _delegate = delegate;
    }

    public int getRefresh()
    {
        return _refresh;
    }

    public void setRefresh(int refresh)
    {
        _refresh = refresh;
    }

    public IAsset getStylesheet()
    {
        return _stylesheet;
    }

    public void setStylesheet(IAsset stylesheet)
    {
        _stylesheet = stylesheet;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public String getDTD()
    {
        return _DTD;
    }

    public void setDTD(String DTD)
    {
        _DTD = DTD;
    }

}