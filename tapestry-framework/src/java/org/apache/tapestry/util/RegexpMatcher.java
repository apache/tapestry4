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

package org.apache.tapestry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * Streamlines the interface to ORO by implicitly constructing the necessary compilers and matchers,
 * and by caching compiled patterns.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class RegexpMatcher
{
    private static final int MAX_ACTIVE = 100;
    
    private static final long SLEEP_TIME = 1000 * 60 * 2;
    
    private PatternMatcher _matcher;
    
    private final KeyedPoolableObjectFactory _factory = new RegexpPoolObjectFactory();
    
    private final GenericKeyedObjectPool _pool;
    
    private Map _escapedPatternStrings = new HashMap();
    
    public RegexpMatcher()
    {
        _pool = new GenericKeyedObjectPool(_factory, MAX_ACTIVE, GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK, -1);
        
        _pool.setMaxIdle(MAX_ACTIVE / 2);
        _pool.setMinEvictableIdleTimeMillis(MAX_ACTIVE);
        _pool.setTimeBetweenEvictionRunsMillis(SLEEP_TIME);
    }
    
    /**
     * Clears any previously compiled patterns.
     */
    public void clear()
    {
        _pool.clear();
    }

    protected PatternMatcher getPatternMatcher()
    {
        if (_matcher == null)
            _matcher = new Perl5Matcher();

        return _matcher;
    }

    public boolean matches(String pattern, String input)
    {
        Pattern compiled = null;
        
        try {
            
            compiled = (Pattern)_pool.borrowObject(pattern);
            
            return getPatternMatcher().matches(input, compiled);
            
        } catch (Exception e) {
            
            throw new ApplicationRuntimeException(e);
        } finally {
            
            try { _pool.returnObject(pattern, compiled); } catch (Throwable t) { }
        }
    }
    
    public boolean contains(String pattern, String input)
    {
        Pattern compiled = null;
        
        try {
            
            compiled = (Pattern)_pool.borrowObject(pattern);
            
            return getPatternMatcher().contains(input, compiled);
            
        } catch (Exception e) {
            
            throw new ApplicationRuntimeException(e);
        } finally {
            
            try { _pool.returnObject(pattern, compiled); } catch (Throwable t) { }
        }
    }

    public String getEscapedPatternString(String pattern)
    {
        String result = (String) _escapedPatternStrings.get(pattern);
        
        if (result == null)
        {
            result = Perl5Compiler.quotemeta(pattern);
            
            _escapedPatternStrings.put(pattern, result);
        }
        
        return result;
    }
    
    /**
     * Given an input string, finds all matches in an input string for the pattern.
     * 
     * @param pattern
     *            the regexp pattern for matching
     * @param input
     *            the string to search for matches within
     * @return array (possibly empty) of matches
     * @since 4.0
     */
    public RegexpMatch[] getMatches(String pattern, String input)
    {
        Pattern compiled = null;
        
        try {
            
            compiled = (Pattern)_pool.borrowObject(pattern);
            
            PatternMatcher matcher = getPatternMatcher();
            PatternMatcherInput matcherInput = new PatternMatcherInput(input);

            List matches = new ArrayList();
            
            while (matcher.contains(matcherInput, compiled))
            {
                MatchResult match = matcher.getMatch();

                matches.add(new RegexpMatch(match));
            }
            
            return (RegexpMatch[]) matches.toArray(new RegexpMatch[matches.size()]);
            
        } catch (Exception e) {
            
            throw new ApplicationRuntimeException(e);
        } finally {
            
            try { _pool.returnObject(pattern, compiled); } catch (Throwable t) { }
        }
    }

    /**
     * Given an input string, finds all matches in an input string for the pattern.
     * 
     * @param pattern
     *            the regexp pattern for matching
     * @param input
     *            the string to search for matches within
     * @param subgroup
     *            the group (sub-expression) within the pattern to return as a match
     * @return array (possibly empty) of matching strings
     */
    public String[] getMatches(String pattern, String input, int subgroup)
    {
        Pattern compiled = null;
        
        try {

            compiled = (Pattern)_pool.borrowObject(pattern);
            
            PatternMatcher matcher = getPatternMatcher();
            PatternMatcherInput matcherInput = new PatternMatcherInput(input);
            
            List matches = new ArrayList();
            
            while (matcher.contains(matcherInput, compiled))
            {
                MatchResult match = matcher.getMatch();

                String matchedInput = match.group(subgroup);

                matches.add(matchedInput);
            }

            return (String[]) matches.toArray(new String[matches.size()]);
            
        } catch (Exception e) {

            throw new ApplicationRuntimeException(e);
        } finally {

            try { _pool.returnObject(pattern, compiled); } catch (Throwable t) { }
        }
    }

}
