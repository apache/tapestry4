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
package net.sf.tapestry.junit;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.event.ChangeObserver;
import net.sf.tapestry.event.PageCleanupListener;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  Fake implementation of {@link IPage} used during unit testing.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class MockPage implements IPage
{
	private IEngine engine;
	private Locale locale;
	private ComponentSpecification specification;

    public void detach()
    {
    }

    public IEngine getEngine()
    {
        return engine;
    }

    public ChangeObserver getChangeObserver()
    {
        return null;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public String getName()
    {
        return null;
    }

    public IComponent getNestedComponent(String path)
    {
        return null;
    }

    public void attach(IEngine value)
    {
    }

    public void renderPage(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    public void setChangeObserver(ChangeObserver value)
    {
    }

    public void setName(String value)
    {
    }

    public void validate(IRequestCycle cycle) throws RequestCycleException
    {
    }

    public IMarkupWriter getResponseWriter(OutputStream out)
    {
        return null;
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    public IRequestCycle getRequestCycle()
    {
        return null;
    }

    public void setRequestCycle(IRequestCycle cycle)
    {
    }

    public void cleanupPage()
    {
    }

    public Object getVisit()
    {
        return null;
    }

    public void addPageRenderListener(PageRenderListener listener)
    {
    }

    public void addPageDetachListener(PageDetachListener listener)
    {
    }

    public void addPageCleanupListener(PageCleanupListener listener)
    {
    }

    public void addAsset(String name, IAsset asset)
    {
    }

    public void addComponent(IComponent component)
    {
    }

    public void addWrapped(IRender element)
    {
    }

    public Map getAssets()
    {
        return null;
    }

    public IAsset getAsset(String name)
    {
        return null;
    }

    public IBinding getBinding(String name)
    {
        return null;
    }

    public Collection getBindingNames()
    {
        return null;
    }

    public Map getBindings()
    {
        return null;
    }

    public IComponent getComponent(String id)
    {
        return null;
    }

    public IComponent getContainer()
    {
        return null;
    }

    public void setContainer(IComponent value)
    {
    }

    public String getExtendedId()
    {
        return null;
    }

    public String getId()
    {
        return null;
    }

    public void setId(String value)
    {
    }

    public String getIdPath()
    {
        return null;
    }

	/**
	 *  Returns this (it is, after all, MockPage, not MockComponent).
	 * 
	 **/
	
    public IPage getPage()
    {
        return this;
    }

    public void setPage(IPage value)
    {
    }

    public ComponentSpecification getSpecification()
    {
        return specification;
    }

    public void setSpecification(ComponentSpecification value)
    {
        specification = value;
    }

    public void renderWrapped(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    public void setBinding(String name, IBinding binding)
    {
    }

    public Map getComponents()
    {
        return null;
    }

    public void finishLoad(
        IPageLoader loader,
        ComponentSpecification specification)
        throws PageLoaderException
    {
    }

	/**
	 *  Gets the string source from the engine, gets the strings
	 *  from the string source, and invokes
	 *  {@link net.sf.tapestry.IComponentStrings#getString(String)}.
	 * 
	 **/
	
    public String getString(String key)
    {
 		return engine.getComponentStringsSource().getStrings(this).getString(key);
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    public void setEngine(IEngine engine)
    {
        this.engine = engine;
    }

}
