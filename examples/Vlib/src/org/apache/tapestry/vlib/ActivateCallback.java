package org.apache.tapestry.vlib;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;

/**
 *  Callback implementation for pages which implement
 *  the {@link org.apache.tapestry.vlib.IActivate}
 *  interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ActivateCallback implements ICallback
{
    private String _pageName;

    public ActivateCallback(IActivate page)
    {
        this(page.getPageName());
    }

    public ActivateCallback(String pageName)
    {
        _pageName = pageName;
    }

    public void performCallback(IRequestCycle cycle)
    {
        IActivate page = (IActivate) cycle.getPage(_pageName);

		page.validate(cycle);
        page.activate(cycle);
    }
}