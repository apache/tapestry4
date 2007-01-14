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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ognl.ClassResolver;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.TypeConverter;
import ognl.enhance.ExpressionAccessor;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.ClassFactory;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ExpressionEvaluatorImpl implements ExpressionEvaluator
{
    // Uses Thread's context class loader

    private final ClassResolver _ognlResolver = new OgnlClassResolver();

    private ExpressionCache _expressionCache;

    private IApplicationSpecification _applicationSpecification;

    private TypeConverter _typeConverter;

    private List _contributions;
    
    private List _nullHandlerContributions;

    // Context, with a root of null, used when evaluating an expression
    // to see if it is a constant.

    private Map _defaultContext;
    
    private ClassFactory _classFactory;
    
    public void setApplicationSpecification(IApplicationSpecification applicationSpecification)
    {
        _applicationSpecification = applicationSpecification;
    }

    public void initializeService()
    {
        if (_applicationSpecification.checkExtension(Tapestry.OGNL_TYPE_CONVERTER))
            _typeConverter = (TypeConverter) _applicationSpecification.getExtension(
                    Tapestry.OGNL_TYPE_CONVERTER,
                    TypeConverter.class);

        Iterator i = _contributions.iterator();

        while (i.hasNext())
        {
            PropertyAccessorContribution c = (PropertyAccessorContribution) i.next();
            
            OgnlRuntime.setPropertyAccessor(c.getSubjectClass(), c.getAccessor());
        }
        
        Iterator j = _nullHandlerContributions.iterator();
        
        while (j.hasNext())
        {
            NullHandlerContribution h = (NullHandlerContribution) j.next();
            
            OgnlRuntime.setNullHandler(h.getSubjectClass(), h.getHandler());
        }        
        
        _defaultContext = Ognl.createDefaultContext(null, _ognlResolver, _typeConverter);
        
        OgnlRuntime.setCompiler(new HiveMindExpressionCompiler(_classFactory));
    }

    public Object read(Object target, String expression)
    {
        Node node = (Node)_expressionCache.getCompiledExpression(target, expression);
        
        if (node.getAccessor() != null)
            return read(target, node.getAccessor());
        
        return readCompiled(target, node);
        
        //return read(target, _expressionCache.getCompiledExpression(expression));
    }

    public Object readCompiled(Object target, Object expression)
    {
        try
        {
            Map context = createContext(target);

            return Ognl.getValue(expression, context, target);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToReadExpression(ImplMessages
                    .parsedExpression(), target, ex), target, null, ex);
        }
    }
    
    public Object read(Object target, ExpressionAccessor expression)
    {
        try
        {
            OgnlContext context = createContext(target);
            
            return expression.get(context, target);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToReadExpression(ImplMessages
                    .parsedExpression(), target, ex), target, null, ex);
        }
    }
    
    public OgnlContext createContext(Object target)
    {
        OgnlContext result = (OgnlContext)Ognl.createDefaultContext(target, _ognlResolver);

        if (_typeConverter != null)
            Ognl.setTypeConverter(result, _typeConverter);

        return result;
    }

    public void write(Object target, String expression, Object value)
    {
        writeCompiled(target, _expressionCache.getCompiledExpression(target, expression), value);
    }

    public void write(Object target, ExpressionAccessor expression, Object value)
    {
        try
        {
            OgnlContext context = createContext(target);
            
            expression.set(context, target, value);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToWriteExpression(ImplMessages
                    .parsedExpression(), target, value, ex), target, null, ex);
        }

    }
    
    public void writeCompiled(Object target, Object expression, Object value)
    {
        try
        {
            Map context = createContext(target);

            Ognl.setValue(expression, context, target, value);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToWriteExpression(ImplMessages
                    .parsedExpression(), target, value, ex), target, null, ex);
        }

    }
    
    public boolean isConstant(Object target, String expression)
    {
        Object compiled = _expressionCache.getCompiledExpression(target, expression);

        try
        {
            return Ognl.isConstant(compiled, _defaultContext);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.isConstantExpressionError(
                    expression,
                    ex), ex);
        }
    }
    
    public boolean isConstant(String expression)
    {
        Object compiled = _expressionCache.getCompiledExpression(expression);

        try
        {
            return Ognl.isConstant(compiled, _defaultContext);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.isConstantExpressionError(
                    expression,
                    ex), ex);
        }
    }
    
    public void setExpressionCache(ExpressionCache expressionCache)
    {
        _expressionCache = expressionCache;
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }
    
    public void setNullHandlerContributions(List nullHandlerContributions)
    {
        _nullHandlerContributions = nullHandlerContributions;
    }    
    
    public void setClassFactory(ClassFactory classFactory)
    {
        _classFactory = classFactory;
    }
}
