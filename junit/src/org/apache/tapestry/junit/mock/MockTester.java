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

package org.apache.tapestry.junit.mock;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import junit.framework.AssertionFailedError;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.xml.DocumentParseException;
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
    private String _path;
    private Document _document;
    private MockContext _context;
    private String _servletName;
    private ApplicationServlet _servlet;
    private MockRequest _request;
    private MockResponse _response;
    private int _requestNumber = 0;
    private Map _ognlContext;
    private Throwable _exception;

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

    public MockTester(String path)
        throws JDOMException, ServletException, DocumentParseException, IOException
    {
        _path = path;

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

            _requestNumber = i + 1;

            executeRequest(request);
        }
    }

    private void executeRequest(Element request)
        throws IOException, ServletException, DocumentParseException
    {
        Cookie[] oldRequestCookies = (_request == null ? null : _request.getCookies());

        _request = new MockRequest(_context, "/" + _servlet.getServletName());

        String contentType = request.getAttributeValue("content-type");
        if (contentType != null)
            _request.setContentType(contentType);

        String contentPath = request.getAttributeValue("content-path");
        if (contentPath != null)
            _request.setContentPath(contentPath);

        _request.addCookies(oldRequestCookies);

        if (_response != null)
            _request.addCookies(_response.getCookies());

        setupRequest(request);

        _exception = null;

        _response = new MockResponse(_request);

        try
        {
            _servlet.service(_request, _response);
        }
        catch (ServletException ex)
        {
            _exception = ex;
        }
        catch (IOException ex)
        {
            _exception = ex;
        }

        _response.end();

        executeAssertions(request);
    }

    private void parse() throws JDOMException, DocumentParseException, IOException
    {
        SAXBuilder builder = new SAXBuilder();

        _document = builder.build(_path);
    }

    private void setup() throws ServletException
    {
        Element root = _document.getRootElement();

        if (!root.getName().equals("mock-test"))
            throw new RuntimeException("Root element of " + _path + " must be 'mock-test'.");

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
        executeNoOutputAssertions(request);
        executeRegexpAssertions(request);
        executeExpressionAssertions(request);
        executeOutputMatchesAssertions(request);
        executeCookieAssertions(request);
        executeOutputStreamAssertions(request);
        executeExceptionAssertions(request);
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
            String expression = a.getTextTrim();

            checkExpression(name, expression);
        }
    }

    private void checkExpression(String name, String expression) throws DocumentParseException
    {

        boolean result = evaluate(expression);

        if (result)
            return;

        throw new AssertionFailedError(
            buildTestName(name) + ": Expression '" + expression + "' was not true.");

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

            if (Tapestry.isBlank(pattern))
                throw new DocumentParseException("Pattern is null in " + a);

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

            if (Tapestry.isBlank(substring))
                throw new DocumentParseException("Substring is null in " + a);

            if (outputString == null)
                outputString = _response.getOutputString();

            matchSubstring(name, outputString, substring);
        }

    }

    /**
         *  Handles &lt;assert-no-output&gt; elements inside &lt;request&gt;.
         *  Checks that a substring appears in the output.
         *  Content of element is the substring to search for.
         *  <p>
         *  Attribute name is used in error messages.
         * 
         **/

    private void executeNoOutputAssertions(Element request) throws DocumentParseException
    {
        String outputString = null;

        List assertions = request.getChildren("assert-no-output");
        int count = assertions.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) assertions.get(i);

            String name = a.getAttributeValue("name");
            String substring = a.getTextTrim();

            if (Tapestry.isBlank(substring))
                throw new DocumentParseException("Substring is null in " + a);

            if (outputString == null)
                outputString = _response.getOutputString();

            matchNoSubstring(name, outputString, substring);
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
            throw new ApplicationRuntimeException(
                "Malformed regular expression: " + pattern + " in " + _path + ".",
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
            buildTestName(name)
                + ": Response does not contain regular expression '"
                + pattern
                + "'.");
    }

    private void matchSubstring(String name, String text, String substring)
    {
        if (text == null)
            throw new AssertionFailedError(buildTestName(name) + " : Response is null.");

        if (text.indexOf(substring) >= 0)
            return;

        System.err.println(text);

        throw new AssertionFailedError(
            buildTestName(name) + ": Response does not contain string '" + substring + "'.");
    }

    private void matchNoSubstring(String name, String text, String substring)
    {
        if (text == null)
            throw new AssertionFailedError(buildTestName(name) + " : Response is null.");

        if (text.indexOf(substring) < 0)
            return;

        System.err.println(text);

        throw new AssertionFailedError(
            buildTestName(name) + ": Response contains string '" + substring + "'.");
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

        if (Tapestry.isBlank(pattern))
            throw new DocumentParseException("Pattern is null in " + element);

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
                throw new AssertionFailedError(
                    buildTestName(name) + ": Too many matches for '" + pattern + "'.");
            }

            Element e = (Element) l.get(i);
            String expected = e.getTextTrim();
            String actual = match.group(subgroup);

            if (!actual.equals(expected))
            {
                System.err.println(outputString);
                throw new AssertionFailedError(
                    buildTestName(name)
                        + "["
                        + i
                        + "]: Expected '"
                        + expected
                        + "' but got '"
                        + actual
                        + "'.");
            }

            i++;
        }

        if (i < count)
        {
            System.err.println(outputString);
            throw new AssertionFailedError(
                buildTestName(name)
                    + ": Too few matches for '"
                    + pattern
                    + "' (expected "
                    + count
                    + " but got "
                    + i
                    + ").");
        }
    }

    private void executeExceptionAssertions(Element request)
    {
        List l = request.getChildren("assert-exception");
        int count = l.size();

        for (int i = 0; i < count; i++)
        {
            Element assertion = (Element) l.get(i);

            executeExceptionAssertion(assertion);
        }

    }

    private void executeExceptionAssertion(Element assertion)
    {
        String name = assertion.getAttributeValue("name");
        String value = assertion.getTextTrim();

        if (_exception == null)
            throw new AssertionFailedError(buildTestName(name) + " no exception thrown.");
        String message = _exception.getMessage();

        if (message.indexOf(value) >= 0)
            return;

        throw new AssertionFailedError(
            buildTestName(name)
                + " exception message ("
                + message
                + ") does not contain '"
                + value
                + "'.");
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
                buildTestName(name)
                    + ": Response cookie '"
                    + name
                    + "': expected '"
                    + value
                    + "', but was '"
                    + cookies[i].getValue()
                    + "'.");
        }

        throw new AssertionFailedError(
            buildTestName(name) + ": Could not find cookie named '" + name + "' in response.");
    }

    private String buildTestName(String name)
    {
        return "Request #" + _requestNumber + "/" + name;
    }

    private void executeOutputStreamAssertions(Element request) throws DocumentParseException
    {
        List l = request.getChildren("assert-output-stream");
        int count = l.size();

        for (int i = 0; i < count; i++)
        {
            Element assertion = (Element) l.get(i);

            executeOutputStreamAssertion(assertion);
        }

    }

    private void executeOutputStreamAssertion(Element element) throws DocumentParseException
    {
        String name = element.getAttributeValue("name");
        String contentType = element.getAttributeValue("content-type");
        String path = element.getAttributeValue("path");

        String actualContentType = _response.getContentType();

        if (!contentType.equals(actualContentType))
            throw new AssertionFailedError(
                buildTestName(name)
                    + " content-type was '"
                    + actualContentType
                    + "', expected '"
                    + contentType
                    + "'.");

        byte[] actualContent = _response.getResponseBytes();
        byte[] expectedContent = getFileContent(path);

        if (actualContent.length != expectedContent.length)
            throw new AssertionFailedError(
                buildTestName(name)
                    + " actual length of "
                    + actualContent.length
                    + " bytes does not match expected length of "
                    + expectedContent.length
                    + " bytes.");

        for (int i = 0; i < actualContent.length; i++)
        {
            if (actualContent[i] != expectedContent[i])
                throw new AssertionFailedError(
                    buildTestName(name) + " content mismatch at index + " + i + ".");

        }
    }

    private byte[] getFileContent(String path)
    {
        try
        {
            InputStream in = new FileInputStream(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1000];

            while (true)
            {
                int length = in.read(buffer);
                if (length < 0)
                    break;

                out.write(buffer, 0, length);
            }

            in.close();
            out.close();

            return out.toByteArray();
        }
        catch (FileNotFoundException ex)
        {
            throw new ApplicationRuntimeException("File '" + path + "' not found.", ex);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException("Unable to read file '" + path + "'.", ex);
        }
    }

}
