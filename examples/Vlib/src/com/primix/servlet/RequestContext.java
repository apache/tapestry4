package com.primix.servlet;

/*
 * ServletUtils - Support library for improved Servlets and JavaServer Pages.
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
 * included    with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.*;
import java.io.Writer;

/**
 * This class encapsulates all the relevant data for one request cycle of an
 * <code>HttpServlet</code>, that is:
 * <ul>
 *  	<li>HttpServletRequest 
 *		<li>HttpServletResponse 
 *		<li>HttpSession
 * 		<li>HttpServlet 
 * </ul>
 * <p>It also provides methods for:
 * <ul>
 * <li>Retrieving the request parameters
 * <li>Getting and setting and removing request attributes
 * <li>Getting, setting and removing Session attributes
 * <li>Forwarding requests (typically to a JSP for rendering)
 * <li>Redirecting requests
 * <li>Getting and setting Cookies
 * <li>Intepreting the request path info
 * <li>Writing an HTML description of the <code>RequestContext</code> (for debugging).
 * </ul>
 *
 * <p>A <code>RequestContext</code> has a built-in ability to produce debugging
 * output (via <code>Servlet.log()</code>) if enabled. Setting the servlet init parameter
 * <code>com.primix.servlet.debugEnabled</code> to true enables this output, which identifies
 * all key operations (such as getting query parameters or getting session values).
 *
 * @version $Id$
 * @author Howard Ship
 */
 
public class RequestContext
{
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpServlet servlet;

	private static final String ATTRIBUTE_NAME =
		"com.primix.servlet.RequestContext";

	/**
	 * This is set from an initial parameter of the servlet.
	 *
	 */

	private boolean debugEnabled = false;

	/**
	 * A mapping of the cookies available in the request.
	 *
	 */

	private Dictionary cookieMap;

	/**
	 * Used to contain the parsed, decoded pathInfo.
	 *
	 */

	private String[] pathInfo;

	private static final String HEADER_COLOR = "#00CCFF";

	private boolean greyRow = false;

	/**
	 * Creates a <code>RequestContext</code> from its components.
	 * <p>Sets the <code>debugEnabled</code> property from
	 * the servlet's initial parameter named
	 * <code>com.primix.servlet.debugEnabled</code>.
	 *
	 * <p>Registers the context as a request parameter so that
	 * it can later be retrieved by {@link #get(HttpServletRequest)}.
	 *
	 */

	public RequestContext(HttpServlet servlet,
		HttpServletRequest request, HttpServletResponse response)
	{
		this.servlet = servlet;
		this.request = request;
		this.response = response;

		String value = servlet.getServletConfig().getInitParameter("com.primix.servlet.debugEnabled");

		debugEnabled = (value != null && value.equalsIgnoreCase("true"));

		request.setAttribute(ATTRIBUTE_NAME, this);
	}
	/**
	 * Adds a simple <code>Cookie</code>. To set a Cookie with attributes,
	 * use {@link #addCookie(Cookie)}.
	 *
	 */

	public void addCookie(String name, String value)
	{
		addCookie(new Cookie(name, value));
	}
	/**
	 * Adds a <code>Cookie</code> to the response. Once added, the
	 * Cookie will also be available to {@link #getCookie(String)} method.
	 *
	 * <p>Cookies should only be added <em>before</em> invoking
	 * <code>HttpServletResponse.getWriter()</code>.
	 *
	 */

