package com.primix.tapestry;


import com.primix.tapestry.spec.ComponentSpecification;
import java.util.*;
import com.primix.tapestry.components.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 * Defines an object which may be used on a Tapestry web page.
 *
 * <p>Components are created dynamically from thier class names (part of the
 *  {@link ComponentSpecification}.  Classes which
 * implement <code>IComponent</code> (but not {@link IPage})
 * must implement a constructor with the
 * following parameters:
 *
 * <p><code>(IPage, IComponent, String, ComponentSpecification)</code>
 *
 * <p>The {@link ILifecycle} interface defines additional methods
 * for components that need to perform extra work as they are loaded and before
 * and after rendering.
 *
 * @author Howard Ship
 * @version $Id$
 */

public interface IComponent extends IRender
{

    /**
     *  Adds a component to a container.  Should only be called during the page
     *  loading process, which is responsible for any checking.
     *
     *  @see IPageLoader
     *
     */
 
    public void addComponent(String name, IComponent component);

    /**
     *  Adds a new renderable element to the receiver.  The element may be either
     *  another component, or static HTML.
     */
 
    public void addWrapped(IRender element);

    /**
     *  Returns the asset map for the component, which may be null.
     *
     *  <p>The return value is unmodifiable.
     */
 
    public Map getAssets();

    /**
     *  Returns the binding with the given name or null if not found.
     *
     *  <p>Bindings are added to a component using {@link #setBinding(String,IBinding)}.
     */
 
    public IBinding getBinding(String name);

    /**
     *  Retrieves an contained component by its id.  
     *  Contained components have unique ids within their container.
     *
     *  @exception NoSuchComponentException runtime exception thrown if the named
     *  component does not exist.
     *
     */
 
    public IComponent getComponent(String id);

    /**
     *  Returns the component which embeds the receiver.  All components are contained within
     *  other components, with the exception of the root page component.  
     *
     *  <p>A page returns null.
     *
     */
 
    public IComponent getContainer();

    /**
     *  Returns a string identifying the name of the page and the id path of the reciever within
     *  the page.  Pages simply return their name.
     *
     *  @see #getIdPath()
     */
 
    public String getExtendedId();

    /**
     *  Returns the simple id of the component, as defined in its specification.  
     *
     *  <p>An id will be unique within the
     *  component which contains this component.
     *
     *  <p>A  {@link IPage page} will always return null.
     *
     */
 
    public String getId();

    /**
     *  Returns the qualified id of the component.  This represents a path from the 
     *  {@link IPage page} to
     *  this component, showing how components contain each other.  
     *
     *  <p>A {@link IPage page} will always return
     *  null.  A component contained on a page returns its simple id.
     *  Other components return their container's id path followed by a period and their
     *  own name.
     *
     *  @see #getId()
     */
 
    public String getIdPath();

    /**
     *  Returns the page which ultimately contains the receiver.  A page will return itself.
     *
     */
 
    public IPage getPage();

    /**
     *  Returns the specification which defines the component.
     *
     */
 
    public ComponentSpecification getSpecification();

    /**
     *  Invoked to make the receiver render any elements it wraps.  This is typically
     *  invoked by the receiver itself.  This method is public so that the
     *  {@link InsertWrapped} component may operate.
     *
     */
 
    public void renderWrapped(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException;

    /**
     *  Adds a binding to a container.  Should only be called during the page
     *  loading process (which is responsible for error checking).
     *
     *  @see IPageLoader
     *
     */
 
    public void setBinding(String name, IBinding binding);
}
