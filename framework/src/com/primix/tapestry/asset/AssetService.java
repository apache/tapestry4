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
 *  A service for building URLs to and accessing {@link IAsset}s.
 *  Most of the work is deferred to the {@link IAsset} instance.
 *
 *  <p>The retrieval part is directly linked to {@link PrivateAsset}.
 *  The service responds to a URL that encodes the path of a resource
 *  within the classpath.  The {@link #service(IRequestCycle,
 *  ResponseOutputStream)} method reads the resource and streams it
 *  out.
 *
 *  <p>TBD: Security issues.  Should only be able to retrieve a
 *  resource that was previously registerred in some way
 *  ... otherwise, hackers will be able to suck out the .class files
 *  of the application!
 *
 *  @author Howard Ship
 *  @version $Id$
 */

package com.primix.tapestry.asset;

import java.net.*;
import javax.servlet.*;
import java.io.*;
import com.primix.tapestry.*;
import java.util.*;

public class AssetService implements IEngineService
{
	private String prefix;
	private IResourceResolver resolver;

	/**
	 *  Defaults MIME types, by extension, used when the servlet container
	 *  doesn't provide MIME types.  ServletExec Debugger, for example,
	 *  fails to do provide these.
	 *
	 */
	 
	private static Map mimeTypes;
	
	static
	{
		mimeTypes = new HashMap(17);
		mimeTypes.put("css", "text/css");
		mimeTypes.put("gif", "image/gif");
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("jpeg", "image/jpeg");
		mimeTypes.put("htm", "text/html");
		mimeTypes.put("html", "text/html");
	}

	private static final int BUFFER_SIZE = 1024;

	public AssetService(IEngine engine)
	{
		prefix = engine.getServletPrefix() + "/" + ASSET_SERVICE;
        resolver = engine.getResourceResolver();
	}

	/**
	*  Builds a URL for a {@link PrivateAsset}.
	*
	*  <p>A single parameter is expected, the resource path of the asset
    *  (which is expected to start with a leading slash).
	*
	*/

	public String buildURL(IRequestCycle cycle, IComponent component, 
		String[] parameters)
	{
		if (parameters == null ||
			parameters.length != 1)
			throw new ApplicationRuntimeException(
				"Service asset requires exactly one parameter.");

		return prefix +	parameters[0];
	}

	public String getName()
	{
		return ASSET_SERVICE;
	}

	private static String getMimeType(String path)
	{
		String key;
		String result;
		int dotx;
		
		dotx = path.lastIndexOf('.');
		key = path.substring(dotx + 1).toLowerCase();
		
		result = (String)mimeTypes.get(key);
		
		if (result == null)
			result = "text/plain";
		
		return result;
	}

	/**
	*  Retrieves a resource from the classpath and returns it to the
	*  client in a binary output stream.
	*
	*  <p>TBD: Security issues.  Hackers can download .class files.
	*
	*  <p>TBD: Error handling.  What to do if an IOException is
	*  thrown, or the resource doesn't exist?  Typically, we're
	*  downloading an image file and there's no way to communicate
	*  the error back to the client web browser.
	*
	*/


	public void service(IRequestCycle cycle, ResponseOutputStream output) 
	throws ServletException, IOException, RequestCycleException
	{
		byte[] buffer;
		InputStream input;
		String resourcePath;
		URL resourceURL;
		int bytesRead;
		URLConnection resourceConnection;
		String contentType;
		ServletContext servletContext;
		int count;
		StringBuffer resourceBuffer;
		RequestContext context;
		int i;
		IMonitor monitor;
		int contentLength;

		resourceBuffer = new StringBuffer();
		context = cycle.getRequestContext();

		count = context.getPathInfoCount();
		for (i = 1; i < count; i++)
		{
			resourceBuffer.append('/');
			resourceBuffer.append(context.getPathInfo(i));
		}

		resourcePath = resourceBuffer.toString();

		monitor = cycle.getMonitor();
		if (monitor != null)
			monitor.serviceBegin("asset", resourcePath);

		resourceURL = resolver.getResource(resourcePath);

		if (resourceURL == null)
			throw new ApplicationRuntimeException(
				"Could not find resource " + resourcePath + ".");

		resourceConnection = resourceURL.openConnection();

		servletContext = cycle.getRequestContext().getServlet().
		getServletContext();

		// Getting the content type and length is very dependant
		// on support from the application server (represented
		// here by the servletContext).

		contentType = servletContext.getMimeType(resourcePath);
		contentLength = resourceConnection.getContentLength();

		if (contentLength > 0)
			cycle.getRequestContext().getResponse().
			setContentLength(contentLength);

		// Set the content type.  If the servlet container doesn't
		// provide it, try and guess it by the extension.

		if (contentType == null ||
			contentType.length() == 0)
				contentType = getMimeType(resourcePath);

		output.setContentType(contentType);
		
		// Disable any further buffering inside the ResponseOutputStream
		
		output.forceFlush();

		input = resourceConnection.getInputStream();

		buffer = new byte[BUFFER_SIZE];

		while (true)
		{
			bytesRead = input.read(buffer);

			if (bytesRead < 0)
				break;

			output.write(buffer, 0, bytesRead);
		}

		input.close();

		if (monitor != null)
			monitor.serviceEnd("asset");

		// The IEngine is responsible for closing the ResponseOutputStream
	}
}

