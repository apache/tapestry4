package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.callback.ICallback;
import net.sf.tapestry.html.BasePage;

/**
 *  Guard page for the Protected page.  Protected checks the "visited" property of this page and,
 *  if false, redirects to this page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class Guard extends BasePage
{
    private ICallback _callback;
    private boolean _visited;
    
    public void detach()
    {
        _callback = null;
        _visited = false;
        
        super.detach();
    }
    
    public ICallback getCallback()
    {
        return _callback;
    }

    public boolean isVisited()
    {
        return _visited;
    }

    public void setCallback(ICallback callback)
    {
        _callback = callback;
        fireObservedChange("callback", callback);
    }

    public void setVisited(boolean visited)
    {
        _visited = visited;
        fireObservedChange("visited", visited);
    }
    
    public void linkClicked(IRequestCycle cycle)
    throws RequestCycleException
    {
        setVisited(true);
        
        ICallback callback = _callback;
        
        setCallback(null);
        
        callback.performCallback(cycle);
    }

}
