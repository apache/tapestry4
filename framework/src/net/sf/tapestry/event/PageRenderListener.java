package net.sf.tapestry.event;

import java.util.EventListener;

/**
 *  An object that listens to page events.  The {@link net.sf.tapestry.IPage page} generates
 *  events before and after rendering a response.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public interface PageRenderListener extends EventListener
{
    /**
     *  Invoked before just before the page renders a response.  This provides
     *  listeners with a last chance to initialize themselves for the render.
     *  This initialization can include modifying peristent page properties.
     *
     *
     **/

    public void pageBeginRender(PageEvent event);

    /**
     *  Invoked after a successful render of the page.
     *  Allows objects to release any resources they needed during the
     *  the render.
     *
     **/

    public void pageEndRender(PageEvent event);
}