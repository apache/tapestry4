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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * Base class for creating tokens which may contain other tokens.
 * 
 * @author Howard Lewis Ship
 * @since 0.2.9
 */

abstract class AbstractToken implements IScriptToken
{
    private List _tokens;

    private Location _location;

    protected AbstractToken(Location location)
    {
        _location = location;
    }

    public Location getLocation()
    {
        return _location;
    }

    public void addToken(IScriptToken token)
    {
        if (_tokens == null)
            _tokens = new ArrayList();

        _tokens.add(token);
    }

    /**
     * Invokes {@link IScriptToken#write(StringBuffer,ScriptSession)}on each child token (if there
     * are any).
     */

    protected void writeChildren(StringBuffer buffer, ScriptSession session)
    {
        if (_tokens == null)
            return;

        Iterator i = _tokens.iterator();

        while (i.hasNext())
        {
            IScriptToken token = (IScriptToken) i.next();

            token.write(buffer, session);
        }
    }

    /**
     * Evaluates the expression against the session's symbols, using
     * {@link ExpressionEvaluator#read(Object, String)}and returns the result.
     */
    protected Object evaluate(String expression, ScriptSession session)
    {

        try
        {
            return session.evaluate(expression);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), _location, ex);
        }
    }

    /**
     * Evaluates an expression and coerces the result to a boolean.
     * 
     * @since 3.1
     */

    protected boolean evaluateBoolean(String expression, ScriptSession session)
    {
        try
        {
            Boolean b = (Boolean) session.evaluate(expression, Boolean.class);

            return b.booleanValue();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), _location, ex);
        }
    }
}