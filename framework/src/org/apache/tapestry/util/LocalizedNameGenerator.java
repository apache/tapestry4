//  Copyright 2004 The Apache Software Foundation
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
 *  @since 3.0
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
    private int _prevState;

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
        _prevState = INITIAL;

        _suffix = suffix;

        _buffer = new StringBuffer(baseName);

        advance();
    }

    private void advance()
    {
        _prevState = _state;

        while (_state != EXHAUSTED)
        {
            _state++;

            switch (_state)
            {
                case LCV :

                    if (Tapestry.isBlank(_variant))
                        continue;

                    return;

                case LC :

                    if (Tapestry.isBlank(_country))
                        continue;

                    return;

                case LV :

                    // If _country is null, then we've already generated this string
                    // as state LCV and we can continue directly to state L

                    if (Tapestry.isBlank(_variant) || Tapestry.isBlank(_country))
                        continue;

                    return;

                case L :

                    if (Tapestry.isBlank(_language))
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

    public Locale getCurrentLocale()
    {
        switch (_prevState)
        {
            case LCV :

                return new Locale(_language, _country, _variant);

            case LC :

                return new Locale(_language, _country, "");

            case LV :

                return new Locale(_language, "", _variant);

            case L :

                return new Locale(_language, "", "");

            default :
                return null;
        }
    }
}
