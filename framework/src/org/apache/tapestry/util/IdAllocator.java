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

import java.util.HashMap;
import java.util.Map;

/**
 *  Used to "uniquify" names within a given context.  A base name
 *  is passed in, and the return value is the base name, or the base name
 *  extended with a suffix to make it unique.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class IdAllocator
{
    private Map _generatorMap = new HashMap();

    private static class NameGenerator
    {
        private String _baseId;
        private int _index;

        NameGenerator(String baseId)
        {
            _baseId = baseId + "$";
        }

        public String nextId()
        {
            return _baseId + _index++;
        }
    }

    /**
     *  Allocates the id.  Repeated calls for the same name will return
     *  "name", "name_0", "name_1", etc.
     * 
     **/

    public String allocateId(String name)
    {
        NameGenerator g = (NameGenerator) _generatorMap.get(name);
        String result = null;

        if (g == null)
        {
            g = new NameGenerator(name);
            result = name;
        }
        else
            result = g.nextId();

        // Handle the degenerate case, where a base name of the form "foo$0" has been
        // requested.  Skip over any duplicates thus formed.
        
        while (_generatorMap.containsKey(result))
            result = g.nextId();

        _generatorMap.put(result, g);

        return result;
    }
    
    /**
     *  Clears the allocator, resetting it to freshly allocated state.
     * 
     **/
    
    public void clear()
    {
        _generatorMap.clear();
    }
}
