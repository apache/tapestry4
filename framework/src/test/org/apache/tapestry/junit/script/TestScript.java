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

package org.apache.tapestry.junit.script;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.script.ScriptParser;
import org.apache.tapestry.script.ScriptSession;
import org.apache.tapestry.script.ScriptSessionImpl;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 * A collection of tests for Tapestry scripting.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class TestScript extends TapestryTestCase
{
    private MockScriptProcessor _processor = new MockScriptProcessor();

    private IScript read(String file) throws IOException, DocumentParseException
    {
        ClassResolver resolver = new DefaultClassResolver();
        ScriptParser parser = new ScriptParser(resolver, createExpressionEvaluator(), null);

        String classAsPath = "/" + getClass().getName().replace('.', '/');

        Resource classLocation = new ClasspathResource(resolver, classAsPath);
        Resource scriptLocation = classLocation.getRelativeResource(file);

        return parser.parse(scriptLocation);
    }

    private IScript execute(String file, Map symbols) throws DocumentParseException, IOException
    {
        IScript script = read(file);

        IRequestCycle cycle = (IRequestCycle) newMock(IRequestCycle.class);

        replayControls();

        script.execute(cycle, _processor, symbols);

        verifyControls();

        return script;
    }

    private void assertSymbol(Map symbols, String key, Object expected)
    {
        Object actual = symbols.get(key);

        assertEquals(key, expected, actual);
    }

    /**
     * Simple test where the body and initialization are static.
     */

    public void testSimple() throws Exception
    {
        execute("simple.script", null);

        assertEquals("body", "\nBODY\n", _processor.getBody());
        assertEquals("initialization", "\nINITIALIZATION\n", _processor.getInitialization());
        assertNull(_processor.getExternalScripts());
    }

    /**
     * Test the &lt;unique&gt; element, new in the 1.3 DTD
     * 
     * @since 3.0
     */

    public void testUnique() throws Exception
    {
        IScript script = read("unique.script");

        IRequestCycle cycle = new RequestCycle(null, null, null, null, null);

        script.execute(cycle, _processor, null);
        script.execute(cycle, _processor, null);

        assertEquals("Block1\nBlock2\nNotUnique\n\n\n\nNotUnique", _processor.getBody().trim());
    }

    /**
     * Test omitting body and initialization, ensure they return null.
     */

    public void testEmpty() throws Exception
    {
        execute("empty.script", null);

        assertNull("body", _processor.getBody());
        assertNull("initialization", _processor.getInitialization());
    }

    /**
     * Test the ability of the let element to create an output symbol. Also, test the insert
     * element.
     */

    public void testLet() throws Exception
    {
        String inputSymbol = Long.toHexString(System.currentTimeMillis());
        Map symbols = new HashMap();
        symbols.put("inputSymbol", inputSymbol);

        execute("let.script", symbols);

        // Unlike body, the let element trims whitespace.

        String outputSymbol = "output: " + inputSymbol;

        assertEquals("Output symbol", outputSymbol, symbols.get("outputSymbol"));
    }

    /**
     * Test the unique attribute on the &lt;let&gt; element. New in the 1.3 DTD
     * 
     * @since 3.0
     */
    public void testUniqueLet() throws Exception
    {
        Map symbols = new HashMap();

        execute("unique-let.script", symbols);

        assertSymbol(symbols, "alpha", "Alpha");
        assertSymbol(symbols, "beta", "Alpha$0");
        assertSymbol(symbols, "gamma", "Alpha$1");
    }

    public void testIncludeScript() throws Exception
    {
        IScript script = execute("include-script.script", null);

        Resource scriptLocation = script.getScriptResource();

        Resource[] expected = new Resource[]
        { scriptLocation.getRelativeResource("first"),
                scriptLocation.getRelativeResource("second"),
                scriptLocation.getRelativeResource("third") };

        assertEquals("included scripts", Arrays.asList(expected), Arrays.asList(_processor
                .getExternalScripts()));
    }

    public void testAntSyntax() throws Exception
    {
        Map form = new HashMap();

        form.put("name", "gallahad");

        Map component = new HashMap();
        component.put("form", form);
        component.put("name", "lancelot");

        Map symbols = new HashMap();
        symbols.put("component", component);

        execute("ant-syntax.script", symbols);

        assertSymbol(symbols, "functionName", "gallahad_lancelot");
        assertSymbol(symbols, "incomplete1", "Incomplete: $");
        assertSymbol(symbols, "incomplete2", "Incomplete: ${");
        assertSymbol(symbols, "nopath", "This ${} ends up as literal.");
        assertSymbol(symbols, "OGNL", "This is a brace: }.");
    }

    public void testSet() throws Exception
    {
        Map symbols = new HashMap();

        execute("set.script", symbols);

        assertSymbol(symbols, "element2", new Character('p'));
    }

    public void testInvalidKeyLet() throws Exception
    {
        try
        {
            execute("invalid-key-let.script", new HashMap());

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "key");
        }
    }

    public void testInvalidKeySet() throws Exception
    {
        try
        {
            execute("invalid-key-Set.script", new HashMap());

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "key");
        }
    }

    public void testInputSymbolClass() throws Exception
    {
        try
        {
            Map symbols = new HashMap();
            symbols.put("input", new Integer(20));

            execute("input-symbol-class.script", symbols);

            unreachable();
        }
        catch (Exception ex)
        {
            checkException(ex, "Integer");
            checkException(ex, "Long");
        }
    }

    public void testInputSymbol() throws Exception
    {
        Map symbols = new HashMap();
        symbols.put("input", new Long(20));

        execute("input-symbol.script", symbols);

        assertSymbol(symbols, "success", "Success");
    }

    public void testInputSymbolRequired() throws Exception
    {
        try
        {
            execute("input-symbol-required.script", new HashMap());

            unreachable();
        }
        catch (Exception ex)
        {
            checkException(ex, "required");
        }
    }

    public void testInputSymbolInvalidKey() throws Exception
    {
        try
        {
            execute("input-symbol-invalid-key.script", new HashMap());

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "key");
        }

    }

    /** @since 3.0 */

    public void testNameAppend() throws Exception
    {
        Map symbols = new HashMap();

        symbols.put("name", "fred");
        execute("name-append.script", symbols);

        assertSymbol(symbols, "output", "fred$suffix");
    }

    /**
     * A bunch of quickies to push up the code coverage numbers.
     */
    public void testCheats() throws Exception
    {
        IScript script = execute("simple.script", null);

        ScriptSession session = new ScriptSessionImpl(script.getScriptResource(), null, null,
                createExpressionEvaluator(), null, null);
        assertEquals("ScriptSession[" + script.getScriptResource() + "]", session.toString());
    }
}