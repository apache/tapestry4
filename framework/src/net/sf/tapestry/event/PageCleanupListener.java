package net.sf.tapestry.event;

import java.util.EventListener;

/**
 *  Listener interface for objects that need to know when the engine
 *  containing the page is discarded.  This is typically relevant
 *  only to components that have persistent page properties that
 *  require some kind of cleanup (typically, because they
 *  are references to EJBs, or something similar).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 **/

public interface PageCleanupListener extends EventListener
{
    /**
     *  Invoked when the page is notified, by the {@link net.sf.tapestry.IEngine}
     *  to cleanup; this occurs when the engine is discarded
     *  because its {@link javax.servlet.http.HttpSession} was
     *  invalidated.  The page is rolled back to its
     *  last state and then invokes this method.
     *
     *  <p>{@link PageEvent#getRequestCycle()} will return null.
     **/

    public void pageCleanup(PageEvent event);
}