package net.sf.tapestry.junit.mock;

import java.io.IOException;
import java.io.InputStream;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.ApplicationServlet;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

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

    public MockTester(String resourcePath) throws JDOMException, ServletException
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
    
    public void execute() throws IOException, ServletException
    {
        _response = new MockResponse(_request);
        
        _servlet.service(_request, _response);
        
        _response.end();
    }

    private void parse() throws JDOMException
    {
        SAXBuilder builder = new SAXBuilder();

        InputStream stream = getClass().getResourceAsStream(_resourcePath);

        _document = builder.build(stream);
    }

    private void setup() throws ServletException
    {
        Element setup = _document.getRootElement().getChild("setup");

        setupContext(setup);
        setupServlet(setup);
        setupRequest(setup);
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

    private void setupRequest(Element parent)
    {
        _request = new MockRequest(_context, "/" + _servlet.getServletName());
     
        Element request = parent.getChild("request");
        
        String method = request.getAttributeValue("method");
        if (method != null)
            _request.setMethod(method);
        
        List parameters = request.getChildren("parameter");
        int count = parameters.size();
        
        for (int i = 0; i < count; i++)
        {
            Element parameter = (Element)parameters.get(i);
            
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
            Element e = (Element)values.get(i);
            value = e.getTextTrim();
            
            values.add(value);
        }
        
        String[] array = (String[])values.toArray(new String[values.size()]);
        
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

}
