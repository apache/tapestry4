package com.primix.tapestry;

import java.awt.Color;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.*;
import java.io.Writer;
import com.primix.foundation.*;

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
 * <li>Getting, setting and removing Session attributes
 * <li>Forwarding requests (typically to a JSP for rendering)
 * <li>Redirecting requests
 * <li>Getting and setting Cookies
 * <li>Intepreting the request path info
 * <li>Writing an HTML description of the <code>RequestContext</code> (for debugging).
 * </ul>
 *
 * <p>This class is derived from the original class <code>com.primix.servlet.RequestContext</code>
 * part of the <b>ServletUtils</b> framework available from
 * <a href="http://www.gjt.org/servlets/JCVSlet/list/gjt/com/primix/servlet">The Giant 
 * Java Tree</a>.
 *
 * @version $Id$
 * @author Howard Ship
 */
 
public class RequestContext
{
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ApplicationServlet servlet;

	/**
	* A mapping of the cookies available in the request.
	*
	*/

	private Map cookieMap;

	private static final int MAP_SIZE = 5;
    
	/**
	* Used to contain the parsed, decoded pathInfo.
	*
	*/

	private String[] pathInfo;

	private boolean evenRow;


	/**
	* Identifies which characters are safe in a URL, and do not need any encoding.
	*
	*/

	private static BitSet safe;

	static
	{
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
	*/

	private static final char HEX[] = {'0', '1', '2', '3', '4', '5', '6', 
				       '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	* Creates a <code>RequestContext</code> from its components.
	*
	*/

	public RequestContext(ApplicationServlet servlet, HttpServletRequest request,
		HttpServletResponse response)
	{
		this.servlet = servlet;
		this.request = request;
		this.response = response;
	}

	/**
	* Adds a simple {@link Cookie}. To set a Cookie with attributes,
	* use {@link #addCookie(Cookie)}.
	*
	*/

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
	*/

	public void addCookie(Cookie cookie)
	{
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
			pathInfo = new String[]{};
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
				pathInfo[i] = decode(pathInfo[i]);
			}
			catch (Exception e)
			{
				// Ignore
			}
		}
	}

	/**
	* Builds a URL from a base name, adding query elements as
	* necessary. It performs escaping of the values in the query
	* string, such as converting spaces to '+', much like
	* <code>URLEncoder.encode()</code>. <p>If there's any doubt about
	* the client's ability to use cookies, this should be passed
	* through <code>HttpServletResponse.encodeURL()</code> before
	* inclusion on the generated page.
	*
	*
	* @param basePath The main component of the URL, such as
	* "/servlets/SomeClass". This is often obtained from
	* <code>HttpRequest.getServletPath()</code>.
	*
	* @param pathInfo An array of values. Each value is appended to
	* the basePath and preceded with a forward slash. Each element is
	* encoded. The array may be empty or null.
	*
	* @param query An array of values used to form the query portion
	* of the URL, which comes after the base path and any pathInfo
	* segments.  The even numbered elements (starting with element
	* zero) are the names of query parameters the odd numbered
	* elements are the parameter values. The array must have an even
	* number of elements, be empty, or be null.
	*
	* @return The final URL with proper URL encoding of all query
	* parameters.
	*/

	public static String buildURL(String basePath, String[] pathInfo, String[] query)
	{
		StringBuffer buffer;
		char sep = '?';
		boolean encodeFirst = false;
		String parameterName;
		String value;
		int i;

		// Start with the base path

		buffer = new StringBuffer(basePath);

		// If there's any path info, add them, seperated
		// by slashes.

		if (pathInfo != null)
		{
			for (i = 0; i < pathInfo.length; i++)
			{
				buffer.append('/');
				encode(buffer, pathInfo[i], false);
			}
		}

		// If there's any query strings, pair them off as parameters
		// and values.

		if (query != null)
		{
			for (i = 0; i < query.length;)
			{
				parameterName = query[i++];

				// Will throw an exception if there isn't an even number of
				// elements in the query array.

				value = query[i++];

				// Append a seperator from the path or the previous parameter/value
				// pair.

				buffer.append(sep);

				encode(buffer, parameterName, encodeFirst);
				buffer.append('=');
				encode(buffer, value, false);

				// Becuase the next parameter name will follow an '&' we want to encode
				// the first character as a hex value to prevent it from resembling
				// an HTML entity.

				sep = '&';
				encodeFirst = true;
			}
		}

		return buffer.toString();
	}

