package tutorial.portal;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.components.Block;

/**
 *  A Portlet component knows how to render the frame around a portlet block,
 *  as well as manage the controls (close and minimize/maximize).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Portlet extends BaseComponent
{
    private PortletModel _model;

    public IAsset getChangeStateImage()
    {
        return getAsset(_model.isExpanded() ? "minimize" : "maximize");
    }

    public IAsset getChangeStateFocus()
    {
        return getAsset(_model.isExpanded() ? "minimizeFocus" : "maximizeFocus");
    }

    public String getChangeStateLabel()
    {
        return _model.isExpanded() ? "[Minimize]" : "[Maximize]";
    }

    public Block getBodyBlock()
    {
        if (_model.isExpanded())
            return _model.getBodyBlock(getPage().getRequestCycle());

        // If minimized, return null to prevent any display.

        return null;
    }

    public void changeState(IRequestCycle cycle)
    {
        _model.toggleExpanded();        
    }

    public PortletModel getModel()
    {
        return _model;
    }

    public void setModel(PortletModel model)
    {
        _model = model;
    }

}