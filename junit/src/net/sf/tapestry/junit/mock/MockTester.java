/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.junit.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import junit.framework.AssertionFailedError;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.ApplicationServlet;
import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.resource.ClasspathResourceLocation;
import net.sf.tapestry.util.xml.DocumentParseException;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *  A complex class that reads an XML description
 *  of a test involving the Mock objects and executes it, pretending
 *  to be a running servlet container.
 * 
 *  <p>
 *  The XML format is pretty simple, it contains declarations
 *  similar to a web.xml deployment descriptor, a description
 *  of the active HttpSession (if any),
 *  a description of the HttpRequest,
 *  and then a set of expectations
 *  for the output stream from the 
 *  request.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockTester
{
    private static final Log LOG = LogFactory.getLog(MockTester.class);

    private IResourceLocation _resourceLocation;
    private Document _document;
    private MockContext _context;
    private String _servletName;
    private ApplicationServlet _servlet;
    private MockRequest _request;
    private MockResponse _response;
    private Map _ognlContext;

    /**
     *  Shared cache of compiled patterns.
     * 
     **/

    private static Map _patternCache = new HashMap();

    private PatternMatcher _matcher;
    private PatternCompiler _compiler;

    /**
     *  Constructs a new MockTester for the given resource path
     *  (which is the XML file to read).
     * 
     **/

    public MockTester(String resourcePath)
        throws JDOMException, ServletException, DocumentParseException, IOException
    {
        IResourceResolver resolver = new DefaultResourceResolver();

        _resourceLocation = new ClasspathResourceLocation(resolver, resourcePath);

        parse();

        setup();
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("MockTester[");

        if (_document != null)
            buffer.append(_document);

        buffer.append(']');

        return buffer.toString();
    }

    /**
     *  Invoked to execute the request cycle.
     * 
     **/

    public void execute() throws IOException, ServletException, DocumentParseException
    {
        Element root = _document.getRootElement();

        List l = root.getChildren("request");
        int count = l.size();

        for (int i = 0; i < count; i++)
        {
            Element request = (Element) l.get(i);

            executeRequest(request);
        }
    }

    private void executeRequest(Element request)
        throws IOException, ServletException, DocumentParseException
    {
        Cookie[] oldRequestCookies = (_request == null ? null : _request.getCookies());

        _request = new MockRequest(_context, "/" + _servlet.getServletName());

        _request.addCookies(oldRequestCookies);

        if (_response != null)
            _request.addCookies(_response.getCookies());

        setupRequest(request);

        _response = new MockResponse(_request);

        _servlet.service(_request, _response);

        _response.end();

        executeAssertions(request);
    }

    private void parse() throws JDOMException, DocumentParseException, IOException
    {
        SAXBuilder builder = new SAXBuilder();

        URL resourceURL = _resourceLocation.getResourceURL();

        InputStream stream = resourceURL.openStream();

        if (stream == null)
            throw new DocumentParseException(
                "Mock test script file " + _resourceLocation + " does not exist.",
                _resourceLocation);

        _document = builder.build(stream);

        stream.close();
    }

    private void setup() throws ServletException, DocumentParseException
    {
        Element root = _document.getRootElement();

        if (!root.getName().equals("mock-test"))
            throw new DocumentParseException(
                "Root element must be 'mock-test'.",
                _resourceLocation);

        setupContext(root);
        setupServlet(root);
    }

    private void setupContext(Element parent)
    {
        _context = new MockContext();

        Element context = parent.getChild("context");

        if (context == null)
            return;

        String name = context.getAttributeValue("name");

        if (name != null)
            _context.setServletContextName(name);

        String root = context.getAttributeValue("root");

        if (root != null)
            _context.setRootDirectory(root);

        setInitParameters(context, _context);
    }

    private void setupServlet(Element parent) throws ServletException
    {
        Element servlet = parent.getChild("servlet");

        String className = servlet.getAttributeValue("class");
        _servletName = servlet.getAttributeValue("name");

        _servlet = createServlet(className);

        MockServletConfig config = new MockServletConfig(_servletName, _context);

        setInitParameters(servlet, config);

        _servlet.init(config);
    }

    private void setupRequest(Element request)
    {
        String method = request.getAttributeValue("method");
        if (method != null)
            _request.setMethod(method);

        // It's really just the language from the locale.

        String locale = request.getAttributeValue("locale");
        if (locale != null)
            _request.setLocale(new Locale(locale, "", ""));

        List parameters = request.getChildren("parameter");
        int count = parameters.size();

        for (int i = 0; i < count; i++)
        {
            Element parameter = (Element) parameters.get(i);

            setRequestParameter(parameter);
        }

        String failover = request.getAttributeValue("failover");

        if (failover != null && failover.equals("true"))
            _request.simulateFailover();

        // TBD: Headers, etc., etc.
    }

    private void setRequestParameter(Element parameter)
    {
        List values = new ArrayList();

        String name = parameter.getAttributeValue("name");

        String value = parameter.getAttributeValue("value");
        if (value != null)
            values.add(value);

        List children = parameter.getChildren("value");
        int count = children.size();
        for (int i = 0; i < count; i++)
        {
            Element e = (Element) children.get(i);
            value = e.getTextTrim();

            values.add(value);
        }

        String[] array = (String[]) values.toArray(new String[values.size()]);

        _request.setParameter(name, array);
    }

    private void setInitParameters(Element parent, IInitParameterHolder holder)
    {
        List children = parent.getChildren("init-parameter");

        int count = children.size();
        for (int i = 0; i < count; i++)
        {
            Element e = (Element) children.get(i);

            String name = e.getAttributeValue("name");
            String value = e.getAttributeValue("value");

            holder.setInitParameter(name, value);
        }
    }

    private ApplicationServlet createServlet(String className)
    {
        Throwable t = null;
        try
        {
            Class servletClass = Class.forName(className);

            return (ApplicationServlet) servletClass.newInstance();
        }
        catch (ClassNotFoundException ex)
        {
            t = ex;
        }
        catch (InstantiationException ex)
        {
            t = ex;
        }
        catch (IllegalAccessException ex)
        {
            t = ex;
        }

        // Just a convient wrapper to percolate to the top and
        // mark this test as an error.

        throw new ApplicationRuntimeException(
            "Unable to instantiate servlet class " + className + ".",
            t);
    }

    public MockContext getContext()
    {
        return _context;
    }

    public MockRequest getRequest()
    {
        return _request;
    }

    public MockResponse getResponse()
    {
        return _response;
    }

    public ApplicationServlet getServlet()
    {
        return _servlet;
    }

    private void executeAssertions(Element request) throws DocumentParseException
    {
        executeOutputAssertions(request);
        executeRegexpAssertions(request);
        executeExpressionAssertions(request);
        executeOutputMatchesAssertions(request);
        executeCookieAssertions(request);
    }

    /**
     *  Handles &lt;assert&gt; elements inside &lt;request&gt;.
     *  Each assertion is in the form of a boolean expression
     *  which must be true.
     * 
     **/

    private void executeExpressionAssertions(Element request) throws DocumentParseException
    {
        List l = request.getChildren("assert");
        int count = l.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) l.get(i);

            String name = a.getAttributeValue("name");
            String expression = a.getAttributeValue("expression");

            checkExpression(name, expression);
        }
    }

    private void checkExpression(String name, String expression) throws DocumentParseException
    {

        boolean result = evaluate(expression);

        if (result)
            return;

        throw new AssertionFailedError(name + ": Expression '" + expression + "' was not true.");

    }

    private boolean evaluate(String expression) throws DocumentParseException
    {
        if (_ognlContext == null)
            _ognlContext = Ognl.createDefaultContext(this);

        Object value = null;

        try
        {
            value = Ognl.getValue(expression, _ognlContext, this);
        }
        catch (OgnlException ex)
        {
            throw new DocumentParseException("Expression '" + expression + "' is not valid.", ex);
        }

        if (value == null)
            return false;

        if (value instanceof Boolean)
            return ((Boolean) value).booleanValue();

        if (value instanceof Number)
            return ((Number) value).longValue() != 0;

        if (value instanceof String)
            return ((String) value).length() > 0;

        throw new DocumentParseException(
            "Expression '"
                + expression
                + "' evaluates to ("
                + value.getClass().getName()
                + ") "
                + value
                + ", which cannot be interpreted as a boolean.");
    }

    /**
     *  Handles &lt;assert-regexp&gt; elements inside &lt;request&gt;.
     *  Checks that a regular expression appears in the output.
     *  Content of element is the regular expression.
     * 
     *  <p>
     *  Attribute name is used in error messages.
     * 
     **/

    private void executeRegexpAssertions(Element request) throws DocumentParseException
    {
        String outputString = null;

        List assertions = request.getChildren("assert-regexp");
        int count = assertions.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) assertions.get(i);

            String name = a.getAttributeValue("name");
            String pattern = a.getTextTrim();

            if (outputString == null)
                outputString = _response.getOutputString();

            matchRegexp(name, outputString, pattern);
        }

    }

    /**
         *  Handles &lt;assert-output&gt; elements inside &lt;request&gt;.
         *  Checks that a substring appears in the output.
         *  Content of element is the substring to search for.
         *  <p>
         *  Attribute name is used in error messages.
         * 
         **/

    private void executeOutputAssertions(Element request) throws DocumentParseException
    {
        String outputString = null;

        List assertions = request.getChildren("assert-output");
        int count = assertions.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) assertions.get(i);

            String name = a.getAttributeValue("name");
            String substring = a.getTextTrim();

            if (outputString == null)
                outputString = _response.getOutputString();

            matchSubstring(name, outputString, substring);
        }

    }

    private PatternMatcher getMatcher()
    {
        if (_matcher == null)
            _matcher = new Perl5Matcher();

        return _matcher;
    }

    private Pattern compile(String pattern) throws DocumentParseException
    {
        Pattern result = (Pattern) _patternCache.get(pattern);

        if (result != null)
            return result;

        if (_compiler == null)
            _compiler = new Perl5Compiler();

        try
        {
            result = _compiler.compile(pattern, Perl5Compiler.MULTILINE_MASK);

        }
        catch (MalformedPatternException ex)
        {
            throw new DocumentParseException(
                "Malformed regular expression: " + pattern,
                _resourceLocation,
                ex);
        }

        _patternCache.put(pattern, result);

        return result;
    }

    private void matchRegexp(String name, String text, String pattern)
        throws DocumentParseException
    {
        Pattern compiled = compile(pattern);

        if (getMatcher().contains(text, compiled))
            return;

        System.err.println(text);

        throw new AssertionFailedError(
            name + ": Response does not contain regular expression '" + pattern + "'.");
    }

    private void matchSubstring(String name, String text, String substring)
    {
        if (text.indexOf(substring) >= 0)
            return;

        System.err.println(text);

        throw new AssertionFailedError(
            name + ": Response does not contain string '" + substring + "'.");
    }

    private void executeOutputMatchesAssertions(Element request) throws DocumentParseException
    {
        List l = request.getChildren("assert-output-matches");
        int count = l.size();
        String outputString = null;

        for (int i = 0; i < count; i++)
        {
            Element e = (Element) l.get(i);

            if (outputString == null)
                outputString = _response.getOutputString();

            executeOutputMatchAssertion(e, outputString);
        }

    }

    private void executeOutputMatchAssertion(Element element, String outputString)
        throws DocumentParseException
    {
        String name = element.getAttributeValue("name");
        String value = element.getAttributeValue("subgroup");
        int subgroup = (value == null) ? 0 : Integer.parseInt(value);

        String pattern = element.getTextTrim();

        PatternMatcherInput input = new PatternMatcherInput(outputString);

        PatternMatcher matcher = getMatcher();
        Pattern compiled = compile(pattern);

        List l = element.getChildren("match");
        int count = l.size();
        int i = 0;

        while (matcher.contains(input, compiled))
        {
            MatchResult match = matcher.getMatch();

            if (i >= count)
            {
                System.err.println(outputString);
                throw new AssertionFailedError(name + ": Too many matches for '" + pattern + "'.");
            }

            Element e = (Element) l.get(i);
            String expected = e.getTextTrim();
            String actual = match.group(subgroup);

            if (!actual.equals(expected))
            {
                System.err.println(outputString);
                throw new AssertionFailedError(
                    name + "[" + i + "]: Expected '" + expected + "' but got '" + actual + "'.");
            }

            i++;
        }

        if (i < count)
        {
            System.err.println(outputString);
            throw new AssertionFailedError(
                name
                    + ": Too few matches for '"
                    + pattern
                    + "' (expected "
                    + count
                    + " but got "
                    + i
                    + ").");
        }
    }

    private void executeCookieAssertions(Element request)
    {
        List l = request.getChildren("assert-cookie");
        int count = l.size();

        for (int i = 0; i < count; i++)
        {
            Element assertion = (Element) l.get(i);

            executeCookieAssertion(assertion);
        }
    }

    private void executeCookieAssertion(Element assertion)
    {
        String name = assertion.getAttributeValue("name");
        String value = assertion.getAttributeValue("value");

        Cookie[] cookies = _response.getCookies();

        for (int i = 0; i < cookies.length; i++)
        {
            if (!cookies[i].getName().equals(name))
                continue;

            if (cookies[i].getValue().equals(value))
                return;

            throw new AssertionFailedError(
                "Response cookie '"
                    + name
                    + "': expected '"
                    + value
                    + "', but was '"
                    + cookies[i].getValue()
                    + "'.");
        }

        throw new AssertionFailedError("Could not find cookie named '" + name + "' in response.");
    }

}
