package net.sf.tapestry;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 *  Additional methods implemented by the engine that are 
 *  exposed to {@link IEngineService engine services}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 */

public interface IEngineServiceView extends IEngine
{
    /**
     *  Invoked by a service to force the page selected by the {@link IRequestCycle}
     *  to be renderred.  This takes care of a number of bookkeeping issues, such
     *  as committing changes in page recorders.
     * 
     **/

    public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException;

    /**
     *  Invoked to restart the application from start; this most frequently follows
     *  some kind of catastrophic failure.  This will invalidate any {@link javax.servlet.http.HttpSession}
     *  and force a redirect to the application servlet (i.e., invoking the home service
     *  in a subsequent request cycle).
     * 
     **/

    public void restart(IRequestCycle cycle) throws IOException;

    /**
     *  Invoked (typically by the reset service) to clear all cached data known
     *  to the engine.  This includes 
     *  pages, templates, helper beans, specifications,
     *  localized strings, etc., and
     *  is used during debugging.
     * 
     **/

    public void clearCachedData();

    /**
     *  Writes a detailed report of the exception to <code>System.err</code>.
     *  This is invoked by services that can't write an HTML description
     *  of the error because they don't provide text/html content (such as
     *  an asset that creates an image).
     *
     *  @since 1.0.10
     */

    public void reportException(String reportTitle, Throwable ex);
}
