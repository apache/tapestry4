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

package org.apache.tapestry.script;

import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.ILocation;
import org.apache.tapestry.Tapestry;

/**
 *  A looping operator, modeled after the Foreach component.  It takes
 *  as its source as property and iterates through the values, updating
 *  a symbol on each pass.
 * 
 *  <p>As of 3.0, the index attribute has been added to foreach to keep 
 *  track of the current index of the iterating collection.</p>
 *
 *  @author Howard Lewis Ship, Harish Krishnaswamy
 *  @version $Id$
 *  @since 1.0.1
 * 
 **/

class ForeachToken extends AbstractToken
{
    private String _key;
    private String _index;
    private String _expression;

    ForeachToken(String key, String index, String expression, ILocation location)
    {
        super(location);

        _key = key;
        _index = index;
        _expression = expression;
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        Map symbols = session.getSymbols();

        Object rawSource = evaluate(_expression, session);

        Iterator i = Tapestry.coerceToIterator(rawSource);
        
        if (i == null)
            return;

        int index = 0;

        while (i.hasNext())
        {
            Object newValue = i.next();

            symbols.put(_key, newValue);
            
            if (_index != null)
            	symbols.put(_index, String.valueOf(index));

            writeChildren(buffer, session);
            
            index++;
        }

        // We leave the last value as a symbol; don't know if that's
        // good or bad.
    }
}