	private static int convertHex(char ch)
	{
		if (ch >= '0' && ch <= '9')
			return ch - '0';

		if (ch >= 'a' && ch <= 'f')
			return (ch - 'a') + 10;

		if (ch >= 'A' && ch <= 'F')
			return (ch - 'A') + 10;

		throw new IllegalArgumentException("Invalid hex character '" + ch + "'.");
	}

	private void datePair(IResponseWriter writer, String name, long value)
	{
		pair(writer, name, new Date(value));
	}

	/**
	*  Decodes an x-www-form-urlencoded string.
	*
	*  @param raw The encoded string.
	*  @return The decoded string.  Returns the empty string if raw is null.
	*  @throws IllegalArgumentException if the string is not properly encoded.
	*
	*/

	public static String decode(String raw)
	{
		StringBuffer buffer;
		int i, length;
		char ch;
		int digit;
		char value;
		boolean invalid = false;
		boolean dirty = false;

		if (raw == null)
			return "";

		length = raw.length();
		
		buffer = new StringBuffer(length);

		try
		{
			for (i = 0; i < length; i++)
			{
				ch = raw.charAt(i);

				// A '+' is the encoding of a space.

				if (ch == '+')
				{
					dirty = true;
					buffer.append(' ');
					continue;
				}

				// These characters are not encoded

				if ((ch >= 'a' && ch <= 'z') || 
					(ch >= 'A' && ch <= 'Z') || 
					(ch >= '0' && ch <= '9'))
				{
					buffer.append(ch);
					continue;
				}

				// Otherwise, the only legit character is the '%', which leads
				// off two hex digits for the character in question.

				if (ch != '%')
				{
					invalid = true;
					break;
				}

				// Advance and get the high order digit.

				ch = raw.charAt(++i);
				digit = convertHex(ch);
				value = (char) (digit << 4);

				// Advance and get the low order digit

				ch = raw.charAt(++i);
				digit = convertHex(ch);
				value += digit;

				// Append the character

				buffer.append(value);

				dirty = true;
			}
		}
		catch (IllegalArgumentException e)
		{
			// Thrown by convertHex() if the character following a '%'
			// is not a hex digit.

			invalid = true;
		}

		// Catch exceptions when string too short after '%'

		catch (IndexOutOfBoundsException e)
		{
			invalid = true;
		}

		if (invalid)
			throw new IllegalArgumentException("'" + raw + 
				"' is not a valid x-www-form-urlencoded String.");

		// If not 'dirty', meaning there were no encoded characters in
		// the string, then return the raw input value.  If
		// substitutions did take place, return the decoded buffer.

		if (dirty)
			return buffer.toString();
		else
			return raw;
	}

	/**
	* Applies URL encoding to the value, appending the result to the
	* <code>StringBuffer</code>.  
	*
	* <p>Why would you want to encode the first character of a
	* string? Well, <em>IE 4.0</em> has a bug or feature where it
	* would convert HTML entities <i>inside</i> a URL.  If a query
	* parameter happened to have a name that looked like an HTML
	* entity, it got compressed down to a single character (even if
	* it <em>didn't end in a semicolon</em>). The end result is that
	* the URL is invalid. This is addressed by encoding the first
	* character after each ampersand so that it is impossible to
	* generate an entity. Any ampersands in a query parameter name or
	* value will be encoded as a hex value so the only real
	* ampersands will be those seperating query parameters.
	*
	*/

	protected static void encode(StringBuffer buffer, String value, boolean encodeFirst)
	{
		int i, length;
		char ch;
		boolean encode;
		length = value.length();

		for (i = 0; i < length; i++)
		{
			// Reduce any Unicode characters to just the low byte.

			ch = (char) (value.charAt(i) & 0xff);

			// The space is a special case.

			if (ch == ' ')
			{
				buffer.append('+');
				continue;
			}

			// Encode if not a safe character, or if the first and
			// encodeFirst is set.

			if (i == 0 && encodeFirst)
				encode = true;
			else
				encode = !safe.get(ch);

			if (encode)
			{
				// Express the character as a two digit hex constant.

				buffer.append('%');
				buffer.append(HEX[ch >> 4]);
				buffer.append(HEX[ch & 0x0F]);
			}
			else
				buffer.append(ch);

		} // for i

	}

