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

package org.apache.tapestry.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.test.mock.InitParameterHolder;
import org.apache.tapestry.test.mock.MockContext;
import org.apache.tapestry.test.mock.MockRequest;
import org.apache.tapestry.test.mock.MockResponse;
import org.apache.tapestry.test.mock.MockServletConfig;
import org.apache.tapestry.util.RegexpMatcher;

/**
 * Executes a series of requests and assertions specified by
 * a {@link org.apache.tapestry.test.ScriptDescriptor).
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ScriptedTestSession
{
    private ScriptDescriptor _scriptDescriptor;
    private MockContext _context;
    private MockRequest _request;
    private MockResponse _response;
    private RegexpMatcher _matcher = new RegexpMatcher();

    public ScriptedTestSession(ScriptDescriptor descriptor)
    {
        _scriptDescriptor = descriptor;
    }

    public void execute()
    {
        setupForExecute();

        Iterator i = _scriptDescriptor.getRequestDescriptors().iterator();

        while (i.hasNext())
        {
            RequestDescriptor rd = (RequestDescriptor) i.next();

            executeRequest(rd);
        }
    }

    private void setupForExecute()
    {
        _context = new MockContext(_scriptDescriptor.getRootDirectory());
        _context.setServletContextName(_scriptDescriptor.getContextName());
    }

    private void executeRequest(RequestDescriptor rd)
    {
        HttpServlet s = getServlet(rd.getServletName());

        _request = new MockRequest(_context, rd.getServletPath());

        loadParameters(_request, rd);

        _response = new MockResponse(_request);

        try
        {
            s.service(_request, _response);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                "Error executing request: " + ex.getMessage(),
                rd.getLocation(),
                ex);
        }

        rd.executeAssertions(this);
    }

    private void loadParameters(MockRequest request, RequestDescriptor rd)
    {
        String[] names = rd.getParameterNames();

        for (int i = 0; i < names.length; i++)
        {
            String[] values = rd.getParameterValues(names[i]);

            request.setParameter(names[i], values);
        }
    }

    private Map _servlets = new HashMap();

    private HttpServlet getServlet(String name)
    {
        if (name == null)
            name = _scriptDescriptor.getDefaultServletDescriptor().getName();

        HttpServlet result = (HttpServlet) _servlets.get(name);

        if (result == null)
        {
            result = readyServlet(name);
            _servlets.put(name, result);
        }

        return result;
    }

    private HttpServlet readyServlet(String name)
    {
        ServletDescriptor sd = _scriptDescriptor.getServletDescriptor(name);

        HttpServlet result = instantiateServlet(sd);

        MockServletConfig config = new MockServletConfig(name, _context);

        loadInitParameters(config, sd.getInitParameters());

        try
        {
            result.init(config);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                "Unable to initialize servlet '" + name + "': " + ex.getMessage(),
                sd.getLocation(),
                ex);
        }

        return result;
    }

    private void loadInitParameters(InitParameterHolder holder, Map parameters)
    {
        Iterator i = parameters.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String name = (String) e.getKey();
            String value = (String) e.getValue();

            holder.setInitParameter(name, value);
        }
    }

    private HttpServlet instantiateServlet(ServletDescriptor sd)
    {
        String className = sd.getClassName();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try
        {
            Class servletClass = Class.forName(className, true, loader);

            return (HttpServlet) servletClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                "Unable to instantiate servlet '" + sd.getName() + "': " + ex.getMessage(),
                sd.getLocation(),
                ex);
        }
    }

    /** Inteaded only for use by the test suite. */

    void setResponse(MockResponse response)
    {
        _response = response;
    }

    public MockResponse getResponse()
    {
        return _response;
    }

    public RegexpMatcher getMatcher()
    {
        return _matcher;
    }

}
