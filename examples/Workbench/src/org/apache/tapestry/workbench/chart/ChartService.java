/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.workbench.chart;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.engine.AbstractService;
import org.apache.tapestry.engine.IEngineServiceView;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;
import org.jCharts.Chart;
import org.jCharts.encoders.JPEGEncoder13;

/**
 *  ServiceLink that works with a {@link Chart} to dynamically render
 *  a chart as a JPEG.  This is a very limited implementation; a full version
 *  would include features such as setting the size of the image, and more flexibility
 *  in defining where the {@link Chart} instance is obtained from.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 * 
 **/

public class ChartService extends AbstractService
{
    private static final Log LOG = LogFactory.getLog(ChartService.class);

    public static final String SERVICE_NAME = "chart";

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        String[] context;
        String pageName = component.getPage().getPageName();
        String idPath = component.getIdPath();

        if (idPath != null)
        {
            context = new String[2];
            context[1] = idPath;
        }
        else
            context = new String[1];

        context[0] = pageName;

        return constructLink(cycle, SERVICE_NAME, context, null, true);
    }

    public boolean service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        String context[] = getServiceContext(cycle.getRequestContext());

        String pageName = context[0];
        String idPath = (context.length == 1) ? null : context[1];

        IPage page = cycle.getPage(pageName);
        IComponent component = (idPath == null) ? page : page.getNestedComponent(idPath);

        try
        {
            IChartProvider provider = (IChartProvider) component;

            Chart chart = provider.getChart();

            output.setContentType("image/jpeg");

            // I've seen a few bits of wierdness (including a JVM crash) inside this code.
            // Hopefully, its a multi-threading problem that can be resolved
            // by synchronizing.

            synchronized (this)
            {
                JPEGEncoder13.encode(chart, 1.0f, output);
            }
        }
        catch (ClassCastException ex)
        {
            engine.reportException(
                "Component " + component.getExtendedId() + " does not implement IChartProvider.",
                ex);

            return false;
        }
        catch (Throwable ex)
        {
            engine.reportException("Error creating JPEG stream.", ex);

            return false;
        }

        return false;
    }

    public String getName()
    {
        return SERVICE_NAME;
    }

}