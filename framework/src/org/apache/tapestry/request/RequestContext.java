//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.multipart.DefaultMultipartDecoder;
import org.apache.tapestry.multipart.IMultipartDecoder;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.IRenderDescription;

/**
 *  This class encapsulates all the relevant data for one request cycle of an
 *  {@link ApplicationServlet}.  This includes:
 *  <ul>
 *  	<li>{@link HttpServletRequest}
 *		<li>{@link HttpServletResponse}
 *		<li>{@link HttpSession}
 * 		<li>{@link javax.servlet.http.HttpServlet}
 *  </ul>
 *  <p>It also provides methods for:
 *  <ul>
 *  <li>Retrieving the request parameters (even if a file upload is involved)
 *  <li>Getting, setting and removing request attributes
 *  <li>Forwarding requests
 *  <li>Redirecting requests
 *  <li>Getting and setting Cookies
 *  <li>Intepreting the request path info
 *  <li>Writing an HTML description of the <code>RequestContext</code> (for debugging).
 *  </ul>
 *
 * 
 *  <p>
 *  If some cases, it is necesary to provide an implementation of
 *  {@link IRequestDecoder} (often, due to a firewall).
 *  If the application specifification
 *  provides an extension named
 *  <code>org.apache.tapestry.request-decoder</code>
 *  then it will be used, instead of a default decoder.
 * 
 *  <p>This class is not a component, but does implement {@link IRender}.  When asked to render
 *  (perhaps as the delegate of a {@link org.apache.tapestry.components.Delegator} component}
 *  it simply invokes {@link #write(IMarkupWriter)} to display all debugging output.
 *
 *  <p>This class is derived from the original class 
 *  <code>com.primix.servlet.RequestContext</code>,
 *  part of the <b>ServletUtils</b> framework available from
 *  <a href="http://www.gjt.org/servlets/JCVSlet/list/gjt/com/primix/servlet">The Giant 
 *  Java Tree</a>.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class RequestContext implements IRender
{
    /** @since 2.2 **/

    private static class DefaultRequestDecoder implements IRequestDecoder
    {
        public DecodedRequest decodeRequest(HttpServletRequest request)
        {
            DecodedRequest result = new DecodedRequest();

            result.setRequestURI(request.getRequestURI());
            result.setScheme(request.getScheme());
            result.setServerName(request.getServerName());
            result.setServerPort(request.getServerPort());

            return result;
        }
    }

    private static final Log LOG = LogFactory.getLog(RequestContext.class);

    private HttpSession _session;
    private HttpServletRequest _request;
    private HttpServletResponse _response;
    private ApplicationServlet _servlet;
    private DecodedRequest _decodedRequest;
    private IMultipartDecoder _decoder;
    private boolean _decoded;

    /**
     * A mapping of the cookies available in the request.
     *
     **/

    private Map _cookieMap;

    /**
     *  Used during {@link #write(IMarkupWriter)}.
     * 
     **/

    private boolean _evenRow;

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
        _servlet = servlet;
        _request = request;
        _response = response;

        // All three parameters may be null if created from
        // AbstractEngine.cleanupEngine().

        if (_request != null && DefaultMultipartDecoder.isMultipartRequest(request))
        {
            IMultipartDecoder decoder = obtainMultipartDecoder(servlet, request);
            setDecoder(decoder);
        }
    }

    /**
     *  Invoked from the constructor to create a {@link DefaultMultipartDecoder} instance.
     *  Applications with specific upload needs may need to override this to
     *  provide a subclass instance instead.  The caller will invoke
     *  {@link IMultipartDecoder#decode(HttpServletRequest)} on the
     *  returned object.
     * 
     *  <p>
     *  This implementation checks for application extension
     *  {@link Tapestry#MULTIPART_DECODER_EXTENSION_NAME}.  If that is not
     *  defined, a shared instance of {@link DefaultMultipartDecoder}
     *  is returned.  
     *
     * 
     *  @see ApplicationServlet#createRequestContext(HttpServletRequest, HttpServletResponse)
     *  @since 3.0
     * 
     **/

    protected IMultipartDecoder obtainMultipartDecoder(
        ApplicationServlet servlet,
        HttpServletRequest request)
        throws IOException
    {
        IApplicationSpecification spec = servlet.getApplicationSpecification();

        if (spec.checkExtension(Tapestry.MULTIPART_DECODER_EXTENSION_NAME))
            return (IMultipartDecoder) spec.getExtension(
                Tapestry.MULTIPART_DECODER_EXTENSION_NAME,
                IMultipartDecoder.class);

        return DefaultMultipartDecoder.getSharedInstance();
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
        if (LOG.isDebugEnabled())
            LOG.debug("Adding cookie " + cookie);

        _response.addCookie(cookie);

        if (_cookieMap == null)
            readCookieMap();

        _cookieMap.put(cookie.getName(), cookie);
    }

    private void datePair(IMarkupWriter writer, String name, long value)
    {
        pair(writer, name, new Date(value));
    }

    /** @since 2.2 **/

    private DecodedRequest getDecodedRequest()
    {
        if (_decodedRequest != null)
            return _decodedRequest;

        IApplicationSpecification spec = _servlet.getApplicationSpecification();
        IRequestDecoder decoder = null;

        if (!spec.checkExtension(Tapestry.REQUEST_DECODER_EXTENSION_NAME))
            decoder = new DefaultRequestDecoder();
        else
            decoder =
                (IRequestDecoder) spec.getExtension(
                    Tapestry.REQUEST_DECODER_EXTENSION_NAME,
                    IRequestDecoder.class);

        _decodedRequest = decoder.decodeRequest(_request);

        return _decodedRequest;
    }

    /** 
     * 
     *  Returns the actual scheme, possibly decoded from the request.
     * 
     *  @see IRequestDecoder
     *  @see javax.servlet.ServletRequest#getScheme()
     *  @since 2.2  
     * 
     **/

    public String getScheme()
    {
        return getDecodedRequest().getScheme();
    }

    /** 
     * 
     *  Returns the actual server name, possibly decoded from the request.
     * 
     *  @see IRequestDecoder
     *  @see javax.servlet.ServletRequest#getServerName()
     *  @since 2.2  
     * 
     **/

    public String getServerName()
    {
        return getDecodedRequest().getServerName();
    }

    /** 
     * 
     *  Returns the actual server port, possibly decoded from the request.
     * 
     *  @see IRequestDecoder
     *  @see javax.servlet.ServletRequest#getServerPort()
     *  @since 2.2  
     * 
     **/

    public int getServerPort()
    {
        return getDecodedRequest().getServerPort();
    }

    /** 
     * 
     *  Returns the actual request URI, possibly decoded from the request.
     * 
     *  @see IRequestDecoder
     *  @see HttpServletRequest#getRequestURI()
     *  @since 2.2  
     * 
     **/

    public String getRequestURI()
    {
        return getDecodedRequest().getRequestURI();
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
        String scheme = getScheme();
        String server = getServerName();
        int port = getServerPort();

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
     * Gets a named {@link Cookie}.
     *
     * @param name The name of the Cookie.
     * @return The Cookie, or null if no Cookie with that
     * name exists.
     *
     **/

    public Cookie getCookie(String name)
    {
        if (_cookieMap == null)
            readCookieMap();

        return (Cookie) _cookieMap.get(name);
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
        IMultipartDecoder decoder = getDecoder();
        if (decoder != null)
            return decoder.getString(_request, name);

        return _request.getParameter(name);
    }

    /**
     *  Convienience method for getting a {@link HttpServletRequest} attribute.
     * 
     *  @since 2.3
     * 
     **/

    public Object getAttribute(String name)
    {
        return _request.getAttribute(name);
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

        IMultipartDecoder decoder = getDecoder();
        if (decoder != null)
            return decoder.getStrings(_request, name);

        return _request.getParameterValues(name);
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
        IMultipartDecoder decoder = getDecoder();
        if (decoder == null)
            return null;

        return decoder.getUploadFile(_request, name);
    }

    /**
     *  Invoked at the end of the request cycle to cleanup and temporary resources.
     *  This is chained to the {@link DefaultMultipartDecoder}, if there is one.
     * 
     *  @since 2.0.1
     **/

    public void cleanup()
    {
        if (_decoder != null)
            _decoder.cleanup(_request);
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
        return _request;
    }

    public HttpServletResponse getResponse()
    {
        return _response;
    }

    private String getRowClass()
    {
        String result;

        result = _evenRow ? "even" : "odd";

        _evenRow = !_evenRow;

        return result;
    }

    public ApplicationServlet getServlet()
    {
        return _servlet;
    }

    /**
     *  Returns the {@link HttpSession}, if necessary, invoking
     * {@link HttpServletRequest#getSession(boolean)}.  However,
     * this method will <em>not</em> create a session.
     *
     **/

    public HttpSession getSession()
    {
        if (_session == null)
            _session = _request.getSession(false);

        return _session;
    }

    /**
     *  Like {@link #getSession()}, but forces the creation of
     *  the {@link HttpSession}, if necessary.
     *
     **/

    public HttpSession createSession()
    {
        if (_session == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Creating HttpSession");

            _session = _request.getSession(true);
        }

        return _session;
    }

    private void header(IMarkupWriter writer, String valueName, String dataName)
    {
        writer.begin("tr");
        writer.attribute("class", "request-context-header");

        writer.begin("th");
        writer.print(valueName);
        writer.end();

        writer.begin("th");
        writer.print(dataName);
        writer.end("tr");

        _evenRow = true;
    }

    private void object(IMarkupWriter writer, String objectName)
    {
        writer.begin("span");
        writer.attribute("class", "request-context-object");
        writer.print(objectName);
        writer.end();
    }

    private void pair(IMarkupWriter writer, String name, int value)
    {
        pair(writer, name, Integer.toString(value));
    }

    private void pair(IMarkupWriter writer, String name, Object value)
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
            writer.println();
            return;
        }

        pair(writer, name, value.toString());
    }

    private void pair(IMarkupWriter writer, String name, String value)
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
        writer.println();
    }

    private void pair(IMarkupWriter writer, String name, boolean value)
    {
        pair(writer, name, value ? "yes" : "no");
    }

    private void readCookieMap()
    {
        _cookieMap = new HashMap();

        Cookie[] cookies = _request.getCookies();

        if (cookies != null)
            for (int i = 0; i < cookies.length; i++)
                _cookieMap.put(cookies[i].getName(), cookies[i]);
    }

    /**
     *  Invokes {@link HttpServletResponse#sendRedirect(String)}</code>, 
     *  but massages <code>path</code>, supplying missing elements to
     *  make it an absolute URL (i.e., specifying scheme, server, port, etc.).
     *
     *  <p>The 2.2 Servlet API will do this automatically, and a little more,
     *  according to the early documentation.
     *
     **/

    public void redirect(String path) throws IOException
    {
        // Now a little magic to convert path into a complete URL. The Servlet
        // 2.2 API does this automatically.

        String absolutePath = getAbsoluteURL(path);

        String encodedURL = _response.encodeRedirectURL(absolutePath);

        _response.sendRedirect(encodedURL);
    }

    private void section(IMarkupWriter writer, String sectionName)
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

    public void write(IMarkupWriter writer)
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

        List parameters = getSorted(_request.getParameterNames());
        int count = parameters.size();

        for (int i = 0; i < count; i++)
        {

            if (i == 0)
            {
                section(writer, "Parameters");
                header(writer, "Name", "Value(s)");
            }

            String name = (String) parameters.get(i);
            String[] values = _request.getParameterValues(name);

            writer.begin("tr");
            writer.attribute("class", getRowClass());
            writer.begin("th");
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

        pair(writer, "authType", _request.getAuthType());
        pair(writer, "characterEncoding", _request.getCharacterEncoding());
        pair(writer, "contentLength", _request.getContentLength());
        pair(writer, "contentType", _request.getContentType());
        pair(writer, "method", _request.getMethod());
        pair(writer, "pathInfo", _request.getPathInfo());
        pair(writer, "pathTranslated", _request.getPathTranslated());
        pair(writer, "protocol", _request.getProtocol());
        pair(writer, "queryString", _request.getQueryString());
        pair(writer, "remoteAddr", _request.getRemoteAddr());
        pair(writer, "remoteHost", _request.getRemoteHost());
        pair(writer, "remoteUser", _request.getRemoteUser());
        pair(writer, "requestedSessionId", _request.getRequestedSessionId());
        pair(writer, "requestedSessionIdFromCookie", _request.isRequestedSessionIdFromCookie());
        pair(writer, "requestedSessionIdFromURL", _request.isRequestedSessionIdFromURL());
        pair(writer, "requestedSessionIdValid", _request.isRequestedSessionIdValid());
        pair(writer, "requestURI", _request.getRequestURI());
        pair(writer, "scheme", _request.getScheme());
        pair(writer, "serverName", _request.getServerName());
        pair(writer, "serverPort", _request.getServerPort());
        pair(writer, "contextPath", _request.getContextPath());
        pair(writer, "servletPath", _request.getServletPath());

        // Now deal with any headers

        List headers = getSorted(_request.getHeaderNames());
        count = headers.size();

        for (int i = 0; i < count; i++)
        {
            if (i == 0)
            {
                section(writer, "Headers");
                header(writer, "Name", "Value");
            }

            String name = (String) headers.get(i);
            String value = _request.getHeader(name);

            pair(writer, name, value);
        }

        // Attributes

        List attributes = getSorted(_request.getAttributeNames());
        count = attributes.size();

        for (int i = 0; i < count; i++)
        {
            if (i == 0)
            {
                section(writer, "Attributes");
                header(writer, "Name", "Value");
            }

            String name = (String) attributes.get(i);

            pair(writer, name, _request.getAttribute(name));
        }

        // Cookies ...

        Cookie[] cookies = _request.getCookies();

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

        pair(writer, "servlet", _servlet);
        pair(writer, "name", _servlet.getServletName());
        pair(writer, "servletInfo", _servlet.getServletInfo());

        ServletConfig config = _servlet.getServletConfig();

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

    private void writeSystemProperties(IMarkupWriter writer)
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

            if (property != null && property.indexOf(pathSeparator) > 0 && name.endsWith(".path"))
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
            else
            {
                pair(writer, name, property);
            }
        }

        writer.end(); // System Properties
    }

    /**
     *  Invokes {@link #write(IMarkupWriter)}, which is used for debugging.
     *  Does nothing if the cycle is rewinding.
     *
     **/

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!cycle.isRewinding())
            write(writer);
    }

    /**
     *  Returns the multipart decoder and lazily decodes the request parameters.
     *  This allows both for this operation to be performed only when really needed
     *  and for opening the request for reading much later, so that the Engine can
     *  have a chance to set the encoding that the request needs to use.
     * 
     *  @return the multipart decoder or null if not needed for this request
     *  @since 3.0
     **/
    private IMultipartDecoder getDecoder()
    {
        if (_decoder != null && !_decoded) {
            _decoder.decode(_request);
            _decoded = true;
        }

        return _decoder;
    }

    /**
     *  Sets the multipart decoder to be used for the request.
     * 
     *  @param decoder the multipart decoder
     *  @since 3.0
     **/
    public void setDecoder(IMultipartDecoder decoder)
    {
        _decoder = decoder;
        _decoded = false;
    }

}