package net.sf.tapestry;

import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;

/**
 *  Defines a page which may be referenced externally via a URL using the 
 *  {@link net.sf.tapestry.engine.ExternalService}. External pages may be bookmarked 
 *  via their URL for latter display. See the 
 *  {@link net.sf.tapestry.link.ExternalLink} for details on how to invoke
 *  <tt>IExternalPage</tt>s.
 * 
 *  @see net.sf.tapestry.callback.ExternalCallback
 *  @see net.sf.tapestry.engine.ExternalService
 *
 *  @author Howard Lewis Ship
 *  @author Malcolm Edgar
 *  @version $Id$
 *  @since 2.2
 **/

public interface IExternalPage extends IPage
{
    /**
     *  Initialize the external page with the given array of parameters and
     *  request cycle.
     *  <p>
     *  This method is invoked after {@link IPage#validate(IRequestCycle)}.
     *
     *  @param parameters the array of page parameters
     *  @param the current request cycle
     * 
     **/
    
    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    throws RequestCycleException;
}
