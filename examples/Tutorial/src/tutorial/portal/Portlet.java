
package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

/**
 *  A Portlet component knows how to render the frame around a portlet block,
 *  as well as manage the controls (close and minimize/maximize).
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

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
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		try
		{
			model = (PortletModel)modelBinding.getObject("model", PortletModel.class);
			
			super.render(writer, cycle);
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
		return getAsset(model.isExpanded() ? "minimize-focus" : "maximize-focus");
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
			public void actionTriggered(IComponent component, IRequestCycle cycle)
				throws RequestCycleException
			{
				changeState();
			}
		};
	}
}
