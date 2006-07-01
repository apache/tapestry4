// Copyright 2004, 2005, 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.asset;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

/**
 * Tests for unprotected resource contributions.
 * 
 * @author jkuhnert
 */
@Test
public class TestUnprotectedAsset extends BaseComponentTestCase
{

    PatternCompiler compiler = new Perl5Compiler();
    PatternMatcher matcher = new Perl5Matcher();

    /**
     * Tests for regexp patterns describing unprotected resources.
     */
    public void testUnProtectedMatch()
    {
        Pattern pr = newPattern("org/apache/tapestry/asset/.*.txt");
        
        assertFalse(matcher.contains("org/apache/tapestry/foobar.png", pr));
        assertTrue(matcher.contains("org/apache/tapestry/asset/base-resource.txt", pr));
        assertFalse(matcher.contains("org/apache/tapestry/asset/foobar.png", pr));
    }

    /**
     * Creates {@link Pattern} objects for regexp matching.
     * 
     * @param pattern
     * @return
     */
    protected Pattern newPattern(String pattern)
    {
        Pattern pr = null;
        try
        {
            pr = compiler.compile("org/apache/tapestry/asset/.*.txt", Perl5Compiler.READ_ONLY_MASK
                    | Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.MULTILINE_MASK);
        }
        catch (MalformedPatternException e)
        {
            unreachable();
        }

        return pr;
    }

    /**
     * Tests and asserts that it doesn't take ~longer~ to work with undigested
     * resources using patterns than normal digested resources.
     */
    public void testResourcePerformanceComparison()
    {
        Pattern pr = newPattern("/org/apache/tapestry/asset/tapestry-in-action.png");

        ResourceDigestSourceImpl s = new ResourceDigestSourceImpl();
        s.setClassResolver(new DefaultClassResolver());
        
        // call methods once to be sure things get cached properly
        
        assertEquals("a5f4663532ea3efe22084df086482290", s
                .getDigestForResource("/org/apache/tapestry/asset/tapestry-in-action.png"));
        
        matcher.contains("/org/apache/tapestry/asset/tapestry-in-action.png", pr);
        
        long currTime = System.currentTimeMillis();
        s.getDigestForResource("/org/apache/tapestry/asset/tapestry-in-action.png");
        long drtime = System.currentTimeMillis() - currTime;

        currTime = System.currentTimeMillis();
        matcher.contains("/org/apache/tapestry/asset/tapestry-in-action.png", pr);
        long urtime = System.currentTimeMillis() - currTime;

        assertFalse("Urtime > drtime: " + urtime + " > " + drtime, urtime < drtime);
    }
    
    /**
     * Tests new path ordering encoding.
     */
    public void testPathComparator()
    {
        Map parameters = new TreeMap(new AssetComparator());
        
        parameters.put(ServiceConstants.SERVICE, "test");
        parameters.put("PATH", "value");
        parameters.put("digest", "digvalue");
        
        assertEquals("test", parameters.get(ServiceConstants.SERVICE));
        assertEquals("value", parameters.get("PATH"));
        assertEquals("digvalue", parameters.get("digest"));
        
        int count = parameters.size();
        String[] result = (String[]) parameters.keySet().toArray(new String[count]);
        assertEquals(3, result.length);
        
        assertEquals("PATH", result[2]);
    }
    
    /**
     * Tests the implementation of {@link ResourceMatcher}.
     */
    public void testResourceMatcher()
    {
        ResourceMatcherImpl rm = new ResourceMatcherImpl();
        List patterns = new ArrayList();
        patterns.add("/org/apache/tapestry/asset/.*.txt");
        patterns.add("/org/apache/tapestry/asset/.*.css");
        patterns.add("/org/apache/tapestry/asset/.*.js");
        patterns.add("/org/apache/tapestry/asset/[%$4]rew\\invalidpattern");
        patterns.add("/org/apache/tapestry/html/dojo*");
        patterns.add("org/apache/tapestry/html/dojo/*/*.png");
        rm.setContributions(patterns);
        rm.initializeService();
        
        assertFalse(rm.containsResource("/org/apache/tapestry/asset/AbstractAsset.class"));
        assertFalse(rm.containsResource("/org/apache/tapestry/.*.class"));
        assertTrue(rm.containsResource("/org/apache/tapestry/asset/assetBuilder.js"));
        assertTrue(rm.containsResource("/org/apache/tapestry/asset/foo.txt"));
        assertFalse(rm.containsResource("/org/apache/tapestry/asset/foo.TXT"));
        assertTrue(rm.containsResource("/org/apache/tapestry/asset/subdirectory/foo.css"));
        assertTrue(rm.containsResource("/org/apache/tapestry/html/dojo/"));
        assertTrue(rm.containsResource("/org/apache/tapestry/html/dojo/dojo.js"));
        assertTrue(rm.containsResource("/org/apache/tapestry/html/dojo/src/json.js"));
        assertTrue(rm.containsResource("/org/apache/tapestry/html/dojo/src/test.png"));
    }
    
    public void testCssPaths()
    {
        AssetService service = new AssetService();
        String path = "/dojo/src/widget/template/HtmlComboBox.cssimages/foo.gif";
        
        assertEquals("/dojo/src/widget/template/images/foo.gif", service.translateCssPath(path));
        assertEquals("/boo/templates/things/", 
                service.translateCssPath("/boo/templates/somethingdumb.cssthings/"));
        assertEquals("/foo/path/css/images.png", 
                service.translateCssPath("/foo/path/css/images.png"));
        assertEquals("/things/mytemplate.css",
                service.translateCssPath("/things/mytemplate.css"));
        assertNull(service.translateCssPath(null));
    }
}
