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

package org.apache.tapestry.binding;

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;

import org.apache.tapestry.BindingException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.StringSplitter;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Implements a dynamic binding, based on getting and fetching
 *  values using JavaBeans property access.  This is built
 *  upon the <a href="http://www.ognl.org">OGNL</a> library.
 *
 *  <p><b>Optimization of the Expression</b>
 * 
 *  <p>There's a lot of room for optimization here because we can
 *  count on some portions of the expression to be
 *  effectively static.  Note that we type the root object as
 *  {@link IComponent}.  We have some expectations that
 *  certain properties of the root (and properties reachable from the root)
 *  will be constant for the lifetime of the binding.  For example, 
 *  components never change thier page or container.  This means
 *  that certain property prefixes can be optimized:
 *
 *  <ul>
 *  <li>page
 *  <li>container
 *  <li>components.<i>name</i>
 *  </ul>
 *
 *  <p>This means that once an ExpressionBinding has been triggered, 
 *  the {@link #toString()} method may return different values for the root
 *  component and the expression than was originally set.
 * 
 *  <p><b>Identifying Invariants</b>
 * 
 *  <p>Most expressions are fully dynamic; they must be
 *  resolved each time they are accessed.  This can be somewhat inefficient.
 *  Tapestry can identify certain paths as invariant:
 * 
 *  <ul>
 *  <li>A component within the page hierarchy 
 *  <li>An {@link org.apache.tapestry.IAsset} from then assets map (property <code>assets</code>)
 *  <li>A {@link org.apache.tapestry.IActionListener}
 *  from the listener map (property <code>listeners</code>)
 *  <li>A bean with a {@link org.apache.tapestry.spec.BeanLifecycle#PAGE}
 *  lifecycle (property <code>beans</code>)
 *  <li>A binding (property <code>bindings</code>)
 *  </ul>
 * 
 *  <p>
 *  These optimizations have some inherent dangers; they assume that
 *  the components have not overidden the specified properties;
 *  the last one (concerning helper beans) assumes that the
 *  component does inherit from {@link org.apache.tapestry.AbstractComponent}.
 *  If this becomes a problem in the future, it may be necessary to
 *  have the component itself involved in these determinations.
 *  
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class ExpressionBinding extends AbstractBinding
{
    /**
     *  The root object against which the nested property name is evaluated.
     *
     **/

    private IComponent _root;

    /**
     *  The OGNL expression, as a string.
     *
     **/

    private String _expression;

    /**
     *  If true, then the binding is invariant, and cachedValue
     *  is the ultimate value.
     * 
     **/

    private boolean _invariant = false;

    /**
     *  Stores the cached value for the binding, if invariant
     *  is true.
     * 
     **/

    private Object _cachedValue;

    /**
     *   Parsed OGNL expression.
     * 
     **/

    private Object _parsedExpression;

    /**
     *  Flag set once the binding has initialized.
     *  _cachedValue, _invariant and _final value
     *  for _expression
     *  are not valid until after initialization.
     * 
     * 
     **/

    private boolean _initialized;

    private IResourceResolver _resolver;

    /**
     *  The OGNL context for this binding.  It is retained
     *  for the lifespan of the binding once created.
     * 
     **/

    private Map _context;

    /**
     *  Creates a {@link ExpressionBinding} from the root object
     *  and an OGNL expression.
     * 
     **/

    public ExpressionBinding(
        IResourceResolver resolver,
        IComponent root,
        String expression,
        ILocation location)
    {
        super(location);

        _resolver = resolver;
        _root = root;
        _expression = expression;
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
     *  Gets the value of the property path, with the assistance of a 
     *  OGNL.
     *
     *  @throws BindingException if an exception is thrown accessing the property.
     *
     **/

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
            return Ognl.getValue(_parsedExpression, getOgnlContext(), _root);
        }
        catch (OgnlException t)
        {
            throw new BindingException(
                Tapestry.format(
                    "ExpressionBinding.unable-to-resolve-expression",
                    _expression,
                    _root),
                this,
                t);
        }
    }

    /**
     *  Creates an OGNL context used to get or set a value.
     *  We may extend this in the future to set additional
     *  context variables (such as page, request cycle and engine).
     *  An optional type converter will be added to the OGNL context
     *  if it is specified as an application extension with the name
     *  {@link Tapestry#OGNL_TYPE_CONVERTER}.
     * 
     **/

    private Map getOgnlContext()
    {
        if (_context == null)
            _context = Ognl.createDefaultContext(_root, _resolver);

        if (_root.getPage() != null)
        {
            if (_root.getPage().getEngine() != null)
            {
                IApplicationSpecification appSpec = _root.getPage().getEngine().getSpecification();

                if (appSpec != null && appSpec.checkExtension(Tapestry.OGNL_TYPE_CONVERTER))
                {
                    TypeConverter typeConverter = (TypeConverter) appSpec.getExtension(
                        Tapestry.OGNL_TYPE_CONVERTER, TypeConverter.class);

                    Ognl.setTypeConverter(_context, typeConverter);
                }
            }
        }

        return _context;
    }

    /**
     *  Returns true if the binding is expected to always 
     *  return the same value.
     * 
     * 
     **/

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
     *  Sets up the helper object, but also optimizes the property path
     *  and determines if the binding is invarant.
     *
     **/

    private void initialize()
    {
        if (_initialized)
            return;

        _initialized = true;

        try
        {
            _parsedExpression = OgnlUtils.getParsedExpression(_expression);
        }
        catch (Exception ex)
        {
            throw new BindingException(ex.getMessage(), this, ex);
        }

        if (checkForConstant())
            return;

        // Split the expression into individual property names.
        // We then optimize what we can from the expression.  This will
        // shorten the expression and, in some cases, eliminate
        // it.  We also check to see if the binding can be an invariant.

        String[] split = new StringSplitter('.').splitToArray(_expression);

        int count = optimizeRootObject(split);

        // We'ver removed some or all of the initial elements of split
        // but have to account for anthing left over.

        if (count == split.length)
        {
            // The property path was something like "page" or "component.foo"
            // and was completely eliminated.

            _expression = null;
            _parsedExpression = null;

            _invariant = true;
            _cachedValue = _root;

            return;
        }

        _expression = reassemble(count, split);
        _parsedExpression = OgnlUtils.getParsedExpression(_expression);

        checkForInvariant(count, split);
    }

    /**
     *  Looks for common prefixes on the expression (provided pre-split) that
     *  are recognized as references to other components.
     * 
     *  @return the number of leading elements of the split expression that
     *  have been removed.
     * 
     **/

    private int optimizeRootObject(String[] split)
    {
        int i;

        for (i = 0; i < split.length; i++)
        {

            if (split[i].equals("page"))
            {
                _root = _root.getPage();
                continue;
            }

            if (split[i].equals("container"))
            {
                _root = _root.getContainer();
                continue;
            }

            // Here's the tricky one ... if its of the form
            // "components.foo" we can get the named component
            // directly.

            if (split[i].equals("components") && i + 1 < split.length)
            {
                _root = _root.getComponent(split[i + 1]);
                i++;
                continue;
            }

            // Not a recognized prefix, break the loop

            break;
        }

        return i;
    }

    private boolean checkForConstant()
    {
        try
        {
            if (Ognl.isConstant(_parsedExpression, getOgnlContext()))
            {
                _invariant = true;

                _cachedValue = resolveProperty();

                return true;
            }
        }
        catch (OgnlException ex)
        {
            throw new BindingException(
                Tapestry.format(
                    "ExpressionBinding.unable-to-resolve-expression",
                    _expression,
                    _root),
                this,
                ex);
        }

        return false;
    }

    /**
     *  Reassembles the remainder of the split property path
     *  from the start point.
     * 
     **/

    private String reassemble(int start, String[] split)
    {
        int count = split.length - start;

        if (count == 0)
            return null;

        if (count == 1)
            return split[split.length - 1];

        StringBuffer buffer = new StringBuffer();

        for (int i = start; i < split.length; i++)
        {
            if (i > start)
                buffer.append('.');

            buffer.append(split[i]);
        }

        return buffer.toString();
    }

    /**
     *  Checks to see if the binding can be converted to an invariant.
     * 
     **/

    private void checkForInvariant(int start, String[] split)
    {
        // For now, all of our conditions are two properties
        // from a root component.

        if (split.length - start != 2)
            return;

        try
        {
            if (!Ognl.isSimpleNavigationChain(_parsedExpression, getOgnlContext()))
                return;
        }
        catch (OgnlException ex)
        {
            throw new BindingException(
                Tapestry.format(
                    "ExpressionBinding.unable-to-resolve-expression",
                    _expression,
                    _root),
                this,
                ex);
        }

        String first = split[start];

        if (first.equals("listeners"))
        {
            _invariant = true;

            // Could cast to AbstractComponent, get listenersMap, etc.,
            // but this is easier.

            _cachedValue = resolveProperty();
            return;
        }

        if (first.equals("assets"))
        {
            String name = split[start + 1];

            _invariant = true;
            _cachedValue = _root.getAsset(name);
            return;
        }

        if (first.equals("beans"))
        {
            String name = split[start + 1];

            IBeanSpecification bs = _root.getSpecification().getBeanSpecification(name);

            if (bs == null || bs.getLifecycle() != BeanLifecycle.PAGE)
                return;

            // Again, could cast to AbstractComponent, but this
            // is easier.

            _invariant = true;
            _cachedValue = resolveProperty();
            return;
        }

        if (first.equals("bindings"))
        {
            String name = split[start + 1];

            _invariant = true;
            _cachedValue = _root.getBinding(name);
            return;
        }

        // Not a recognized pattern for conversion
        // to invariant.
    }

    /**
     *  Updates the property for the binding to the given value.  
     *
     *  @throws BindingException if the property can't be updated (typically
     *  due to an security problem, or a missing mutator method).
     *  @throws BindingException if the binding is invariant.
     **/

    public void setObject(Object value)
    {
        initialize();

        if (_invariant)
            throw createReadOnlyBindingException(this);

        try
        {
            Ognl.setValue(_parsedExpression, getOgnlContext(), _root, value);
        }
        catch (OgnlException ex)
        {
            throw new BindingException(
                Tapestry.format(
                    "ExpressionBinding.unable-to-update-expression",
                    _expression,
                    _root,
                    value),
                this,
                ex);
        }
    }

    /**
     *  Returns the a String representing the property path.  This includes
     *  the {@link IComponent#getExtendedId() extended id} of the root component
     *  and the property path ... once the binding is used, these may change
     *  due to optimization of the property path.
     *
     **/

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