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

import java.util.Locale;
import java.util.NoSuchElementException;

import org.apache.tapestry.Tapestry;

/**
 *  Used in a wide variety of resource searches.  Generates
 *  a series of name variations from a base name, a 
 *  {@link java.util.Locale} and an optional suffix.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class LocalizedNameGenerator
{
    private int _baseNameLength;
    private String _suffix;
    private StringBuffer _buffer;
    private String _language;
    private String _country;
    private String _variant;
    private int _state;

    private static final int INITIAL = 0;
    private static final int LCV = 1;
    private static final int LC = 2;
    private static final int LV = 3;
    private static final int L = 4;
    private static final int BARE = 5;
    private static final int EXHAUSTED = 6;

    public LocalizedNameGenerator(String baseName, Locale locale, String suffix)
    {
        _baseNameLength = baseName.length();

        if (locale != null)
        {
            _language = locale.getLanguage();
            _country = locale.getCountry();
            _variant = locale.getVariant();
        }

        _state = INITIAL;

        _suffix = suffix;

        _buffer = new StringBuffer(baseName);

        advance();
    }

    private void advance()
    {
        while (_state != EXHAUSTED)
        {
            _state++;

            switch (_state)
            {
                case LCV :

                    if (Tapestry.isNull(_variant))
                        continue;

                    return;

                case LC :

                    if (Tapestry.isNull(_country))
                        continue;

                    return;

                case LV :

                    // If _country is null, then we've already generated this string
                    // as state LCV and we can continue directly to state L
                    
                    if (Tapestry.isNull(_variant) || Tapestry.isNull(_country))
                        continue;

                    return;

                case L :

                    if (Tapestry.isNull(_language))
                        continue;

                    return;

                default :
                    return;
            }
        }
    }

    /**
     *  Returns true if there are more name variants to be
     *  returned, false otherwise.
     * 
     **/

    public boolean more()
    {
        return _state != EXHAUSTED;
    }

    /**
     *  Returns the next localized variant.
     * 
     *  @throws NoSuchElementException if all variants have been
     *  returned.
     * 
     **/

    public String next()
    {
        if (_state == EXHAUSTED)
            throw new NoSuchElementException();

        String result = build();

        advance();

        return result;
    }

    private String build()
    {
        _buffer.setLength(_baseNameLength);

        if (_state == LC || _state == LCV || _state == L)
        {
            _buffer.append('_');
            _buffer.append(_language);
        }

        // For LV, we want two underscores between language
        // and variant.

        if (_state == LC || _state == LCV || _state == LV)
        {
            _buffer.append('_');

            if (_state != LV)
                _buffer.append(_country);
        }

        if (_state == LV || _state == LCV)
        {
            _buffer.append('_');
            _buffer.append(_variant);
        }

        if (_suffix != null)
            _buffer.append(_suffix);

        return _buffer.toString();
    }
}
