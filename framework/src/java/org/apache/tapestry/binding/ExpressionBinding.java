// Copyright 2004 The Apache Software Foundation
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

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.StringSplitter;

/**
 * Implements a dynamic binding, based on getting and fetching values using JavaBeans property
 * access. This is built upon the <a href="http://www.ognl.org">OGNL </a> library.
 * <p>
 * <b>Optimization of the Expression </b>
 * <p>
 * There's a lot of room for optimization here because we can count on some portions of the
 * expression to be effectively static. Note that we type the root object as {@link IComponent}. We
 * have some expectations that certain properties of the root (and properties reachable from the
 * root) will be constant for the lifetime of the binding. For example, components never change
 * thier page or container. This means that certain property prefixes can be optimized:
 * <ul>
 * <li>page
 * <li>container
 * <li>components. <i>name </i>
 * </ul>
 * <p>
 * This means that once an ExpressionBinding has been triggered, the {@link #toString()}method may
 * return different values for the root component and the expression than was originally set.
 * <p>
 * <b>Identifying Invariants </b>
 * <p>
 * Most expressions are fully dynamic; they must be resolved each time they are accessed. This can
 * be somewhat inefficient. Tapestry can identify certain paths as invariant:
 * <ul>
 * <li>A component within the page hierarchy
 * <li>An {@link org.apache.tapestry.IAsset}from then assets map (property <code>assets</code>)
 * <li>A {@link org.apache.tapestry.IActionListener}from the listener map (property
 * <code>listeners</code>)
 * <li>A bean with a {@link org.apache.tapestry.spec.BeanLifecycle#PAGE}lifecycle (property
 * <code>beans</code>)
 * <li>A binding (property <code>bindings</code>)
 * </ul>
 * <p>
 * These optimizations have some inherent dangers; they assume that the components have not
 * overidden the specified properties; the last one (concerning helper beans) assumes that the
 * component does inherit from {@link org.apache.tapestry.AbstractComponent}. If this becomes a
 * problem in the future, it may be necessary to have the component itself involved in these
 * determinations.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class ExpressionBinding extends AbstractBinding
{
    /**
     * The root object against which the nested property name is evaluated.
     */

    private IComponent _root;

    /**
     * The OGNL expression, as a string.
     */

    private String _expression;

    /**
     * If true, then the binding is invariant, and cachedValue is the ultimate value.
     */

    private boolean _invariant = false;

    /**
     * Stores the cached value for the binding, if invariant is true.
     */

    private Object _cachedValue;

    /**
     * Parsed OGNL expression.
     */

    private Object _parsedExpression;

    /**
     * Flag set once the binding has initialized. _cachedValue, _invariant and _final value for
     * _expression are not valid until after initialization.
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

    public ExpressionBinding(IComponent root, String expression, Location location,
            ExpressionEvaluator evaluator, ExpressionCache cache)
    {
        super(location);

        _root = root;
        _expression = expression;
        _evaluator = evaluator;
        _cache = cache;
    }

    public String getExpression()
    {
        return _expression;
    }

    public IComponent getRoot()
    {
        return _root;
    }

    /**
     * Gets the value of the property path, with the assistance of a OGNL.
     * 
     * @throws BindingException
     *             if an exception is thrown accessing the property.
     */

    public Object getObject()
    {
        initialize();

        if (_invariant)
            return _cachedValue;

        return resolveProperty();
    }

    private Object resolveProperty()
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

    public void setBoolean(boolean value)
    {
        setObject(value ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setInt(int value)
    {
        setObject(new Integer(value));
    }

    public void setDouble(double value)
    {
        setObject(new Double(value));
    }

    public void setString(String value)
    {
        setObject(value);
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
        }
        catch (Exception ex)
        {
            throw new BindingException(ex.getMessage(), this, ex);
        }

        checkForConstant();
    }

    private void checkForConstant()
    {
        try
        {
            if (_evaluator.isConstant(_expression))
            {
                _invariant = true;

                _cachedValue = resolveProperty();
            }
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

        if (_invariant)
        {
            buffer.append(" cachedValue=");
            buffer.append(_cachedValue);
        }

        buffer.append(']');

        return buffer.toString();
    }

}