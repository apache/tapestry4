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

package com.primix.tapestry;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;


import com.primix.tapestry.multipart.MultipartDecoder;
import com.primix.tapestry.util.StringSplitter;

/**
 * This class encapsulates all the relevant data for one request cycle of an
 * {@link ApplicationServlet}, that is:
 * <ul>
 *  	<li>{@link HttpServletRequest}
 *		<li>{@link HttpServletResponse}
 *		<li>{@link HttpSession}
 * 		<li>{@link HttpServlet}
 * </ul>
 * <p>It also provides methods for:
 * <ul>
 * <li>Retrieving the request parameters
 * <li>Getting and setting and removing request attributes
 * <li>Forwarding requests
 * <li>Redirecting requests
 * <li>Getting and setting Cookies
 * <li>Intepreting the request path info
 * <li>Writing an HTML description of the <code>RequestContext</code> (for debugging).
 * </ul>
 *
 * <p>This class is not a component, but does implement {@link IRender}.  When asked to render
 * (perhaps as the delegate of a {@link com.primix.tapestry.components.Delegator} component}
 * it simply invokes {@link #write(IResponseWriter)} to display all debugging output.
 *
 * <p>This class is derived from the original class 
 * <code>com.primix.servlet.RequestContext</code>,
 * part of the <b>ServletUtils</b> framework available from
 * <a href="http://www.gjt.org/servlets/JCVSlet/list/gjt/com/primix/servlet">The Giant 
 * Java Tree</a>.
 *
 *
 * @version $Id$
 * @author Howard Ship
 **/

public class RequestContext implements IRender
{
	private static final Category CAT = Category.getInstance(RequestContext.class);

	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ApplicationServlet servlet;
	private MultipartDecoder decoder;

	/**
	 * A mapping of the cookies available in the request.
	 *
	 **/

	private Map cookieMap;

	/**
	 * Used to contain the parsed, decoded pathInfo.
	 *
	 **/

	private String[] pathInfo;

	private boolean evenRow;

	/**
	 * Identifies which characters are safe in a URL, and do not need any encoding.
	 *
	 **/

	private static BitSet safe;

	static {
		int i;
		safe = new BitSet(256);

		for (i = 'a'; i <= 'z'; i++)
			safe.set(i);

		for (i = 'A'; i <= 'Z'; i++)
			safe.set(i);

		for (i = '0'; i <= '9'; i++)
			safe.set(i);

		safe.set('.');
		safe.set('-');
		safe.set('_');
		safe.set('*');
	}

	/**
	 * Used to quickly convert a 8 bit character value to a hex string.
	 **/

