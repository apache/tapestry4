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
    private String pageName;

    public void setPageName(String value)
    {
        pageName = value;
    }

    public String getPageName()
    {
        return pageName;
    }

    public boolean getDisablePageLink()
    {
        return pageName.equals(getPage().getPageName());
    }
}