package com.primix.tapestry;

import java.util.Locale;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.ComponentSpecification;
import java.io.OutputStream;

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
 * A root level component responsible for generating an entire HTML page.
 *
 * <p>The page has special knowledge of all the lifecycle components 
 * ({@link ILifecycle}) that are contained anywhere within it.
 *
 * <p>Pages are created dynamically from thier class names (part of the
 * {@link ComponentSpecification}).  Classes which
 * implement <code>IPage</code> must implement a constructor with the
 * following parameters:
 *
 * <p><code>({@link IApplication}, {@link ComponentSpecification})</code>
 *
 * <p>The constructor is expected to set the traceLogger and locale for the page
 * from application.
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
     * ILifecycle#finishLoad(IPageLoader, ComponentSpecification)} has
     * been invoked on the component.
     *
     *  <p>Note that the page itself may be one component passed to
     *  <code>addLifecycleComponent()</code>, if it implements the *
     *  {@link ILifecycle} interface.
     *
     */
 
    public void addLifecycleComponent(ILifecycle component);

    /**
     *  Invoked on a page when it is removed from an application (and
     *  returned to a pool).  This nulls the application and
     *  and changeObserver properties.
     *
     *  @see IPageSource#releasePage(IPage)
     *
     */
 
    public void detachFromApplication();

    /**
     *  Returns the IApplication that the IPage belongs to.
     *
     */
 
    public IApplication getApplication();

    public ChangeObserver getChangeObserver();

    /**
     *  Returns the <code>Locale</code> of the page.  A page's locale
     *  is determined from the application when the page is initially
     *  loaded.  The locale may be used to determine what template is
     *  used by the page and the components contained by the page.
     *
     */
 
    public Locale getLocale();

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
     *  Makes the page part of the application.
	 *  This method is used when a pooled page is
     *  claimed for use with a particular application.
     *
     *  <p>This method should invoke {@link ILifecycle#reset()} on any
     *  lifecycle components.
     *
     */
 
    public void joinApplication(IApplication value);

    /**
     *  Invoked to render the entire page.  This should only be invoked by
     *  {@link IRequestCycle#renderPage(IResponseWriter writer)}.
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
     *  the page a chance to perform any additional setup.  Typically, this
     *  invokes setting HTTP headers and cookies.
     *
     */
     
    public void beginResponse(IResponseWriter writer, IRequestCycle cycle)
        throws RequestCycleException;
}
