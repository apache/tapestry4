/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.tapestry.ApplicationRuntimeException;

/**
 *  Streamlines the interface to ORO by implicitly constructing the
 *  necessary compilers and matchers, and by
 *  caching compiled patterns.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
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

}
