/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;

import org.apache.tapestry.engine.IComponentClassEnhancer;
import org.apache.tapestry.engine.IComponentMessagesSource;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IPageRecorder;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.ITemplateSource;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.io.DataSqueezer;
import org.apache.tapestry.util.pool.Pool;

/**
 * Defines the core, session-persistant object used to run a Tapestry
 * application for a single client (each client will have its own instance of the engine).
 *
 * <p>The engine exists to provide core services to the pages and components
 * that make up the application.  The engine is a delegate to the
 * {@link ApplicationServlet} via the {@link #service(RequestContext)} method.
 *
 * <p>Engine instances are persisted in the {@link javax.servlet.http.HttpSession} and are serializable.
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
     *  @see org.apache.tapestry.engine.HomeService
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
     *  <p>The page must implement a writeable JavaBeans proeprty named
     *  'message' of type <code>String</code>.
     *
     **/ 

    public static final String STALE_LINK_PAGE = "StaleLink";
    
    /**
     *  Returns a recorder for a page.  Returns null if the page recorder has
     *  not been created yet.
     *
     *  @see #createPageRecorder(String, IRequestCycle)
     * 
     **/

    public IPageRecorder getPageRecorder(String pageName, IRequestCycle cycle);

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
     *  org.apache.tapestry.ApplicationRuntimeException} 
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
     *  <p>This value is obtained from 
     *  {@link javax.servlet.http.HttpServletRequest#getContextPath()}.
     * 
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
     *  @see org.apache.tapestry.engine.AbstractEngine#createSpecificationSource(RequestContext)
     * 
     **/

    public ISpecificationSource getSpecificationSource();

    /**
     *  Returns the source for HTML templates.
     *
     *  @see  org.apache.tapestry.engine.AbstractEngine#createTemplateSource(RequestContext)
     * 
     **/

    public ITemplateSource getTemplateSource();

    /**
     *  Method invoked from the {@link org.apache.tapestry.ApplicationServlet} 
     *  to perform processing of the
     *  request.  In release 3.0, this has become more of a dirty flag, indicating
     *  if any state stored by the engine instance itself has changed.
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
     *  {@link org.apache.tapestry.IScript}s.  The source is 
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
     *  @see org.apache.tapestry.engine.AbstractEngine#createComponentStringsSource(RequestContext)
     * 
	 **/
	
	public IComponentMessagesSource getComponentMessagesSource();

    /**
     *  Returns a shared instance of {@link org.apache.tapestry.util.io.DataSqueezer}.
     * 
     *  @since 2.2
     * 
     *  @see org.apache.tapestry.engine.AbstractEngine#createDataSqueezer()
     * 
     **/
    
    public DataSqueezer getDataSqueezer();

    /**
     *  Returns a {@link org.apache.tapestry.engine.IPropertySource} that should be
     *  used to obtain configuration data.  The returned source represents
     *  a search path that includes (at a minimum):
     *  
     *  <ul>
     *  <li>Properties of the {@link org.apache.tapestry.spec.ApplicationSpecification}
     *  <li>Initial Parameters of servlet (configured in the <code>web.xml</code> deployment descriptor)
     *  <li>Initial Parameter of the servlet context (also configured in <code>web.xml</code>)
     *  <li>System properties (defined with the <code>-D</code> JVM command line parameter)
     *  <li>Hard-coded "factory defaults" (for some properties)
     *  </ul>
     * 
     *  @since 2.3
     *  @see org.apache.tapestry.engine.AbstractEngine#createPropertySource(RequestContext)
     * 
     **/

    public IPropertySource getPropertySource();
    
    /**
     *  Returns a {@link org.apache.tapestry.util.pool.Pool} that is used
     *  to store all manner of objects that are needed throughout the system.
     *  This is the best way to deal with objects that are both expensive to
     *  create and not threadsafe.  The reset service
     *  will clear out this Pool.
     * 
     *  @since 3.0
     *  @see org.apache.tapestry.engine.AbstractEngine#createPool(RequestContext)
     * 
     **/
    
    public Pool getPool();
    
    /**
     *  Returns an object that can create enhanced versions of component classes.
     * 
     *  @since 3.0
     *  @see org.apache.tapestry.engine.AbstractEngine#createComponentClassEnhancer(RequestContext)
     * 
     **/
    
    public IComponentClassEnhancer getComponentClassEnhancer();
    
    /**
     *  Returns the encoding to be used to generate the servlet responses and 
     *  accept the servlet requests.
     * 
     *  @since 3.0
     * 
     **/
    
    public String getOutputEncoding();
}