	private static final char HEX[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Creates a <code>RequestContext</code> from its components.
	 *
	 **/

	public RequestContext(
		ApplicationServlet servlet,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException
	{
		this.servlet = servlet;
		this.request = request;
		this.response = response;

        if (MultipartDecoder.isMultipartRequest(request))
            decoder = new MultipartDecoder(request);    
	}

	/**
	 * Adds a simple {@link Cookie}. To set a Cookie with attributes,
	 * use {@link #addCookie(Cookie)}.
	 *
	 **/

	public void addCookie(String name, String value)
	{
		addCookie(new Cookie(name, value));
	}

	/**
	 * Adds a {@link Cookie} to the response. Once added, the
	 * Cookie will also be available to {@link #getCookie(String)} method.
	 *
	 * <p>Cookies should only be added <em>before</em> invoking
	 * {@link HttpServletResponse#getWriter()}..
	 *
	 **/

	public void addCookie(Cookie cookie)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Addining cookie " + cookie);

		response.addCookie(cookie);

		if (cookieMap == null)
			readCookieMap();

		cookieMap.put(cookie.getName(), cookie);
	}

	private void buildPathInfo()
	{
		String raw;
		StringSplitter splitter;
		int i;

		raw = request.getPathInfo();

		if (raw == null)
		{
			pathInfo = new String[] {
			};
			return;
		}

		splitter = new StringSplitter('/');

		pathInfo = splitter.splitToArray(raw);

		// Decode any URL encoded values (such as bad punctuation,
		// etc.) in the path info.

		for (i = 0; i < pathInfo.length; i++)
		{
			try
			{
				// Note: deprecation warning for compatibility with
				// Servlet API 2.2

				pathInfo[i] = URLDecoder.decode(pathInfo[i]);
			}
			catch (Exception e)
			{
				// Ignore
			}
		}
	}

	private void datePair(IResponseWriter writer, String name, long value)
	{
		pair(writer, name, new Date(value));
	}

	/**
	 * Encodes a <code>java.awt.Color</code> in the standard HTML
	 * format: a pound sign ('#'), followed by six hex digits for
	 * specifying the red, green and blue components of the color.
	 *
	 **/

	public static String encodeColor(Color color)
	{
		char[] buffer;
		int component;

		buffer = new char[7];
		buffer[0] = '#';

		// Red

		component = color.getRed();
		buffer[1] = HEX[component >> 4];
		buffer[2] = HEX[component & 0x0F];

		// Green

		component = color.getGreen();
		buffer[3] = HEX[component >> 4];
		buffer[4] = HEX[component & 0x0F];

		// Blue
		component = color.getBlue();
		buffer[5] = HEX[component >> 4];
		buffer[6] = HEX[component & 0x0F];

		return new String(buffer);
	}

	/**
	 * Forwards the request to a new resource, typically a JSP.
	 **/

	public void forward(String path) throws ServletException, IOException
	{
		RequestDispatcher dispatcher;

		dispatcher = servlet.getServletContext().getRequestDispatcher(path);

		dispatcher.forward(request, response);
	}

	/**
	 * Builds an absolute URL from the given URI, using the {@link HttpServletRequest}
	 * as the source for scheme, server name and port.
	 *
	 * @see #getAbsoluteURL(String, String, String, int)
	 * 
	 **/

	public String getAbsoluteURL(String URI)
	{
		String server;
		int port;
		String scheme;

		scheme = request.getScheme();
		server = request.getServerName();
		port = request.getServerPort();

		// Keep things simple ... port 80 is accepted as the
		// standard port for http so it can be ommitted.
		// Some of the Tomcat code indicates that port 443 is the default
		// for https, and that needs to be researched.

		if (scheme.equals("http") && port == 80)
			port = 0;

		return getAbsoluteURL(URI, scheme, server, port);
	}

	/**
	 * Does some easy checks to turn a path (or URI) into an absolute URL. We assume
	 * <ul>
	 * <li>The presense of a colon means the path is complete already (any other colons
	 * in the URI portion should have been converted to %3A).
	 *
	 * <li>A leading pair of forward slashes means the path is simply missing
	 * the scheme.
	 * <li>Otherwise, we assemble the scheme, server, port (if non-zero) and the URI
	 * as given.
	 * </ul>
	 *
	 **/

	public String getAbsoluteURL(String URI, String scheme, String server, int port)
	{
		StringBuffer buffer = new StringBuffer();

		// Though, really, what does a leading colon with no scheme before it
		// mean?

		if (URI.indexOf(':') >= 0)
			return URI;

		// Should check the length here, first.

		if (URI.substring(0, 1).equals("//"))
		{
			buffer.append(scheme);
			buffer.append(':');
			buffer.append(URI);
			return buffer.toString();
		}

		buffer.append(scheme);
		buffer.append("://");
		buffer.append(server);

		if (port > 0)
		{
			buffer.append(':');
			buffer.append(port);
		}

		if (URI.charAt(0) != '/')
			buffer.append('/');

		buffer.append(URI);

		return buffer.toString();
	}

	/**
	 * Gets a named {@link Cookie}>.
	 *
	 * @param name The name of the Cookie.
	 * @return The Cookie, or null if no Cookie with that
	 * name exists.
	 *
	 **/

	public Cookie getCookie(String name)
	{
		if (cookieMap == null)
			readCookieMap();

		return (Cookie) cookieMap.get(name);
	}

	/**
	 * Reads the named {@link Cookie} and returns its value (if it exists), or
	 * null if it does not exist.
	 **/

	public String getCookieValue(String name)
	{
		Cookie cookie;

		cookie = getCookie(name);

		if (cookie == null)
			return null;

		return cookie.getValue();
	}

	/**
	 *  Returns the named parameter from the {@link HttpServletRequest}.
	 *
	 *  <p>Use {@link #getParameters(String)} for parameters that may
	 *  include multiple values.
	 * 
	 *  <p>This is the preferred way to obtain parameter values (rather than
	 *  obtaining the {@link HttpServletRequest} itself).  For form/multipart-data
	 *  encoded requests, this method will still work.
	 *
	 **/

	public String getParameter(String name)
	{
		if (decoder != null)
			return decoder.getString(name);

		return request.getParameter(name);
	}

	/**
	 * For parameters that are, or are possibly, multi-valued, this
	 * method returns all the values as an array of Strings.
	 * 
	 *  @see #getParameter(String)
	 *
	 **/

	public String[] getParameters(String name)
	{
		// Note: this may not be quite how we want it to work; we'll have to see.

		if (decoder != null)
            return decoder.getStrings(name);

		return request.getParameterValues(name);
	}

	/**
	 * Returns the named {@link IUploadFile}, if it exists, or null if it doesn't.
	 * Uploads require an encoding of <code>multipart/form-data</code>
	 * (this is specified in the
	 * form's enctype attribute).  If the encoding type
	 * is not so, or if no upload matches the name, then this method returns null.
	 * 
	 **/

	public IUploadFile getUploadFile(String name)
	{
		if (decoder == null)
			return null;

        return decoder.getUploadFile(name);
	}

    /**
     *  Invoked at the end of the request cycle to cleanup and temporary resources.
     *  This is chained to the {@link MultipartDecoder}, if there is one.
     * 
     *  @since 2.0.1
     **/
    
    public void cleanup()
    {
        if (decoder != null)
            decoder.cleanup();
    }
    
	/**
	 * Returns the pathInfo string at the given index. If the index
	 * is out of range, this returns null.
	 *
	 **/

	public String getPathInfo(int index)
	{
		if (pathInfo == null)
			buildPathInfo();

		try
		{
			return pathInfo[index];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return null;
		}

	}

	/**
	 * Returns the number of items in the pathInfo.
	 **/

	public int getPathInfoCount()
	{
		if (pathInfo == null)
			buildPathInfo();

		return pathInfo.length;
	}

	/**
	 *  Returns the request which initiated the current request cycle.  Note that
	 *  the methods {@link #getParameter(String)} and {@link #getParameters(String)}
	 *  should be used, rather than obtaining parameters directly from the request
	 *  (since the RequestContext handles the differences between normal and multipart/form
	 *  requests).
	 * 
	 **/

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	private String getRowClass()
	{
		String result;

		result = evenRow ? "even" : "odd";

		evenRow = !evenRow;

		return result;
	}

	public ApplicationServlet getServlet()
	{
		return servlet;
	}

	/**
	 *  Returns the {@link HttpSession}, if necessary, invoking
	 * {@link HttpServletRequest#getSession(boolean)}.  However,
	 * this method will <em>not</em> create a session.
	 *
	 **/

	public HttpSession getSession()
	{
		if (session == null)
			session = request.getSession(false);

		return session;
	}

	/**
	 *  Like {@link #getSession()}, but forces the creation of
	 *  the {@link HttpSession}, if necessary.
	 *
	 **/

	public HttpSession createSession()
	{
		if (session == null)
		{
			if (CAT.isDebugEnabled())
				CAT.debug("Creating HttpSession");

			session = request.getSession(true);
		}

		return session;
	}

	private void header(IResponseWriter writer, String valueName, String dataName)
	{
		writer.begin("tr");
		writer.attribute("class", "request-context-header");

		writer.begin("th");
		writer.print(valueName);
		writer.end();

		writer.begin("th");
		writer.print(dataName);
		writer.end("tr");

		evenRow = true;
	}

	private void object(IResponseWriter writer, String objectName)
	{
		writer.begin("span");
		writer.attribute("class", "request-context-object");
		writer.print(objectName);
		writer.end();
	}

	private void pair(IResponseWriter writer, String name, int value)
	{
		pair(writer, name, Integer.toString(value));
	}

	private void pair(IResponseWriter writer, String name, Object value)
	{
		if (value == null)
			return;

		if (value instanceof IRenderDescription)
		{
			IRenderDescription renderValue = (IRenderDescription) value;

			writer.begin("tr");
			writer.attribute("class", getRowClass());

			writer.begin("th");
			writer.print(name);
			writer.end();

			writer.begin("td");

			renderValue.renderDescription(writer);

			writer.end("tr");

			return;
		}

		pair(writer, name, value.toString());
	}

	private void pair(IResponseWriter writer, String name, String value)
	{
		if (value == null)
			return;

		if (value.length() == 0)
			return;

		writer.begin("tr");
		writer.attribute("class", getRowClass());

		writer.begin("th");
		writer.print(name);
		writer.end();

		writer.begin("td");
		writer.print(value);
		writer.end("tr");
	}

	private void pair(IResponseWriter writer, String name, boolean value)
	{
		pair(writer, name, value ? "yes" : "no");
	}

	private void readCookieMap()
	{
		cookieMap = new HashMap();

		Cookie[] cookies = request.getCookies();

		if (cookies != null)
			for (int i = 0; i < cookies.length; i++)
				cookieMap.put(cookies[i].getName(), cookies[i]);
	}

	/**
	 * Invokes {@liknk HttpResponse#sendRedirect(String)}</code>, but
	 * massages <code>path</code>, supplying missing elements to
	 * make it an absolute URL (i.e., specifying scheme, server, port, etc.).
	 *
	 * <p>The 2.2 Servlet API will do this automatically, and a little more,
	 * according to the early documentation.
	 *
	 **/

	public void redirect(String path) throws IOException
	{
		String absolutePath;
		String encodedURL;

		// Now a little magic to convert path into a complete URL. The Servlet
		// 2.2 API does this automatically.

		absolutePath = getAbsoluteURL(path);

		encodedURL = response.encodeRedirectURL(absolutePath);

		response.sendRedirect(encodedURL);
	}

	private void section(IResponseWriter writer, String sectionName)
	{
		writer.begin("tr");
		writer.attribute("class", "request-context-section");
		writer.begin("th");
		writer.attribute("colspan", 2);

		writer.print(sectionName);
		writer.end("tr");
	}

	private List getSorted(Enumeration e)
	{
		List result = new ArrayList();
		
		// JDK 1.4 includes a helper method in Collections for
		// this; but we want 1.2 compatibility for the
		// forseable future.
		
		while (e.hasMoreElements())
			result.add(e.nextElement());
			
		Collections.sort(result);

		return result;
	}

	/**
	 * Writes the state of the context to the writer, typically for inclusion
	 * in a HTML page returned to the user. This is useful
	 * when debugging.  The Inspector uses this as well.
	 *
	 **/

	public void write(IResponseWriter writer)
	{
		// Create a box around all of this stuff ...

		writer.begin("table");
		writer.attribute("class", "request-context-border");
		writer.begin("tr");
		writer.begin("td");

		// Get the session, if it exists, and display it.

		HttpSession session = getSession();

		if (session != null)
		{
			object(writer, "Session");
			writer.begin("table");
			writer.attribute("class", "request-context-object");

			section(writer, "Properties");
			header(writer, "Name", "Value");

			pair(writer, "id", session.getId());
			datePair(writer, "creationTime", session.getCreationTime());
			datePair(writer, "lastAccessedTime", session.getLastAccessedTime());
			pair(writer, "maxInactiveInterval", session.getMaxInactiveInterval());
			pair(writer, "new", session.isNew());

			List names = getSorted(session.getAttributeNames());
			int count = names.size();

			for (int i = 0; i < count; i++)
			{
				if (i == 0)
				{
					section(writer, "Attributes");
					header(writer, "Name", "Value");
				}

				String name = (String) names.get(i);
				pair(writer, name, session.getAttribute(name));
			}

			writer.end(); // Session

		}

		object(writer, "Request");
		writer.begin("table");
		writer.attribute("class", "request-context-object");

		// Parameters ...

		List parameters = getSorted(request.getParameterNames());
		int count = parameters.size();

		for (int i = 0; i < count; i++)
		{

			if (i == 0)
			{
				section(writer, "Parameters");
				header(writer, "Name", "Value(s)");
			}

			String name = (String) parameters.get(i);
			String[] values = request.getParameterValues(name);

			writer.begin("tr");
			writer.attribute("class", getRowClass());
			writer.begin("td");
			writer.print(name);
			writer.end();
			writer.begin("td");

			if (values.length > 1)
				writer.begin("ul");

			for (int j = 0; j < values.length; j++)
			{
				if (values.length > 1)
					writer.beginEmpty("li");

				writer.print(values[j]);

			}

			writer.end("tr");
		}

		section(writer, "Properties");
		header(writer, "Name", "Value");

		pair(writer, "authType", request.getAuthType());
		pair(writer, "characterEncoding", request.getCharacterEncoding());
		pair(writer, "contentLength", request.getContentLength());
		pair(writer, "contentType", request.getContentType());
		pair(writer, "method", request.getMethod());
		pair(writer, "pathInfo", request.getPathInfo());
		pair(writer, "pathTranslated", request.getPathTranslated());
		pair(writer, "protocol", request.getProtocol());
		pair(writer, "queryString", request.getQueryString());
		pair(writer, "remoteAddr", request.getRemoteAddr());
		pair(writer, "remoteHost", request.getRemoteHost());
		pair(writer, "remoteUser", request.getRemoteUser());
		pair(writer, "requestedSessionId", request.getRequestedSessionId());
		pair(writer, "requestedSessionIdFromCookie", request.isRequestedSessionIdFromCookie());
		pair(writer, "requestedSessionIdFromURL", request.isRequestedSessionIdFromURL());
		pair(writer, "requestedSessionIdValid", request.isRequestedSessionIdValid());
		pair(writer, "requestURI", request.getRequestURI());
		pair(writer, "scheme", request.getScheme());
		pair(writer, "serverName", request.getServerName());
		pair(writer, "serverPort", request.getServerPort());
		pair(writer, "contextPath", request.getContextPath());
		pair(writer, "servletPath", request.getServletPath());

		// Now deal with any headers

		List headers = getSorted(request.getHeaderNames());
		count = headers.size();

		for (int i = 0; i < count; i++)
		{
			if (i == 0)
			{
				section(writer, "Headers");
				header(writer, "Name", "Value");
			}

			String name = (String) headers.get(i);
			String value = request.getHeader(name);

			pair(writer, name, value);
		}

		// Attributes

		List attributes = getSorted(request.getAttributeNames());
		count = attributes.size();

		for (int i = 0; i < count; i++)
		{
			if (i == 0)
			{
				section(writer, "Attributes");
				header(writer, "Name", "Value");
			}

			String name = (String) attributes.get(i);

			pair(writer, name, request.getAttribute(name));
		}

		if (pathInfo == null)
			buildPathInfo();

		for (int i = 0; i < pathInfo.length; i++)
		{
			if (i == 0)
			{
				section(writer, "Path Info");
				header(writer, "Index", "Value");
			}

			pair(writer, Integer.toString(i), pathInfo[i]);
		}

		// Cookies ...

		Cookie[] cookies = request.getCookies();

		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{

				if (i == 0)
				{
					section(writer, "Cookies");
					header(writer, "Name", "Value");
				}

				Cookie cookie = cookies[i];

				pair(writer, cookie.getName(), cookie.getValue());

			} // Cookies loop
		}

		writer.end(); // Request

		object(writer, "Servlet");
		writer.begin("table");
		writer.attribute("class", "request-context-object");

		section(writer, "Properties");
		header(writer, "Name", "Value");

		pair(writer, "servlet", servlet);
		pair(writer, "servletInfo", servlet.getServletInfo());

		ServletConfig config = servlet.getServletConfig();

		List names = getSorted(config.getInitParameterNames());
		count = names.size();

		for (int i = 0; i < count; i++)
		{

			if (i == 0)
			{
				section(writer, "Init Parameters");
				header(writer, "Name", "Value");
			}

			String name = (String) names.get(i);
			;
			pair(writer, name, config.getInitParameter(name));

		}

		writer.end(); // Servlet

		ServletContext context = config.getServletContext();

		object(writer, "Servlet Context");
		writer.begin("table");
		writer.attribute("class", "request-context-object");

		section(writer, "Properties");
		header(writer, "Name", "Value");

		pair(writer, "majorVersion", context.getMajorVersion());
		pair(writer, "minorVersion", context.getMinorVersion());
		pair(writer, "serverInfo", context.getServerInfo());

		names = getSorted(context.getInitParameterNames());
		count = names.size();
		for (int i = 0; i < count; i++)
		{
			if (i == 0)
			{
				section(writer, "Initial Parameters");
				header(writer, "Name", "Value");
			}

			String name = (String) names.get(i);
			pair(writer, name, context.getInitParameter(name));
		}

		names = getSorted(context.getAttributeNames());
		count = names.size();
		for (int i = 0; i < count; i++)
		{
			if (i == 0)
			{
				section(writer, "Attributes");
				header(writer, "Name", "Value");
			}

			String name = (String) names.get(i);
			pair(writer, name, context.getAttribute(name));
		}

		writer.end(); // Servlet Context

		writeSystemProperties(writer);

		writer.end("table"); // The enclosing border
	}

