package tutorial.workbench.chart;

import java.io.InputStream;
import java.util.Locale;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;

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
    private IEngineService _chartService;
    private IComponent _chartProvider;

    public ChartAsset(IRequestCycle cycle, IComponent chartProvider)
    {
        IEngine engine = cycle.getEngine();

        _chartService = engine.getService(ChartService.SERVICE_NAME);
        _chartProvider = chartProvider;
    }

    public String buildURL(IRequestCycle cycle)
    {
        Gesture g = _chartService.buildGesture(cycle, _chartProvider, null);

        return g.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle) 
    {
        return null;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale)
    {
        return null;
    }

}