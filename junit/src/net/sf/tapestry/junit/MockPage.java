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
import net.sf.tapestry.INamespace;
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
    private IEngine _engine;
    private Locale locale;
    private ComponentSpecification _specification;

    public void detach()
    {
    }

    public IEngine getEngine()
    {
        return _engine;
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

    public void renderPage(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
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

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
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
        return _specification;
    }

    public void setSpecification(ComponentSpecification value)
    {
        _specification = value;
    }


    public void setBinding(String name, IBinding binding)
    {
    }

    public Map getComponents()
    {
        return null;
    }

    public void finishLoad(IRequestCycle cycle, IPageLoader loader, ComponentSpecification specification)
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
        return _engine.getComponentStringsSource().getStrings(this).getString(key);
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

    public void setEngine(IEngine engine)
    {
        _engine = engine;
    }

    public void removePageCleanupListener(PageCleanupListener listener)
    {
    }

    public void removePageDetachListener(PageDetachListener listener)
    {
    }

    public void removePageRenderListener(PageRenderListener listener)
    {
    }

    public INamespace getNamespace()
    {
        return null;
    }

    public void setNamespace(INamespace namespace)
    {
    }

    public void beginPageRender()
    {
    }

    public void endPageRender()
    {
    }

    public void addBody(IRender element)
    {
    }

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

    public String getQualifiedName()
    {
        return null;
    }

    public String getPageName()
    {
        return null;
    }

    public void setPageName(String pageName)
    {
    }

}
