/* ----------------------------------------------------------------------
 * Copyright (c) 2002 by WebCT.
 * All Rights Reserved
 *
 * Permission to use this program (WebCT) and its related files is at the
 * discretion of WebCT.
 *
 * The licensee of WebCT agrees that:
 *    - Redistribution in whole or in part is not permitted.
 *    - Modification of source is not permitted.
 *    - Use of the source in whole or in part outside of WebCT is not
 *      permitted.
 *
 * THIS SOFTWARE IS PROVIDED ''AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL WEBCT OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * ----------------------------------------------------------------------
 */

package net.sf.tapestry.junit.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IScript;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.script.ScriptParser;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 *  A collection of tests for Tapestry scripting.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class ScriptTest extends TestCase
{

    public ScriptTest(String name)
    {
        super(name);
    }

    private IScript read(String file) throws IOException, DocumentParseException
    {
        ScriptParser parser = new ScriptParser(new DefaultResourceResolver());

        InputStream stream = getClass().getResourceAsStream(file);

        IScript result = parser.parse(stream, file);

        stream.close();

        return result;
    }

    private ScriptSession execute(String file, Map symbols) throws ScriptException, DocumentParseException, IOException
    {
        IScript script = read(file);

        return script.execute(symbols);
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
        ScriptSession session = execute("simple.script", null);

        assertEquals("body", "\nBODY\n", session.getBody());
        assertEquals("initialization", "\nINITIALIZATION\n", session.getInitialization());
        assertNull("", session.getIncludedScripts());
    }

    /**
     *  Test omitting body and initialization, ensure they return null.
     * 
     **/

    public void testEmpty() throws Exception
    {
        ScriptSession session = execute("empty.script", null);

        assertNull("body", session.getBody());
        assertNull("initialization", session.getInitialization());
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

        ScriptSession session = execute("let.script", symbols);

        // Unlike body, the let element trims whitespace.

        String outputSymbol = "output: " + inputSymbol;

        assertEquals("Output symbol", outputSymbol, symbols.get("outputSymbol"));

        assertSame("Session symbols", symbols, session.getSymbols());
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

        ScriptSession session = execute("if.script", symbols);

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

        ScriptSession session = execute("if-not.script", symbols);

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

        ScriptSession session = execute("foreach.script", symbols);

        assertSymbol(symbols, "outputMissing", "");
        assertSymbol(symbols, "outputEmptyArray", "");
        assertSymbol(symbols, "outputEmptyCollection", "");
        assertSymbol(symbols, "outputSingle", "SINGLE");
        assertSymbol(symbols, "outputArray", "ALPHA\n\nBETA\n\nGAMMA");
        assertSymbol(symbols, "outputCollection", "MOE\n\nLARRY\n\nCURLY");
    }

    public void testIncludeScript() throws Exception
    {
        ScriptSession session = execute("include-script.script", null);

        List expected = Arrays.asList(new String[] { "first", "second", "third" });

        assertEquals("included scripts", expected, session.getIncludedScripts());
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

        ScriptSession session = execute("ant-syntax.script", symbols);

        assertSymbol(symbols, "functionName", "gallahad_lancelot");
        assertSymbol(symbols, "incomplete1", "Incomplete: $");
        assertSymbol(symbols, "incomplete2", "Incomplete: ${");
        assertSymbol(symbols, "nopath", "This ${} ends up as literal.");
        assertSymbol(symbols, "OGNL", "This is a brace: }.");
    }

    public void testSet() throws Exception
    {
        Map symbols = new HashMap();

        ScriptSession session = execute("set.script", symbols);

        assertSymbol(symbols, "element2", new Character('p'));
    }

    public void testInvalidKeyLet() throws Exception
    {
        try
        {
            execute("invalid-key-let.script", new HashMap());

            throw new AssertionFailedError("Exception expected.");
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

            throw new AssertionFailedError("Exception expected.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "key");
        }
    }

    private void checkException(Exception ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionFailedError("Exception " + ex + " does not contain sub-string '" + string + "'.");
    }

    public void testInputSymbolClass() throws Exception
    {
        try
        {
            Map symbols = new HashMap();
            symbols.put("input", new Integer(20));

            execute("input-symbol-class.script", symbols);

            throw new AssertionFailedError("Exception expected.");
        }
        catch (ScriptException ex)
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

            throw new AssertionFailedError("Exception expected.");
        }
        catch (ScriptException ex)
        {
            checkException(ex, "required");
        }
    }

    public void testInputSymbolInvalidKey() throws Exception
    {
        try
        {
            execute("input-symbol-invalid-key.script", new HashMap());

            throw new AssertionFailedError("Exception expected.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "key");
        }

    }
}
