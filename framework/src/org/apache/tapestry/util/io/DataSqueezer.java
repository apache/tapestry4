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

package org.apache.tapestry.util.io;

import java.io.IOException;

import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.AdaptorRegistry;

/**
 *  A class used to convert arbitrary objects to Strings and back.
 *  This has particular uses involving HTTP URLs and Cookies.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class DataSqueezer
{
    private static final String NULL_PREFIX = "X";
    private static final char NULL_PREFIX_CH = 'X';

    private static final int ARRAY_SIZE = 90;
    private static final int FIRST_ADAPTOR_OFFSET = 33;

    /**
     *  An array of adaptors; this is used as a cheap lookup-table when unsqueezing.  
     *  Each adaptor is identified by a single ASCII character, in the range of
     *  33 ('!') to 122 (the letter 'z').  The offset into this table
     *  is the character minus 33.
     *
     **/

    private ISqueezeAdaptor[] _adaptorByPrefix = new ISqueezeAdaptor[ARRAY_SIZE];

    /**
     *  AdaptorRegistry cache of adaptors.
     *
     **/

    private AdaptorRegistry _adaptors = new AdaptorRegistry();

    /**
     *  Resource resolver used to deserialize classes.
     * 
     **/

    private IResourceResolver _resolver;

    /**
     *  Creates a new squeezer with the default set of adaptors.
     *
     **/

    public DataSqueezer(IResourceResolver resolver)
    {
        this(resolver, null);
    }

    /**
     *  Creates a new data squeezer, which will have the default set of
     *  adaptors, and may add additional adaptors.
     *
     *  @param adaptors an optional list of adaptors that will be registered to
     *  the data squeezer (it may be null or empty)
     *
     **/

    public DataSqueezer(IResourceResolver resolver, ISqueezeAdaptor[] adaptors)
    {
        _resolver = resolver;

        registerDefaultAdaptors();

        if (adaptors != null)
            for (int i = 0; i < adaptors.length; i++)
                adaptors[i].register(this);
    }

    private void registerDefaultAdaptors()
    {
        new CharacterAdaptor().register(this);
        new StringAdaptor().register(this);
        new IntegerAdaptor().register(this);
        new DoubleAdaptor().register(this);
        new ByteAdaptor().register(this);
        new FloatAdaptor().register(this);
        new LongAdaptor().register(this);
        new ShortAdaptor().register(this);
        new BooleanAdaptor().register(this);
        new SerializableAdaptor().register(this);
        new ComponentAddressAdaptor().register(this);
        new EnumAdaptor().register(this);
    }

    /**
     *  Registers the adaptor with one or more single-character prefixes.
     *
     *  @param prefix one or more characters, each of which will be a prefix for
     *  the adaptor.
     *  @param dataClass the class (or interface) which can be encoded by the adaptor.
     *  @param adaptor the adaptor which to be registered.
     *
     **/

    public synchronized void register(String prefix, Class dataClass, ISqueezeAdaptor adaptor)
    {
        int prefixLength = prefix.length();
        int offset;

        if (prefixLength < 1)
            throw new IllegalArgumentException(Tapestry.getMessage("DataSqueezer.short-prefix"));

        if (dataClass == null)
            throw new IllegalArgumentException(Tapestry.getMessage("DataSqueezer.null-class"));

        if (adaptor == null)
            throw new IllegalArgumentException(Tapestry.getMessage("DataSqueezer.null-adaptor"));

        for (int i = 0; i < prefixLength; i++)
        {
            char ch = prefix.charAt(i);

            if (ch < '!' | ch > 'z')
                throw new IllegalArgumentException(
                    Tapestry.getMessage("DataSqueezer.prefix-out-of-range"));

            offset = ch - FIRST_ADAPTOR_OFFSET;

            if (_adaptorByPrefix[offset] != null)
                throw new IllegalArgumentException(
                    Tapestry.format(
                        "DataSqueezer.adaptor-prefix-taken",
                        prefix.substring(i, i)));

            _adaptorByPrefix[offset] = adaptor;

        }

        _adaptors.register(dataClass, adaptor);
    }

    /**
     *  Squeezes the data object into a String by locating an appropriate
     *  adaptor that can perform the conversion.  data may be null.
     *
     **/

    public String squeeze(Object data) throws IOException
    {
        ISqueezeAdaptor adaptor;

        if (data == null)
            return NULL_PREFIX;

        adaptor = (ISqueezeAdaptor) _adaptors.getAdaptor(data.getClass());

        return adaptor.squeeze(this, data);
    }

    /**
     *  A convience; invokes {@link #squeeze(Object)} for each element in the
     *  data array.  If data is null, returns null.
     *
     **/

    public String[] squeeze(Object[] data) throws IOException
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
     *  Unsqueezes the string.  Note that in a special case, where the first
     *  character of the string is not a recognized prefix, it is assumed
     *  that the string is simply a string, and return with no
     *  change.
     *
     **/

    public Object unsqueeze(String string) throws IOException
    {
        ISqueezeAdaptor adaptor = null;

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
     *  Convienience method for unsqueezing many strings (back into objects).
     *  
     *  <p>If strings is null, returns null.
     * 
     **/

    public Object[] unsqueeze(String[] strings) throws IOException
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
     *  Checks to see if a given prefix character has a registered
     *  adaptor.  This is used by the String adaptor to
     *  determine whether it needs to put a prefix on its String.
     *
     **/

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

    /**
     *  Returns the resource resolver used with this squeezer.
     * 
     *  @since 2.2
     * 
     **/

    public IResourceResolver getResolver()
    {
        return _resolver;
    }

}