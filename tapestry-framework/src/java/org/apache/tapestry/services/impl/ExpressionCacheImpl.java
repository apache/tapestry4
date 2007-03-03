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

import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import ognl.Node;
import ognl.Ognl;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ExpressionCacheImpl implements ExpressionCache, ResetEventListener, ReportStatusListener
{
    private final ReentrantLock _lock = new ReentrantLock();
    
    private String _serviceId;

    private Map _cache = new WeakHashMap();
    
    private Map _objectCache = new WeakHashMap();
    
    private ExpressionEvaluator _evaluator;
    
    public void resetEventDidOccur()
    {
        try {
            
            _lock.lock();
            
            _cache.clear();
            _objectCache.clear();
        } finally {
            
            _lock.unlock();
        }
    }

    public void reportStatus(ReportStatusEvent event)
    {
        event.title(_serviceId);

        event.property("cached expression count", _cache.size());
        event.collection("cached expressions", _cache.keySet());
        
        event.property("cached object expression count", _objectCache.size());
    }
    
    public Object getCompiledExpression(Object target, String expression)
    {
        try {   
            
            _lock.lock();
            
            Map cached = (Map)_objectCache.get(target.getClass());
            
            if (cached == null) {
                
                cached = new HashMap();
                _objectCache.put(target.getClass(), cached);
            }
            
            Node result = (Node)cached.get(expression);
            
            if (result == null || result.getAccessor() == null)
            {
                result = parse(target, expression);
                
                cached.put(expression, result);
            }
            
            return result;
            
        } finally {

            _lock.unlock();
        }
    }
    
    public Object getCompiledExpression(String expression)
    {
        try {
            
            _lock.lock();
            
            Object result = _cache.get(expression);

            if (result == null)
            {
                result = parse(expression);
                _cache.put(expression, result);
            }
            
            return result;
        } finally {

            _lock.unlock();
        }
    }

    private Node parse(Object target, String expression)
    {
        try
        {
            return Ognl.compileExpression(_evaluator.createContext(target), target, expression);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToParseExpression(
                    expression,
                    ex), ex);
        }
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

    public void setServiceId(String serviceId)
    {
        _serviceId = serviceId;
    }
    
    public void setEvaluator(ExpressionEvaluator evaluator)
    {
        _evaluator = evaluator;
    }
    
}
