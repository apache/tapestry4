package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  Used to test {@link net.sf.tapestry.StaleSessionException}.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class Stale extends BasePage
{
    public void noop(IRequestCycle cycle)
    {
        throw new ApplicationRuntimeException("noop listener should not be reachable.");
    }

}
