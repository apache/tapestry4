package net.sf.tapestry.callback;

import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Simple callback for returning to a page.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 *
 **/

public class PageCallback implements ICallback
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -3286806776105690068L;

    private String _pageName;

    public PageCallback(String pageName)
    {
        _pageName = pageName;
    }

    public PageCallback(IPage page)
    {
        this(page.getName());
    }

    public String toString()
    {
        return "PageCallback[" + _pageName + "]";
    }

    /**
     *  Invokes {@link IRequestCycle#setPage(String)} to select the previously
     *  identified page as the response page.
     *
     **/

    public void performCallback(IRequestCycle cycle) throws RequestCycleException
    {
        cycle.setPage(_pageName);
    }
}