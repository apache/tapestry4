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
    private int _id;
    private String _title;
    private String _pageName;
    private String _componentPath;

    public PortletChannel(int id, String title, String pageName, String componentPath)
    {
        _id = id;
        _title = title;
        _pageName = pageName;
        _componentPath = componentPath;
    }

    public PortletModel getModel()
    {
        return new PortletModel(_id, _title, _pageName, _componentPath);
    }

    public int getId()
    {
        return _id;
    }

    public String getTitle()
    {
        return _title;
    }
}