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

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.ReadOnlyBindingException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.BeanLifecycle;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Implements a dynamic binding, based on getting and fetching
 *  values using JavaBeans property access.
 *
 *  <p><b>Optimization of the Property Path</b>
 * 
 *  <p>There's a lot of room for optimization here because we can
 *  count on some portions of the nested property name to be
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
 *  <p>This means that once a PropertyBinding has been triggered, 
 *  the {@link #toString()} method may return different values for the root
 *  component and the property path than was originally set.
 *
 *  <p>Another option (much more involved) is to replace the
 *  dynamic property access, which depends upon reflection (i.e.,
 *  the <code>Method</code> class), with dynamically generated
 *  bytecodes.  This has been done before, to create <a
 *  href="http://java.sun.com/products/jfc/tsc/articles/generic-listener/index.html">
 *  dynamic adapter classes</a>.
 *
 *  <p>These operate orders-of-magnitude faster, though there is
 *  the question of building the bytecodes (non trivial!) and all
 *  the other classloader and security issues.
 *
 *  <p>In the meantime, no optimization is done.
 * 
 * 
 *  <p><b>Identifying Invariants</b>
 * 
 *  <p>Most property paths are fully dynamic; they must be
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
 *  the lasat one (concerning helper beans) assumes that the
 *  component does inherit from {@link net.sf.tapestry.AbstractComponent}.
 *  If this becomes a problem in the future, it may be necessary to
 *  have the component itself involved in these determinations.
 *  
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PropertyBinding extends AbstractBinding
{
    /**
     *  The root object against which the nested property name is evaluated.
     *
     **/

    private IComponent root;

    /**
     *  Allows dynamic property access to the root object.
     *
     **/

    private PropertyHelper helper;

    /**
     *  The name of the property to bind to.  This may be either a
     *  simple name, or a nested property name.  A nested property
     *  name consists of a series of simple names seperated by
     *  periods.  In this way the path can 'navigate' to various other
     *  objects. For example, the property 'page.visit.foo' is
     *  equivalent to the expression
     *  <code>getPage().getVisit().getFoo()</code>.
     *
     **/

    private String propertyPath;

    /**
     *  Property path split to individual Strings.
     *
     **/

    private String[] splitPropertyPath;

    /**
     *  If true, the propertyPath is a simple propertyName (and
     *  splitPropertyPath will be null).
     *
     **/

    private boolean simple = false;

    /**
     *  If true, then the binding is invariant, and cachedValue
     *  is the ultimate value.
     * 
     *  @since 2.0.4
     * 
     **/

    private boolean invariant = false;

    /**
     *  Stores the cached value for the binding, if invariant
     *  is true.
     * 
     *  @since 2.0.4
     * 
     **/

    private Object cachedValue;

    /**
     *  Creates a {@link PropertyBinding} from the root object
     *  and a nested property name.
     * 
     **/

    public PropertyBinding(IComponent root, String propertyPath)
    {
        this.root = root;
        this.propertyPath = propertyPath;
    }

    public String getPropertyPath()
    {
        return propertyPath;
    }

    public IComponent getRoot()
    {
        return root;
    }

    /**
     *  Gets the value of the property path, with the assistance of a 
     *  {@link PropertyHelper}.
     *
     *  @throws BindingException if an exception is thrown accessing the property.
     *
     **/

    public Object getObject()
    {
        if (helper == null)
            setupHelper();

        if (invariant)
            return cachedValue;

        return resolveProperty();

    }

    /**
     *  Invoked to resolve the property to an object value.  This is
     *  used when getting the value of a dynamic binding, or
     *  to cache the value of an invariant binding.
     * 
     *  @since 2.0.4
     * 
     **/

    private Object resolveProperty()
    {
        try
        {
            if (simple)
                return helper.get(root, propertyPath);
            else
                return helper.getPath(root, splitPropertyPath);
        }
        catch (Throwable ex)
        {
            throw new BindingException(
                Tapestry.getString(
                    "PropertyBinding.unable-to-resolve-property",
                    propertyPath,
                    root),
                this,
                ex);
        }
    }

    /**
     *  Returns false.
     * 
     *  @since 2.0.3
     * 
     **/

    public boolean isInvariant()
    {
        return invariant;
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

    private void setupHelper()
    {
        String[] split;
        int i;

        // Split the property path into individual property names.
        // We then optimize what we can from the path.  This will
        // shorten the property path and, in some cases, eliminate
        // it (if the property path was to an IComponent).
        // We also check to see if the binding can be an invariant.
        
        split = PropertyHelper.splitPropertyPath(propertyPath);

        for (i = 0; i < split.length; i++)
        {

            if (split[i].equals("page"))
            {
                root = root.getPage();
                continue;
            }

            if (split[i].equals("container"))
            {
                root = root.getContainer();
                continue;
            }

            // Here's the tricky one ... if its of the form
            // "components.foo" we can get the named component
            // directly.

            if (split[i].equals("components") && i + 1 < split.length)
            {
                root = root.getComponent(split[i + 1]);
                i++;
                continue;
            }

            // Not a recognized prefix, break the loop

            break;
        }

        helper = PropertyHelper.forInstance(root);
        
        // We'ver removed some or all of the initial elements of split
        // but have to account for anthing left over.

        int count = split.length - i;

        if (count == 0)
        {
            // The property path was something like "page" or "component.foo"
            // and was completely eliminated.

            simple = true;
            propertyPath = null;

            invariant = true;
            cachedValue = root;
        }
        else if (count == 1)
        {
            // The path was something like "page.foo" and shrinks to just "foo".

            simple = true;
            propertyPath = split[i];
            
            // None of the invariant checks work with a single element
            // property path.
        }
        else
        {
            // The path was something like "page.foo.baz" and shrinks to just
            // "foo.baz"

            splitPropertyPath = new String[count];
            System.arraycopy(split, i, splitPropertyPath, 0, count);

            // Create a new propertyPath to replace the original

            StringBuffer buffer = new StringBuffer();
            for (int j = i; j < split.length; j++)
            {
                if (j > i)
                    buffer.append('.');

                buffer.append(split[j]);
            }

            propertyPath = buffer.toString();

            checkInvariant();
        }

    }

    /**
     *  Checks to see if the binding can be converted to an invariant.
     * 
     **/

    private void checkInvariant()
    {
        if (splitPropertyPath == null ||
        	splitPropertyPath.length != 2)
        		return;
        		
        String first = splitPropertyPath[0];
        
        if (first.equals("listeners"))
        {
			invariant = true;
			
			// Could cast to AbstractComponent, get listenersMap, etc.,
			// but this is easier.
			
			cachedValue = resolveProperty();
			return;
        }
        
        if (first.equals("assets"))
        {
            String name = splitPropertyPath[1];
            
            invariant = true;
            cachedValue = root.getAsset(name);
            return;
        }
        
        if (first.equals("beans"))
        {
            String name = splitPropertyPath[1];
            
            BeanSpecification bs = root.getSpecification().getBeanSpecification(name);
            
            if (bs == null || bs.getLifecycle() != BeanLifecycle.PAGE)
            	return;
            	
            // Again, could cast to AbstractComponent, but this
            // is easier.
            	
            invariant = true;
            cachedValue = resolveProperty();
            return;
        }
        
        if (first.equals("bindings"))
        {
            String name = splitPropertyPath[1];
            
            invariant = true;
            cachedValue = root.getBinding(name);
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
        if (helper == null)
            setupHelper();

        if (invariant)
            throw new ReadOnlyBindingException(this);

        try
        {
            if (simple)
                helper.set(root, propertyPath, value);
            else
                helper.setPath(root, splitPropertyPath, value);
        }
        catch (Throwable ex)
        {
            throw new BindingException(
                Tapestry.getString(
                    "PropertyBinding.unable-to-update-property",
                    propertyPath,
                    root,
                    value),
                this,
                ex);
        }

    }

    /**
     *  Follows the property path to determine the type.
     *
     *  @since 1.0.5
     *
     **/

    public Class getType()
    {
        if (helper == null)
            setupHelper();

        try
        {

            if (simple)
                return helper.getAccessor(root, propertyPath).getType();

            return helper.getAccessorPath(root, splitPropertyPath).getType();
        }
        catch (Throwable ex)
        {
            throw new BindingException(
                Tapestry.getString(
                    "PropertyBinding.unable-to-resolve-type",
                    propertyPath,
                    root),
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

        buffer.append("PropertyBinding[");
        buffer.append(root.getExtendedId());

        if (propertyPath != null)
        {
            buffer.append(' ');
            buffer.append(propertyPath);
        }

        if (invariant)
        {
            buffer.append(" cachedValue=");
            buffer.append(cachedValue);
        }

        buffer.append(']');

        return buffer.toString();
    }
}