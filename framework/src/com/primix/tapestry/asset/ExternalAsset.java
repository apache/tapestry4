package com.primix.tapestry.asset;

import java.net.*;
import java.io.*;
import com.primix.tapestry.*;

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
 *  A reference to an external URL.  {@link ExternalAsset}s are not
 *  localizable.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ExternalAsset implements IAsset
{
	private String URL;

	public ExternalAsset(String URL)
	{
		this.URL = URL;
	}
	
	/**
	*  Simply returns the URL of the external asset.
	*
	*/

	public String buildURL(IRequestCycle cycle)
	{
		return URL;
	}

	public InputStream getResourceAsStream(IRequestCycle cycle)
	throws ResourceUnavailableException
	{
		URL url;

		try
		{
			url = new URL(URL);

			return url.openStream();	
		}
		catch (Exception e)
		{
			// MalrformedURLException or IOException

			throw new ResourceUnavailableException("Could not access external asset " + URL + ".",
				e);
		}

	}

	public String toString()
	{
		return "ExternalAsset[" + URL + "]";
	}
}

