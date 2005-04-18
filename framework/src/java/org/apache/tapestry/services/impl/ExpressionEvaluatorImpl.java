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
import ognl.Ognl;
import ognl.OgnlRuntime;
import ognl.TypeConverter;

import org.apache.hivemind.ApplicationRuntimeException;
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

    private ClassResolver _ognlResolver = new OgnlClassResolver();

    private ExpressionCache _expressionCache;

    private IApplicationSpecification _applicationSpecification;

    private TypeConverter _typeConverter;

    private List _contributions;

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

    }

    public Object read(Object target, String expression)
    {
        return readCompiled(target, _expressionCache.getCompiledExpression(expression));
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

    private Map createContext(Object target)
    {
        Map result = Ognl.createDefaultContext(target, _ognlResolver);

        if (_typeConverter != null)
            Ognl.setTypeConverter(result, _typeConverter);

        return result;
    }

    public void write(Object target, String expression, Object value)
    {
        writeCompiled(target, _expressionCache.getCompiledExpression(expression), value);
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

    public boolean isConstant(String expression)
    {
        Object compiled = _expressionCache.getCompiledExpression(expression);

        try
        {
            return Ognl.isConstant(compiled);
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
}