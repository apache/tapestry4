package net.sf.tapestry;

import java.io.OutputStream;
import java.util.Locale;

import net.sf.tapestry.event.ChangeObserver;
import net.sf.tapestry.event.PageCleanupListener;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  A root level component responsible for generating an entire a page
 *  within the application.
 *
 *  <p>Pages are created dynamically from thier class names (part of the
 *  {@link ComponentSpecification}).
 *
 *  @see IPageSource
 *  @see IPageLoader
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IPage extends IComponent
{
	/**
	 *  Invoked on a page when it is no longer needed by
	 *  the engine, just before is is
	 *  returned to the pool.  The page is expected to
	 *  null the engine, visit and changeObserver properties.
	 * 
	 *  <p>Classes should also reset any properties to
	 *  default values (as if the instance
	 *  was freshly instantiated).
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
	 *  Returns the object (effectively, an 
	 *  {@link net.sf.tapestry.IPageRecorder}) that is notified
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
	 *  Returns the simple name of the page within its namespace.
	 *
     *  @deprecated This method has no use and will be removed after releaes 2.4.
     * 
	 **/

	public String getName();
    
    /**
     *  Returns the fully qualified name of the page, including its
     *  namespace prefix, if any.
     * 
     *  @since 2.3
     *  @deprecated This method will be removed after release 2.4, use
     *  {@link #getPageName()} instead.
     * 
     **/
    
    public String getQualifiedName();

    /**
     *  Returns the fully qualified name of the page, including its
     *  namespace prefix, if any.
     * 
     *  @since 2.4
     * 
     **/
    
    public String getPageName();


    /**
     *  Sets the name of the page.
     * 
     *  @param name fully qualified page name (including namespace prefix, if any)
     * 
     *  @since 2.4
     * 
     **/
    
    public void setPageName(String pageName);
    
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
	 *  <p>This method is rarely overriden; to initialize
	 *  page properties before a render, override
	 *  {@link #beginResponse(IMarkupWriter, IRequestCycle)}.
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
	 *  even if a previous step throws an exception).
	 * </ul>
	 *
	 **/

	public void renderPage(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException;

    /**
     *  Invoked before a partial render of the page occurs
     *  (this happens when rewinding a {@link net.sf.tapestry.form.Form}
     *  within the page).  The page is expected to fire appopriate
     *  events.
     * 
     *  @since 2.2
     * 
     **/
    
    public void beginPageRender();
    
    /**
     *  Invoked after a partial render of the page occurs
     *  (this happens when rewinding a {@link net.sf.tapestry.form.Form}
     *  within the page).  The page is expected to fire
     *  appropriate events.
     * 
     *  @since 2.2
     * 
     **/    
    
    public void endPageRender();
    
	public void setChangeObserver(ChangeObserver value);

    /**
     *  Sets the simple (unqualified) name for the page.
     * 
     *  @deprecated To be removed after 2.4, use {@link #setPageName(String)}
     *  instead.
     * 
     **/
    
	public void setName(String value);

	/**
	 *  Method invoked by the page, action and direct services 
	 *  to validate that the
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
	 *  (i.e., appropriate to the content of the page).
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
	 *  Invoked when the application terminates (that is, when the 
     *  {@link javax.servlet.http.HttpSession}
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
     *
     *  @since 2.1
     * 
     **/
    
    public void removePageRenderListener(PageRenderListener listener);

	/**
	 *  @since 1.0.5
	 *
	 **/

	public void addPageDetachListener(PageDetachListener listener);

    /**
     * 
     *  @since 2.1
     * 
     **/
    
    public void removePageDetachListener(PageDetachListener listener);
    
	/**
	 *  @since 1.0.5
	 *
	 **/

	public void addPageCleanupListener(PageCleanupListener listener);
    
    /**
     * 
     *  @since 2.1
     * 
     **/
    
    public void removePageCleanupListener(PageCleanupListener listener);
    
}