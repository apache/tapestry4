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

import java.io.InputStream;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.ResourceUnavailableException;

/**
 *  An asset used with the {@link ChartService}.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 * 
 **/

public class ChartAsset implements IAsset
{
    private IEngineService chartService;
    private IComponent chartProvider;

    public ChartAsset(IRequestCycle cycle, IComponent chartProvider)
    {
        IEngine engine = cycle.getEngine();

        chartService = engine.getService(ChartService.SERVICE_NAME);
        this.chartProvider = chartProvider;
    }

    public String buildURL(IRequestCycle cycle)
    {
        Gesture g = chartService.buildGesture(cycle, chartProvider, null);

        return g.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle) throws ResourceUnavailableException
    {
        return null;
    }

}