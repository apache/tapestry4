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

package org.apache.tapestry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 *  Streamlines the interface to ORO by implicitly constructing the
 *  necessary compilers and matchers, and by
 *  caching compiled patterns.
 *
 *  @author Howard Lewis Ship
 *  @since 3.0
 *
 **/

public class RegexpMatcher
{
    private PatternCompiler _patternCompiler;

    private PatternMatcher _matcher;

    private Map _compiledPatterns = new HashMap();

    private Map _escapedPatternStrings = new HashMap();

    protected Pattern compilePattern(String pattern)
    {
        if (_patternCompiler == null)
            _patternCompiler = new Perl5Compiler();

        try
        {
            return _patternCompiler.compile(pattern, Perl5Compiler.SINGLELINE_MASK);
        }
        catch (MalformedPatternException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    protected Pattern getCompiledPattern(String pattern)
    {
        Pattern result = (Pattern) _compiledPatterns.get(pattern);

        if (result == null)
        {
            result = compilePattern(pattern);
            _compiledPatterns.put(pattern, result);
        }

        return result;
    }

    /**
     *  Clears any previously compiled patterns.
     * 
     **/

    public void clear()
    {
        _compiledPatterns.clear();
    }

    protected PatternMatcher getPatternMatcher()
    {
        if (_matcher == null)
            _matcher = new Perl5Matcher();

        return _matcher;
    }

    public boolean matches(String pattern, String input)
    {
        Pattern compiledPattern = getCompiledPattern(pattern);

        return getPatternMatcher().matches(input, compiledPattern);
    }

    public boolean contains(String pattern, String input)
    {
        Pattern compiledPattern = getCompiledPattern(pattern);

        return getPatternMatcher().contains(input, compiledPattern);
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
     * @param pattern the regexp pattern for matching
     * @param input the string to search for matches within
     * @param subgroup the group (sub-expression) within the pattern to return as a match
     * @return array (possibly empty) of matching strings
     * 
     */
    public String[] getMatches(String pattern, String input, int subgroup)
    {
        Pattern compiledPattern = getCompiledPattern(pattern);

        PatternMatcher matcher = getPatternMatcher();
        PatternMatcherInput matcherInput = new PatternMatcherInput(input);

        List matches = new ArrayList();

        while (matcher.contains(matcherInput, compiledPattern))
        {
            MatchResult match = matcher.getMatch();

            String matchedInput = match.group(subgroup);

            matches.add(matchedInput);
        }

        return (String[]) matches.toArray(new String[matches.size()]);
    }

}
