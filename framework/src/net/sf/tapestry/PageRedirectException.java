package net.sf.tapestry;

/**
 *  Exception thrown by a {@link IComponent component} or {@link IEngineService}
 *  that wishes to force the application to a particular page.  This is often used
 *  to protect a sensitive page until the user is authenticated.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class PageRedirectException extends RequestCycleException
{
    private String targetPageName;

    public PageRedirectException(String targetPageName)
    {
        super();

        this.targetPageName = targetPageName;
    }

    public PageRedirectException(IPage page)
    {
        this(page.getPageName());
    }

    public PageRedirectException(String message, IComponent component, String targetPageName)
    {
        super(message, component);
        this.targetPageName = targetPageName;
    }

    public PageRedirectException(
        String message,
        IComponent component,
        Throwable rootCause,
        String targetPageName)
    {
        super(message, component, rootCause);

        this.targetPageName = targetPageName;
    }

    public PageRedirectException(String message, String targetPageName)
    {
        super(message);

        this.targetPageName = targetPageName;
    }

    public String getTargetPageName()
    {
        return targetPageName;
    }
}