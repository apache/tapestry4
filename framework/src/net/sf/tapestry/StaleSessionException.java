package net.sf.tapestry;

/**
 *  Exception thrown by an {@link IEngineService} when it discovers that
 *  the {@link javax.servlet.http.HttpSession}
 *  has timed out (and been replaced by a new, empty
 *  one).
 *
 *  <p>The application should redirect to the stale-session page.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class StaleSessionException extends RequestCycleException
{
    private transient IPage _page;
    private String _pageName;

    public StaleSessionException()
    {
        super();
    }

    public StaleSessionException(String message, IPage page)
    {
        super(message, null, null);
        _page = page;

        if (page != null)
            _pageName = page.getPageName();
    }

    public String getPageName()
    {
        return _pageName;
    }

    /**
     *  Returns the page referenced by the service URL, if known, or null otherwise.
     *
     **/

    public IPage getPage()
    {
        return _page;
    }
}