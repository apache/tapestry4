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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Base class for creating tokens which may contain other tokens.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 * 
 **/

abstract class AbstractToken implements IScriptToken
{
    private List _tokens;
    private ILocation _location;
    private IResourceResolver _resolver;

    protected AbstractToken(ILocation location)
    {
        _location = location;
    }

    public ILocation getLocation()
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
     *  Invokes {@link IScriptToken#write(StringBuffer,ScriptSession)}
     *  on each child token (if there are any).
     *
     **/

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
     * {@link OgnlUtils#get(String, ClassResolver, Object)} and
     * returns the result.
     */
    protected Object evaluate(String expression, ScriptSession session)
    {
        if (_resolver == null)
            _resolver = session.getRequestCycle().getEngine().getResourceResolver();

        try
        {
            return OgnlUtils.get(expression, _resolver, session.getSymbols());
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), _location, ex);
        }
    }
}