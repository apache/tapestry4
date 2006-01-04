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

package org.apache.tapestry.script;

import org.apache.hivemind.Location;

/**
 * A conditional portion of the generated script.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.1
 */

class IfToken extends AbstractToken
{
    private boolean _condition;

    private String _expression;

    IfToken(boolean condition, String expression, Location location)
    {
        super(location);

        _condition = condition;
        _expression = expression;
    }

    private boolean evaluate(ScriptSession session)
    {
        return evaluateBoolean(_expression, session);
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        if (evaluate(session) == _condition)
            writeChildren(buffer, session);
    }
}