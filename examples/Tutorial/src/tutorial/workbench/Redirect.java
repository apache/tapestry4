package tutorial.workbench;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RedirectException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;

/**
 *  PageLink to demonstrate redirect from a listener method.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Redirect extends BasePage
{
    public void redirectInternal(IRequestCycle cycle)
    throws RequestCycleException
    {
        throw new RedirectException("redirect-target.html");
    }
    
    public void redirectExternal(IRequestCycle cycle)
    throws RequestCycleException
    {
        throw new RedirectException("http://sf.net/projects/tapestry");
    }
}
