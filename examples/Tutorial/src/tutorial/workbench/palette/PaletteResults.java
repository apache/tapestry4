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

public class PaletteResults extends BasePage
{
    private List selectedColors;

    public void initialize()
    {
        selectedColors = null;
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