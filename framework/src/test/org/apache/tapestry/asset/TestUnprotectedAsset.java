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

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * Tests for unprotected resource contributions.
 * 
 * @author jkuhnert
 */
public class TestUnprotectedAsset extends HiveMindTestCase
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

        assertEquals("a5f4663532ea3efe22084df086482290", s
                .getDigestForResource("/org/apache/tapestry/asset/tapestry-in-action.png"));

        long currTime = System.currentTimeMillis();
        s.getDigestForResource("/org/apache/tapestry/asset/tapestry-in-action.png");
        long drtime = System.currentTimeMillis() - currTime;

        currTime = System.currentTimeMillis();
        matcher.contains("/org/apache/tapestry/asset/tapestry-in-action.png", pr);
        long urtime = System.currentTimeMillis() - currTime;

        assertFalse("Urtime > drtime: " + urtime + " > " + drtime, urtime < drtime);
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
        rm.setContributions(patterns);
        rm.initializeService();
        
        assertFalse(rm.containsResource("/org/apache/tapestry/asset/AbstractAsset.class"));
        assertFalse(rm.containsResource("/org/apache/tapestry/.*.class"));
        assertTrue(rm.containsResource("/org/apache/tapestry/asset/assetBuilder.js"));
        assertTrue(rm.containsResource("/org/apache/tapestry/asset/foo.txt"));
        assertFalse(rm.containsResource("/org/apache/tapestry/asset/foo.TXT"));
        assertTrue(rm.containsResource("/org/apache/tapestry/asset/subdirectory/foo.css"));
    }
}
