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
    private int id;
    private boolean expanded = true;
    private String title;
    private String bodyPage;
    private String bodyIdPath;

    public PortletModel(int id, String title, String bodyPage, String bodyIdPath)
    {
        this.id = id;
        this.title = title;
        this.bodyPage = bodyPage;
        this.bodyIdPath = bodyIdPath;
    }

    public Block getBodyBlock(IRequestCycle cycle)
    {
        IPage page = cycle.getPage(bodyPage);
        IComponent component = page.getNestedComponent(bodyIdPath);

        return (Block) component;
    }

    public void toggleExpanded()
    {
        expanded = !expanded;
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean value)
    {
        expanded = value;
    }

    public String getTitle()
    {
        return title;
    }

    public int getId()
    {
        return id;
    }
}