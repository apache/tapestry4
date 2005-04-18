// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry;

import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.event.ChangeObserver;
import org.apache.tapestry.event.PageAttachListener;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEndRenderListener;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.util.ContentType;

/**
 * A root level component responsible for generating an entire a page within the application.
 * <p>
 * Pages are created dynamically from thier class names (part of the
 * {@link org.apache.tapestry.spec.IComponentSpecification}).
 * 
 * @see org.apache.tapestry.engine.IPageSource
 * @see org.apache.tapestry.engine.IPageLoader
 * @author Howard Lewis Ship
 */

public interface IPage extends IComponent
{
    /**
     * Invoked on a page when it is no longer needed by the engine, just before is is returned to
     * the pool. The page is expected to null the engine, visit and changeObserver properties.
     * <p>
     * Classes should also reset any properties to default values (as if the instance was freshly
     * instantiated).
     * 
     * @see org.apache.tapestry.engine.IPageSource#releasePage(IPage)
     */

    public void detach();

    /**
     * Returns the {@link IEngine}that the page is currently attached to.
     */

    public IEngine getEngine();

    /**
     * Returns the object (effectively, an {@link org.apache.tapestry.engine.IPageRecorder}) that
     * is notified of any changes to persistant properties of the page.
     */

    public ChangeObserver getChangeObserver();

    /**
     * Returns the <code>Locale</code> of the page. The locale may be used to determine what
     * template is used by the page and the components contained by the page.
     */

    public Locale getLocale();

    /**
     * Updates the page's locale. This is write-once, a subsequent attempt will throw an
     * {@link ApplicationRuntimeException}.
     */

    public void setLocale(Locale value);

    /**
     * Returns the fully qualified name of the page, including its namespace prefix, if any.
     * 
     * @since 2.3
     */

    public String getPageName();

    /**
     * Sets the name of the page.
     * 
     * @param pageName
     *            fully qualified page name (including namespace prefix, if any)
     * @since 3.0
     */

    public void setPageName(String pageName);

    /**
     * Returns a particular component from within the page. The path is a dotted name sequence
     * identifying the component. It may be null in which case the page returns itself.
     * 
     * @exception ApplicationRuntimeException
     *                runtime exception thrown if the path does not identify a component.
     */

    public IComponent getNestedComponent(String path);

    /**
     * Attaches the page to the {@link IEngine engine}. This method is used when a pooled page is
     * claimed for use with a particular engine; it will stay attached to the engine until the end
     * of the current request cycle, then be returned to the pool.
     * <p>
     * This method will notify any {@link PageAttachListener}s.
     * <p>
     * This method is rarely overriden; to initialize page properties before a render, implement the
     * {@link PageBeginRenderListener}interface.
     */

    public void attach(IEngine engine, IRequestCycle cycle);

    /**
     * Invoked to render the entire page. This should only be invoked by
     * {@link IRequestCycle#renderPage(IMarkupWriter writer)}.
     * <p>
     * The page performs a render using the following steps:
     * <ul>
     * <li>Invokes
     * {@link PageBeginRenderListener#pageBeginRender(org.apache.tapestry.event.PageEvent)}
     * <li>Invokes {@link #beginResponse(IMarkupWriter, IRequestCycle)}
     * <li>Invokes {@link IRequestCycle#commitPageChanges()}(if not rewinding)
     * <li>Invokes {@link #render(IMarkupWriter, IRequestCycle)}
     * <li>Invokes {@link PageEndRenderListener#pageEndRender(org.apache.tapestry.event.PageEvent)}
     * (this occurs even if a previous step throws an exception).
     * </ul>
     */

    public void renderPage(IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Invoked before a partial render of the page occurs (this happens when rewinding a
     * {@link org.apache.tapestry.form.Form}within the page). The page is expected to fire
     * appopriate events.
     * 
     * @since 2.2
     */

    public void beginPageRender();

    /**
     * Invoked after a partial render of the page occurs (this happens when rewinding a
     * {@link org.apache.tapestry.form.Form}within the page). The page is expected to fire
     * appropriate events.
     * 
     * @since 2.2
     */

    public void endPageRender();

    public void setChangeObserver(ChangeObserver value);

    /**
     * Method invoked by the page, action and direct services to validate that the user is allowed
     * to visit the page.
     * <p>
     * Most web applications have a concept of 'logging in' and pages that an anonymous (not logged
     * in) user should not be able to visit directly. This method acts as the first line of defense
     * against a malicous user hacking URLs.
     * <p>
     * Pages that should be protected will typically throw a {@linkPageRedirectException}, to
     * redirect the user to an appropriate part of the system (such as, a login page).
     * <p>
     * Since 3.0, it is easiest to not override this method, but to implement the
     * {@link PageValidateListener}interface instead.
     */

    public void validate(IRequestCycle cycle);

    /**
     * Invoked to obtain the content type to be used for the response. The implementation of this
     * method is the primary difference between an HTML page and an XML/WML/etc. page.
     */

    public ContentType getResponseContentType();

    /**
     * Invoked just before rendering of the page is initiated. This gives the page a chance to
     * perform any additional setup. One possible behavior is to set HTTP headers and cookies before
     * any output is generated.
     * <p>
     * The timing of this explicitly <em>before</em>
     * {@link org.apache.tapestry.engine.IPageRecorder page recorder}changes are committed.
     * Rendering occurs <em>after</em> the recorders are committed, when it is too late to make
     * changes to dynamic page properties.
     */

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Returns the current {@link IRequestCycle}. This is set when the page is loaded (or obtained
     * from the pool) and attached to the {@link IEngine engine}.
     */

    public IRequestCycle getRequestCycle();

    /**
     * Returns the visit object for the application; the visit object contains application-specific
     * information.
     */

    public Object getVisit();

    /**
     * Returns the globally shared application object. The global object is stored in the servlet
     * context.
     * <p>
     * Returns the global object, if it exists, or null if not defined.
     * 
     * @since 2.3
     */

    public Object getGlobal();

    /**
     * @since 1.0.5
     * @deprecated To be removed in 4.1 Use
     *             {@link #addPageBeginRenderListener(PageBeginRenderListener)}or
     *             {@link #addPageEndRenderListener(PageEndRenderListener)}.
     */

    public void addPageRenderListener(PageRenderListener listener);

    /**
     * @since 2.1
     * @deprecated To be removed in 4.1. Use
     *             {@link #removePageBeginRenderListener(PageBeginRenderListener)}or
     *             {@link #removePageEndRenderListener(PageEndRenderListener)}.
     */

    public void removePageRenderListener(PageRenderListener listener);

    /** @since 4.0 */
    public void addPageBeginRenderListener(PageBeginRenderListener listener);

    /** @since 4.0 */
    public void removePageBeginRenderListener(PageBeginRenderListener listener);

    /** @since 4.0 */

    public void addPageEndRenderListener(PageEndRenderListener listener);

    /** @since 4.0 */

    public void removePageEndRenderListener(PageEndRenderListener listener);

    /**
     * @since 1.0.5
     */

    public void addPageDetachListener(PageDetachListener listener);

    /**
     * @since 2.1
     */

    public void removePageDetachListener(PageDetachListener listener);

    /**
     * @since 3.0
     */

    public void addPageValidateListener(PageValidateListener listener);

    /**
     * @since 3.0
     */

    public void removePageValidateListener(PageValidateListener listener);

    /** @since 4.0 */

    public void addPageAttachListener(PageAttachListener listener);

    /** @since 4.0 */

    public void removePageAttachListener(PageAttachListener listener);
}