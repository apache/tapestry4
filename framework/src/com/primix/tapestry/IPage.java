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

import java.util.Locale;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.ComponentSpecification;
import java.io.OutputStream;
import javax.servlet.http.HttpSession;

/**
 * A root level component responsible for generating an entire a page
 * within the application.
 *
 * <p>The page has special knowledge of all the lifecycle components 
 * ({@link ILifecycle}) that are contained anywhere within it.
 *
 * <p>Pages are created dynamically from thier class names (part of the
 * {@link ComponentSpecification}).
 *
 * @see IPageSource
 * @see IPageLoader
 *
 * @author Howard Ship
 * @version $Id$
 */

public interface IPage extends IComponent
{
	/**
	*  Notifies the page about a component somewhere on the page
	*  (possibly, hidden inside deeply nested hiearchy of
	*  components).  The page is expected to use this to determine
	*  which components implement the {@link ILifecycle} interface,
	*  so that it can run lifecycle operations on them.
	*
	* <p>This method is invoked during the page loading process.  It
	* is invoked after the component and all of its children have
	* been created, and after {@link
	* IComponent#finishLoad(IPageLoader, ComponentSpecification)} has
	* been invoked on the component.
	*
	*  <p>Note that the page itself may be one component passed to
	*  <code>addLifecycleComponent()</code>, if it implements the
	*  {@link ILifecycle} interface.
	*
	*/

	public void addLifecycleComponent(ILifecycle component);

	/**
	*  Invoked on a page when it is no longer needed by
	*  the engine, just before is is
	*  returned to the pool.  The page is expected to
	*  null the engine, visit and changeObserver properties.
	*
	*  @see IPageSource#releasePage(IPage)
	*
	*/

	public void detach();

	/**
	*  Returns the {@link IEngine} that the page is currently
	*  attached to.
	*
	*/

	public IEngine getEngine();

	/**
	*  Returns the object (effectively, an {@link IPageRecorder}) that is notified
	*  of any changes to persistant properties of the page.
	*
	*/

	public ChangeObserver getChangeObserver();

	/**
	*  Returns the <code>Locale</code> of the page.
	*  The locale may be used to determine what template is
	*  used by the page and the components contained by the page.
	*
	*/

	public Locale getLocale();

	/**
	*  Updates the page's locale.  This is write-once, a subsequent attempt
	*  will throw an {@link ApplicationRuntimeException}.
	*
	*/

	public void setLocale(Locale value);

	/**
	*  The logical name is the name given by the application.
	*
	*/

	public String getName();

	/**
	*  Returns a particular component from within the page.  The path is a dotted
	*  name sequence identifying the component.  It may be null
	*  in which case the page returns itself.
	*
	*  @exception NoSuchComponentException runtime exception
	*  thrown if the path does not identify a component.
	*
	*/

	public IComponent getNestedComponent(String path);

	/**
	*  Attaches the page to the {@link IEngine engine}.
	*  This method is used when a pooled page is
	*  claimed for use with a particular engine; it will stay attached
	*  to the engine until the end of the current request cycle,
	*  then be returned to the pool.
	*
	*/

	public void attach(IEngine value);

	/**
	*  Invoked to render the entire page.  This should only be invoked by
	*  {@link IRequestCycle#renderPage(IResponseWriter writer)}.
	*
	*  <p>The page is responsible for:     
	* <ul>
	* <li>Invoking 
	*  {@link #beginResponse(IResponseWriter,IRequestCycle)}
	*  <li>Invoking {@link ILifecycle#prepareForRender(IRequestCycle)} on 
	*  each lifecycle component 
	*  <li>Invoking
	*  {@link IRequestCycle#commitPageChanges()}
	*  (unless the {@link IRequestCycle#isRewinding() cycle is rewinding})
	*  <li>Invoking
	*  {@link #render(IResponseWriter,IRequestCycle)}
	*  <li>Invoking
	*  {@link ILifecycle#cleanupAfterRender(IRequestCycle)} on each lifecycle component
	* </ul>
	*/

	public void renderPage(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException;

	public void setChangeObserver(ChangeObserver value);

	public void setName(String value);

	/**
	*  Method invoked by the page, action and immediate services to validate that the 
	*  user is allowed to visit the page.
	*
	*  <p>Most web applications have a concept of 'logging in' and
	*  pages that an anonymous (not logged in) user should not be
	*  able to visit directly.  This method acts as the first line of
	*  defense against a malicous user hacking URLs.
	*
	*  <p>Pages that should be protected will typically thow a {@link
	*  PageRedirectException}, to redirect the user to an appropriate
	*  part of the system (such as, a login page).
	*
	*/

	public void validate(IRequestCycle cycle)
	throws RequestCycleException;

	/**
	*  Invoked to create a response writer appropriate to the page
	*  (i.e., appropriate to the content of the page).  At this time,
	*  Tapestry only supports HTML, to an {@link HTMLResponseWriter}
	*  will be returned, but future enhancements may support XML, WAP,
	*  WML, etc., and thus other implementations of {@link IResponseWriter}
	*  will be returned.
	*
	*/

	public IResponseWriter getResponseWriter(OutputStream out);

	/**
	*  Invoked just before rendering of the page is initiated.  This gives
	*  the page a chance to perform any additional setup.  One possible behavior is
	*  to set HTTP headers and cookies before any output is generated.
	*
	*  <p>The timing of this explicitly <em>before</em> {@link IPageRecorder page recorder}
	*  changes are committed.  Rendering occurs <em>after</em> the recorders
	*  are committed, when it is too late to make changes to dynamic page
	*  properties.
	*
	*  
	*/

	public void beginResponse(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException;

	/**
	*  Returns the current {@link IRequestCycle}.  This is set when the
	*  page is loaded (or obtained from the pool) and attached to the
	*  {@link IEngine engine}.
	*
	*/

	public IRequestCycle getRequestCycle();	

	/**
	 *  Invoked by the {@link IRequestCycle} to inform the page of the cycle,
	 *  as it is loaded.
	 *
	 */
	 
	public void setRequestCycle(IRequestCycle cycle);
	
	/**
	*  Invoked when the application terminates (that is, when the {@link HttpSession}
	*  containing the {@link IEngine} is invalidated or times out).  This gives
	*  the page a chance to release any additional resources it may have ... 
	*  in particular,
	*  it allows a page to remove stateful session EJBs it may be using.
	*
	*  <p>Invokes {@link ILifecycle#cleanupComponent()} on any lifecycle components.
	*/

	public void cleanupPage();

	/**
	*  Returns the visit object for the application; the visit object
	*  contains application-specific information.
	*
	*/

	public Object getVisit();
}