	private void writeSystemProperties(IResponseWriter writer)
	{
		Properties properties = null;

		object(writer, "JVM System Properties");

		try
		{
			properties = System.getProperties();
		}
		catch (SecurityException se)
		{
			writer.print("<p>");
			writer.print(se.toString());
			return;
		}

		String pathSeparator = System.getProperty("path.separator", ";");

		writer.begin("table");
		writer.attribute("class", "request-context-object");

		List names = new ArrayList(properties.keySet());
		Collections.sort(names);
		int count = names.size();

		for (int i = 0; i < count; i++)
		{

			if (i == 0)
				header(writer, "Name", "Value");

			String name = (String) names.get(i);

			String property = properties.getProperty(name);

			if (property.indexOf(pathSeparator) < 0)
				pair(writer, name, property);
			else
			{

				writer.begin("tr");
				writer.attribute("class", getRowClass());

				writer.begin("th");
				writer.print(name);
				writer.end();

				writer.begin("td");
				writer.begin("ul");

				StringTokenizer tokenizer = new StringTokenizer(property, pathSeparator);

				while (tokenizer.hasMoreTokens())
				{
					writer.beginEmpty("li");
					writer.print(tokenizer.nextToken());
				}

				writer.end("tr");
			}
		}

		writer.end(); // System Properties
	}

	/**
	 *  Invokes {@link #write(IResponseWriter)}, which is used for debugging.
	 *
	 **/

	public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		write(writer);
	}
}