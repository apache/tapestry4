package com.primix.tapestry;

import com.primix.tapestry.event.ChangeObserver;
import com.primix.tapestry.spec.*;
import java.util.*;
import java.io.OutputStream;
import javax.servlet.http.*;
import com.primix.foundation.*;

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
 * Abstract base class implementing the {@link IPage} interface.
 *
 * @version $Id$
 * @author Howard Ship, David Solis
 * @since 0.2.9
 */


public abstract class AbstractPage
extends BaseComponent 
implements IPage
{
	/**
	*  Object to be notified when a observered property changes.  Observered
	*  properties are the ones that will be persisted between request cycles.
	*  Unobserved properties are reconstructed.
	*
	*/

	private ChangeObserver changeObserver;

	/**
	*  The {@link IEngine} the page is currently attached to.  This may
	*  be read, but not changed, but subclasses.
	*
	*/

	protected IEngine engine;

	/**
	*  The visit object, if any, for the application.  Set inside
	*  {@link #attach(IEngine)} and cleared
	*  by {@link #detach()}.
	*
	*/

	private Object visit;

	private static final int LIFECYCLE_INIT_SIZE = 3;

	private int lifecycleComponentCount = 0;

	private ILifecycle[] lifecycleComponents;

	/**
	*  The name of this page.  This may be read, but not changed, by
	*  subclasses.
	*
	*/

	protected String name;

	/**
	*  Only valid while the page is actually rendering.
	*
	*/

	private IRequestCycle requestCycle;

	/**
	*  The locale of the page, initially determined from the {@link IEngine engine}.
	*
	*/

	private Locale locale;

	/**
	 *  Implemented in subclasses to provide a particular kind of
	 *  response writer (and therefore, a particular kind of
	 *  content).
	 *
	 */

	abstract public IResponseWriter getResponseWriter(OutputStream out);

	public void addLifecycleComponent(ILifecycle component)
	{
		if (lifecycleComponents == null)
		{
			lifecycleComponents = new ILifecycle[LIFECYCLE_INIT_SIZE];
			lifecycleComponents[0] = component;

			lifecycleComponentCount = 1;
			return;
		}

		if (lifecycleComponentCount == lifecycleComponents.length)
		{
			ILifecycle[] newList;

			newList = new ILifecycle[lifecycleComponentCount * 2];

			System.arraycopy(lifecycleComponents, 0, newList, 0, lifecycleComponentCount);

			lifecycleComponents = newList;
		}

		lifecycleComponents[lifecycleComponentCount++] = component;
	}

	/**
	*  Clears the engine, visit and changeObserver properties, then
	*  invokes {@link ILifecycle#reset()} on all lifecycle components.
	*
	*/

	public void detach()
	{
		engine = null;
		visit = null;
		changeObserver = null;

		for (int i = 0; i < lifecycleComponentCount; i++)
			lifecycleComponents[i].reset();

	}

	public IEngine getEngine()
	{
		return engine;
	}

	public ChangeObserver getChangeObserver()
	{
		return changeObserver;
	}

	/**
	*  Returns the name of the page.
	*
	*/

	public String getExtendedId()
	{
		return name;
	}

	/**
	*  Pages always return null for idPath.
	*
	*/

	public String getIdPath()
	{
		return null;
	}

	/**
	*  Returns the locale for the page, which may be null if the
	*  locale is not known (null corresponds to the "default locale").
	*
	*/

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale value)
	{
		if (locale != null)
			throw new ApplicationRuntimeException("Attempt to change existing locale for a page.");

		locale = value;
	}

	public String getName()
	{
		return name;
	}

	public IPage getPage()
	{
		return this;
	}

	public IComponent getNestedComponent(String path)
	{
		StringSplitter splitter;
		IComponent current;
		String[] elements;
		int i;

		if (path == null)
			return this;

		splitter = new StringSplitter('.');
		current = this;

		elements = splitter.splitToArray(path);
		for (i = 0; i < elements.length; i++)
		{
			current = current.getComponent(elements[i]);
		}

		return current;

	}

	/**
	*  Called by the {@link IEngine engine} to attach the page
	*  to itself.  Does
	*  <em>not</em> change the locale, but since a page is selected
	*  from the {@link IPageSource} pool based on its
	*  locale matching the engine's locale, they should match
	*  anyway.
	*
	*/

	public void attach(IEngine value)
	{
		engine = value;
	}

	/**
	*
	*  Invokes lifecycle methods on any components (as necessary).
	*
	*/

	public void renderPage(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		requestCycle = cycle;

		try
		{			
            for (int i = 0; i < lifecycleComponentCount; i++)
				lifecycleComponents[i].prepareForRender(cycle);

			beginResponse(writer, cycle);

			if (!cycle.isRewinding())
				cycle.commitPageChanges();

			render(writer, cycle);
			}
		catch (PageRecorderCommitException ex)
		{
			throw new RequestCycleException(ex.getMessage(), null, cycle, ex);
			}
		finally
		{
			requestCycle = null;

			// Open question:  how to handle any exceptions thrown here.

			for (int i = 0; i < lifecycleComponentCount; i++)
				lifecycleComponents[i].cleanupAfterRender(cycle);
			}		
	}

	public void setChangeObserver(ChangeObserver value)
	{
		changeObserver = value;
	}

	public void setName(String value)
	{
		if (name != null)
			throw new ApplicationRuntimeException("Attempt to change existing name for a page.");

		name = value;
	}

	/**
	*  By default, pages are not protected and this method does nothing.
	*
	*/

	public void validate(IRequestCycle cycle)
	throws RequestCycleException
	{
		// Does nothing.
	}

	/**
	*  Returns a new {@link HTMLResponseWriter}.
	*
	*/

	/**
	*  Does nothing, subclasses may override as needed.
	*
	*
	*/

	public void beginResponse(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
	}

	public IRequestCycle getRequestCycle()
	{
		return requestCycle;
	}

	/**
	*  Invokes {@link ILifecycle#cleanupComponent()} on any lifecycle components.
	*
	*  <p>Subclasses may override, but should invoke this implementation.
	*
	*/

	public void cleanupPage()
	{
		for (int i = 0; i < lifecycleComponentCount; i++)
			lifecycleComponents[i].cleanupComponent();	
	}

	/**
	*  Returns the visit object obtained from the 
	*  {@link IEngine#getVisit() engine}.
	*
	*/

	public Object getVisit()
	{
		if (visit == null)
			visit = engine.getVisit();

		return visit;
	}
}

