// Copyright 2004 The Apache Software Foundation
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

import java.io.PrintWriter;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.test.mock.MockContext;
import org.apache.tapestry.test.mock.MockRequest;
import org.apache.tapestry.test.mock.MockResponse;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 * Tests {@link org.apache.tapestry.test.IntegrationTestScriptParser}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestScriptParser extends TapestryTestCase
{
    private ClassResolver _resolver = new DefaultClassResolver();

    private ScriptDescriptor parse(String file) throws Exception
    {
        String basePath = "/" + getClass().getName().replace('.', '/');
        Resource baseResource = new ClasspathResource(_resolver, basePath);

        Resource fileResource = baseResource.getRelativeResource(file);

        return new IntegrationTestScriptParser().parse(fileResource);
    }

    /**
     * Test a script that contains just the &lt;test-script&gt; element and its attributes.
     */
    public void testEmpty() throws Exception
    {
        ScriptDescriptor sd = parse("Empty.xml");

        assertNotNull(sd.getLocation());
        assertEquals("test-context", sd.getContextName());
        assertEquals("root", sd.getRootDirectory());
    }

    public void testEmptyMissingAttribute() throws Exception
    {
        try
        {
            parse("EmptyMissingAttribute.xml");
            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(
                    ex,
                    "Required attribute 'context' is not supplied for element test-script.");
        }
    }

    public void testEmptyUnknownAttribute() throws Exception
    {
        try
        {
            parse("EmptyUnknownAttribute.xml");
            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Unexpected attribute 'cntext' (in element test-script).");
        }
    }

    public void testServlet() throws Exception
    {
        ScriptDescriptor sd = parse("Servlet.xml");

        ServletDescriptor d = sd.getServletDescriptor("default");
        assertEquals("default", d.getName());
        assertEquals("MyClass", d.getClassName());

        d = sd.getServletDescriptor("other");
        assertEquals("other", d.getName());
        assertEquals("org.apache.tapestry.ApplicationServlet", d.getClassName());

        assertNull(sd.getServletDescriptor("unknown"));
    }

    public void testServletDuplicateName() throws Exception
    {
        try
        {
            parse("ServletDuplicateName.xml");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Servlet descriptor 'default'");
            checkException(ex, "conflicts with prior instance");
        }
    }

    public void testInitParameter() throws Exception
    {
        ScriptDescriptor sd = parse("InitParameter.xml");

        ServletDescriptor s1 = sd.getServletDescriptor("default");

        assertEquals("v1", s1.getInitParameter("p1"));
        assertEquals("v2", s1.getInitParameter("p2"));

        ServletDescriptor s2 = sd.getServletDescriptor("other");

        assertEquals("v3", s2.getInitParameter("p3"));
        assertNull(s2.getInitParameter("foo"));
    }

    public void testRequest() throws Exception
    {
        ScriptDescriptor sd = parse("Request.xml");

        List l = sd.getRequestDescriptors();

        assertEquals(2, l.size());

        RequestDescriptor rd = (RequestDescriptor) l.get(0);

        assertEquals("alpha", rd.getServletName());
        assertEquals("/app", rd.getServletPath());

        rd = (RequestDescriptor) l.get(1);

        assertEquals("beta", rd.getServletName());
        assertEquals("/beta", rd.getServletPath());
    }

    public void testRequestParameters() throws Exception
    {
        ScriptDescriptor sd = parse("RequestParameters.xml");

        List l = sd.getRequestDescriptors();

        assertEquals(2, l.size());

        RequestDescriptor rd = (RequestDescriptor) l.get(0);

        assertEquals("alpha", rd.getServletName());

        assertListsEqual(new String[]
        { "manchu" }, rd.getParameterValues("foo"));
        assertListsEqual(new String[]
        { "fred", "wilma" }, rd.getParameterValues("flintstone"));

        rd = (RequestDescriptor) l.get(1);

        assertNull(rd.getServletName());
        assertListsEqual(new String[]
        { "pebbles" }, rd.getParameterValues("flintstone"));
    }

    public void testAssertOutput() throws Exception
    {
        ScriptDescriptor sd = parse("AssertOutput.xml");

        ScriptedTestSession ss = createSession();

        RequestDescriptor rd = (RequestDescriptor) sd.getRequestDescriptors().get(0);

        try
        {
            rd.executeAssertions(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                    "Expected text \\(\"<title>Away</title>\", at .*?\\) was not found in the response\\.",
                    ex.getMessage());
            assertNotNull(ex.getLocation());
        }
    }

    public void testAssertRegexp() throws Exception
    {
        ScriptDescriptor sd = parse("AssertRegexp.xml");

        ScriptedTestSession ss = createSession();

        RequestDescriptor rd = (RequestDescriptor) sd.getRequestDescriptors().get(0);

        try
        {
            rd.executeAssertions(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                    "Expected regular expression \\(\"<body>.*</body>\", at .*?\\) was not found in the response\\.",
                    ex.getMessage());
            assertNotNull(ex.getLocation());
        }
    }

    public void testAssertRegexpMatch() throws Exception
    {
        ScriptDescriptor sd = parse("AssertRegexpMatch.xml");

        ScriptedTestSession ss = createSession();

        RequestDescriptor rd = (RequestDescriptor) sd.getRequestDescriptors().get(0);

        try
        {
            rd.executeAssertions(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                    "Regular expression '<\\(\\.\\*\\?\\)>' \\(at .*?\\) should have generated 4 matches, but generated 2 instead\\.",
                    ex.getMessage());
            assertNotNull(ex.getLocation());
        }
    }

    /**
     * Kludges together a context, request and response as well as some simple text to run
     * assertions against.
     */
    static ScriptedTestSession createSession() throws Exception
    {
        ScriptedTestSession result = new ScriptedTestSession(null);

        MockContext context = new MockContext();
        context.setServletContextName("context");
        MockRequest request = new MockRequest(context, "/app");

        MockResponse r = new MockResponse(request);
        result.setResponse(r);

        PrintWriter p = result.getResponse().getWriter();

        p.println("Now is the time for all good men to come to the aid of their country.");
        p.println("Here comes that text: <title>Test</title>");
        p.println("That wasn't so bad?");

        p.close();

        return result;
    }
}