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

package org.apache.tapestry.workbench.chart;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
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

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
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

            return;
        }
        catch (Throwable ex)
        {
            engine.reportException("Error creating JPEG stream.", ex);

            return;
        }

        return;
    }

    public String getName()
    {
        return SERVICE_NAME;
    }

}