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

package net.sf.tapestry;

import java.io.OutputStream;
import java.util.Locale;

import net.sf.tapestry.event.ChangeObserver;
import net.sf.tapestry.event.PageCleanupListener;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageRenderListener;

/**
 * A root level component responsible for generating an entire a page
 * within the application.
 *
 * <p>Pages are created dynamically from thier class names (part of the
 * {@link ComponentSpecification}).
 *
 * @see IPageSource
 * @see IPageLoader
 *
 * @author Howard Lewis Ship
 * @version $Id$
 **/

public interface IPage extends IComponent
{
	/**
	 *  Invoked on a page when it is no longer needed by
	 *  the engine, just before is is
	 *  returned to the pool.  The page is expected to
	 *  null the engine, visit and changeObserver properties.
	 *
	 *  @see IPageSource#releasePage(IPage)
	 *
	 **/

	public void detach();

	/**
	 *  Returns the {@link IEngine} that the page is currently
	 *  attached to.
	 *
	 **/

	public IEngine getEngine();

	/**
	 *  Returns the object (effectively, an {@link IPageRecorder}) that is notified
	 *  of any changes to persistant properties of the page.
	 *
	 **/

	public ChangeObserver getChangeObserver();

	/**
	 *  Returns the <code>Locale</code> of the page.
	 *  The locale may be used to determine what template is
	 *  used by the page and the components contained by the page.
	 *
	 **/

	public Locale getLocale();

	/**
	 *  Updates the page's locale.  This is write-once, a subsequent attempt
	 *  will throw an {@link ApplicationRuntimeException}.
	 *
	 **/

	public void setLocale(Locale value);

	/**
	 *  The logical name is the name given by the application.
	 *
	 **/

	public String getName();

	/**
	 *  Returns a particular component from within the page.  The path is a dotted
	 *  name sequence identifying the component.  It may be null
	 *  in which case the page returns itself.
	 *
	 *  @exception NoSuchComponentException runtime exception
	 *  thrown if the path does not identify a component.
	 *
	 **/

	public IComponent getNestedComponent(String path);

	/**
	 *  Attaches the page to the {@link IEngine engine}.
	 *  This method is used when a pooled page is
	 *  claimed for use with a particular engine; it will stay attached
	 *  to the engine until the end of the current request cycle,
	 *  then be returned to the pool.
	 *
	 **/

	public void attach(IEngine value);

	/**
	 *  Invoked to render the entire page.  This should only be invoked by
	 *  {@link IRequestCycle#renderPage(IMarkupWriter writer)}.
	 *
	 *  <p>The page performs a render using the following steps:
	 *
	 * <ul>
	 *  <li>Invokes {@link PageRenderListener#pageBeginRender(PageEvent)}
	 *  <li>Invokes {@link #beginResponse(IMarkupWriter, IRequestCycle)}
	 *  <li>Invokes {@link IRequestCycle#commitPageChanges()} (if not rewinding)
	 *  <li>Invokes {@link #render(IMarkupWriter, IRequestCycle)}
	 *  <li>Invokes {@link PageRenderListener#pageEndRender(PageEvent)} (this occurs
	 *  even if a previous step throws an exception)
	 *
	 **/

	public void renderPage(IMarkupWriter writer, IRequestCycle cycle)
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
	 **/

	public void validate(IRequestCycle cycle) throws RequestCycleException;

	/**
	 *  Invoked to create a response writer appropriate to the page
	 *  (i.e., appropriate to the content of the page).  At this time,
	 *  Tapestry only supports HTML, to an {@link HTMLResponseWriter}
	 *  will be returned, but future enhancements may support XML, WAP,
	 *  WML, etc., and thus other implementations of {@link IMarkupWriter}
	 *  will be returned.
	 *
	 **/

	public IMarkupWriter getResponseWriter(OutputStream out);

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
	 **/

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException;

	/**
	 *  Returns the current {@link IRequestCycle}.  This is set when the
	 *  page is loaded (or obtained from the pool) and attached to the
	 *  {@link IEngine engine}.
	 *
	 **/

	public IRequestCycle getRequestCycle();

	/**
	 *  Invoked by the {@link IRequestCycle} to inform the page of the cycle,
	 *  as it is loaded.
	 *
	 **/

	public void setRequestCycle(IRequestCycle cycle);

	/**
	 *  Invoked when the application terminates (that is, when the {@link HttpSession}
	 *  containing the {@link IEngine} is invalidated or times out).  This gives
	 *  the page a chance to release any additional resources it may have ...
	 *  in particular,
	 *  it allows a page to remove stateful session EJBs it may be using.
	 *
	 *  <p>Invokes {@link PageCleanupListener#pageCleanup(PageEvent)} on any listeners.
	 *
	 **/

	public void cleanupPage();

	/**
	 *  Returns the visit object for the application; the visit object
	 *  contains application-specific information.
	 *
	 **/

	public Object getVisit();

	/**
	 *  @since 1.0.5
	 *
	 **/

	public void addPageRenderListener(PageRenderListener listener);

	/**
	 *  @since 1.0.5
	 *
	 **/

	public void addPageDetachListener(PageDetachListener listener);

	/**
	 *  @since 1.0.5
	 *
	 **/

	public void addPageCleanupListener(PageCleanupListener listener);
}