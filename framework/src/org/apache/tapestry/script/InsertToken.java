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

import org.apache.tapestry.ILocation;

/**
 *  A token that writes the value of a property using a property path
 *  routed in the symbols..
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class InsertToken extends AbstractToken
{
    private String _expression;

    InsertToken(String expression, ILocation location)
    {
        super(location);

        _expression = expression;
    }

    /**
     *  Gets the named symbol from the symbols {@link Map}, verifies that
     *  it is a String, and writes it to the {@link Writer}.
     *
     **/

    public void write(StringBuffer buffer, ScriptSession session)
    {
        Object value = evaluate(_expression, session);

        if (value != null)
            buffer.append(value);
    }

    public void addToken(IScriptToken token)
    {
        // Should never be invoked.
    }

}