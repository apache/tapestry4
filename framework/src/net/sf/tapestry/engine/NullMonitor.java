package net.sf.tapestry.engine;

import net.sf.tapestry.IMonitor;

/**
 *  Null implementation of {@link net.sf.tapestry.IMonitor}.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class NullMonitor implements IMonitor
{
    public static final NullMonitor SHARED = new NullMonitor();

    public void pageCreateBegin(String pageName)
    {
    }

    public void pageCreateEnd(String pageName)
    {
    }

    public void pageLoadBegin(String pageName)
    {
    }

    public void pageLoadEnd(String pageName)
    {
    }

    public void pageRenderBegin(String pageName)
    {
    }

    public void pageRenderEnd(String pageName)
    {
    }

    public void pageRewindBegin(String pageName)
    {
    }

    public void pageRewindEnd(String pageName)
    {
    }

    public void serviceBegin(String serviceName, String detailMessage)
    {
    }

    public void serviceEnd(String serviceName)
    {
    }

    public void serviceException(Throwable exception)
    {
    }

    public void sessionBegin()
    {
    }

}
