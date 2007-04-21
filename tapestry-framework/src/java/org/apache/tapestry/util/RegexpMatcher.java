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

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.oro.text.regex.Perl5Compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Streamlines the interface to ORO by implicitly constructing the necessary compilers and matchers,
 * and by caching compiled patterns.
 * 
 */

public class RegexpMatcher
{
    private static final int MAX_ACTIVE = 100;
    
    private static final long SLEEP_TIME = 1000 * 60 * 4;
    
    private final KeyedPoolableObjectFactory _factory = new RegexpPoolObjectFactory();
    
    private final GenericKeyedObjectPool _pool;
    
    private Map _escapedPatternStrings = new HashMap();
    
    public RegexpMatcher()
    {
        _pool = new GenericKeyedObjectPool(_factory, MAX_ACTIVE, GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK, -1);
        
        _pool.setMaxIdle(MAX_ACTIVE / 2);
        _pool.setMinEvictableIdleTimeMillis(SLEEP_TIME);
        _pool.setTimeBetweenEvictionRunsMillis(SLEEP_TIME);
    }
    
    /**
     * Clears any previously compiled patterns.
     */
    public void clear()
    {
        _pool.clear();
    }
    
    public boolean matches(String pattern, String input)
    {
        Pattern compiled = null;
        
        try {
            
            compiled = (Pattern)_pool.borrowObject(pattern);
            
            return compiled.matcher(input).matches();
            
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
            
            return compiled.matcher(input).find();
            
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

            Matcher matcher = compiled.matcher(input);
            List matches = new ArrayList();
            
            while (matcher.find())
            {
                int length = matcher.groupCount();
                String[] groups = new String[length + 1];
                groups[0] = matcher.group();
                
                for (int i=1; i <= length; i++) {
                    groups[i] = matcher.group(i);
                }

                matches.add(new RegexpMatch(length, groups));
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

            Matcher matcher = compiled.matcher(input);
            List matches = new ArrayList();
            
            while (matcher.find())
            {
                String matchedInput = matcher.group(subgroup);
                
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
