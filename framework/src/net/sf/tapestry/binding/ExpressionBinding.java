//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.binding;

import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ReadOnlyBindingException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.BeanLifecycle;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.util.StringSplitter;
import net.sf.tapestry.util.prop.OgnlUtils;
import net.sf.tapestry.util.prop.PropertyFinder;
import net.sf.tapestry.util.prop.PropertyInfo;
import ognl.Ognl;

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
 *  <li>An {@link net.sf.tapestry.IAsset} from then assets map (property <code>assets</code>)
 *  <li>A {@link net.sf.tapestry.IActionListener}
 *  from the listener map (property <code>listeners</code>)
 *  <li>A bean with a {@link net.sf.tapestry.spec.BeanLifecycle#PAGE}
 *  lifecycle (property <code>beans</code>)
 *  <li>A binding (property <code>bindings</code>)
 *  </ul>
 * 
 *  <p>
 *  These optimizations have some inherent dangers; they assume that
 *  the components have not overidden the specified properties;
 *  the last one (concerning helper beans) assumes that the
 *  component does inherit from {@link net.sf.tapestry.AbstractComponent}.
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

    /**
     *  Records the first type we see for the expression.
     * 
     **/

    private Class _type;

    private IResourceResolver _resolver;

    /**
     *  Creates a {@link ExpressionBinding} from the root object
     *  and an OGNL expression.
     * 
     **/

    public ExpressionBinding(IResourceResolver resolver, IComponent root, String expression)
    {
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
        Object result;

        try
        {
            if (_parsedExpression == null)
                _parsedExpression = OgnlUtils.getParsedExpression(_expression);

            Map context = getOgnlContext();

            result = Ognl.getValue(_parsedExpression, context, _root);
        }
        catch (Throwable t)
        {
            throw new BindingException(
                Tapestry.getString("PropertyBinding.unable-to-resolve-property", _expression, _root),
                this,
                t);
        }

        if (_type == null && result != null)
            _type = result.getClass();

        return result;
    }

    /**
     *  Creates an OGNL context used to get or set a value.
     *  We may extend this in the future to set additional
     *  context variables (such as page, request cycle and engine).
     * 
     **/

    private Map getOgnlContext()
    {
        return Ognl.createDefaultContext(_root, _resolver);
    }

    /**
     *  Returns true if the binding is expected to always 
     *  return the same value.
     * 
     * 
     **/

    public boolean isInvariant()
    {
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

        int i;

        // Split the expression into individual property names.
        // We then optimize what we can from the expression.  This will
        // shorten the expression and, in some cases, eliminate
        // it.  We also check to see if the binding can be an invariant.

        String[] split = new StringSplitter('.').splitToArray(_expression);

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

        // We'ver removed some or all of the initial elements of split
        // but have to account for anthing left over.

        if (i == split.length)
        {
            // The property path was something like "page" or "component.foo"
            // and was completely eliminated.

            _expression = null;

            _invariant = true;
            _cachedValue = _root;
        }
        else
        {
            _expression = reassemble(i, split);

            checkInvariant(i, split);
        }

        _initialized = true;

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

    private void checkInvariant(int start, String[] split)
    {
        // For now, all of our conditions are two properties
        // from a root component.

        if (split.length - start != 2)
            return;

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

            BeanSpecification bs = _root.getSpecification().getBeanSpecification(name);

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
     *  @throws ReadOnlyBindingException if the binding is invariant.
     **/

    public void setObject(Object value)
    {
        initialize();

        if (_invariant)
            throw new ReadOnlyBindingException(this);

        try
        {
            if (_parsedExpression == null)
                _parsedExpression = OgnlUtils.getParsedExpression(_expression);

            Map context = getOgnlContext();

            Ognl.setValue(_parsedExpression, context, _root, value);
        }
        catch (Throwable ex)
        {
            throw new BindingException(
                Tapestry.getString("PropertyBinding.unable-to-update-property", _expression, _root, value),
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

        if (_type != null)
        {
            buffer.append(" type=");
            // May not be pretty if array or primitive type ...
            buffer.append(_type.getName());
        }

        buffer.append(']');

        return buffer.toString();
    }

    /**
     *  Returns a best-estimate of the property type.  OGNL is missing
     *  this important feature ... what is the type of an expression?
     * 
     *  <p>This is not perfect, but it reads the property (just once)
     *  to see what its real type is.
     * 
     **/

    public Class getType()
    {
        initialize();

        if (_type == null)
            resolveProperty();

        if (_type != null)
            return _type;

        // Desperate measures!  Assume
        // that it is a property path and not a
        // OGNL expression.

        String[] split = new StringSplitter('.').splitToArray(_expression);

        Class beanClass = _root.getClass();

        for (int i = 0; i < split.length; i++)
        {
            PropertyInfo info = PropertyFinder.getPropertyInfo(beanClass, split[i]);

            if (info == null)
                throw new BindingException(Tapestry.getString("PropertyBinding.unable-to-resolve-type"), this);

            beanClass = info.getType();
        }

        _type = beanClass;

        return _type;

    }

}