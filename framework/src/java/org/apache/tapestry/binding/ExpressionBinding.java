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

package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * Implements a dynamic binding, based on evaluating an expression using an expression language.
 * Tapestry's default expression language is the <a href="http://www.ognl.org/">Object Graph
 * Navigation Language </a>.
 * 
 * @see org.apache.tapestry.services.ExpressionEvaluator
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class ExpressionBinding extends AbstractBinding
{
    /**
     * The root object against which the nested property name is evaluated.
     */

    private final IComponent _root;

    /**
     * The OGNL expression, as a string.
     */

    private String _expression;

    /**
     * If true, then the binding is invariant.
     */

    private boolean _invariant = false;

    /**
     * Parsed OGNL expression.
     */

    private Object _parsedExpression;

    /**
     * Flag set to true once the binding has initialized.
     */

    private boolean _initialized;

    /**
     * @since 3.1
     */

    private ExpressionEvaluator _evaluator;

    /** @since 3.1 */

    private ExpressionCache _cache;

    /**
     * Creates a {@link ExpressionBinding}from the root object and an OGNL expression.
     */

    public ExpressionBinding(IComponent root, String description, String expression,
            ValueConverter valueConverter, Location location, ExpressionEvaluator evaluator,
            ExpressionCache cache)
    {
        super(description, valueConverter, location);

        _root = root;
        _expression = expression;
        _evaluator = evaluator;
        _cache = cache;
    }

    /**
     * Gets the value of the property path, with the assistance of the {@link ExpressionEvaluator}.
     * 
     * @throws BindingException
     *             if an exception is thrown accessing the property.
     */

    public Object getObject()
    {
        initialize();

        return resolveExpression();
    }

    private Object resolveExpression()
    {
        try
        {
            return _evaluator.readCompiled(_root, _parsedExpression);
        }
        catch (Throwable t)
        {
            throw new BindingException(t.getMessage(), this, t);
        }
    }

    /**
     * Returns true if the binding is expected to always return the same value.
     */

    public boolean isInvariant()
    {
        initialize();

        return _invariant;
    }

    /**
     * Sets up the helper object, but also optimizes the property path and determines if the binding
     * is invarant.
     */

    private void initialize()
    {
        if (_initialized)
            return;

        _initialized = true;

        try
        {
            _parsedExpression = _cache.getCompiledExpression(_expression);

            _invariant = _evaluator.isConstant(_expression);
        }
        catch (Exception ex)
        {
            throw new BindingException(ex.getMessage(), this, ex);
        }
    }

    /**
     * Updates the property for the binding to the given value.
     * 
     * @throws BindingException
     *             if the property can't be updated (typically due to an security problem, or a
     *             missing mutator method).
     * @throws ReadOnlyBindingException
     *             if the binding is invariant.
     */

    public void setObject(Object value)
    {
        initialize();

        if (_invariant)
            throw createReadOnlyBindingException(this);

        try
        {
            _evaluator.writeCompiled(_root, _parsedExpression, value);
        }
        catch (Throwable ex)
        {
            throw new BindingException(ex.getMessage(), this, ex);
        }
    }

    /**
     * Returns the a String representing the property path. This includes the
     * {@link IComponent#getExtendedId() extended id}of the root component and the property path
     * ... once the binding is used, these may change due to optimization of the property path.
     */

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ExpressionBinding[");
        buffer.append(_root.getExtendedId());

        if (_expression != null)
        {
            buffer.append(' ');
            buffer.append(_expression);
        }

        buffer.append(']');

        return buffer.toString();
    }

    /** @since 3.1 */
    public Object getComponent()
    {
        return _root;
    }
}