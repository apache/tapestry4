package tutorial.portal;

import java.io.Serializable;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Block;

/**
 *  Models an active portlet within
 *  the application.  Tracks whether the portlet is expanded (maximimized)
 *  or minimized.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PortletModel implements Serializable
{
    private int _id;
    private boolean _expanded = true;
    private String _title;
    private String _bodyPage;
    private String _bodyIdPath;

    public PortletModel(int id, String title, String bodyPage, String bodyIdPath)
    {
        _id = id;
        _title = title;
        _bodyPage = bodyPage;
        _bodyIdPath = bodyIdPath;
    }

    public Block getBodyBlock(IRequestCycle cycle)
    {
        IPage page = cycle.getPage(_bodyPage);
        IComponent component = page.getNestedComponent(_bodyIdPath);

        return (Block) component;
    }

    public void toggleExpanded()
    {
        _expanded = !_expanded;
    }

    public boolean isExpanded()
    {
        return _expanded;
    }

    public void setExpanded(boolean expanded)
    {
        _expanded = expanded;
    }

    public String getTitle()
    {
        return _title;
    }

    public int getId()
    {
        return _id;
    }
}