	public void addCookie(Cookie cookie)
	{
		if (debugEnabled)
			servlet.log("addCookie(" + cookie.getName() + ") [" +
				cookie.getValue() + "]");

		response.addCookie(cookie);

		if (cookieMap == null)
			readCookieMap();

		cookieMap.put(cookie.getName(), cookie);
	}
	private void buildPathInfo()
	{
		String raw;
		Vector elements;
		StringTokenizer tokenizer;
		String rawItem;
		String decodedItem;

		raw = request.getPathInfo();

		if (raw == null)
		{
			pathInfo = new String[]{};
			return;
		}

		elements = new Vector();

		tokenizer = new StringTokenizer(raw, "/");

		while (tokenizer.hasMoreTokens())
		{
			rawItem = tokenizer.nextToken();

			// Decode any encoded characters in the path.

			try
			{
				// Under JDK1.2 we could use URLDecoder.decode(), but
				// this needs to work under JDK1.1.
				
				decodedItem = HTMLUtils.decode(rawItem);
			}
			catch(Exception e)
			{
				decodedItem = rawItem;
			}

			elements.addElement(decodedItem);
		}

		pathInfo = new String[elements.size()];
		elements.copyInto(pathInfo);
	}
	private void datePair(HTMLWriter writer, String name, long value)
	{
		pair(writer, name, new Date(value));
	}
	/**
	 * Forwards the request to a new resource, typically a JSP.
	 */

	public void forward(String path) throws ServletException, IOException
	{
		RequestDispatcher dispatcher;

		if (debugEnabled)
			servlet.log("Forwarding to: " + path);

		dispatcher = servlet.getServletContext().getRequestDispatcher(path);

		dispatcher.forward(request, response);
	}
	/**
	 * Returns the <code>RequestContext</code> as previously
	 * stored as a request attribute.
	 *
	 * @return The context, or null if not
	 * previously stored.
	 *
	 * @see #RequestContext(HttpServlet, HttpServletRequest,
	 * HttpServletResponse)
	 */

	public static RequestContext get(HttpServletRequest request)
	{
		return (RequestContext)request.getAttribute(ATTRIBUTE_NAME);
	}
	/**
	 * Returns a named attribute of the <code>HttpServletRequest</code>,
	 * or null if not defined.
	 *
	 */

	public Object getAttribute(String name)
	{
		Object result;

		result = request.getAttribute(name);

		if (debugEnabled)
			servlet.log("request.getAttribute(" + name + ")=" + result);

		return result;
	}
	/**
	 * Gets a named <code>Cookie</code>.
	 *
	 * @param name The name of the <code>Cookie</code>.
	 * @return The <code>Cookie</code>, or null if no Cookie with that
	 * name exists.
	 *
	 */

	public Cookie getCookie(String name)
	{
		Cookie result;
		String resultValue = "<null>";

		if (cookieMap == null)
			readCookieMap();

		result = (Cookie)cookieMap.get(name);

		if (result != null)
			resultValue = result.getValue();

		if (debugEnabled)
			servlet.log("getCookie(" + name + ")=" + resultValue);

		return result;
	}
	/**
	 * Reads the named <CODE>Cookie</CODE> and returns its value (if it exists), or
	 * null if it does not exist.
	 */

	public String getCookieValue(String name)
	{
		Cookie cookie;

		cookie = getCookie(name);

		if (cookie == null)
			return null;

		return cookie.getValue();
	}
	/**
	 * Returns the value of a <CODE>Cookie</CODE>, if the <CODE>Cookie</CODE> exists, or the
	 * <code>defaultValue</code> if the <code>Cookie</code> does not exist.
	 */

	public String getCookieValue(String name, String defaultValue)
	{
		Cookie cookie;

		cookie = getCookie(name);
		if (cookie == null)
			return defaultValue;

		return cookie.getValue();
	}
	/**
	 *  Returns the named parameter from the <code>HttpServletRequest</code>.
	 *
	 *  <p>Use {@link #getParameterValues(String)} for parameters that may
	 *  invlude multiple values.
	 *
	 */
	 
	public String getParameter(String name)
	{
		String result;

		result = request.getParameter(name);

		if (debugEnabled)
			servlet.log("request.getParameter(" + name + ")=" + result);

		return result;
	}
	/**
	 * For parameters that are, or are possibly, multi-valued, this
	 * method returns all the values as an array of Strings.
	 *
	 */