	/**
	* Encodes a <code>java.awt.Color</code> in the standard HTML
	* format: a pound sign ('#'), followed by six hex digits for
	* specifying the red, green and blue components of the color.
	*
	*/

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
	*/

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
     */

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
	*/

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
	* Returns a named attribute of the <code>HttpServletRequest</code>,
	* or null if not defined.
	*
	*/

	public Object getAttribute(String name)
	{
		return request.getAttribute(name);
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
		if (cookieMap == null)
			readCookieMap();

		return (Cookie)cookieMap.get(name);
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
	*  Returns the named parameter from the <code>HttpServletRequest</code>.
	*
	*  <p>Use {@link #getParameterValues(String)} for parameters that may
	*  invlude multiple values.
	*
	*/

	public String getParameter(String name)
	{
		return request.getParameter(name);
	}

	/**
	* For parameters that are, or are possibly, multi-valued, this
	* method returns all the values as an array of Strings.
	*
	*/

	public String[] getParameterValues(String name)
	{
		return request.getParameterValues(name);
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
	*  Returns the session. The session is created if it doesn't already
	*  exist.
	*
	*/

	public HttpSession getSession()
	{
		if (session == null)
			session = request.getSession(true);

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

		result = getSession().getAttribute(name);

		return result;
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
		if (value != null)
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
		Cookie[] cookies;
		int i;

		cookieMap = new HashMap(MAP_SIZE);

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

		// Now a little magic to convert path into a complete URL. The Servlet
		// 2.2 API does this automatically.

		absolutePath = getAbsoluteURL(path);

		encodedURL = response.encodeRedirectURL(absolutePath);

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
		getSession().removeAttribute(name);
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

	/**
	* Sets an attribute of the <code>HttpServletRequest</code>.
	*/

	public void setAttribute(String name, Object value)
	{
		request.setAttribute(name, value);
	}

	/**
	* Stores an attribute into the <code>HttpSession</code>.
	*
	* @see #getSessionAttribute(String)
	*
	*/

	public void setSessionAttribute(String name, Object value)
	{
		getSession().setAttribute(name, value);
	}

	/**
	* Writes the state of the context to the writer, typically for inclusion
	* in a HTML page returned to the user. This is useful
	* when debugging, and is typically invoked from a JSP.
	*
	*/

	public void write(IResponseWriter writer)
	{
		int i;
		String names[];
		String values[];
		Enumeration e;
		String name;
		String value;
		boolean first;;
		Cookie[] cookies;
		Cookie cookie;
		ServletConfig config;
		ServletContext context;

		// Create a box around all of this stuff ...

		writer.begin("table");
		writer.attribute("class", "request-context-border");
		writer.begin("tr");
		writer.begin("td");

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

        first = true;
		e = session.getAttributeNames();

		while (e.hasMoreElements())
		{

			if (first)
			{
				section(writer, "Attributes");
				header(writer, "Name", "Value");
                first = false;
			}

            name = (String)e.nextElement();

			pair(writer, name, session.getAttribute(name));
		}

		writer.end(); // Session

		object(writer, "Request");
		writer.begin("table");
		writer.attribute("class", "request-context-object");

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

        first = true;
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
				writer.attribute("class", getRowClass());
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
		writer.attribute("class", "request-context-object");

		section(writer, "Properties");
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
		writer.attribute("class", "request-context-object");

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

		writeSystemProperties(writer);

		writer.end("table"); // The enclosing border
	}

	private void writeSystemProperties(IResponseWriter writer)
	{
		boolean first = true;
		Properties properties;
		List names;
		Iterator i;
		String name;
		String property;
		String pathSeparator;
		StringTokenizer tokenizer;
 
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

		pathSeparator = System.getProperty("path.separator", ";");

		writer.begin("table");
		writer.attribute("class", "request-context-object");

        names = new ArrayList(properties.keySet());
        Collections.sort(names);

		i = names.iterator();
		while (i.hasNext())
		{
			name = (String)i.next();

			property = properties.getProperty(name);

			if (first)
			{
				header(writer, "Name", "Value");
				first = false;
				first = false;
			}

			if (property.indexOf(pathSeparator) < 0)
				pair(writer, name, property);
			else
			{
				tokenizer = new StringTokenizer(property, pathSeparator);

				while (tokenizer.hasMoreTokens())
				{
					pair(writer, name, tokenizer.nextToken());

					name = "";
				}
			}
		}

		writer.end(); // System Properties
	}

}

