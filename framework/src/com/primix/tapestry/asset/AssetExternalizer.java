package com.primix.tapestry.asset;

import java.net.URL;
import com.primix.foundation.StringSplitter;
import com.primix.tapestry.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.primix.tapestry.spec.ApplicationSpecification;

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
 *  Responsible for copying assets from the classpath to an external directory that
 *  is visible to the web server.  Lives inside
 *  the {@link ServletContext} as a named parameter.
 *
 *  <p>The externalizer uses the name <code>com.primix.tapestry.AssetExternalizer.<i>application name</i>
 *  </code>.  It configures itself using two additional 
 *  servlet initial parameters:
 *
 *  <table border=1>
 *  <tr> <th>Parameter</th> <th>Description</th> </tr>
 *  <tr valign=top> 
 *		<td><code>com.primix.tapestry.asset.dir</code> </td>
 * 		 <td>The directory to which assets will be copied.</td> </tr>
 *  <tr valign=top>
 *		<td><code>com.primix.tapestry.asset.URL</code> </td>
 *		  <td>The corresponding URL for the asset directory.</td> </tr>
 *	</table>
 *
 * <p>If either of these parameters is null, then no externalization occurs.
 * Private assets will still be available, just less efficiently, as the application
 * will be invoked via its servlet and, ultimately, the {@link AssetService} will need
 * to retrieve the asset.
 *
 * <p>Assets maintain thier directory structure when copied.  For example,
 * an asset with a resource path of <code>/com/skunkworx/Banner.gif</code> would
 * be copied to the file system as <code><i>dir</i>/com/skunkworx/Banner.gif</code> and
 * would have a URL of <code><i>URL</i>/com/skunkworx/Banner.gif</code>.
 *
 * <p>The externalizer will create any directories as needed.
 *
 * <p>The externalizer will not overwrite existing files.  When a new version of the application
 * is deployed with changed assets, there are two deployment stategies:
 * <ul>
 * <li>Delete the existing asset directory and allow the externalizer to recreate and
 * repopulate it.
 * <li>Change the asset directory and URL, allowing the old and new assets to exist
 *  side-by-side.
 * </ul>
 *
 * <p>When using the second approach, it is best to use a directory that has
 * a version number in it, for example, <code>D:/inetpub/assets/0</code> mapped to the URL
 * <code>/assets/0</code>.  When a new version of the application is deployed, the trailing
 * version number is incremented from 0 to 1.
 */
 
public class AssetExternalizer
{
	private IResourceResolver resolver;
	private File assetDir;
	private String URL;

	private static final int MAP_SIZE = 7;

	/**
	*  A map from resource path (as a String) to final URL (as a String).
	*
	*/

	private Map resources;

	private static final int BUFFER_SIZE = 2048;
	
	protected AssetExternalizer(IRequestCycle cycle)
	{
		HttpServlet servlet;
		String directory;

		resolver = cycle.getApplication().getResourceResolver();
		
		servlet = cycle.getRequestContext().getServlet();

		directory = servlet.getInitParameter("com.primix.tapestry.asset.dir");

		if (directory == null)
			return;

		URL = servlet.getInitParameter("com.primix.tapestry.asset.URL");

		if (URL == null)
			return;

		assetDir = new File(directory);
	}

	protected void externalize(String resourcePath)
	throws IOException
	{
		String[] path;
		int i;
		File file;
		StringSplitter splitter;
		InputStream in;
		OutputStream out;
		int bytesRead;
		URL inputURL;
		byte[] buffer;

		file = assetDir;

		// Resources are always split by the unix seperator, even on Win32.

		splitter = new StringSplitter('/');

		path = splitter.splitToArray(resourcePath);

		// Since the path is expected to start with a leading slash, the first
		// element of path[] will be the empty string and we skip it.

		for (i = 1; i < path.length - 1; i++)
		{
			// Doing it this way makes sure the path seperators are right.

			file = new File(file, path[i]);
		}

		// Make sure the directories exist.

		file.mkdirs();

		file = new File(file, path[path.length - 1]);

		// If the file exists, then assume all is well.

		if (file.exists())
			return;

		// Get the resource and copy it to the file.

		inputURL = resolver.getResource(resourcePath);
		if (inputURL == null)
			throw new IOException("Could not locate resource " + resourcePath + ".");

		in = inputURL.openStream();

		out = new FileOutputStream(file);

		buffer = new byte[BUFFER_SIZE];

		while (true)
		{
			bytesRead = in.read(buffer, 0, BUFFER_SIZE);
			if (bytesRead < 0)
				break;

			out.write(buffer, 0, bytesRead);
		}

		in.close();
		out.close();

		// The file is copied!
	}

	/**
	*  Gets the externalizer for the current application.  If it does not already
	*  exist, it is created and stored into the {@link ServletContext}.
	*
	*  <p>Each Tapestry application within a single {@link ServletContext}
	*  will have its own externalizer; they are differentiated by the
	*  application name.
	*
	*  @see ApplicationSpecification#getName()
	*
	*/

	public static AssetExternalizer get(IRequestCycle cycle)
	{
		ServletContext context;
		String attributeName;
		String applicationName;
		AssetExternalizer result;

		context = cycle.getRequestContext().getServlet().getServletContext();

		applicationName = cycle.getApplication().getSpecification().getName();

		attributeName = "com.primix.tapestry.AssetExternalizer." + applicationName;

		result = (AssetExternalizer)context.getAttribute(attributeName);

		if (result == null)
		{
			result = new AssetExternalizer(cycle);
			context.setAttribute(attributeName, result);
		}

		return result;
	}

	/**
	*  Gets the URL to a private resource.  If the resource was
	*  previously copied out of the classpath, the previously
	*  generated URL is returned.
	* 
	*  <p>If the asset directory and URL are not configured, then
	*  returns null.
	*
	*  <p>Otherwise, the asset is copied out to the asset directory,
	*  the URL is constructed (and recorded for later) and the URL is
	*  returned.
	*
	*  <p>This method is not explicitly synchronized but should work
	*  multi-threaded.  It synchronizes on the internal
	*  <code>Map</code> used to map resource paths to URLs.
	*
	* @param resourcePath The full path of the resource within the
	* classpath.  This is expected to include a leading slash.  For
	* example: <code>/com/skunkworx/Banner.gif</code>.
	*
	*/

	public String getURL(String resourcePath)
	throws ResourceUnavailableException
	{
		String result;

		if (assetDir == null)
			return null;

		if (resources == null)
			resources = new HashMap(MAP_SIZE);

		synchronized (resources)
		{
			result = (String)resources.get(resourcePath);

			if (result != null)
				return result;

			try
			{
				externalize(resourcePath);
			}
			catch (IOException e)
			{
				throw new ResourceUnavailableException(
					"Could not externalize asset " + resourcePath + " to " +
					assetDir + ".", e);
			}

			result = URL + resourcePath;

			resources.put(resourcePath, result);

			return result;
		}
	}
}

