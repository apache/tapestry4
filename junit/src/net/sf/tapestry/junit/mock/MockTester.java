package net.sf.tapestry.junit.mock;

import java.io.IOException;
import java.io.InputStream;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.ApplicationServlet;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.xml.DocumentParseException;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.jdom.*;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import junit.framework.AssertionFailedError;

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
    private static final Logger LOG = LogManager.getLogger(MockTester.class);

    private String _resourcePath;
    private Document _document;
    private MockContext _context;
    private String _servletName;
    private ApplicationServlet _servlet;
    private MockRequest _request;
    private MockResponse _response;
    private PatternMatcher _matcher;
    private PatternCompiler _compiler;
    private Map _ognlContext;

    private static class ServletConfigImpl implements ServletConfig, IInitParameterHolder
    {
        private String _name;
        private ServletContext _context;
        private Map _initParameters = new HashMap();

        private ServletConfigImpl(String name, ServletContext context)
        {
            _name = name;
            _context = context;
        }

        public String getInitParameter(String name)
        {
            return (String) _initParameters.get(name);
        }

        public Enumeration getInitParameterNames()
        {
            return Collections.enumeration(_initParameters.keySet());
        }

        public ServletContext getServletContext()
        {
            return _context;
        }

        public String getServletName()
        {
            return _name;
        }

        public void setInitParameter(String name, String value)
        {
            _initParameters.put(name, value);
        }

    }

    /**
     *  Constructs a new MockTester for the given resource path
     *  (which is the XML file to read).
     * 
     **/

    public MockTester(String resourcePath) throws JDOMException, ServletException, DocumentParseException
    {
        _resourcePath = resourcePath;

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

    private void executeRequest(Element request) throws IOException, ServletException, DocumentParseException
    {
        setupRequest(request);

        _response = new MockResponse(_request);

        _servlet.service(_request, _response);

        _response.end();

        executeAssertions(request);
    }

    private void parse() throws JDOMException
    {
        SAXBuilder builder = new SAXBuilder();

        InputStream stream = getClass().getResourceAsStream(_resourcePath);

        _document = builder.build(stream);
    }

    private void setup() throws ServletException, DocumentParseException
    {
        Element root = _document.getRootElement();

        if (!root.getName().equals("mock-test"))
            throw new DocumentParseException("Root element must be 'mock-test'.", _resourcePath);

        setupContext(root);
        setupServlet(root);
    }

    private void setupContext(Element parent)
    {
        Element context = parent.getChild("context");

        _context = new MockContext();

        if (context == null)
            return;

        String name = context.getAttributeValue("name");

        if (name != null)
            _context.setServletContextName(name);

        setInitParameters(context, _context);
    }

    private void setupServlet(Element parent) throws ServletException
    {
        Element servlet = parent.getChild("servlet");

        String className = servlet.getAttributeValue("class");
        _servletName = servlet.getAttributeValue("name");

        _servlet = createServlet(className);

        ServletConfigImpl config = new ServletConfigImpl(_servletName, _context);

        setInitParameters(servlet, config);

        _servlet.init(config);
    }

    private void setupRequest(Element request)
    {
        _request = new MockRequest(_context, "/" + _servlet.getServletName());

        String method = request.getAttributeValue("method");
        if (method != null)
            _request.setMethod(method);

        List parameters = request.getChildren("parameter");
        int count = parameters.size();

        for (int i = 0; i < count; i++)
        {
            Element parameter = (Element) parameters.get(i);

            setRequestParameter(parameter);
        }

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
            Element e = (Element) values.get(i);
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

        throw new ApplicationRuntimeException("Unable to instantiate servlet class " + className + ".", t);
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
        executeExpressionAssertions(request);
        executeOutputMatchesAssertions(request);
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
     *  Handles &lt;assert-output&gt; elements inside &lt;request&gt;.
     *  Checks that a regular expression appears in the output.
     *  Content of element is the regular expression.
     * 
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
            String pattern = a.getTextTrim();

            if (outputString == null)
                outputString = _response.getOutputString();

            match(name, outputString, pattern);
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
        if (_compiler == null)
            _compiler = new Perl5Compiler();

        try
        {
            return _compiler.compile(pattern, Perl5Compiler.MULTILINE_MASK);

        }
        catch (MalformedPatternException ex)
        {
            throw new DocumentParseException("Malformed regular expression: " + pattern, _resourcePath, ex);
        }

    }

    private void match(String name, String text, String pattern) throws DocumentParseException
    {

        Pattern compiled = compile(pattern);

        if (getMatcher().contains(text, compiled))
            return;

        System.err.println(text);

        throw new AssertionFailedError(name + ": Response does not contain regular expression '" + pattern + "'.");

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

    private void executeOutputMatchAssertion(Element element, String outputString) throws DocumentParseException
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
            throw new AssertionFailedError(name + ": Too few matches for '" + pattern + "'.");
        }
    }

}
