package com.primix.tapestry;

import com.primix.tapestry.event.ChangeObserver;
import com.primix.tapestry.spec.*;
import java.util.*;
import java.io.OutputStream;
import javax.servlet.http.*;
import com.primix.foundation.*;

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
 * Base implementation of the {@link IPage} interface.  Most pages
 * should be able to simply subclass this, adding new properties and
 * methods.  An unlikely exception would be a page that was not based
 * on a template.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class BasePage extends BaseComponent implements IPage
{
	/**
	*  Object to be notified when a observered property changes.  Observered
	*  properties are the ones that will be persisted between request cycles.
	*  Unobserved properties are reconstructed.
	*
	*/

	protected ChangeObserver changeObserver;

	protected IApplication application;
	
	private static final int LIFECYCLE_INIT_SIZE = 3;

	protected int lifecycleComponentCount = 0;

	protected ILifecycle[] lifecycleComponents;

	protected String name;
	
	/**
	 *  Only valid while the page is actually rendering.
	 *
	 */
	 
	private IRequestCycle requestCycle;
	
	/**
	*  The locale of the page, initially determined from the application.
	*
	*/

	protected Locale locale;

	/**
	*  Standard constructor.  Sets the locale for the page from the applications' locale.
	*
	*/

	public BasePage(IApplication application,
		ComponentSpecification componentSpecification)
	{
		// No page, no container, no name.

		super(null, null, null, componentSpecification);

		this.application = application;
		this.locale = application.getLocale();

		// This ensures that Component.readTemplate() works.

		page = this;

	}

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
	*  Clears the application and changeObserver properties, then
	*  invokes {@link ILifecycle#reset()} on all lifecycle components.
	*
	*/

	public void detachFromApplication()
	{
		application = null;
		changeObserver = null;

		for (int i = 0; i < lifecycleComponentCount; i++)
			lifecycleComponents[i].reset();

	}

	public IApplication getApplication()
	{
		return application;
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

	public String getName()
	{
		return name;
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
	*  Sets the application and trace logger for the page.  Does
	*  <em>not</em> change the locale, but since a page is selected
	*  from the {@link IPageSource} pool partially based on its
	*  locale matching the application locale, they should match
	*  anyway.
	*
	*  <p>Invokes {@link ILifecycle#reset()} on any lifecycle components.
	*
	*/

	public void joinApplication(IApplication value)
	{
		application = value;

	}

	/**
	*
	*  Invokes lifecycle methods on any components (as necessary).
	*
	*/

	public void renderPage(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		int i;

		for (i = 0; i < lifecycleComponentCount; i++)
			lifecycleComponents[i].prepareForRender(cycle);

		try
		{
			requestCycle = cycle;
			
            beginResponse(writer, cycle);
            
			render(writer, cycle);
		}
		finally
		{
			requestCycle = null;
			
			// Open question:  how to handle an exceptions thrown here.

			for (i = 0; i < lifecycleComponentCount; i++)
				lifecycleComponents[i].cleanupAfterRender(cycle);
		}		
	}

	public void setChangeObserver(ChangeObserver value)
	{
		changeObserver = value;
	}

	public void setName(String value)
	{
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
	 *  Returns an {@link HTMLResponseWriter}.
	 *
	 */
	 
	 
	public IResponseWriter getResponseWriter(OutputStream out)
	{
		return new HTMLResponseWriter(out);
	}

    /**
     *  Does nothing.  Subclasses may override.
     *
     */
     
	public void beginResponse(IResponseWriter writer, IRequestCycle cycle) 
    throws RequestCycleException
    { 
		// Does nothing.
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
		int i;
		
		for (i = 0; i < lifecycleComponentCount; i++)
			lifecycleComponents[i].cleanupComponent();	
	}
}

