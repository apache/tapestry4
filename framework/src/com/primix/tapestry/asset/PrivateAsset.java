package com.primix.tapestry.asset;

import java.net.URL;
import com.primix.tapestry.*;
import java.io.*;

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
 *  An implementation of {@link IAsset} for localizable assets within
 *  the JVM's classpath.
 *
 *  <p>TBD: Localization.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class PrivateAsset implements IAsset
{
	private AssetExternalizer externalizer;

	private String resourcePath;

	public PrivateAsset(String resourcePath)
	{
		this.resourcePath = resourcePath;
	}

	/**
	*  Gets the localized version of the resource (localized
	*  according to the containing {@link IPage}'s locale).  Build
	*  the URL for the resource.  If possible, the application's
	*  {@link AssetExternalizer} is located, to copy the resource to
	*  a directory visible to the web server.
	*
	*/

	public String buildURL(IRequestCycle cycle)
	{
		String[] parameters;
		String externalURL;
		IApplicationService service;
		String URL;

		if (externalizer == null)
			externalizer = AssetExternalizer.get(cycle);

		try
		{
			externalURL = externalizer.getURL(resourcePath);
		}
		catch (ResourceUnavailableException e)
		{
			throw new ApplicationRuntimeException(
				"Could not build URL for private asset " + resourcePath + ".", e);
		}

		if (externalURL != null)
			return externalURL;

		// Otherwise, the service is responsible for dynamically retrieving the
		// resource.	

		parameters = new String[] { resourcePath };

		service = cycle.getApplication().getService(IApplicationService.ASSET_SERVICE);

		URL = service.buildURL(cycle, null, parameters);

		return cycle.encodeURL(URL);

	}

	public String getLocalizedResourcePath()
	{
		return resourcePath;
	}

	public InputStream getResourceAsStream(IRequestCycle cycle)
	throws ResourceUnavailableException
	{
		URL url;

		try
		{
			url = getClass().getResource(resourcePath);

			return url.openStream();
		}
		catch (Exception e)
		{
			throw new ResourceUnavailableException("Could not access private asset " +
				resourcePath + ".", e);
		}
	}

	public String toString()
	{
		return "PrivateAsset[" + resourcePath + "]";
	}
}

