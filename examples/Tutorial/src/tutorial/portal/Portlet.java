/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

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
			model = (PortletModel) modelBinding.getObject("model", PortletModel.class);

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