package tutorial.workbench.palette;

import java.util.List;

import net.sf.tapestry.html.BasePage;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Results extends BasePage
{
    private List selectedColors;

    public void detach()
    {
        selectedColors = null;

        super.detach();
    }

    public void setSelectedColors(List value)
    {
        selectedColors = value;
    }

    public List getSelectedColors()
    {
        return selectedColors;
    }

}