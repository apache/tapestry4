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

package org.apache.tapestry.util.io;

import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.lib.util.StrategyRegistry;
import org.apache.hivemind.lib.util.StrategyRegistryImpl;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.DataSqueezer;

/**
 * A class used to convert arbitrary objects to Strings and back. This has particular uses involving
 * HTTP URLs and Cookies.
 * 
 * @author Howard Lewis Ship
 */

public class DataSqueezerImpl implements DataSqueezer
{
    private static final String NULL_PREFIX = "X";

    private static final char NULL_PREFIX_CH = 'X';

    private static final int ARRAY_SIZE = 90;

    private static final int FIRST_ADAPTOR_OFFSET = 33;

    /**
     * An array of adaptors; this is used as a cheap lookup-table when unsqueezing. Each adaptor is
     * identified by a single ASCII character, in the range of 33 ('!') to 122 (the letter 'z'). The
     * offset into this table is the character minus 33.
     */

    private SqueezeAdaptor[] _adaptorByPrefix = new SqueezeAdaptor[ARRAY_SIZE];

    /**
     * AdaptorRegistry cache of adaptors.
     */

    private StrategyRegistry _adaptors = new StrategyRegistryImpl();

    public void setSqueezeAdaptors(List adaptors)
    {
        Iterator i = adaptors.iterator();

        while (i.hasNext())
        {
            SqueezeAdaptor adaptor = (SqueezeAdaptor) i.next();
            register(adaptor);
        }
    }

    /**
     * Registers the adaptor with one or more single-character prefixes.
     * <p>
     * <b>Note</b>: This method should be used for testing purposes only! Squeeze adaptors are
     * normally injected by HiveMind.
     * 
     * @param adaptor
     *            the adaptor which to be registered.
     */

    public synchronized void register(SqueezeAdaptor adaptor)
    {
        if (adaptor == null)
            throw new IllegalArgumentException(Tapestry.getMessage("DataSqueezer.null-adaptor"));

        String prefix = adaptor.getPrefix();
        int prefixLength = prefix.length();
        int offset;

        if (prefixLength < 1)
            throw new IllegalArgumentException(Tapestry.getMessage("DataSqueezer.short-prefix"));

        Class dataClass = adaptor.getDataClass();
        if (dataClass == null)
            throw new IllegalArgumentException(Tapestry.getMessage("DataSqueezer.null-class"));

        for (int i = 0; i < prefixLength; i++)
        {
            char ch = prefix.charAt(i);

            if (ch < '!' | ch > 'z')
                throw new IllegalArgumentException(Tapestry
                        .getMessage("DataSqueezer.prefix-out-of-range"));

            offset = ch - FIRST_ADAPTOR_OFFSET;

            if (_adaptorByPrefix[offset] != null)
                throw new IllegalArgumentException(Tapestry.format(
                        "DataSqueezer.adaptor-prefix-taken",
                        prefix.substring(i, i)));

            _adaptorByPrefix[offset] = adaptor;

        }

        _adaptors.register(dataClass, adaptor);
    }

    /**
     * Squeezes the data object into a String by locating an appropriate adaptor that can perform
     * the conversion. data may be null.
     */

    public String squeeze(Object data)
    {
        SqueezeAdaptor adaptor;

        if (data == null)
            return NULL_PREFIX;

        adaptor = (SqueezeAdaptor) _adaptors.getStrategy(data.getClass());

        return adaptor.squeeze(this, data);
    }

    /**
     * A convience; invokes {@link #squeeze(Object)}for each element in the data array. If data is
     * null, returns null.
     */

    public String[] squeeze(Object[] data)
    {
        if (data == null)
            return null;

        int length = data.length;
        String[] result;

        result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = squeeze(data[i]);

        return result;
    }

    /**
     * Unsqueezes the string. Note that in a special case, where the first character of the string
     * is not a recognized prefix, it is assumed that the string is simply a string, and return with
     * no change.
     */

    public Object unsqueeze(String string)
    {
        SqueezeAdaptor adaptor = null;

        if (string.equals(NULL_PREFIX))
            return null;

        int offset = string.charAt(0) - FIRST_ADAPTOR_OFFSET;

        if (offset >= 0 && offset < _adaptorByPrefix.length)
            adaptor = _adaptorByPrefix[offset];

        // If the adaptor is not otherwise recognized, the it is simply
        // an encoded String (the StringAdaptor may not have added
        // a prefix).

        if (adaptor == null)
            return string;

        // Adaptor should never be null, because we always supply
        // an adaptor for String

        return adaptor.unsqueeze(this, string);
    }

    /**
     * Convienience method for unsqueezing many strings (back into objects).
     * <p>
     * If strings is null, returns null.
     */

    public Object[] unsqueeze(String[] strings)
    {
        if (strings == null)
            return null;

        int length = strings.length;
        Object[] result;

        result = new Object[length];

        for (int i = 0; i < length; i++)
            result[i] = unsqueeze(strings[i]);

        return result;
    }

    /**
     * Checks to see if a given prefix character has a registered adaptor. This is used by the
     * String adaptor to determine whether it needs to put a prefix on its String.
     */

    public boolean isPrefixRegistered(char prefix)
    {
        int offset = prefix - FIRST_ADAPTOR_OFFSET;

        // Special case for handling nulls.

        if (prefix == NULL_PREFIX_CH)
            return true;

        if (offset < 0 || offset >= _adaptorByPrefix.length)
            return false;

        return _adaptorByPrefix[offset] != null;
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer();
        buffer.append("DataSqueezer[adaptors=<");
        buffer.append(_adaptors.toString());
        buffer.append(">]");

        return buffer.toString();
    }
}