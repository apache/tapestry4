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

package org.apache.tapestry.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.hivemind.parse.AbstractParser;
import org.apache.hivemind.parse.ElementParseInfo;
import org.apache.hivemind.sdl.SDLResourceParser;
import org.apache.tapestry.test.assertions.AssertOutput;
import org.apache.tapestry.test.assertions.AssertRegexp;
import org.apache.tapestry.test.assertions.RegexpMatch;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 * Parses Tapestry test scripts; SDL files that define an execution environment and
 * a sequence of operations and assertions.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ScriptParser extends AbstractParser
{
    private ScriptDescriptor _scriptDescriptor;
    private Map _attributes;
    private String _elementName;

    /**
     * Map from element name to a ElementParseInfo
     */
    private final Map _elementParseInfo = new HashMap();

    public ScriptParser()
    {
        initializeFromPropertiesFile();
    }

    private void initializeFromPropertiesFile()
    {
        Properties p = new Properties();
        InputStream stream = null;

        try
        {

            InputStream rawStream = getClass().getResourceAsStream("ScriptParser.properties");
            stream = new BufferedInputStream(rawStream);

            p.load(stream);

            stream.close();
            stream = null;
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        finally
        {
            close(stream);
        }

        initializeFromProperties(p);
    }

    private void initializeFromProperties(Properties p)
    {
        Iterator i = p.keySet().iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();
            String value = p.getProperty(key);

            initializeFromProperty(key, value);
        }
    }

    private void initializeFromProperty(String key, String value)
    {
        // Ignore keys that don't start with "required."

        if (!key.startsWith("required."))
            return;

        int lastDotx = key.lastIndexOf('.');

        String elementName = key.substring(9, lastDotx);
        String attributeName = key.substring(lastDotx + 1);

        boolean required = value.equalsIgnoreCase("true");

        ElementParseInfo epi = getElementParseInfo(elementName);

        epi.addAttribute(attributeName, required);
    }

    private void close(InputStream stream)
    {
        try
        {
            if (stream != null)
                stream.close();
        }
        catch (IOException ex)
        {
            // Ingore.
        }
    }

    public ScriptDescriptor parse(Resource script)
    {
        initializeParser(script, STATE_INITIAL);

        try
        {
            startParse();

            return _scriptDescriptor;
        }
        finally
        {
            resetParser();
        }
    }

    private void startParse()
    {
        try
        {
            new SDLResourceParser().parseResource(getResource(), this);
        }
        catch (Exception ex)
        {
            throw new DocumentParseException(ex.getMessage(), getResource(), ex);
        }
    }

    protected void initializeParser(Resource resource, int startState)
    {
        super.initializeParser(resource, startState);

        _attributes = new HashMap();
    }

    protected void resetParser()
    {
        _attributes = null;
        _elementName = null;
        _scriptDescriptor = null;

        super.resetParser();
    }

    private static final int STATE_INITIAL = 0;
    private static final int STATE_TEST_SCRIPT = 1;
    private static final int STATE_SERVLET = 2;
    private static final int STATE_INIT_PARAMETER = 3;
    private static final int STATE_REQUEST = 4;
    private static final int STATE_ASSERT_OUTPUT = 5;
    private static final int STATE_ASSERT_REGEXP = 6;
    private static final int STATE_MATCH = 7;

    private static final int STATE_NO_CONTENT = 1000;

    protected void begin(String elementName, Map attributes)
    {
        _elementName = elementName;
        _attributes = attributes;

        switch (getState())
        {
            case STATE_INITIAL :
                beginInitial();
                break;

            case STATE_TEST_SCRIPT :
                beginTestScript();
                break;

            case STATE_SERVLET :
                beginServlet();
                break;

            case STATE_REQUEST :
                beginRequest();
                break;

            case STATE_ASSERT_REGEXP :
                beginAssertRegexp();
                break;

            default :
                unexpectedElement(_elementName);
        }
    }

    protected void end(String elementName)
    {
        _elementName = elementName;

        switch (getState())
        {
            case STATE_ASSERT_OUTPUT :

                endAssertOutput();
                break;

            case STATE_ASSERT_REGEXP :
                endAssertRegexp();
                break;

            case STATE_MATCH :
                endMatch();
                break;

            default :
                break;
        }

        pop();

    }

    private void beginInitial()
    {
        if (_elementName.equals("test-script"))
        {
            enterTestScript();
            return;
        }

        unexpectedElement(_elementName);
    }

    private void beginTestScript()
    {
        if (_elementName.equals("servlet"))
        {
            enterServlet();
            return;
        }

        if (_elementName.equals("request"))
        {
            enterRequest();
            return;
        }

        unexpectedElement(_elementName);
    }

    private void beginServlet()
    {
        if (_elementName.equals("init-parameter"))
        {
            enterInitParameter();
            return;
        }

        unexpectedElement(_elementName);
    }

    private void enterRequest()
    {
        validateAttributes();

        String servletName = getAttribute("servlet");
        String servletPath = getAttribute("servlet-path", "/app");

        RequestDescriptor rd = new RequestDescriptor();
        rd.setServletName(servletName);
        rd.setServletPath(servletPath);

        ScriptDescriptor sd = (ScriptDescriptor) peekObject();

        sd.addRequestDescriptor(rd);

        push(_elementName, rd, STATE_REQUEST);
    }

    public void beginRequest()
    {
        if (_elementName.equals("parameter"))
        {
            enterParameter();
            return;
        }

        if (_elementName.equals("assert-output"))
        {
            enterAssertOutput();
            return;
        }

        if (_elementName.equals("assert-regexp"))
        {
            enterAssertRegexp();
            return;
        }

        unexpectedElement(_elementName);
    }

    private void enterAssertOutput()
    {
        validateAttributes();

        AssertOutput ao = new AssertOutput();
        RequestDescriptor rd = (RequestDescriptor) peekObject();

        rd.addAssertion(ao);

        push(_elementName, ao, STATE_ASSERT_OUTPUT, false);
    }

    private void enterAssertRegexp()
    {
        validateAttributes();

        int subgroup = getIntAttribute("subgroup", 0);

        AssertRegexp ar = new AssertRegexp();
        ar.setSubgroup(subgroup);

        RequestDescriptor rd = (RequestDescriptor) peekObject();

        rd.addAssertion(ar);

        push(_elementName, ar, STATE_ASSERT_REGEXP, false);
    }

    private void beginAssertRegexp()
    {
        if (_elementName.equals("match"))
        {
            enterMatch();
            return;
        }

        unexpectedElement(_elementName);
    }

    private void enterMatch()
    {
        validateAttributes();

        RegexpMatch m = new RegexpMatch();
        AssertRegexp ar = (AssertRegexp) peekObject();

        ar.addMatch(m);

        push(_elementName, m, STATE_MATCH, false);
    }

    private void endAssertOutput()
    {
        String content = peekContent();
        AssertOutput ao = (AssertOutput) peekObject();

        ao.setExpectedSubstring(content);
    }

    private void endAssertRegexp()
    {
        String content = peekContent();

        AssertRegexp ar = (AssertRegexp) peekObject();

        ar.setRegexp(content);
    }

    private void endMatch()
    {
        String content = peekContent();

        RegexpMatch m = (RegexpMatch) peekObject();

        m.setExpectedString(content);
    }

    protected String peekContent()
    {
        String rawContent = super.peekContent();

        if (rawContent == null)
            return null;

        return rawContent.trim();
    }

    private void enterParameter()
    {
        validateAttributes();

        String name = getAttribute("name");
        String value = getAttribute("value");

        RequestDescriptor rd = (RequestDescriptor) peekObject();

        rd.addParameter(name, value);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterInitParameter()
    {
        validateAttributes();

        String name = getAttribute("name");
        String value = getAttribute("value");

        ServletDescriptor sd = (ServletDescriptor) peekObject();

        sd.addInitParameter(name, value);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterTestScript()
    {
        validateAttributes();

        String contextName = getAttribute("context");
        String directory = getAttribute("directory");

        ScriptDescriptor sd = new ScriptDescriptor();
        sd.setContextName(contextName);
        sd.setRootDirectory(directory);

        _scriptDescriptor = sd;

        push(_elementName, sd, STATE_TEST_SCRIPT);
    }

    private void enterServlet()
    {
        validateAttributes();

        String name = getAttribute("name");
        String className = getAttribute("class", "org.apache.tapestry.ApplicationServlet");

        ServletDescriptor sd = new ServletDescriptor();
        sd.setName(name);
        sd.setClassName(className);

        // Can't wait for push() to do this, because of checks inside
        // addServletDescriptor(). 
        sd.setLocation(getLocation());

        ScriptDescriptor scriptDescriptor = (ScriptDescriptor) peekObject();

        scriptDescriptor.addServletDescriptor(sd);

        push(_elementName, sd, STATE_SERVLET);
    }

    private String getAttribute(String name)
    {
        return (String) _attributes.get(name);
    }

    private String getAttribute(String name, String defaultValue)
    {
        if (!_attributes.containsKey(name))
            return defaultValue;

        return (String) _attributes.get(name);
    }

    private void validateAttributes()
    {
        Iterator i = _attributes.keySet().iterator();

        ElementParseInfo epi = getElementParseInfo(_elementName);

        // First, check that each attribute is in the set of expected attributes.

        while (i.hasNext())
        {
            String name = (String) i.next();

            if (!epi.isKnown(name))
                throw new DocumentParseException(
                    ScriptMessages.unexpectedAttributeInElement(name, _elementName),
                    getLocation(),
                    null);
        }

        // Now check that all required attributes have been specified.

        i = epi.getRequiredNames();
        while (i.hasNext())
        {
            String name = (String) i.next();

            if (!_attributes.containsKey(name))
                throw new DocumentParseException(
                    ScriptMessages.missingRequiredAttribute(name, _elementName),
                    getLocation(),
                    null);
        }

    }

    private ElementParseInfo getElementParseInfo(String elementName)
    {
        ElementParseInfo result = (ElementParseInfo) _elementParseInfo.get(elementName);

        if (result == null)
        {
            result = new ElementParseInfo();
            _elementParseInfo.put(elementName, result);
        }

        return result;
    }

    private int getIntAttribute(String name, int defaultValue)
    {
        String attributeValue = getAttribute(name);

        if (attributeValue == null)
            return defaultValue;

        try
        {
            return Integer.parseInt(attributeValue);
        }
        catch (NumberFormatException ex)
        {
            throw new ApplicationRuntimeException(
                ScriptMessages.invalidIntAttribute(
                    name,
                    _elementName,
                    getLocation(),
                    attributeValue),
                getLocation(),
                ex);
        }
    }
}
