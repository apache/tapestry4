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
    private transient IPage page;
    private String pageName;
    private String targetIdPath;
    private String targetActionId;

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

        page = component.getPage();
        pageName = page.getName();
        this.targetActionId = targetActionId;
        this.targetIdPath = targetIdPath;
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

        this.targetActionId = targetActionId;
        this.targetIdPath = targetIdPath;
    }

    public StaleLinkException(String message, IComponent component)
    {
        super(message, component);
    }

    public StaleLinkException(String message, IPage page)
    {

        super(message, null);
        this.page = page;

        if (page != null)
            pageName = page.getName();
    }

    public String getPageName()
    {
        return pageName;
    }

    /**
     *  Returns the page referenced by the service URL, if known, 
     *  or null otherwise.
     *
     **/

    public IPage getPage()
    {
        return page;
    }
}