package net.sf.tapestry.event;

import java.util.EventListener;

/**
 *  An interface for objects that want to know when the end of the
 *  request cycle occurs, so that any resources that should be limited
 *  to just one request cycle can be released.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 **/

public interface PageDetachListener extends EventListener
{
    /**
     *  Invoked by the page from its {@link net.sf.tapestry.IPage#detach()} method.
     *
     **/

    public void pageDetached(PageEvent event);
}