package tutorial.portal;

/**
 *  Defines the content that can be presented in a {@link Portlet}; providing a unique id,
 *  title, and the page and component path of the
 *  {@link net.sf.tapestry.components.Block} providing content.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$ 
 *
 **/

public class PortletChannel
{
    private int id;
    private String title;
    private String pageName;
    private String componentPath;

    public PortletChannel(int id, String title, String pageName, String componentPath)
    {
        this.id = id;
        this.title = title;
        this.pageName = pageName;
        this.componentPath = componentPath;
    }

    public PortletModel getModel()
    {
        return new PortletModel(id, title, pageName, componentPath);
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }
}