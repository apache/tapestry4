/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Implements a dynamic binding, based on getting and fetching
 *  values using JavaBeans property access.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.tapestry.binding;

import com.primix.tapestry.util.*;
import com.primix.tapestry.util.prop.*;
import java.util.*;
import com.primix.tapestry.*;

public class PropertyBinding extends AbstractBinding
{
    private static final StringSplitter splitter 
        = new StringSplitter(PropertyHelper.PATH_SEPERATOR);

    /**
    *  The root object against which the nested property name is evaluated.
    *
    */

    private IComponent root;

    /**
    *  Allows dynamic property access to the root object.
    *
    */

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
    */

    private String propertyPath;

    /**
    *  Property path split to individual Strings.
    *
    */

    private String[] splitPropertyPath;

    /**
    *  If true, the propertyPath is a simple propertyName (and
    *  splitPropertyPath will be null).
    *
    */

    private boolean simple = false;

    /**
    *  Creates a {@link PropertyBinding} from the root object
    *  and a nested property name.
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
    * <ul>
    * <li>page
    * <li>container
    * <li>components.<i>name</i>
    * </ul>
    *
    * <p>This means that once a PropertyBinding has been triggered, 
    * the {@link #toString()} method may return different values for the root
    * component and the property path than was originally set.
    *
    * <p>Another option (much more involved) is to replace the
    * dynamic property access, which depends upon reflection (i.e.,
    * the <code>Method</code> class), with dynamically generated
    * bytecodes.  This has been done before, to create <a
    * href="http://java.sun.com/products/jfc/tsc/articles/generic-listener/index.html">
    * dynamic adapter classes</a>.
    *
    * <p>These operate orders-of-magnitude faster, though there is
    * the question of building the bytecodes (non trivial!) and all
    * the other classloader and security issues.
    *
    * <p>In the meantime, no optimization is done.
    */

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
    */

    public Object getObject()
    {
        if (helper == null)
            setupHelper();

        try
        {
            // In certain cases, the property path is to an IComponent
            // instance directly and that's that!

            if (propertyPath == null)
                return root;

            if (simple)
                return helper.get(root, propertyPath);
            else
                return helper.getPath(root, splitPropertyPath);
        }
        catch (Throwable e)
        {
            StringBuffer buffer;

            buffer = new StringBuffer("Unable to resolve property ");
            buffer.append(propertyPath);
            buffer.append(" of ");
            buffer.append(root);
            buffer.append(".");

            throw new BindingException(buffer.toString(), this, e);
        }
    }

    /**
    *  Returns false.
    *
    */

    public boolean isStatic()
    {
        return false;
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
    *  Sets up the helper object, but also optimizes the property path.
    *
    */

    private void setupHelper()
    {
        String[] split;
        int i;

        // Split the property path into individual property names.
        // We then optimize what we can from the path.  This will
        // shorten the property path and, in some cases, eliminate
        // it (if the property path was to an IComponent).

        split = splitter.splitToArray(propertyPath);

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

            if (split[i].equals("components") &&
                i + 1 < split.length)
            {
                root = root.getComponent(split[i+1]);
                i++;
				continue;
            }

            // Not a recognized prefix, break the loop

            break;
        }

        // We'ver removed some or all of the initial elements of split
        // but have to account for anthing left over.

        int count = split.length - i;

        if (count == 0)
        {
            // The property path was something like "page" or "component.foo"
            // and was completely eliminated.

            simple = true;
            propertyPath = null;
        }
        else if (count == 1)
        {
            // The path was something like "page.foo" and shrinks to just "foo".

            simple = true;
            propertyPath = split[i];
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

        }

        helper = PropertyHelper.forClass(root.getClass());
    }

    /**
    *  Updates the property for the binding to the given value.  
    *
    *  @throws BindingException if the property can't be updated (typically
    *  due to an security problem, or a missing mutator method).
    */

    public void setObject(Object value)
    {
        if (helper == null)
            setupHelper();

        try
        {
            if (simple)
                helper.set(root, propertyPath, value);
            else
                helper.setPath(root, splitPropertyPath, value);
        }
        catch (Throwable e)
        {
            StringBuffer buffer;

            buffer = new StringBuffer("Unable to update property ");
            buffer.append(propertyPath);
            buffer.append(" of ");
            buffer.append(root);
            buffer.append(" to ");
            buffer.append(value);

            buffer.append('.');

            throw new BindingException(buffer.toString(), this, e);
        }

    }

    /**
     *  Returns the a String representing the property path.  This includes
     *  the {@link IComponent#getExtendedId() extended id} of the root component
     *  and the property path ... once the binding is used, these may change
     *  due to optimization of the property path.
     *
     */

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

        buffer.append(']');

        return buffer.toString();
    }
}

