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

package org.apache.tapestry.junit.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.IScript;
import org.apache.tapestry.junit.MockRequestCycle;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.script.ScriptParser;
import org.apache.tapestry.script.ScriptSession;
import org.apache.tapestry.util.DefaultResourceResolver;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 *  A collection of tests for Tapestry scripting.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class TestScript extends TapestryTestCase
{
    private MockScriptProcessor _processor = new MockScriptProcessor();

    private IScript read(String file) throws IOException, DocumentParseException
    {
        IResourceResolver resolver = new DefaultResourceResolver();
        ScriptParser parser = new ScriptParser(resolver);

        String classAsPath = "/" + getClass().getName().replace('.', '/');

        IResourceLocation classLocation = new ClasspathResourceLocation(resolver, classAsPath);
        IResourceLocation scriptLocation = classLocation.getRelativeLocation(file);

        return parser.parse(scriptLocation);
    }

    private IScript execute(String file, Map symbols) throws DocumentParseException, IOException
    {
        IScript script = read(file);

        script.execute(new MockRequestCycle(), _processor, symbols);

        return script;
    }

    private void assertSymbol(Map symbols, String key, Object expected)
    {
        Object actual = symbols.get(key);

        assertEquals(key, expected, actual);
    }

    /**
     *  Simple test where the body and initialization are static.
     * 
     **/

    public void testSimple() throws Exception
    {
        execute("simple.script", null);

        assertEquals("body", "\nBODY\n", _processor.getBody());
        assertEquals("initialization", "\nINITIALIZATION\n", _processor.getInitialization());
        assertNull(_processor.getExternalScripts());
    }

    /**
     * Test the &lt;unique&gt; element, new in the 1.3 DTD
     * @since 3.0
     */

    public void testUnique() throws Exception
    {
        IScript script = read("unique.script");

        IRequestCycle cycle = new MockRequestCycle();

        script.execute(cycle, _processor, null);
        script.execute(cycle, _processor, null);

        assertEquals("Block1\nBlock2\nNotUnique\n\n\n\nNotUnique", _processor.getBody().trim());
    }

    /**
     *  Test omitting body and initialization, ensure they return null.
     * 
     **/

    public void testEmpty() throws Exception
    {
        execute("empty.script", null);

        assertNull("body", _processor.getBody());
        assertNull("initialization", _processor.getInitialization());
    }

    /**
     *  Test the ability of the let element to create an output symbol.  Also,
     *  test the insert element.
     * 
     **/

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
     *  Tests the if element, using strings, numbers, booleans, nulls, arrays
     *  and collections.
     * 
     **/

    public void testIf() throws Exception
    {
        Map input = new HashMap();

        input.put("true_string", "anything");
        input.put("false_string", " ");
        input.put("boolean_true", Boolean.TRUE);
        input.put("boolean_false", Boolean.FALSE);
        input.put("collection_non_empty", Collections.singletonList(Boolean.TRUE));
        input.put("collection_empty", new ArrayList());
        input.put("array_nonempty", new String[] { "alpha", "beta" });
        input.put("array_empty", new Integer[0]);
        input.put("number_zero", new Long(0));
        input.put("number_nonzero", new Integer(1));

        Map symbols = new HashMap();
        symbols.put("input", input);

        execute("if.script", symbols);

        assertSymbol(symbols, "output_true_string", "TRUE-STRING");
        assertSymbol(symbols, "output_false_string", "");
        assertSymbol(symbols, "output_null", "");
        assertSymbol(symbols, "output_boolean_true", "BOOLEAN-TRUE");
        assertSymbol(symbols, "output_boolean_false", "");
        assertSymbol(symbols, "output_collection_nonempty", "COLLECTION-NON-EMPTY");
        assertSymbol(symbols, "output_collection_empty", "");
        assertSymbol(symbols, "output_array_nonempty", "ARRAY-NON-EMPTY");
        assertSymbol(symbols, "output_array_empty", "");
        assertSymbol(symbols, "output_number_zero", "");
        assertSymbol(symbols, "output_number_nonzero", "NUMBER-NON-ZERO");
    }

    /**
     * Test the unique attribute on the &lt;let&gt; element.  New in
     * the 1.3 DTD
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

    /**
     *  Tests the if-not element.
     * 
     **/

    public void testIfNot() throws Exception
    {
        Map input = new HashMap();

        input.put("trueString", "anything");
        input.put("falseString", " ");
        input.put("booleanTrue", Boolean.TRUE);
        input.put("booleanFalse", Boolean.FALSE);
        input.put("collectionNonEmpty", Collections.singletonList(Boolean.TRUE));
        input.put("collectionEmpty", new ArrayList());
        input.put("arrayNonempty", new String[] { "alpha", "beta" });
        input.put("arrayEmpty", new Integer[0]);
        input.put("numberZero", new Long(0));
        input.put("numberNonzero", new Integer(1));

        Map symbols = new HashMap();
        symbols.put("input", input);

        execute("if-not.script", symbols);

        assertSymbol(symbols, "outputTrueString", "");
        assertSymbol(symbols, "outputFalseString", "FALSE-STRING");
        assertSymbol(symbols, "outputNull", "NULL");
        assertSymbol(symbols, "outputBooleanTrue", "");
        assertSymbol(symbols, "outputBooleanFalse", "BOOLEAN-FALSE");
        assertSymbol(symbols, "outputCollectionNonempty", "");
        assertSymbol(symbols, "outputCollectionEmpty", "COLLECTION-EMPTY");
        assertSymbol(symbols, "outputArrayNonempty", "");
        assertSymbol(symbols, "outputArrayEmpty", "ARRAY-EMPTY");
        assertSymbol(symbols, "outputNumberZero", "NUMBER-ZERO");
        assertSymbol(symbols, "outputNumberNonzero", "");
    }

    /**
     *  Tests a bunch of variations on the foreach element.
     * 
     **/

    public void testForeach() throws Exception
    {
        Map input = new HashMap();
        input.put("single", "SINGLE");
        input.put("emptyArray", new String[0]);
        input.put("array", new String[] { "ALPHA", "BETA", "GAMMA" });
        input.put("collection", Arrays.asList(new String[] { "MOE", "LARRY", "CURLY" }));
        input.put("emptyCollection", new ArrayList());

        Map symbols = new HashMap();
        symbols.put("input", input);

        execute("foreach.script", symbols);

        assertSymbol(symbols, "outputMissing", "");
        assertSymbol(symbols, "outputEmptyArray", "");
        assertSymbol(symbols, "outputEmptyCollection", "");
        assertSymbol(symbols, "outputSingle", "SINGLE");
        assertSymbol(symbols, "outputArray", "ALPHA\n\nBETA\n\nGAMMA");
        assertSymbol(symbols, "outputCollection", "MOE\n\nLARRY\n\nCURLY");

        // Tests for the "index" attribute
        assertSymbol(symbols, "outputMissingIndex", "");
        assertSymbol(symbols, "outputEmptyArrayIndex", "");
        assertSymbol(symbols, "outputEmptyCollectionIndex", "");
        assertSymbol(symbols, "outputSingleIndex", "SINGLE 0");
        assertSymbol(symbols, "outputArrayIndex", "ALPHA 0\n\nBETA 1\n\nGAMMA 2");
        assertSymbol(symbols, "outputCollectionIndex", "MOE 0\n\nLARRY 1\n\nCURLY 2");
        
        // Test implied key
        assertSymbol(symbols, "outputCollectionIndexOnly", "0\n\n1\n\n2");
    }

    public void testIncludeScript() throws Exception
    {
        IScript script = execute("include-script.script", null);

        IResourceLocation scriptLocation = script.getScriptLocation();

        IResourceLocation[] expected =
            new IResourceLocation[] {
                scriptLocation.getRelativeLocation("first"),
                scriptLocation.getRelativeLocation("second"),
                scriptLocation.getRelativeLocation("third")};

        assertEquals(
            "included scripts",
            Arrays.asList(expected),
            Arrays.asList(_processor.getExternalScripts()));
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

        ScriptSession session = new ScriptSession(script.getScriptLocation(), null, null, null);
        assertEquals("ScriptSession[" + script.getScriptLocation() + "]", session.toString());
    }
}
