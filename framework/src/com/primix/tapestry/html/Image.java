package com.primix.tapestry.components.html;

import com.primix.tapestry.*;
import com.primix.tapestry.event.*;

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
 *  Used to insert an image.  To create a rollover image, use the
 *  {@link Rollover} class, which integrates a link with the image assets
 *  used with the button.
 *
 * <table border=1>
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
 *    <td>image</td>
 *    <td>{@link IAsset}</td>
 *    <td>R</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The image to show.</td>
 *	</tr>
 *
 *
 * <tr>
 *		<td>border</td>
 *		<td>int</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>0</td>
 *		<td>Corresponds to the HTML <code>border</code> attribute.</td>
 * </tr>
 *
 * </table>
 *
 * <p>Informal parameters are allowed.  A body is not allowed.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Image extends AbstractComponent
{
	private IBinding imageBinding;
	private IBinding borderBinding;
	private boolean staticBorder;
	private int borderValue;

	private final static String[] reservedNames = { "src" };

	/**
	*  Converts an {@link IAsset} binding into a usable URL.  Returns null
	*  if the binding does not exist or the binding's value is null.
	*
	*/

	protected String getAssetURL(String name, IBinding binding, IRequestCycle cycle)
	throws RequestCycleException
	{
		IAsset asset;

		if (binding == null)
			return null;

		try
		{
			asset = (IAsset)binding.getValue();
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException("Binding " + name + " is not of type IAsset.",
				this, cycle, e);
		}

		if (asset == null)
			return null;

		return asset.buildURL(cycle);
	}

	public IBinding getBorderBinding()
	{
		return borderBinding;
	}

	public IBinding getImageBinding()
	{
		return imageBinding;
	}

	/**
	*  Renders the &lt;img&gt; element.
	*
	* <table border=1>
	* <tr> <th>attribute</th>  <th>value</th> </tr>
	* <tr> <td>src</td> <td>from <code>image</code> property</td> </tr>
	* <tr> <td>border</td> <td>from <code>border</td> property, or 0 if not
	*		specified </td> </tr>
	* </table>
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
		String imageURL = null;
		int border = 0;
		IAsset imageAsset;

		// Doesn't contain a body so no need to do anything on rewind (assumes no
		// sideffects to accessor methods via bindings).

		if (cycle.isRewinding())
			return;

		try
		{
			imageAsset = (IAsset)imageBinding.getValue();
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException("Binding image is not an IAsset.",
				this, cycle, e);
		}

		if (imageAsset == null)
			throw new RequiredParameterException(this, "image", imageBinding, cycle);

			imageURL = imageAsset.buildURL(cycle);

		if (staticBorder)
			border = borderValue;
		else if (borderBinding != null)
			border = borderBinding.getInt();

		writer.beginEmpty("img");

		writer.attribute("src", imageURL);

		writer.attribute("border", border);

		generateAttributes(cycle, writer, reservedNames);

		writer.closeTag();

	}

	public void setBorderBinding(IBinding value)
	{
		borderBinding = value;
	}

	public void setImageBinding(IBinding value)
	{
		imageBinding = value;
	}
}

