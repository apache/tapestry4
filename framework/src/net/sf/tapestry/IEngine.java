package net.sf.tapestry;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;

import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 * Defines the core, session-persistant object used to run a Tapestry
 * application for a single client (each client will have its own instance of the engine).
 *
 * <p>The engine exists to provide core services to the pages and components
 * that make up the application.  The engine is a delegate to the
 * {@link ApplicationServlet} via the {@link #service(RequestContext)} method.
 *
 * <p>Engine instances are persisted in the {@link HttpSession} and are serializable.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 **/

public interface IEngine
{
    /**
     *  The name ("Home") of the default page presented when a user first accesses the
     *  application.
     *
     **/

    public static final String HOME_PAGE = "Home";

    /**
     *  The name ("Exception") of the page used for reporting exceptions.
     *  
     *  <p>Such a page must have
     *  a writable JavaBeans property named 'exception' of type 
     * <code>java.lang.Throwable</code>.
     *
     **/

    public static final String EXCEPTION_PAGE = "Exception";

    /**
     *  The name ("StaleLink") of the page used for reporting stale links.
     *
     **/

    public static final String STALE_LINK_PAGE = "StaleLink";

    /**
     *  The name ("StaleSession") of the page used for reporting state sessions.
     *
     **/

    public static final String STALE_SESSION_PAGE = "StaleSession";

    /**
     *  Forgets changes to the named page by discarding the page recorder for the page.
     *  This is used when transitioning from one part
     *  of an application to another.  All property changes for the page are lost.
     *
     *  <p>This should be done if the page is no longer needed or relevant, otherwise
     *  the properties for the page will continue to be recorded by the engine, which
     *  is wasteful (especially if clustering or failover is employed on the application).
     *
     *  <p>Throws an {@link ApplicationRuntimeException} if there are uncommitted changes
     *  for the recorder (in the current request cycle).
     *
     **/

    public void forgetPage(String name);

    /**
     *  Returns the locale for the engine.  This locale is used when selecting
     *  templates and assets.
     **/

    public Locale getLocale();

    /**
     *  Changes the engine's locale.  Any subsequently loaded pages will be
     *  in the new locale (though pages already loaded stay in the old locale).
     *  Generally, you should render a new page after changing the locale, to
     *  show that the locale has changed.
     *
     **/

    public void setLocale(Locale value);

    /**
     *  Returns a recorder for a page.  Returns null if the page record has
     *  not been created yet.
     *
     *  @see #createPageRecorder(String, IRequestCycle)
     **/

    public IPageRecorder getPageRecorder(String pageName);

    /**
     *  Creates a new page recorder for the named page.
     *
     **/

    public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle);

    /**
     *  Returns the object used to load a page from its specification.
     *
     **/

    public IPageSource getPageSource();

    /**
     *  Gets the named service, or throws an {@link
     *  net.sf.tapestry.ApplicationRuntimeException} 
     *  if the application can't provide
     *  the named server.
     *
     *  <p>The object returned has a short lifecycle (it isn't
     *  serialized with the engine).  Repeated calls with the
     *  same name are not guarenteed to return the same object,
     *  especially in different request cycles.
     *
     **/

    public IEngineService getService(String name);

    /**
     *  Returns the URL path that corresponds to the servlet for the application.  
     *  This is required by instances of {@link IEngineService} that need 
     *  to construct URLs for the application.  This value will include
     *  the context path.
     **/

    public String getServletPath();

    /**
     *  Returns the context path, a string which is prepended to the names of
     *  any assets or servlets.  This may be the empty string, but won't be null.
     *
     *  <p>This value is obtained from {@link javax.servlet.http.HttpServletRequest#getContextPath()}.
     **/

    public String getContextPath();

    /**
     *  Returns the application specification that defines the application
     *  and its pages.
     *
     **/

    public IApplicationSpecification getSpecification();

    /**
     *  Returns the source of all component specifications for the application.  
     *  The source is shared between sessions.
     *
     **/

    public ISpecificationSource getSpecificationSource();

    /**
     *  Returns the source for HTML templates.
     *
     **/

    public ITemplateSource getTemplateSource();

    /**
     *  Method invoked from the {@link net.sf.tapestry.ApplicationServlet} 
     *  to perform processing of the
     *  request.  The return value for this method indicates whether the state of the engine
     *  could possibly change during processing; in most cases this is true.  In a few
     *  possible cases, this is false.  The ApplicationServlet sometimes performs extra
     *  work to ensure that the engine is properly replicated in a clustering environment; if
     *  this method returns false, it can skip that extra work.
     *
     *  @return true if the state of the engine was, or could have been, changed during
     *  processing.
     *
     **/

    public boolean service(RequestContext context) throws ServletException, IOException;

    /**
     *  Returns an object that can resolve resources and classes.
     *
     **/

    public IResourceResolver getResourceResolver();

    /**
     *  Returns the visit object, an object that represents the client's visit
     *  to the application.  This is where most server-side state is stored (with
     *  the exception of persistent page properties).
     *
     *  <p>Returns the visit, if it exists, or null if it has not been created.
     *
     **/

    public Object getVisit();

    /**
     *  Returns the visit object, creating it if necessary.
     *
     **/

    public Object getVisit(IRequestCycle cycle);

    /**
     *  Allows the visit object to be removed; typically done when "shutting down"
     *  a user's session (by setting the visit to null).
     *
     **/

    public void setVisit(Object value);

    /**
     *  Returns the globally shared application object. The global object is
     *  stored in the servlet context and shared by all instances of the engine
     *  for the same application (within the same JVM; the global is
     *  <em>not</em> shared between nodes in a cluster).
     *
     *  <p>Returns the global object, if it exists, or null if not defined.
     *
     *  @since 2.3
     * 
     **/

    public Object getGlobal();

    /**
     *  Returns true if the application allows the reset service.
     *
     *  @since 0.2.9
     * 
     **/

    public boolean isResetServiceEnabled();

    /**
     *  Returns a source for parsed 
     *  {@link net.sf.tapestry.IScript}s.  The source is typically
     *  shared between all sessions.
     *
     *  @since 1.0.2
     *
     **/

    public IScriptSource getScriptSource();

    /**
     *  Returns true if the engine has state and, therefore, should be stored
     *  in the HttpSession.  This starts as false, but becomes true when
     *  the engine requires state (such as when a visit object is created,
     *  or when a peristent page property is set).
     *
     *  @since 1.0.2
     *
     **/

    public boolean isStateful();

    /**
     *  Returns a shared object that allows components to find
     *  their set of localized strings.
     * 
     *  @since 2.0.4
     * 
     **/

    public IComponentStringsSource getComponentStringsSource();

    /**
     *  Returns a shared instance of {@link net.sf.tapestry.util.io.DataSqueezer}.
     * 
     *  @since 2.2
     * 
     **/

    public DataSqueezer getDataSqueezer();

    /** 
     * 
     *  @see #setRefreshing(boolean)
     *  @since 2.2 
     * 
     **/

    public boolean isRefreshing();

    /** 
     * 
     *  Set by the application servlet just before the engine is
     *  "refreshed" into the HttpSession.  Some servlet containers
     *  will invoke the 
     *  {@link javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)}
     *  event listener method (before updating the attribute and invoking
     *  {@link javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)}), 
     *  which will be interpreted by the engine
     *  as the session being invalidated, at which point it will (normally)
     *  true to cleanup the pages ... setting this flag, prevents the unwanted cleanup.     
     * 
     *  @since 2.2
     * 
     **/

    public void setRefreshing(boolean refreshing);

    /**
     *  Returns a {@link net.sf.tapestry.IPropertySource} that should be
     *  used to obtain configuration data.  The returned source represents
     *  a search path that includes (at a minimum):
     *  
     *  <ul>
     *  <li>Properties of the {@link net.sf.tapestry.spec.ApplicationSpecification}
     *  <li>Initial Parameters of servlet (configured in the <code>web.xml</code> deployment descriptor)
     *  <li>Initial Parameter of the servlet context (also configured in <code>web.xml</code>)
     *  <li>System properties (defined with the <code>-D</code> JVM command line parameter)
     *  </ul>
     * 
     *  @since 2.3
     **/

    public IPropertySource getPropertySource();

}