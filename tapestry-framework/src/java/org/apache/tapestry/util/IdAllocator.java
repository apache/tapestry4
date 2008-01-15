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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.TapestryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to "uniquify" names within a given context. A base name is passed in,
 * and the return value is the base name, or the base name extended with a
 * suffix to make it unique.
 *
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class IdAllocator
{

    private static final String SEPARATOR = "_";

    private static final char NAME_SEPARATOR = '$';

    final Map _generatorMap = new HashMap();
    final List _uniqueGenerators = new ArrayList();

    final String _namespace;

    /** Class used only by IdAllocator. */
    private class NameGenerator implements Cloneable
    {
        private final String _baseId;

        private int _index;

        NameGenerator(String baseId)
        {
            _baseId = baseId + SEPARATOR;
        }

        NameGenerator(String baseId, int index)
        {
            _baseId = baseId + SEPARATOR;
            _index = index;
        }

        public String nextId()
        {
            return _baseId + _index++;
        }

        public String peekId()
        {
            return _baseId + _index;
        }

        public String getBaseId()
        {
            return _baseId.substring(0, _baseId.length() - 1);
        }

        /**
         * {@inheritDoc}
         */
        protected Object clone()
                throws CloneNotSupportedException
        {
            return super.clone();
        }

        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (!(o instanceof NameGenerator))
                return false;

            NameGenerator that = (NameGenerator) o;

            if (_baseId != null ? !_baseId.equals(that._baseId) : that._baseId != null)
                return false;

            return true;
        }

        public int hashCode()
        {
            int result;
            result = (_baseId != null ? _baseId.hashCode() : 0);
            result = 31 * result + _index;
            return result;
        }
    }

    public IdAllocator()
    {
        this("");
    }

    public IdAllocator(String namespace)
    {
        Defense.notNull(namespace, "namespace");

        _namespace = namespace;
    }

    /**
     * Allocates the id. Repeated calls for the same name will return "name",
     * "name_0", "name_1", etc.
     *
     * @param name
     *          The base id to allocate new unique ids from.
     *
     * @return A unique version of the passed in id.
     */

    public String allocateId(String name)
    {
        String key = name + _namespace;

        NameGenerator g = (NameGenerator) _generatorMap.get(key.toLowerCase());
        String result = null;

        if (g == null)
        {
            g = new NameGenerator(key);
            _uniqueGenerators.add(g);
            result = key;
        }
        else
            result = g.nextId();

        // Handle the degenerate case, where a base name of the form "foo$0" has
        // been
        // requested. Skip over any duplicates thus formed.

        while(_generatorMap.containsKey(result.toLowerCase()))
            result = g.nextId();

        _generatorMap.put(result.toLowerCase(), g);

        return result;
    }

    /**
     * Should return the exact same thing as {@link #allocateId(String)}, with the difference
     * that the calculated id is not allocated and stored so multiple calls will always return the 
     * same thing. 
     *
     * @param name The name to peek at.
     * @return The next id that will be allocated for the given name.
     */
    public String peekNextId(String name)
    {
        String key = name + _namespace;

        NameGenerator g = (NameGenerator) _generatorMap.get(key.toLowerCase());
        String result = null;

        if (g == null)
        {
            g = new NameGenerator(key);
            result = key;
        } else
            result = g.peekId();

        // Handle the degenerate case, where a base name of the form "foo_0" has
        // been
        // requested. Skip over any duplicates thus formed.

        // in a peek we don't want to actually increment any id state so we must
        // clone

        if (_generatorMap.containsKey(result.toLowerCase())) {

            try {
                NameGenerator cg = (NameGenerator)g.clone();

                while (_generatorMap.containsKey(result.toLowerCase()))
                    result = cg.nextId();

            } catch (CloneNotSupportedException e) {
                throw new ApplicationRuntimeException(e);
            }
        }

        return result;
    }

    /**
     * Creates a custom string representation of the current state of this instance, capable
     * of being re-created by using the corresponding {@link IdAllocator#fromExternalString(String)} method.
     *
     * @return The external string representation of the current state of this instance.
     */
    public String toExternalString()
    {
        StringBuffer str = new StringBuffer(_namespace);

        for (int i=0; i < _uniqueGenerators.size(); i++)
        {
            // namespace is always the first element, so safe to always add comma here

            str.append(",");

            NameGenerator g = (NameGenerator) _uniqueGenerators.get(i);

            str.append(g.getBaseId()).append(NAME_SEPARATOR);
            str.append(g._index);
        }

        return str.toString();
    }

    /**
     * Using the base id and index value,  re-creates the state of generated id values in the
     * internal map to what it would have been when generating ids orginally.
     *
     * @param baseId The base id being seeded.
     * @param index The last known index value used for the id.
     */
    void addSeed(String baseId, int index)
    {
        NameGenerator g = new NameGenerator(baseId, 0);
        _uniqueGenerators.add(g);
        _generatorMap.put(baseId.toLowerCase(), g);

        // add generated key to map until we reach top level index value
        while(g._index != index)
        {
            _generatorMap.put(g.nextId().toLowerCase(), g);
        }
    }

    /**
     * Clears the allocator, resetting it to freshly allocated state.
     */

    public void clear()
    {
        _generatorMap.clear();
        _uniqueGenerators.clear();
    }

    public static IdAllocator fromExternalString(String seed)
    {
        Defense.notNull(seed, "seed");

        String[] values = TapestryUtils.split(seed);
        if (values.length == 0)
        {
            return new IdAllocator();
        }

        String namespace = values[0];

        IdAllocator idAllocator = new IdAllocator(namespace);

        for (int i=1; i < values.length; i++)
        {
            int index = values[i].lastIndexOf(NAME_SEPARATOR);

            if (index < 0)
                continue;

            String baseId = values[i].substring(0, index);
            int valueIndex = Integer.parseInt(values[i].substring(index + 1, values[i].length()));

            idAllocator.addSeed(baseId, valueIndex);
        }

        return idAllocator;
    }
}
