package net.sf.tapestry.html;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implements a &lt;frame&gt; within a &lt;frameset&gt;.
 * 
 *  [<a href="../../../../../ComponentReference/Frame.html">Component Reference</a>]
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Frame extends AbstractComponent
{
	private String _targetPage;
	    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;
            
        IEngine engine = cycle.getEngine();
        IEngineService pageService = engine.getService(IEngineService.PAGE_SERVICE);
        
        Gesture g = pageService.buildGesture(cycle, this, new String[] { _targetPage });
        
        writer.beginEmpty("frame");
        writer.attribute("src", g.getURL());
        
        generateAttributes(writer, cycle);
            
        writer.closeTag();
    }


    public String getTargetPage()
    {
        return _targetPage;
    }

    public void setTargetPage(String targetPage)
    {
        _targetPage = targetPage;
    }

}
