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
 *  
 *  Like {@link org.apache.tapestry.script.LetToken}, but sets the value
 *  from an expression attribute, rather than a body of full content.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

class SetToken extends AbstractToken
{
    private String _key;
    private String _expression;

    SetToken(String key, String expression, ILocation location)
    {
        super(location);
        _key = key;
        _expression = expression;
    }

    /**
     *   
     *  Doesn't <em>write</em>, it evaluates the expression and assigns
     *  the result back to the key. 
     * 
     **/

    public void write(StringBuffer buffer, ScriptSession session)
    {

        Object value = evaluate(_expression, session);

        session.getSymbols().put(_key, value);
    }

}