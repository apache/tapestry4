// Copyright May 9, 2006 The Apache Software Foundation
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
package org.apache.tapestry.util;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MatchResult;


/**
 * Tests functionality of {@link ScriptUtils}.
 * 
 * @author jkuhnert
 */
public class ScriptUtilsTest extends HiveMindTestCase
{
    protected static final String JAVASCRIPT_NOCOMMENT = 
        "<script type=\"text/javascript\">"
        + "if (document.updateObject) {"
        + " document.updateObject.progressFinished('updateId');"
        + "}"
        + "</script>";
    
    protected static final String MULTI_JAVASCRIPT_NOCOMMENT = 
        "<script type=\"text/javascript\">"
        + "if (document.updateObject) {"
        + " document.updateObject.progressFinished('updateId');"
        + "}"
        + "if (document.updateObject) {"
        + " document.updateObject.progressFinished('updateId');"
        + "}"
        + "</script>"
        + JAVASCRIPT_NOCOMMENT
        + JAVASCRIPT_NOCOMMENT;
    
    protected static final String TEST_INPUT1 =
        JAVASCRIPT_NOCOMMENT 
        + "some text is here";
    
    protected static final String TEST_INPUT2 =
        "beginning text"
        + JAVASCRIPT_NOCOMMENT 
        + "some text is here";
    
    protected static final String TEST_INPUT3 =
        JAVASCRIPT_NOCOMMENT 
        + "Here yee"
        + JAVASCRIPT_NOCOMMENT
        + "some text is here";
    
    protected static final String TEST_INPUT4 =
        TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3
        + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3 + TEST_INPUT3;
    
    protected static final String TEST_INPUT_INCLUDE =
        JAVASCRIPT_NOCOMMENT 
        + "some text is here"
        + "<script type=\"text/javascript\" src=\"http://yourmom.js\"></script>";
    
    public static final String BEGIN_COMMENT = "//<![CDATA[";
    public static final String END_COMMENT = "//]]>";
    
    /**
     * Tests finding {@link #JAVASCRIPT_NOCOMMENT} with 
     * regular expressions.
     * @testng.test groups = "functest", "util"
     */
    public void testFindScript()
    {
        Perl5Util util = new Perl5Util();
        String expr = "/(?:<script.*?>)((\\n|.)*?)(?:<\\/script>)/";
        
        assertTrue(util.match(expr, JAVASCRIPT_NOCOMMENT));
        
        MatchResult result = util.getMatch();
        assertNotNull(result);
        assertEquals(3, result.groups());
        assertEquals("if (document.updateObject) { document.updateObject.progressFinished('updateId');}",
                result.group(1));
    }
    
    /**
     * Tests finding {@link #JAVASCRIPT_NOCOMMENT} with 
     * regular expressions.
     * @testng.test groups = "functest", "util"
     */
    public void testFindMultipleScripts()
    {
        Perl5Util util = new Perl5Util();
        String expr = "/(?:<script.*?>)((\\n|.)*?)(?:<\\/script>)/";
        
        assertTrue(util.match(expr, MULTI_JAVASCRIPT_NOCOMMENT));
        
        MatchResult result = util.getMatch();
        assertNotNull(result);
        assertEquals(3, result.groups());
        assertEquals("if (document.updateObject) { document.updateObject.progressFinished('updateId');}"
                + "if (document.updateObject) { document.updateObject.progressFinished('updateId');}",
                result.group(1));
    }
    
    /**
     * Calls {@link ScriptUtils#ensureValidScriptTags(String)} with 
     * {@link #JAVASCRIPT_NOCOMMENT} and tests that it returns a validly
     * marked up content block capable of being embedded in an xml document.
     * @testng.test groups = "functest" , "util"
     */
    public void testEnsureValidScriptTags()
    {
        assertEquals(ScriptUtils.ensureValidScriptTags(JAVASCRIPT_NOCOMMENT),
                ScriptUtils.BEGIN_COMMENT
                + "if (document.updateObject) { document.updateObject.progressFinished('updateId');}"
                + ScriptUtils.END_COMMENT);
        
        //Test other various non jscript text
        assertNull(null, ScriptUtils.ensureValidScriptTags(null));
        assertEquals("", ScriptUtils.ensureValidScriptTags(""));
        assertEquals(ScriptUtils.ensureValidScriptTags("<html>This is html</html>"),
                "<html>This is html</html>");
    }
    
    /**
     * @testng.test groups = "functest" , "util"
     */
    public void testEnsureValidScriptTagsWithHtmlComments()
    {  
        String data = "<!-- some comments1 -->" + TEST_INPUT1 + "<b>test</b><!-- some comments2 -->";
        data += " <!-- some comments3 -->" + TEST_INPUT1 + "<b>test</b><!-- some comments4 -->";
        String result = ScriptUtils.ensureValidScriptTags(data);
        
        assertTrue(result.indexOf("<!-- some comments1 -->") >= 0);
        assertTrue(result.indexOf("<!-- some comments2 -->") >= 0);
        assertTrue(result.indexOf("<!-- some comments3 -->") >= 0);
        assertTrue(result.indexOf("<!-- some comments4 -->") >= 0);
    }
    
    /**
     * Tests that the complete string is returned, with 
     * any js in it "fixed".
     * @testng.test groups = "functest", "util"
     */
    public void testCompleteReturn()
    {
        assertEquals(ScriptUtils.ensureValidScriptTags(TEST_INPUT1),
                ScriptUtils.BEGIN_COMMENT
                + "if (document.updateObject) { document.updateObject.progressFinished('updateId');}"
                + ScriptUtils.END_COMMENT + "some text is here");

        assertEquals(ScriptUtils.ensureValidScriptTags(TEST_INPUT2),
                "beginning text" 
                + ScriptUtils.BEGIN_COMMENT
                + "if (document.updateObject) { document.updateObject.progressFinished('updateId');}"
                + ScriptUtils.END_COMMENT + "some text is here");
        
        assertEquals(ScriptUtils.ensureValidScriptTags(TEST_INPUT3),
                ScriptUtils.BEGIN_COMMENT
                + "if (document.updateObject) { document.updateObject.progressFinished('updateId');}"
                + ScriptUtils.END_COMMENT
                + "Here yee" 
                +  ScriptUtils.BEGIN_COMMENT
                + "if (document.updateObject) { document.updateObject.progressFinished('updateId');}"
                + ScriptUtils.END_COMMENT
                + "some text is here");
    }
}
