package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.IDirect;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.callback.DirectCallback;
import net.sf.tapestry.callback.ICallback;
import net.sf.tapestry.html.BasePage;

/**
 *  Tests the ability to "protect" a link with a Guard page.  Tests
 *  the {@link net.sf.tapestry.callback.DirectCallback} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class ProtectedLink extends BasePage
{
    public void linkClicked(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        
        Guard guard = (Guard)cycle.getPage("Guard");
        
        if (!guard.isVisited())
        {
            ICallback callback = new DirectCallback((IDirect)getComponent("link"), parameters);
            guard.setCallback(callback);
            cycle.setPage(guard);
            return;
        }
        
        ProtectedLinkResult page = (ProtectedLinkResult)cycle.getPage("ProtectedLinkResult");
        page.setParameters(parameters);
        
        cycle.setPage(page);
    }
}