	public String[] getParameterValues(String name)
	{
		String[] result;
		int resultCount = 0;

		result = request.getParameterValues(name);
		if (result != null)
			resultCount = result.length;

		if (debugEnabled)
			servlet.log("request.getParameterValues(" + name + ")= (" +
				resultCount + " values)");

		return result;
	}
	/**
	 * Returns the pathInfo string at the given index. If the index
	 * is out of range, this returns null.
	 *
	 */

	public String getPathInfo(int index)
	{
		if (pathInfo == null)
			buildPathInfo();

		try
		{
			return pathInfo[index];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}

	}
	/**
	 * Returns the number of items in the pathInfo.
	 */

	public int getPathInfoCount()
	{
		if (pathInfo == null)
			buildPathInfo();

		return pathInfo.length;
	}
	public HttpServletRequest getRequest()
	{ return request; }
	public HttpServletResponse getResponse()
	{ return response; }
	private String getRowColor()
	{
		String result;

		result = greyRow ? "#c0c0c0" : "#ffffff";

		greyRow = !greyRow;

		return result;
	}
	public HttpServlet getServlet()
	{
		return servlet;
	}
/**
 *  Returns the session. The session is created if it doesn't already
 *  exist.
 *
 */

public HttpSession getSession()
{
	if (session == null)
	{
		if (debugEnabled)
			servlet.log("Retrieving (or creating) session from request.");
			
		session = request.getSession(true);
	}
	
	return session;
}
	/**
	 * Gets an attribute from the HttpSession. Attributes are named
	 * "values" through the 2.1 version of the API. In 2.2, the
	 * <code>getValue()</code> method will be deprecated in favor
	 * of <code>getAttribute</code>.
	 *
	 * @return The named value, or null if not found.
	 *
	 * @see #setSessionAttribute(String,Object)
	 */

	public Object getSessionAttribute(String name)
	{
		Object result;

		result = getSession().getValue(name);

		if (debugEnabled)
			servlet.log("session.getValue(" + name + ")=" + result);
			
		return result;
	}
/**
 * A convience method. Sets the content type of the response
 * to 'text/html' and invokes <code>HttpServletResponse.getWriter()</code>, then
 * returns the response's writer wrapped in an 
 * {@link HTMLWriter}.
 *
 * <p>This is typically used by a servlet that produces its own HTML output, rather
 * than forwarding to a JSP.
 *
 * <p>It is imporant to close the {@link HTMLWriter}, since that
 * will close the <code>HttpServletResponse</code>'s writer as well.
 */

public HTMLWriter getWriter() throws IOException
{
	response.setContentType("text/html");

	return new HTMLWriter(response.getWriter(), true);
}
	private void header(HTMLWriter writer, String valueName, String dataName)
	{
		writer.begin("tr");
		writer.attribute("bgcolor", HEADER_COLOR);
		writer.begin("th");
		writer.print(valueName);
		writer.end();

		writer.begin("th");
		writer.print(dataName);
		writer.end("tr");

		greyRow = false;
	}
	public boolean isDebugEnabled()
	{
		return debugEnabled;
	}
	/**
	 * Invokes <code>Servlet.log()</code> to generate a log message.
	 *
	 */

	public void log(String message)
	{
		servlet.log(message);
	}
	private void object(HTMLWriter writer, String objectName)
	{
		writer.begin("h2");
		writer.print(objectName);
		writer.end();
	}
	private void pair(HTMLWriter writer, String name, int value)
	{
		pair(writer, name, Integer.toString(value));
	}
	private void pair(HTMLWriter writer, String name, Object value)
	{
		if (value != null)
			pair(writer, name, value.toString());
	}
	private void pair(HTMLWriter writer, String name, String value)
	{
		if (value == null)
			return;

		if (value.length() == 0)
			return;

		writer.begin("tr");
		writer.attribute("bgcolor", getRowColor());

		writer.begin("td");
		writer.print(name);
		writer.end();

		writer.begin("td");
		writer.print(value);
		writer.end("tr");
	}
	private void pair(HTMLWriter writer, String name, boolean value)
	{
		pair(writer, name, value ? "yes" : "no");
	}
	private void readCookieMap()
	{
		Cookie[] cookies;
		int i;

		cookieMap = new Hashtable();

		cookies = request.getCookies();
		for (i = 0; i < cookies.length; i++)
			cookieMap.put(cookies[i].getName(), cookies[i]);
	}
	/**
	 * Invokes <code>HttpResponse.sendRedirect()</code>, but
	 * massages <code>path</code>, supplying missing elements to
	 * make it a full URL (i.e., specifying scheme, server, port, etc.).
	 *
	 * <p>The 2.2 Servlet API will do this automatically, and a little more,
	 * according to the early documentation.
	 *
	 */

