/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
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

package com.primix.tapestry.asset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Category;

import com.primix.tapestry.IRequestCycle;
import com.primix.tapestry.IResourceResolver;
import com.primix.tapestry.ResourceUnavailableException;
import com.primix.tapestry.Tapestry;
import com.primix.tapestry.util.StringSplitter;

/**
 *  Responsible for copying assets from the classpath to an external directory that
 *  is visible to the web server. The externalizer is stored inside
 *  the {@link ServletContext} as a named attribute.
 *
 *  <p>The externalizer uses the name <code>com.primix.tapestry.AssetExternalizer.<i>application name</i>
 *  </code>.  It configures itself using two additional 
 *  context initial parameters:
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
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class AssetExternalizer
{
	private static final Category CAT = Category.getInstance(AssetExternalizer.class);

	private IResourceResolver resolver;
	private File assetDir;
	private String URL;

	/**
	*  A map from resource path (as a String) to final URL (as a String).
	*
	**/

	private Map resources = new HashMap();

	private static final int BUFFER_SIZE = 2048;

	protected AssetExternalizer(IRequestCycle cycle)
	{
		resolver = cycle.getEngine().getResourceResolver();

		String directory = System.getProperty("com.primix.tapestry.asset.dir");

		if (directory == null)
			return;

		URL = System.getProperty("com.primix.tapestry.asset.URL");

		if (URL == null)
			return;

		assetDir = new File(directory);

		CAT.debug("Initialized with directory " + assetDir + " mapped to " + URL);
	}

	protected void externalize(String resourcePath) throws IOException
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

		if (CAT.isDebugEnabled())
			CAT.debug("Externalizing " + resourcePath);

		file = assetDir;

		// Resources are always split by the unix seperator, even on Win32.

		splitter = new StringSplitter('/');

		path = splitter.splitToArray(resourcePath);

		// The path is expected to start with a leading slash, but the StringSplitter
		// will ignore that leading slash.

		for (i = 0; i < path.length - 1; i++)
		{
			// Doing it this way makes sure the path seperators are right.

			file = new File(file, path[i]);
		}

		// Make sure the directories exist.

		file.mkdirs();

		file = new File(file, path[path.length - 1]);

		// If the file exists, then assume all is well.  This is OK for development,
		// but there may be multithreading (or even multiprocess) race conditions
		// around the creation of the file.

		if (file.exists())
			return;

		// Get the resource and copy it to the file.

		inputURL = resolver.getResource(resourcePath);
		if (inputURL == null)
			throw new IOException(Tapestry.getString("missing-resource", resourcePath));

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
	*  Gets the externalizer singleton for the application.  If it does not already
	*  exist, it is created and stored into the {@link ServletContext}.
	*
	*  <p>Each Tapestry application within a single {@link ServletContext}
	*  will have its own externalizer; they are differentiated by the
	*  application name.
	*
	*  @see ApplicationSpecification#getName()
	*
	**/

	public static AssetExternalizer get(IRequestCycle cycle)
	{
		ServletContext context;
		String attributeName;
		String applicationName;
		AssetExternalizer result;

		context = cycle.getRequestContext().getServlet().getServletContext();

		applicationName = cycle.getEngine().getSpecification().getName();

		attributeName = "com.primix.tapestry.AssetExternalizer." + applicationName;

		result = (AssetExternalizer) context.getAttribute(attributeName);

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
	**/

	public String getURL(String resourcePath) throws ResourceUnavailableException
	{
		String result;

		if (assetDir == null)
			return null;

		synchronized (resources)
		{
			result = (String) resources.get(resourcePath);

			if (result != null)
				return result;

			try
			{
				externalize(resourcePath);
			}
			catch (IOException ex)
			{
				throw new ResourceUnavailableException(
					Tapestry.getString("AssetExternalizer.externalize-failure", resourcePath, assetDir),
					ex);
			}

			result = URL + resourcePath;

			resources.put(resourcePath, result);

			return result;
		}
	}
}