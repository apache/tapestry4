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

import java.util.Iterator;

import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.ResponseBuilder;

/**
 * Controller object that manages a single request cycle. A request cycle is one 'hit' on the web
 * server. In the case of a Tapestry application, this will involve:
 * <ul>
 * <li>Responding to the URL by finding an {@link IEngineService}object
 * <li>Determining the result page
 * <li>Renderring the result page
 * <li>Releasing any resources
 * </ul>
 * <p>
 * Mixed in with this is:
 * <ul>
 * <li>Exception handling
 * <li>Loading of pages and templates from resources
 * <li>Tracking changes to page properties, and restoring pages to prior states
 * <li>Pooling of page objects
 * </ul>
 * 
 * <p>
 * A request cycle is broken up into two phases. The <em>rewind</em> phase is optional, as it tied
 * to {@link org.apache.tapestry.link.ActionLink}or {@link org.apache.tapestry.form.Form}
 * components. In the rewind phase, a previous page render is redone (discarding output) until a
 * specific component of the page is reached. This rewinding ensures that the page is restored to
 * the exact state it had when the URL for the request cycle was generated, taking into account the
 * dynamic nature of the page ({@link org.apache.tapestry.components.For},
 * {@link org.apache.tapestry.components.Conditional}, etc.). Once this component is reached, it
 * can notify its {@link IActionListener}. The listener has the ability to update the state of any
 * pages and select a new result page.
 * </p>
 * 
 * <p>
 * Following the rewind phase is the <em>render</em> phase. During the render phase, a page is
 * actually rendered and output sent to the client web browser.
 * </p>
 * @author Howard Lewis Ship
 */

public interface IRequestCycle
{
    /**
     * Invoked after the request cycle is no longer needed, to release any resources it may have.
     * This includes releasing any loaded pages back to the page source.
     */

    void cleanup();

    /**
     * Passes the String through
     * {@link javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)}, which ensures
     * that the session id is encoded in the URL (if necessary).
     */

    String encodeURL(String URL);

    /**
     * Returns the engine which is processing this request cycle.
     */

    IEngine getEngine();

    /**
     * Retrieves a previously stored attribute, returning null if not found. Attributes allow
     * components to locate each other; primarily they allow a wrapped component to locate a
     * component which wraps it. Attributes are cleared at the end of the render (or rewind).
     */

    Object getAttribute(String name);

    /**
     * Identifies the active page, the page which will ultimately render the response.
     */

    IPage getPage();

    /**
     * Returns the page with the given name. If the page has been previously loaded in the current
     * request cycle, that page is returned. Otherwise, the engine's page loader is used to load the
     * page.
     * 
     * @throws PageNotFoundException
     *             if the page does not exist.
     * @see org.apache.tapestry.engine.IPageSource#getPage(IRequestCycle, String, IMonitor)
     */

    IPage getPage(String name);

    /**
     * Returns true if the context is being used to rewind a prior state of the page. This is only
     * true when there is a target action id.
     */

    boolean isRewinding();

    /**
     * Checks to see if the current action id matches the target action id. Returns true only if
     * they match. Returns false if there is no target action id (that is, during page rendering).
     * <p>
     * If theres a match on action id, then the component is compared against the target component.
     * If there's a mismatch then a {@link StaleLinkException}is thrown.
     */

    boolean isRewound(IComponent component);

    /**
     * Sets the {@link ResponseBuilder} to use for this response, don't 
     * try setting this unless you're very sure you know what you are doing as
     * this isn't the only way that it is used. (ie replacing this builder won't 
     * necessarily override another builder being used already)
     * 
     * @param builder The response builder that may be used by components
     * to help with delegate/sub component rendering.
     */
    void setResponseBuilder(ResponseBuilder builder);
    
    /**
     * Entry point for getting the response builder used to build
     * this response.
     * @return The response builder used for this response.
     */
    ResponseBuilder getResponseBuilder();
    
    /**
     * Tests if the render component chain is empty, meaning no components have
     * been loaded onto the stack yet.
     * 
     * @return True, if the current stack is empty.
     */
    boolean renderStackEmpty();
    
    /**
     * Looks at the object at the top of the render stack without removing 
     * the {@link IRender} from the stack.
     * 
     * @return The last (parent) item added to the current render stack.
     */
    IRender renderStackPeek();
    
    /**
     * Removes the {@link IRender} at the top of the stack, if any.
     * 
     * @return The removed {@link IRender}, if any.
     */
    IRender renderStackPop();
    
    /**
     * Pushes the specified render onto the current render stack.
     * 
     * @param render The {@link IRender} object being pushed.
     * @return The added {@link IRender}.
     */
    IRender renderStackPush(IRender render);
    
    /**
     * Returns the 1-based position where an object is on this stack. If the object 
     * o occurs as an item in this stack, this method returns the distance from the 
     * top of the stack of the occurrence nearest the top of the stack; the topmost 
     * item on the stack is considered to be at distance 1. The equals method is used 
     * to compare o to the items in this stack.
     * 
     * @param render The {@link IRender} being searched for.
     * 
     * @return the 1-based position from the top of the stack where the object is 
     *          located; the return value -1  indicates that the object is not on the stack.
     */
    int renderStackSearch(IRender render);
    
    /**
     * Creates a traversable iterator for moving through the stack.
     * 
     * @return An iterator over the current stack.
     */
    Iterator renderStackIterator();
    