	public void redirect(String path) throws IOException
	{
		String absolutePath;
		String encodedURL;

		if (debugEnabled)
			servlet.log("Redirecting to: " + path);

		// Now a little magic to convert path into a complete URL. The Servlet
		// 2.2 API does this automatically.

		absolutePath = HTMLUtils.getAbsoluteURL(path, request);

		if (debugEnabled && !absolutePath.equals(path))
			servlet.log("Absolute path: " + absolutePath);

		encodedURL = response.encodeRedirectURL(absolutePath);

		if (debugEnabled && !encodedURL.equals(absolutePath))
			servlet.log("Encoded URL: " + encodedURL);

		response.sendRedirect(encodedURL);
	}
	/**
	 * Removes an attribute previously stored in the
	 * <code>HttpSession</code>.
	 *
	 * @see #setSessionAttribute(String,Object)
	 *
	 */

	public void removeSessionAttribute(String name)
	{
		if (debugEnabled)
			servlet.log("session.removeValue(" + name + ")");

		getSession().removeValue(name);
	}
	private void section(HTMLWriter writer, String sectionName)
	{
		writer.begin("tr");
		writer.attribute("bgcolor", HEADER_COLOR);
		writer.begin("th");
		writer.attribute("colspan", 2);

		writer.print(sectionName);
		writer.end("tr");
	}
	/**
	 * Sets an attribute of the <code>HttpServletRequest</code>.
	 */

	public void setAttribute(String name, Object value)
	{
		if (debugEnabled)
			servlet.log("request.setAttribute(" + name + ", " + value + ")");

		request.setAttribute(name, value);
	}
	public void setDebugEnabled(boolean value)
	{
		debugEnabled = value;
	}
	/**
	 * Stores an attribute into the <code>HttpSession</code>.
	 *
	 * @see #getSessionAttribute(String)
	 *
	 */

	public void setSessionAttribute(String name, Object value)
	{
		if (debugEnabled)
			servlet.log("session.putValue(" + name + ", " + value + ")");

		getSession().putValue(name, value);
	}
	/**
	 * Writes the state of the context to the writer, typically for inclusion
	 * in a HTML page returned to the user. This is useful
	 * when debugging, and is typically invoked from a JSP.
	 *
	 */

