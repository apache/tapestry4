package tutorial.portal;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IMarkupWriter;
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
    private IBinding modelBinding;
    private PortletModel model;

    public IBinding getModelBinding()
    {
        return modelBinding;
    }

    public void setModelBinding(IBinding value)
    {
        modelBinding = value;
    }

    public Object getModel()
    {
        return model;
    }

	// Simplify for new scheme
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        try
        {
            model = (PortletModel) modelBinding.getObject("model", PortletModel.class);

            super.renderComponent(writer, cycle);
        }
        finally
        {
            model = null;
        }
    }

    public IAsset getChangeStateImage()
    {
        return getAsset(model.isExpanded() ? "minimize" : "maximize");
    }

    public IAsset getChangeStateFocus()
    {
        return getAsset(model.isExpanded() ? "minimizeFocus" : "maximizeFocus");
    }

    public String getChangeStateLabel()
    {
        return model.isExpanded() ? "[Minimize]" : "[Maximize]";
    }

    public Block getBodyBlock()
    {
        if (model.isExpanded())
            return model.getBodyBlock(getPage().getRequestCycle());

        // If minimized, return null to prevent any display.

        return null;
    }

    private void changeState()
    {
        model.toggleExpanded();
    }

    public IActionListener getChangeStateListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle) throws RequestCycleException
            {
                changeState();
            }
        };
    }
}