package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */


/**
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.  It's look and feel can be modified
 *  to blend into an application if desired.
 *
 *  <p>Typically, the inspector will be enabled only when debugging; the application
 *  can control this by binding the enabled parameter.  Most applications will include
 *  the ShowInspector component as a portion of the persistent navigational border,
 *  using images that blend into the page (especially when the inspector is disabled).
 *
 *  <p>Because the ShowInspector component is implemented using a {@link Rollover},
 *  the containing page must use a {@link Body} component instead of
 *  a &lt;body&gt; tag.
 *
 *
 * <p><table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>enabled</td>
 *	  <td>boolean</td>
 *	  <td>R</td>
 *	  <td>no</td>
 *	  <td>true</td>
 *	  <td>May be set to false to disable the inspector button.</td>
 *  </tr>
 *
 *  <tr>
 *    <td>image</td>
 *    <td>{@link IAsset}</td>
 *    <td>R</td>
 *   	<td>no</td>
 *		<td>The default inspector image.</td>
 *		<td>The (initial) image to show.</td>
 *	</tr>
 *
 *  <tr>
 *    <td>image</td>
 *    <td>{@link IAsset}</td>
 *    <td>R</td>
 *   	<td>no</td>
 *		<td>The default inspector focus image.</td>
 *		<td>The image to be displayed when the mouse is over the inspector button.</td>
 *	</tr>
*
 * <tr>
 *		<td>disabled</td>
 *		<td>{@link IAsset}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>A blank space.</td>
 *		<td>A normally blank image displayed when the inspector is disabled.</td> </tr>
 * </table>
 *
 *  Informal parameters are not allowed.  May not contain a body.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowInspector extends BaseComponent
{
	private IBinding imageBinding;
	private IBinding focusBinding;
	private IBinding disabledBinding;

	public void setImageBinding(IBinding value)
	{
		imageBinding = value;
	}
	
	public IBinding getImageBinding()
	{
		return imageBinding;
	}
	
	public void setFocusBinding(IBinding value)
	{
		focusBinding = value;
	}
	
	public IBinding getFocusBinding()
	{
		return focusBinding;
	}
	
	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}
	
	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}

	private IAsset getAsset(String name)
	{
		return (IAsset)getAssets().get(name);
	}
	
	public IAsset getImage()
	{
		if (imageBinding != null)
			return (IAsset)imageBinding.getValue();
		
		return getAsset("image");
	}
	
	public IAsset getDisabled()
	{
		if (disabledBinding != null)
			return (IAsset)disabledBinding.getValue();
		
		return getAsset("disabled");	
	}
	
	public IAsset getFocus()
	{
		if (focusBinding != null)
			return (IAsset)focusBinding.getValue();
		
		return getAsset("focus");
	}	
	
	/**
	 *  Returns the window target (used in the target attribute of the &lt;a&gt; tag).
	 *
	 *  <p>We use the creation time of the session as a kind of unique key.
	 *
	 *  @since 0.2.9
	 *
	 */
	 
	public String getWindowTarget()
	{
		return "Tapestry Inspector " +
			getPage().getRequestCycle().getRequestContext().getSession().getCreationTime();
	}

	/**
	 *  Gets the listener for the link component.
	 *
	 */
	 
	public IDirectListener getListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				Inspector inspector;
				
				inspector = (Inspector)cycle.getPage("Inspector");
				
				inspector.inspect(getPage().getName(), cycle);
			}
		};
	}
}