	public void write(HTMLWriter writer)
	{
		int i;
		String names[];
		String values[];
		Enumeration e;
		String name;
		String value;
		boolean first = true;
		Cookie[] cookies;
		Cookie cookie;
		ServletConfig config;
		ServletContext context;

		if (debugEnabled)
			servlet.log("Writing RequestContext");

		// Create a box around all of this stuff ...

		writer.begin("table");
		writer.attribute("border", 1);
		writer.begin("tr");
		writer.begin("td");

		object(writer, "Session");
		writer.begin("table");

		section(writer, "Properties");
		header(writer, "Name", "Value");

		pair(writer, "id", session.getId());
		datePair(writer, "creationTime", session.getCreationTime());
		datePair(writer, "lastAccessedTime", session.getLastAccessedTime());
		pair(writer, "maxInactiveInterval", session.getMaxInactiveInterval());
		pair(writer, "new", session.isNew());

		// In 2.2, getValue...() will be deprecated in favor of
		// getAttribute...().

		names = session.getValueNames();
		for (i = 0; i < names.length; i++)
		{

			if (i == 0)
			{
				section(writer, "Attributes");
				header(writer, "Name", "Value");
			}

			pair(writer, names[i], session.getValue(names[i]));
		}

		writer.end(); // Session

		object(writer, "Request");
		writer.begin("table");

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
		pair(writer, "requestedSessionIdFromCookie",
			request.isRequestedSessionIdFromCookie());
		pair(writer, "requestedSessionIdFromURL",
			request.isRequestedSessionIdFromURL());
		pair(writer, "requestedSessionIdValid",
			request.isRequestedSessionIdValid());
		pair(writer, "requestURI", request.getRequestURI());
		pair(writer, "scheme", request.getScheme());
		pair(writer, "serverName", request.getServerName());
		pair(writer, "serverPort", request.getServerPort());
		pair(writer, "servletPath", request.getServletPath());

		// Now deal with any headers

		e = request.getHeaderNames();
		while (e.hasMoreElements())
		{
			if (first)
			{
				section(writer, "Headers");
				header(writer, "Name", "Value");
				first = false;
			}

			name = (String)e.nextElement();
			value = request.getHeader(name);

			pair(writer, name, value);
		}

		// Parameters ...

		first = true;

		e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			if (first)
			{
				section(writer, "Parameters");
				header(writer, "Name", "Value(s)");
				first = false;
			}

			name = (String)e.nextElement();
			values = request.getParameterValues(name);

			for (i = 0; i < values.length; i++)
			{
				writer.begin("tr");
				writer.attribute("bgcolor", getRowColor());
				writer.begin("td");

				if (i == 0)
					writer.print(name);

				writer.end();

				writer.begin("td");
				writer.print(values[i]);
				writer.end("tr");
			}
		}

		// Attributes

		first = true;
		e = request.getAttributeNames();

		while (e.hasMoreElements())
		{
			if (first)
			{
				section(writer, "Attributes");
				header(writer, "Name", "Value");
				first = false;
			}

			name = (String)e.nextElement();

			pair(writer, name, request.getAttribute(name));
		}

		if (pathInfo == null)
			buildPathInfo();

		for (i = 0; i < pathInfo.length; i++)
		{
			if (i == 0)
			{
				section(writer, "Path Info");
				header(writer, "Index", "Value");
			}

			pair(writer, Integer.toString(i), pathInfo[i]);
		}

		// Cookies ...

		cookies = request.getCookies();
		for (i = 0; i < cookies.length; i++)
		{

			if (i == 0)
			{
				section(writer, "Cookies");
				header(writer, "Name", "Value");
			}

			cookie = cookies[i];

			pair(writer, cookie.getName(), cookie.getValue());

		} // Cookies loop

		writer.end(); // Request

		object(writer, "Servlet");
		writer.begin("table");

		section(writer, "Properies");
		header(writer, "Name", "Value");

		pair(writer, "servlet", servlet);
		pair(writer, "servletInfo", servlet.getServletInfo());

		config = servlet.getServletConfig();

		first = true;

		e = config.getInitParameterNames();
		while (e.hasMoreElements())
		{
			if (first)
			{
				section(writer, "Init Parameters");
				header(writer, "Name", "Value");
				first = false;
			}

			name = (String)e.nextElement();
			pair(writer, name, config.getInitParameter(name));

		}

		writer.end(); // Servlet

		context = config.getServletContext();

		object(writer, "Servlet Context");
		writer.begin("table");

		section(writer, "Properties");
		header(writer, "Name", "Value");

		pair(writer, "majorVersion", context.getMajorVersion());
		pair(writer, "minorVersion", context.getMinorVersion());
		pair(writer, "serverInfo", context.getServerInfo());

		first = true;

		e = context.getAttributeNames();
		while (e.hasMoreElements())
		{
			if (first)
			{
				section(writer, "Attributes");
				header(writer, "Name", "Value");
				first = false;
			}

			name = (String)e.nextElement();
			pair(writer, name, context.getAttribute(name));
		}

		writer.end(); // Servlet Context

		writer.end("table"); // The enclosing border
	}
}
