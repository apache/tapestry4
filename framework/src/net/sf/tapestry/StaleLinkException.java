package net.sf.tapestry;

/**
 *  Exception thrown by an {@link IEngineService} when it discovers that
 *  the an action link was for an out-of-date version of the page.
 *
 *  <p>The application should redirect to the StaleLink page.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class StaleLinkException extends RequestCycleException
{
    private transient IPage _page;
    private String _pageName;
    private String _targetIdPath;
    private String _targetActionId;

    public StaleLinkException()
    {
        super();
    }

    /**
     *  Constructor used when the action id is found, but the target id path
     *  did not match the actual id path.
     *
     **/

    public StaleLinkException(IComponent component, String targetActionId, String targetIdPath)
    {
        super(
            Tapestry.getString(
                "StaleLinkException.action-mismatch",
                new String[] { targetActionId, component.getIdPath(), targetIdPath }),
            component);

        _page = component.getPage();
        _pageName = _page.getPageName();
        
        _targetActionId = targetActionId;
        _targetIdPath = targetIdPath;
    }

    /**
     *  Constructor used when the target action id is not found.
     *
     **/

    public StaleLinkException(IPage page, String targetActionId, String targetIdPath)
    {
        this(
            Tapestry.getString("StaleLinkException.component-mismatch", targetActionId, targetIdPath),
            page);

        _targetActionId = targetActionId;
        _targetIdPath = targetIdPath;
    }

    public StaleLinkException(String message, IComponent component)
    {
        super(message, component);
    }

    public StaleLinkException(String message, IPage page)
    {

        super(message, null);
        _page = page;

        if (page != null)
            _pageName = page.getPageName();
    }

    public String getPageName()
    {
        return _pageName;
    }

    /**
     *  Returns the page referenced by the service URL, if known, 
     *  or null otherwise.
     *
     **/

    public IPage getPage()
    {
        return _page;
    }
}