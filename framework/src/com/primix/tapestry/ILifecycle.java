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

package com.primix.tapestry;

import com.primix.tapestry.spec.ComponentSpecification;

// Appease Javadoc
import com.primix.tapestry.components.*;

/**
 *  Additional {@link IComponent} methods for components with special lifecycles.
 *  Few components require these methods.
 *
 *  <p>The advantage of these methods versus simply
 *  overriding {@link IComponent#render(IResponseWriter, IRequestCycle)} is that
 *  they are called just once, before and after all rendering takes place.
 *  Components may be inside one or more {@link Foreach} components, or something
 *  similar, in which case <code>render()</code> is invoked multiple times.
 *
 * <p>{@link AbstractComponent} provides null implementations of these methods,
 * even though it does not implement the interface.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public interface ILifecycle
{
    
    /**
     *  Invoked after rendering has taken place, even if an exception is thrown.
     *  <p>A component is <em>not</em> guarenteed to have been sent
     *  {@link #prepareForRender(IRequestCycle)} yet,
     *  as an exception may be thrown while lifecycle methods are being invoked.
     *
     */
 
    public void cleanupAfterRender(IRequestCycle cycle);

    /**
     *  Invoked on all lifecycle components prior to starting the rendering process.
     *  This gives components an opportunity to claim any resources needed for the duration of
     *  the render process.  Lifecycle components on a page will have this method
     *  invoked in an unspecified order.
     *
     */
 
    public void prepareForRender(IRequestCycle cycle);

    /**
     *  Invoked from {@link IPage#detach()}, this allows
     *  a component to reset its state so that it is equivalent to a newly
     *  constructed instance.
     *
     *  <p>Imagine the following scenario:
     *  <ul>
     *  <li>Page P is created in session 1
     *  <li>Session 1 modifies the property x
     *  <li>Session 1 releases page P to the {@link IPageSource} pool
     *  <li>A new session, number 2, gets page P from the pool
     *  </ul>
     *
     *  <p>What is the value of property x?  It's the value left there by session 1,
     *  but session 2 expects property x to have its initial value.  Since session 2
     *  has never changed property x, the {@link IPageRecorder} for page P in session 2
     *  has no data with which to rollback the property.
     *
     *  <p>This method allows page P to set the value for property X back to its
     *  initial value, hiding the change made by session 1.  If session 1 later
     *  reuses this same instance of page P, the {@link IPageRecorder} will restore
     *  the property back to the value previously stored.
     *
     *  <p>Components should be designed to minimize the need for this method, and
     *  to make it easy to implement it.  Initial values should usually be constants,
     *  preferably null or 0.
     *
     */
 
    public void reset();
	
	/**
	 *  Invoked from {@link IPage#cleanupPage()} to cleanup any long-held resources
	 *  (typically, stateful session EJBs) that the component may have a reference
	 *  to.
	 *
	 */
	 
	public void cleanupComponent();
}
