// Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.RequestExceptionReporter;
import org.apache.tapestry.util.ComponentAddress;
import org.jCharts.Chart;
import org.jCharts.encoders.JPEGEncoder13;

/**
 * ServiceLink that works with a {@link Chart}to dynamically render a chart as a JPEG. This is a
 * very limited implementation; a full version would include features such as setting the size of
 * the image, and more flexibility in defining where the {@link Chart}instance is obtained from.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.10
 */

public class ChartService implements IEngineService
{
    public static final String SERVICE_NAME = "chart";

    /** @since 3.1 */
    private RequestExceptionReporter _exceptionReporter;

    /** @since 3.1 */
    private LinkFactory _linkFactory;

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        Defense.isAssignable(parameter, IComponent.class, "parameter");

        IComponent component = (IComponent) parameter;

        Object[] serviceParameters = new Object[]
        { new ComponentAddress(component) };

        return _linkFactory.constructLink(cycle, SERVICE_NAME, null, serviceParameters, true);
    }

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws ServletException,
            IOException
    {
        Object[] serviceParameters = _linkFactory.extractServiceParameters(cycle);

        ComponentAddress address = (ComponentAddress) serviceParameters[0];

        IComponent component = address.findComponent(cycle);

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
            _exceptionReporter.reportRequestException("Component " + component.getExtendedId()
                    + " does not implement IChartProvider.", ex);

            return;
        }
        catch (Throwable ex)
        {
            _exceptionReporter.reportRequestException("Error creating JPEG stream.", ex);

            return;
        }

        return;
    }

    public String getName()
    {
        return SERVICE_NAME;
    }

    /** @since 3.1 */
    public void setExceptionReporter(RequestExceptionReporter exceptionReporter)
    {
        _exceptionReporter = exceptionReporter;
    }

    /** @since 3.1 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }
}