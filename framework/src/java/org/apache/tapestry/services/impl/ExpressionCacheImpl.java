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

package org.apache.tapestry.services.impl;

import java.util.HashMap;
import java.util.Map;

import ognl.Ognl;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ExpressionCache;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ExpressionCacheImpl implements ExpressionCache, ResetEventListener
{
    public synchronized void resetEventDidOccur()
    {
        _cache.clear();
    }

    private Map _cache = new HashMap();

    public synchronized Object getCompiledExpression(String expression)
    {
        Object result = _cache.get(expression);

        if (result == null)
        {
            result = parse(expression);
            _cache.put(expression, result);
        }

        return result;
    }

    private Object parse(String expression)
    {
        try
        {
            return Ognl.parseExpression(expression);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToParseExpression(
                    expression,
                    ex), ex);
        }
    }

}