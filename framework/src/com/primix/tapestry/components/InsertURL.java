package com.primix.tapestry.components;

import com.primix.tapestry.event.ChangeObserver;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  Used to insert some the URL from an asset.
 *
 *
  * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th>
 * <th>Read / Write</th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>asset</td> <td>{@link IAsset}</td> <td>R</td>
 *  <td>yes</td> <td>&nbsp;</td>
 *  <td>The asset whose URL is to be inserted.</td> </tr>
 * </table>
 *
 * <p>Informal parameters are not allowed.  The component must not have a body.
 *
 * @author Howard Ship
 * @version $Id$
 */


 
public class InsertURL extends AbstractComponent
{
	private IBinding assetBinding;

	public InsertURL(IPage page, IComponent container, String id, 
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public IBinding getAssetBinding()
	{
		return assetBinding;
	}

	/**
	*  Prints the URL derived from the <b>asset</b> parameter to the writer. Uses
	*  {@link  IResponseWriter#printRaw(String)} to output the URL.
	*
	*  <p>Does nothing when rewinding.
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
		IAsset asset;
		String url;

		if (cycle.isRewinding())
			return;

		try
		{
			asset = (IAsset)assetBinding.getValue();
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException("Parameter asset is not type IAsset.",
				this, cycle, e);
		}

		if (asset == null)
			throw new RequiredParameterException(this, "asset", cycle);

		url = asset.buildURL(cycle);

		writer.printRaw(url);		
	}

	public void setAssetBinding(IBinding value)
	{
		assetBinding = value;
	}
}

