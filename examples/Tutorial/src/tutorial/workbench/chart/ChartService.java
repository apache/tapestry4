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

package tutorial.workbench.chart;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
    private static final Logger LOG = LogManager.getLogger(ChartService.class);

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