    /**
     * Removes a previously stored attribute, if one with the given name exists.
     */

    void removeAttribute(String name);

    /**
     * Renders the given page. Applications should always use this method to render the page, rather
     * than directly invoking {@link IPage#render(IMarkupWriter, IRequestCycle)}since the request
     * cycle must perform some setup before rendering.
     */

    void renderPage(ResponseBuilder builder);

    /**
     * Allows a temporary object to be stored in the request cycle, which allows otherwise unrelated
     * objects to communicate. This is similar to <code>HttpServletRequest.setAttribute()</code>,
     * except that values can be changed and removed as well.
     * <p>
     * This is used by components to locate each other. A component, such as
     * {@link org.apache.tapestry.html.Body}, will write itself under a well-known name into the
     * request cycle, and components it wraps can locate it by that name.
     * <p>
     * Attributes are cleared at the end of each render or rewind phase.
     */

    void setAttribute(String name, Object value);

    /**
     * Invoked just before rendering the response page to get all
     * {@link org.apache.tapestry.engine.IPageRecorder page recorders}touched in this request cycle
     * to commit their changes (save them to persistant storage).
     * 
     * @see org.apache.tapestry.engine.IPageRecorder#commit()
     */

    void commitPageChanges();

    /**
     * Returns the service which initiated this request cycle.
     * 
     * @since 1.0.1
     */

    IEngineService getService();

    /**
     * Used by {@link IForm forms}to perform a <em>partial</em> rewind so as to respond to the
     * form submission (using the direct service).
     * <p>
     * Note: the targetActionId parameter was removed in release 4.0.
     * 
     * @since 1.0.2
     */

    void rewindForm(IForm form);

    /**
     * Invoked by a {@link IEngineService service}&nbsp;to store an array of application-specific
     * parameters. These can later be retrieved (typically, by an application-specific listener
     * method) by invoking {@link #getListenerParameters()}.
     * 
     * @see org.apache.tapestry.engine.DirectService
     * @since 4.0
     */
    void setListenerParameters(Object[] parameters);

    /**
     * Returns parameters previously stored by {@link #setListenerParameters(Object[])}.
     * 
     * @since 4.0
     */

    Object[] getListenerParameters();

    /**
     * A convienience for invoking {@link #activate(IPage)}. Invokes {@link #getPage(String)}to
     * get an instance of the named page.
     * 
     * @since 3.0
     */

    void activate(String name);

    /**
     * Sets the active page for the request. The active page is the page which will ultimately
     * render the response. The activate page is typically set by the {@link IEngineService service}.
     * Frequently, the active page is changed (from a listener method) to choose an alternate page
     * to render the response).
     * <p>
     * {@link IPage#validate(IRequestCycle)}is invoked on the page to be activated.
     * {@link PageRedirectException}is caught and the page specified in the exception will be the
     * active page instead (that is, a page may "pass the baton" to another page using the
     * exception). The new page is also validated. This continues until a page does not throw
     * {@link PageRedirectException}.
     * <p>
     * Validation loops can occur, where page A redirects to page B and then page B redirects back
     * to page A (possibly with intermediate steps). This is detected and results in an
     * {@link ApplicationRuntimeException}.
     * 
     * @since 3.0
     */
    void activate(IPage page);
    
    /**
     * Returns a query parameter value, or null if not provided in the request. If multiple values
     * are provided, returns the first value.
     * 
     * @since 4.0
     */
    String getParameter(String name);

    /**
     * Returns all query parameter values for the given name. Returns null if no values were
     * provided.
     * 
     * @since 4.0
     */
    String[] getParameters(String name);

    /**
     * Converts a partial URL into an absolute URL. Prefixes the provided URL with servlet context
     * path (if any), then expands it to a full URL by prepending with the scheme, server and port
     * (determined from the current {@link org.apache.tapestry.web.WebRequest request}.
     * 
     * @since 4.0
     */

    String getAbsoluteURL(String partialURL);

    /**
     * Forgets any stored changes to the specified page. If the page has already been loaded (and
     * rolled back) then the loaded page instance is not affected; if the page is only loaded
     * subsequently, the page instance will not see any persisted property changes.
     * 
     * @since 4.0
     */

    void forgetPage(String name);

    /**
     * Returns the central {@link org.apache.tapestry.services.Infrastructure}&nbsp;object used to
     * manage the processing of the request.
     * 
     * @since 4.0
     */

    Infrastructure getInfrastructure();

    /**
     * Returns the provided string, possibly modified (with an appended suffix) to make it unique.
     * 
     * @param baseId
     *            the base id from which to generate the unique string.
     * @return baseId, or baseId with a suffix appended (if the method has been previously invoked
     *         with the same baseId).
     */

    String getUniqueId(String baseId);
    
    /**
     * Returns what <i>will</i> be the next unique id generated based on the given input, but doesn't
     * store the result.
     * 
     * @param baseId
     *            the base id from which to generate the unique string.
     * @return baseId, or baseId with a suffix appended (if the method has been previously invoked
     *         with the same baseId).
     */

    String peekUniqueId(String baseId);
    
    /**
     * Sends a redirect to the client web browser. This is currently a convinience for constructing
     * and throwing a {@link RedirectException}, but may change in a later release.
     * 
     * @since 4.0
     * @throws RedirectException
     */

    void sendRedirect(String URL);
}
