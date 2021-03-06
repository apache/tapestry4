// Copyright 2004, 2005 The Apache Software Foundation
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

import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.oro.text.regex.*;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.test.mock.*;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.beans.Introspector;
import java.io.*;
import java.util.*;

/**
 * A complex class that reads an XML description of a test involving the Mock objects and executes
 * it, pretending to be a running servlet container.
 * <p>
 * The XML format is pretty simple, it contains declarations similar to a web.xml deployment
 * descriptor, a description of the active HttpSession (if any), a description of the HttpRequest,
 * and then a set of expectations for the output stream from the request.
 *
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class TestMockApplications
{
    public static final String LOGS_DIR = "target/logs";

    public static final String DEFAULT_BASE_DIR = "./";

    public static final String SCRIPTS_DIR = "src/scripts";

    private static String _baseDir;

    private String _testRootDirectory;

    private String _path;

    private String _fileName;

    private Document _document;

    private MockContext _context;

    private String _servletName;

    private ApplicationServlet _servlet;

    private MockRequest _request;

    private MockResponse _response;

    private int _requestNumber = 0;

    private Map _ognlContext = Ognl.createDefaultContext(this);

    private Throwable _exception;

    /**
     * Shared cache of compiled patterns.
     */

    private static Map _patternCache = new HashMap();

    private PatternMatcher _matcher = new Perl5Matcher();

    private PatternCompiler _compiler = new Perl5Compiler();

    private PrintStream _savedOut;

    private PrintStream _savedErr;

    private SAXBuilder _builder = new SAXBuilder();

    /**
     * Closes System.out and System.err, then restores them to their original values.
     */
    @AfterMethod
    public void tearDown() throws Exception
    {
        System.err.close();
        System.setErr(_savedErr);

        System.out.close();
        System.setOut(_savedOut);

        _requestNumber = 0;
        _request = null;
        _response = null;
    }

    @DataProvider(name = "mockTestScripts")
    public Object[][] createTestParameters()
    {
        List data = new ArrayList();

        File scriptsDir = new File(getBaseDirectory() + SCRIPTS_DIR);

        String[] names = scriptsDir.list();

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];

            if (name.endsWith(".xml"))
            {
                data.add(new Object[] {
                  getBaseDirectory() + "/src/test-data/",
                  getBaseDirectory() + SCRIPTS_DIR + "/" + name,
                  name
                });
            }
        }

        return (Object[][])data.toArray(new Object[data.size()][3]);
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
     * Invoked to execute the request cycle.
     */
    @Test(dataProvider = "mockTestScripts", enabled = false)
    public void execute(String testRootDirectory, String path, String fileName)
      throws Exception
    {
        _testRootDirectory = testRootDirectory;
        _path = path;
        _fileName = fileName;

        // setup and get environment ready
        createLogs();
        parse();
        setup();

        Element root = _document.getRootElement();

        List l = root.getChildren("request");
        int count = l.size();

        for (int i = 0; i < count; i++)
        {
            Element request = (Element) l.get(i);

            _requestNumber = i + 1;

            executeRequest(request);
        }

        _servlet.destroy();

        PropertyUtils.clearCache();
        OgnlRuntime.clearCache();
        Introspector.flushCaches();
    }

    private void executeRequest(Element request) throws IOException, DocumentParseException
    {
        Cookie[] oldRequestCookies = (_request == null ? null : _request.getCookies());

        _request = new MockRequest(_context, "/" + _servlet.getServletName());

        String contentType = request.getAttributeValue("content-type");
        if (contentType != null)
            _request.setContentType(contentType);

        String contentPath = request.getAttributeValue("content-path");
        if (contentPath != null)
            _request.setContentPath(_testRootDirectory + contentPath);

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

        System.out.println("=== Response #" + _requestNumber + " ===\n\n");
        System.out.println(_response.getOutputString());
        System.out.println("\n\n");

        executeAssertions(request);
    }

    private void parse()
      throws Exception
    {
        _document = _builder.build(_path);
    }

    private void setup() throws ServletException
    {
        Element root = _document.getRootElement();

        if (!root.getName().equals("mock-test"))
            throw new RuntimeException("Root element of " + _path + " must be 'mock-test'.");

        System.setProperty("org.apache.tapestry.disable-caching", "false");

        setupContext(root);
        setupServlet(root);
    }

    private void setupContext(Element parent)
    {
        _context = new MockContext(_testRootDirectory);

        Element context = parent.getChild("context");

        if (context == null)
            return;

        String name = context.getAttributeValue("name");

        if (name != null)
            _context.setServletContextName(name);

        String root = context.getAttributeValue("root");

        if (root != null)
            _context.setRootDirectory(_testRootDirectory + root);

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

    private void setInitParameters(Element parent, InitParameterHolder holder)
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

        throw new ApplicationRuntimeException("Unable to instantiate servlet class " + className
                                              + ".", t);
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
     * Handles &lt;assert&gt; elements inside &lt;request&gt;. Each assertion is in the form of a
     * boolean expression which must be true.
     */

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

        throw new AssertionError(buildTestName(name) + ": Expression '" + expression
                                 + "' was not true.");

    }

    private boolean evaluate(String expression) throws DocumentParseException
    {
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

        throw new DocumentParseException("Expression '" + expression + "' evaluates to ("
                                         + value.getClass().getName() + ") " + value
                                         + ", which cannot be interpreted as a boolean.");
    }

    /**
     * Handles &lt;assert-regexp&gt; elements inside &lt;request&gt;. Checks that a regular
     * expression appears in the output. Content of element is the regular expression.
     * <p>
     * Attribute name is used in error messages.
     */

    private void executeRegexpAssertions(Element request)
      throws DocumentParseException
    {
        String outputString = null;

        List assertions = request.getChildren("assert-regexp");
        int count = assertions.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) assertions.get(i);

            String name = a.getAttributeValue("name");
            String pattern = a.getTextTrim();

            if (HiveMind.isBlank(pattern))
                throw new DocumentParseException("Pattern is null in " + a);

            if (outputString == null)
                outputString = _response.getOutputString();

            matchRegexp(name, outputString, pattern);
        }

    }

    /**
     * Handles &lt;assert-output&gt; elements inside &lt;request&gt;. Checks that a substring
     * appears in the output. Content of element is the substring to search for.
     * <p>
     * Attribute name is used in error messages.
     */

    private void executeOutputAssertions(Element request)
      throws DocumentParseException
    {
        String outputString = null;

        List assertions = request.getChildren("assert-output");
        int count = assertions.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) assertions.get(i);

            String name = a.getAttributeValue("name");
            String substring = a.getTextTrim();

            if (HiveMind.isBlank(substring))
                throw new DocumentParseException("Substring is null in " + a);

            if (outputString == null)
                outputString = _response.getOutputString();

            matchSubstring(name, outputString, substring);
        }

    }

    /**
     * Handles &lt;assert-no-output&gt; elements inside &lt;request&gt;. Checks that a substring
     * does not appear in the output. Content of element is the substring to search for.
     * <p>
     * Attribute name is used in error messages.
     */

    private void executeNoOutputAssertions(Element request)
      throws DocumentParseException
    {
        String outputString = null;

        List assertions = request.getChildren("assert-no-output");
        int count = assertions.size();

        for (int i = 0; i < count; i++)
        {
            Element a = (Element) assertions.get(i);

            String name = a.getAttributeValue("name");
            String substring = a.getTextTrim();

            if (HiveMind.isBlank(substring))
                throw new DocumentParseException("Substring is null in " + a, (Resource) null);

            if (outputString == null)
                outputString = _response.getOutputString();

            matchNoSubstring(name, outputString, substring);
        }

    }

    private PatternMatcher getMatcher()
    {
        return _matcher;
    }

    private Pattern compile(String pattern)
      throws DocumentParseException
    {
        Pattern result = (Pattern) _patternCache.get(pattern);

        if (result != null)
            return result;

        try
        {
            result = _compiler.compile(pattern, Perl5Compiler.MULTILINE_MASK);

        }
        catch (MalformedPatternException ex)
        {
            throw new ApplicationRuntimeException("Malformed regular expression: " + pattern
                                                  + " in " + _path + ".", ex);
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

        throw new AssertionError(buildTestName(name)
                                 + ": Response does not contain regular expression '" + pattern + "'.");
    }

    private void matchSubstring(String name, String text, String substring)
    {
        if (text == null)
            throw new AssertionError(buildTestName(name) + " : Response is null.");

        if (text.indexOf(substring) >= 0)
            return;

        System.err.println(text);

        throw new AssertionError(buildTestName(name) + ":" + text + "\n Response does not contain string '"
                                 + substring + "'.");
    }

    private void matchNoSubstring(String name, String text, String substring)
    {
        if (text == null)
            throw new AssertionError(buildTestName(name) + " : Response is null.");

        if (text.indexOf(substring) < 0)
            return;

        System.err.println(text);

        throw new AssertionError(buildTestName(name) + ": Response contains string '"
                                 + substring + "'.");
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

        if (HiveMind.isBlank(pattern))
            throw new DocumentParseException("Pattern is null in " + element);

        PatternMatcherInput input = new PatternMatcherInput(outputString);

        PatternMatcher matcher = getMatcher();
        Pattern compiled = compile(pattern);

        List<Element> l = element.getChildren("match");
        int count = l.size();
        int i = 0;

        while (matcher.contains(input, compiled))
        {
            MatchResult match = matcher.getMatch();
            String actual = match.group(subgroup);

            boolean matched = contentContains(l, actual);

            if (i >= count)
            {
                System.err.println(outputString);
                throw new AssertionError(buildTestName(name) + ": Too many matches for '"
                                         + pattern + "'.");
            }

            if (!matched) {
                System.err.println(outputString);
                throw new AssertionError(buildTestName(name) + ": No expected match found for "
                                         + "output of '" + actual + "'. ");
            }

            i++;
        }

        if (i < count)
        {
            System.err.println(outputString);
            throw new AssertionError(buildTestName(name) + ": Too few matches for '"
                                     + pattern + "' (expected " + count + " but got " + i + ").");
        }
    }

    private boolean contentContains(List<Element> elements, String text)
    {
        for (Element e : elements) {
            if (e.getTextTrim().equals(text))
                return true;
        }

        return false;
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
            throw new AssertionError(buildTestName(name) + " no exception thrown.");
        String message = _exception.getMessage();

        if (message.indexOf(value) >= 0)
            return;

        throw new AssertionError(buildTestName(name) + " exception message (" + message
                                 + ") does not contain '" + value + "'.");
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

            throw new AssertionError(buildTestName(name) + ": Response cookie '" + name
                                     + "': expected '" + value + "', but was '" + cookies[i].getValue() + "'.");
        }

        throw new AssertionError(buildTestName(name) + ": Could not find cookie named '"
                                 + name + "' in response.");
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
            throw new AssertionError(buildTestName(name) + " content-type was '"
                                     + actualContentType + "', expected '" + contentType + "'.");

        byte[] actualContent = _response.getResponseBytes();
        byte[] expectedContent = getFileContent(getBaseDirectory() + "/" + path);

        if (actualContent.length != expectedContent.length)
            throw new AssertionError(buildTestName(name) + " actual length of "
                                     + actualContent.length + " bytes does not match expected length of "
                                     + expectedContent.length + " bytes.");

        for (int i = 0; i < actualContent.length; i++)
        {
            if (actualContent[i] != expectedContent[i])
                throw new AssertionError(buildTestName(name)
                                         + " content mismatch at index + " + i + ".");

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

    private void createLogs()
      throws Exception
    {
        File outDir = new File(getBaseDirectory() + LOGS_DIR);

        if (!outDir.isDirectory())
            outDir.mkdirs();

        _savedOut = System.out;
        _savedErr = System.err;

        System.setOut(createPrintStream(outDir.getPath() + "/" + _fileName, "out"));
        System.setErr(createPrintStream(outDir.getPath() + "/" + _fileName, "err"));
    }

    private PrintStream createPrintStream(String path, String extension) throws Exception
    {
        File file = new File(path + "." + extension);

        // Open and truncate file.

        FileOutputStream fos = new FileOutputStream(file);

        BufferedOutputStream bos = new BufferedOutputStream(fos);

        return new PrintStream(bos, true);
    }

    @AfterClass
    public static void deleteDir()
    {
        File file = new File(getBaseDirectory() + "/target/.private");

        if (!file.exists())
            return;

        deleteRecursive(file);
    }

    private static void deleteRecursive(File file)
    {
        if (file.isFile())
        {
            file.delete();
            return;
        }

        String[] names = file.list();

        for (int i = 0; i < names.length; i++)
        {
            File f = new File(file, names[i]);
            deleteRecursive(f);
        }

        file.delete();
    }

    public static String getBaseDirectory()
    {
        if (_baseDir == null) {
            _baseDir = System.getProperty("BASEDIR", DEFAULT_BASE_DIR);
            File test = new File(_baseDir + SCRIPTS_DIR);
            if (!test.exists()) {
                test = new File(_baseDir + "tapestry-framework/" + SCRIPTS_DIR);
                if (test.exists())
                    _baseDir = _baseDir + "tapestry-framework/";
            }
        }

        return _baseDir;
    }
}
