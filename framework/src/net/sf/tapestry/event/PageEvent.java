package net.sf.tapestry.event;

import java.util.EventObject;

import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;

/**
 *  Encapsulates information related to the page listener
 *  interfaces.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public class PageEvent extends EventObject
{
    private transient IPage page;
    private transient IRequestCycle requestCycle;

    /**
     *  Constructs a new instance of the event.  The
     *  {@link EventObject#getSource()} of the event will
     *  be the {@link IPage}.
     *
     **/

    public PageEvent(IPage page, IRequestCycle cycle)
    {
        super(page);

        this.page = page;
        this.requestCycle = cycle;
    }

    public IPage getPage()
    {
        return page;
    }

    public IRequestCycle getRequestCycle()
    {
        return requestCycle;
    }
}