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
package tutorial.workbench.chart;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jrefinery.chart.ChartUtilities;
import com.jrefinery.chart.JFreeChart;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.engine.AbstractService;

/**
 *  ServiceLink that works with a {@link JFreeChart} to dynamically render
 *  a chart as a JPEG.  This is a very limited implementation; a full version
 *  would include features such as setting the size of the image, and more flexibility
 *  in defining where the {@link JFreeChart} instance is obtained from.
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

    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        String[] context;
        String pageName = component.getPage().getName();
        String idPath = component.getIdPath();

        if (idPath != null)
        {
            context = new String[2];
            context[1] = idPath;
        }
        else
            context = new String[1];

        context[0] = pageName;

        return assembleGesture(cycle, SERVICE_NAME, context, null, true);
    }

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
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

            JFreeChart chart = provider.getChart();

            output.setContentType("image/jpeg");

            // I've seen a few bits of wierdness (including a JVM crash) inside this code.
            // Hopefully, its a multi-threading problem that can be resolved
            // by synchronizing.

            synchronized (this)
            {
                ChartUtilities.writeChartAsJPEG(output, chart, 400, 350);
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