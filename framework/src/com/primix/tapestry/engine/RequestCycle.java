package com.primix.tapestry.engine;

import com.primix.foundation.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.record.*;
import com.primix.tapestry.event.*;
import java.util.*;
import com.primix.tapestry.*;
import javax.servlet.http.*;
import com.primix.tapestry.components.html.link.*;
import org.apache.log4j.*;

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

/**
 *  Provides the logic for processing a single request cycle.  Provides access to
 *  the {@link IEngine engine} and the {@link RequestContext}.
 *
 * @author Howard Ship
 * @version $Id$
 */
 
public class RequestCycle 
    implements IRequestCycle, ChangeObserver
{
	private static final Category CAT = Category.getInstance(RequestCycle.class.getName());

	private IPage page;
	private IEngine engine;

	private RequestContext requestContext;

	private IMonitor monitor;

	private HttpServletResponse response;

	/**
	*  Temporary string buffer used for assembling things.
	*
	*/

	private StringBuffer buffer;

	private static final int MAP_SIZE = 5;

	/**
	*  A mapping of pages loaded during the current request cycle.
	*  Key is the page name, value is the {@link IPage} instance.
	*
	*/

	private Map loadedPages;

	/**
	 * A mapping of page recorders for the current request cycle.
	 * Key is the page name, value is the {@link IPageRecorder} instance.
	 *
	 */

	private Map loadedRecorders;

	private boolean rewinding = false;

	private Map attributes;

	private int actionId;
	private int targetActionId;
	private String targetIdPath;

	/**
	*  Standard constructor used to render a response page.
	*
	*/

	public RequestCycle(IEngine engine, RequestContext requestContext,
		IMonitor monitor)
	{
		this.engine = engine;
		this.requestContext = requestContext;
		this.monitor = monitor;
	}

	/**
	*  Called at the end of the request cycle (i.e., after all responses have been
	*  sent back to the client), to release all pages loaded during the request cycle.
	*
	*/

	public void cleanup()
	{
		if (loadedPages == null)
			return;

		IPageSource source = engine.getPageSource();
		Iterator i = loadedPages.values().iterator();

		while (i.hasNext())
		{
			IPage page = (IPage)i.next();

			source.releasePage(page);
		}

		loadedPages = null;
		loadedRecorders = null;
	}

	public String encodeURL(String URL)
	{
		if (response == null)
			response = requestContext.getResponse();

		return response.encodeURL(URL);
	}

	public IEngine getEngine()
	{
		return engine;
	}

	public Object getAttribute(String name)
	{
		if (attributes == null)
			return null;

		return attributes.get(name);
	}

	public IMonitor getMonitor()
	{
		return monitor;
	}

	public String getNextActionId()
	{
		return Integer.toHexString(++actionId);
	}

	public IPage getPage()
	{
		return page;
	}

	/**
	*  Gets the page from the engines's {@link IPageSource}.
	*
	*/

	public IPage getPage(String name)
	{
		IPage result = null;
		IPageRecorder recorder;
		IPageSource pageSource;

		if (name == null)
			throw new NullPointerException("Parameter name may not be null in RequestCycle.getPage().");

		if (monitor != null)
			monitor.pageLoadBegin(name);

		if (loadedPages != null)
			result = (IPage)loadedPages.get(name);

		if (result == null)
		{
			pageSource = engine.getPageSource();

			try
			{
				result = pageSource.getPage(engine, name, monitor);
			}
			catch (PageLoaderException e)
			{
				throw new ApplicationRuntimeException("Failed to acquire page " + 
					name + ".", e);
			}

			result.setRequestCycle(this);

			// Get the recorder that will eventually observe and record
			// changes to persistent properties of the page.  If the page
			// has never emitted any page changes, then it will
			// not have a recorder.

			recorder = getPageRecorder(name);

			if (recorder != null)
			{
				// Have it rollback the page to the prior state.  Note that
				// the page has a null observer at this time.

				recorder.rollback(result);

				// Now, have the page use the recorder for any future
				// property changes.

				result.setChangeObserver(recorder);

				// And, if this recorder observed changes in a prior request cycle
				// (and was locked after committing in that cycle), it's time
				// to unlock.

				recorder.setLocked(false);
			}
			else
			{
				// No page recorder for the page.  We'll observe its
				// changes and create the page recorder dynamically
				// if it emits any.
				
				result.setChangeObserver(this);
			}

			if (loadedPages == null)
				loadedPages = new HashMap(MAP_SIZE);

			loadedPages.put(name, result);
		}

		if (monitor != null)
			monitor.pageLoadEnd(name);

		return result;
	}

	/**
	 *  Returns the page recorder for the named page.  This may come
	 *  form the cycle's cache of page recorders or, if not yet encountered
	 *  in this request cycle, the {@link IEngine#getPageRecorder(String)} is
	 *  invoked to get the recorder, if it exists.
	 */

	protected IPageRecorder getPageRecorder(String name)
	{
		IPageRecorder result = null;

		if (loadedRecorders != null)
			result = (IPageRecorder)loadedRecorders.get(name);

		if (result != null)
			return result;

		result = engine.getPageRecorder(name);

		if (result == null)
			return null;
			
		if (loadedRecorders == null)
			loadedRecorders = new HashMap(MAP_SIZE);

		loadedRecorders.put(name, result);

		return result;
	}

	public RequestContext getRequestContext()
	{
		return requestContext;
	}

	public boolean isRewinding()
	{
		return rewinding;
	}

	public boolean isRewound(IComponent component)
	throws StaleLinkException
	{
		// If not rewinding ...

		if (!rewinding)
			return false;

		if (actionId != targetActionId)
			return false;

		// OK, we're there, is the page is good order?

		if (component.getIdPath().equals(targetIdPath))
			return true;

		// Woops.  Mismatch.

		throw new StaleLinkException(component,  
			Integer.toString(targetActionId), targetIdPath);
	}

	public void removeAttribute(String name)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Removing attribute " + name);
			
		if (attributes == null)
			return;

		attributes.remove(name);
	}

	/**
	*  Renders the page by invoking 
	* {@link IPage#renderPage(IResponseWriter, IRequestCycle)}.  
	*  This clears all attributes.
	*
	*/

	public void renderPage(IResponseWriter writer)
	throws RequestCycleException
	{
		String pageName = null;

		if (monitor != null)
		{
			pageName = page.getName();
			monitor.pageRenderBegin(pageName);
		}

		rewinding = false;
		actionId = -1;
		targetActionId = 0;

		// Forget any attributes from a previous render cycle.

		attributes = null;
	
		try
		{
			page.renderPage(writer, this);

			}
		catch (RequestCycleException e)
		{
			// RenderExceptions don't need to be wrapped.
throw e;
			}
		catch (ApplicationRuntimeException ex)
		{
		    // Nothing much to add here.

			throw ex;
			}
		catch (Throwable ex)
		{
			// But wrap other exceptions in a RequestCycleException ... this
			// will ensure that some of the context is available.

			throw new RequestCycleException(ex.getMessage(), page,  ex);
			}
		finally
		{
			actionId = 0;
			targetActionId = 0;
			}

		if (monitor != null)
		    monitor.pageRenderEnd(pageName);

	}

	/**
	*  Rewinds the page by invoking 
	*  {@link IPage#renderPage(IResponseWriter, IRequestCycle)}.
	*
	* <p>The process is expected to end with a {@link RenderRewoundException}.
	* If the entire page is renderred without this exception being thrown, it means
	* that the target action id was not valid, and a 
	* {@link RequestCycleException}
	* is thrown.
	*
	* <p>This clears all attributes.
	*
	*/

	public void rewindPage(String targetActionId, String targetIdPath)
	throws RequestCycleException
	{
		String pageName = null;

		if (monitor != null)
		{
			pageName = page.getName();
			monitor.pageRewindBegin(pageName);
		}

		rewinding = true;
		attributes = null;
		actionId = -1;

		this.targetActionId = Integer.parseInt(targetActionId);
		this.targetIdPath = targetIdPath;

		try
		{
			page.renderPage(NullResponseWriter.getSharedInstance(), this);

			// Shouldn't get this far, because the target component should
			// throw the RenderRewoundException.

			throw new StaleLinkException(page,  
				targetActionId, targetIdPath);
		}
		catch (RenderRewoundException ex)
		{
			// This is acceptible and expected.
		}
		catch (RequestCycleException ex)
		{
			// RequestCycleException don't need to be wrapped.
			throw ex;
		}
		catch (Throwable ex)
		{
			// But wrap other exceptions in a RequestCycleException ... this
			// will ensure that some of the context is available.

			throw new RequestCycleException(ex.getMessage(), page,  ex);
		}
		finally
		{
			rewinding = false;
			actionId = 0;
			this.targetActionId = 0;
			this.targetIdPath = null;
		}

		if (monitor != null)
			monitor.pageRewindEnd(pageName);

	}

	public void setAttribute(String name, Object value)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Set attribute " + name + " to " + value);
			
		if (attributes == null)
			attributes = new HashMap(MAP_SIZE);

		attributes.put(name, value);
	}

	public void setPage(IPage value)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Set page to " + value);

			page = value;
	}

	public void setPage(String name)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Set page to " + name);

			page = getPage(name);
	}

	/**
	*  Invokes {@link IPageRecorder#commit()} on each page recorder loaded
	*  during the request cycle.
	*
	*/

	public void commitPageChanges()
	throws PageRecorderCommitException
	{
		Iterator i;
		IPageRecorder recorder;

		if (CAT.isDebugEnabled())
			CAT.debug("Committing page changes");

		if (loadedRecorders == null)
			return;

		i = loadedRecorders.values().iterator();

		while (i.hasNext())
		{
			recorder = (IPageRecorder)i.next();

			recorder.commit();
		}
	}

	/**
	 *  For pages without a {@link IPageRecorder page recorder}, 
	 *  we're the {@link ChangeObserver change observer}.
	 *  If such a page actually emits a change, then
	 *  we'll obtain a new page recorder from the
	 *  {@link IEngine engine}, set the recorder
	 *  as the page's change observer, and forward the event
	 *  to the newly created recorder.  In addition, the
	 *  new page recorder is remembered so that it will
	 *  be committed by {@link #commitPageChanges()}.
	 *
	 */
	 
	public void observeChange(ObservedChangeEvent event)
	{
		IPage page = event.getComponent().getPage();
		String pageName = page.getName();
		
		if (CAT.isDebugEnabled())
			CAT.debug("Observed change in page " + pageName +
					  ", creating page recorder.");
		
		IPageRecorder recorder = engine.createPageRecorder(pageName);
		
		page.setChangeObserver(recorder);

		if (loadedRecorders == null)
			loadedRecorders = new HashMap(MAP_SIZE);
			
		loadedRecorders.put(pageName, recorder);
				
		recorder.observeChange(event);			
	}
}


