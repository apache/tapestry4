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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.tapestry.IScript;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.script.ScriptParser;

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

    private IScript read(String file) throws Exception
    {
        ScriptParser parser = new ScriptParser();

        InputStream stream = getClass().getResourceAsStream(file);

        IScript result = parser.parse(stream, file);

        stream.close();

        return result;
    }

    private ScriptSession execute(String file, Map symbols) throws Exception
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
        symbols.put("input-symbol", inputSymbol);

        ScriptSession session = execute("let.script", symbols);

        // Unlike body, the let element trims whitespace.

        String outputSymbol = "output: " + inputSymbol;

        assertEquals("Output symbol", outputSymbol, symbols.get("output-symbol"));

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

        input.put("true-string", "anything");
        input.put("false-string", " ");
        input.put("boolean-true", Boolean.TRUE);
        input.put("boolean-false", Boolean.FALSE);
        input.put("collection-non-empty", Collections.singletonList(Boolean.TRUE));
        input.put("collection-empty", new ArrayList());
        input.put("array-nonempty", new String[] { "alpha", "beta" });
        input.put("array-empty", new Integer[0]);
        input.put("number-zero", new Long(0));
        input.put("number-nonzero", new Integer(1));

        Map symbols = new HashMap();
        symbols.put("input", input);

        ScriptSession session = execute("if.script", symbols);

        assertSymbol(symbols, "output-true-string", "TRUE-STRING");
        assertSymbol(symbols, "output-false-string", "");
        assertSymbol(symbols, "output-null", "");
        assertSymbol(symbols, "output-boolean-true", "BOOLEAN-TRUE");
        assertSymbol(symbols, "output-boolean-false", "");
        assertSymbol(symbols, "output-collection-nonempty", "COLLECTION-NON-EMPTY");
        assertSymbol(symbols, "output-collection-empty", "");
        assertSymbol(symbols, "output-array-nonempty", "ARRAY-NON-EMPTY");
        assertSymbol(symbols, "output-array-empty", "");
        assertSymbol(symbols, "output-number-zero", "");
        assertSymbol(symbols, "output-number-nonzero", "NUMBER-NON-ZERO");
    }

    /**
     *  Tests the if-not element.
     * 
     **/

    public void testIfNot() throws Exception
    {
        Map input = new HashMap();

        input.put("true-string", "anything");
        input.put("false-string", " ");
        input.put("boolean-true", Boolean.TRUE);
        input.put("boolean-false", Boolean.FALSE);
        input.put("collection-non-empty", Collections.singletonList(Boolean.TRUE));
        input.put("collection-empty", new ArrayList());
        input.put("array-nonempty", new String[] { "alpha", "beta" });
        input.put("array-empty", new Integer[0]);
        input.put("number-zero", new Long(0));
        input.put("number-nonzero", new Integer(1));

        Map symbols = new HashMap();
        symbols.put("input", input);

        ScriptSession session = execute("if-not.script", symbols);

        assertSymbol(symbols, "output-true-string", "");
        assertSymbol(symbols, "output-false-string", "FALSE-STRING");
        assertSymbol(symbols, "output-null", "NULL");
        assertSymbol(symbols, "output-boolean-true", "");
        assertSymbol(symbols, "output-boolean-false", "BOOLEAN-FALSE");
        assertSymbol(symbols, "output-collection-nonempty", "");
        assertSymbol(symbols, "output-collection-empty", "COLLECTION-EMPTY");
        assertSymbol(symbols, "output-array-nonempty", "");
        assertSymbol(symbols, "output-array-empty", "ARRAY-EMPTY");
        assertSymbol(symbols, "output-number-zero", "NUMBER-ZERO");
        assertSymbol(symbols, "output-number-nonzero", "");
    }

    /**
     *  Tests a bunch of variations on the foreach element.
     * 
     **/

    public void testForeach() throws Exception
    {
        Map input = new HashMap();
        input.put("single", "SINGLE");
        input.put("empty-array", new String[0]);
        input.put("array", new String[] { "ALPHA", "BETA", "GAMMA" });
        input.put("collection", Arrays.asList(new String[] { "MOE", "LARRY", "CURLY" }));
        input.put("empty-collection", new ArrayList());

        Map symbols = new HashMap();
        symbols.put("input", input);

        ScriptSession session = execute("foreach.script", symbols);

        assertSymbol(symbols, "output-missing", "");
        assertSymbol(symbols, "output-empty-array", "");
        assertSymbol(symbols, "output-empty-collection", "");
        assertSymbol(symbols, "output-single", "SINGLE");
        assertSymbol(symbols, "output-array", "ALPHA\n\nBETA\n\nGAMMA");
        assertSymbol(symbols, "output-collection", "MOE\n\nLARRY\n\nCURLY");
    }

    public void testIncludeScript() throws Exception
    {
        ScriptSession session = execute("include-script.script", null);

        List expected = Arrays.asList(new String[] { "first", "second", "third" });

        assertEquals("included scripts", expected, session.getIncludedScripts());
    }
    
    public void testAntSyntax()
    throws Exception
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
    }
}
