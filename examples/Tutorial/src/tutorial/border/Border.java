package tutorial.border;

import net.sf.tapestry.BaseComponent;

/**
 *  Reusable component to provide a standard navigational border around
 *  a set of related pages.
 * 
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Border extends BaseComponent
{
    private String _pageName;

    public void setPageName(String value)
    {
        _pageName = value;
    }

    public String getPageName()
    {
        return _pageName;
    }

    public boolean getDisablePageLink()
    {
        return _pageName.equals(getPage().getPageName());
    }
}