package com.primix.tapestry.asset;

import java.net.*;
import javax.servlet.*;
import java.io.*;
import com.primix.tapestry.spec.ApplicationSpecification;
import com.primix.tapestry.*;
import com.primix.tapestry.app.*;

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
 *  Internal asset; one that is visible to the web server directly.
 *
 *  <p>TBD: Localization
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class InternalAsset implements IAsset
{
	private String assetPath;
	private String URL;

	public InternalAsset(String assetPath)
	{
		this.assetPath = assetPath;
	}

	/**
	 *  Generates a URL for the client to retrieve the asset.  The context path
	 *  is prepended to the asset path, which means that assets deployed inside
	 *  web applications will still work (if things are configured properly).
	 *
	 *  <p>A temporary kludge:  This method casts the {@link IApplication}
	 *  (retrieved from the {@link IRequestCycle}) to
	 *  {@link AbstractApplication} and invokes 
	 *  {@link AbstractApplication#getContextPath()}.
	 *
	 */
	 
	public String buildURL(IRequestCycle cycle)
	{
		if (URL == null)
		{
			AbstractApplication application;
			String contextPath;
			
			application = (AbstractApplication)cycle.getApplication();
			
			contextPath = application.getContextPath();
			if (contextPath == null)
				URL = assetPath;
			else
				URL = contextPath + "/" + assetPath;
		}

		return URL;
	}

	public InputStream getResourceAsStream(IRequestCycle cycle)
	throws ResourceUnavailableException
	{
		ServletContext context;
		URL url;

		context = cycle.getRequestContext().getServlet().getServletContext();

		try
		{
			url = context.getResource(assetPath);

			return url.openStream();
		}
		catch (Exception e)
		{
			throw new ResourceUnavailableException("Could not access internal asset " +
				assetPath + ".", e);
		}
	}
	public String toString()
	{
		return "InternalAsset[" + assetPath + "]";
	}
}

