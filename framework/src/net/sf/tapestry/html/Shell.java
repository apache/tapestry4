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
package net.sf.tapestry.html;

import java.util.Date;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
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

            if (!Tapestry.isNull(_DTD))
            {
                writer.printRaw("<!DOCTYPE HTML PUBLIC \"");
                writer.printRaw(_DTD);
                writer.printRaw("\">");
                writer.println();
            }

            IPage page = getPage();

            writer.comment("Application: " + page.getEngine().getSpecification().getName());

            writer.comment("Page: " + page.getPageName());
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

    private void writeRefresh(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (_refresh <= 0)
            return;

        // Here comes the tricky part ... have to assemble a complete URL
        // for the current page.

        IEngineService pageService = cycle.getEngine().getService(Tapestry.PAGE_SERVICE);
        String pageName = getPage().getPageName();

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