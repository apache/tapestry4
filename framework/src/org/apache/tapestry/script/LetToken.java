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

import java.util.Map;

import org.apache.tapestry.ILocation;

/**
 *  Allows for the creation of new symbols that can be used in the script
 *  or returned to the caller.
 *
 *  <p>The &lt;let&gt; tag wraps around static text and &lt;insert&gt;
 *  elements.  The results are trimmed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

class LetToken extends AbstractToken
{
    private String _key;
    private boolean _unique;
    private int _bufferLengthHighwater = 20;

    public LetToken(String key, boolean unique, ILocation location)
    {
        super(location);

        _key = key;
        _unique = unique;
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        if (buffer != null)
            throw new IllegalArgumentException();

        buffer = new StringBuffer(_bufferLengthHighwater);

        writeChildren(buffer, session);

        // Store the symbol back into the root set of symbols.

        Map symbols = session.getSymbols();

        String value = buffer.toString().trim();

        if (_unique)
            value = session.getProcessor().getUniqueString(value);

        symbols.put(_key, value);

        // Store the buffer length from this run for the next run, since its
        // going to be approximately the right size.

        _bufferLengthHighwater = Math.max(_bufferLengthHighwater, buffer.length());
    }
}