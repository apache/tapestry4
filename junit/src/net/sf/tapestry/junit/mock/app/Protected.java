package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageRedirectException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.callback.ICallback;
import net.sf.tapestry.callback.PageCallback;
import net.sf.tapestry.html.BasePage;

/**
 *  Part of Mock application.  Works with the {@link Guard} page to ensure
 *  that the Guard page has been visited before displaying.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class Protected extends BasePage
{

    public void validate(IRequestCycle cycle) throws RequestCycleException
    {
        Guard guard = (Guard)cycle.getPage("Guard");
        
        if (!guard.isVisited())
        {
            ICallback callback = new PageCallback(this);
            guard.setCallback(callback);

            throw new PageRedirectException(guard);
        }            
    }

}
