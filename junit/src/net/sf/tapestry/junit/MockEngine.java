package net.sf.tapestry.junit;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import net.sf.tapestry.IComponentStringsSource;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IPageSource;
import net.sf.tapestry.IPropertySource;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.util.io.DataSqueezer;
import net.sf.tapestry.util.pool.Pool;

/**
 *  An implementation of {@link IEngine} used for unit testing.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class MockEngine implements IEngine
{
	private IComponentStringsSource componentStringsSource;

    private boolean _refreshing;
    private Pool _pool = new Pool();

    public void forgetPage(String name)
    {
    }

    public Locale getLocale()
    {
        return null;
    }

    public void setLocale(Locale value)
    {
    }

    public IPageRecorder getPageRecorder(String pageName, IRequestCycle cycle)
    {
        return null;
    }

    public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle)
    {
        return null;
    }

    public IPageSource getPageSource()
    {
        return null;
    }

    public IEngineService getService(String name)
    {
        return null;
    }

    public String getServletPath()
    {
        return null;
    }

    public String getContextPath()
    {
        return null;
    }

    public IApplicationSpecification getSpecification() 
    {
        return null;
    }

    public ISpecificationSource getSpecificationSource()
    {
        return null;
    }

    public ITemplateSource getTemplateSource()
    {
        return null;
    }

    public boolean service(RequestContext context)
        throws ServletException, IOException
    {
        return false;
    }

    public IResourceResolver getResourceResolver()
    {
        return null;
    }

    public Object getVisit()
    {
        return null;
    }

    public Object getVisit(IRequestCycle cycle)
    {
        return null;
    }

    public void setVisit(Object value)
    {
    }

    public boolean isResetServiceEnabled()
    {
        return false;
    }

    public IScriptSource getScriptSource()
    {
        return null;
    }

    public boolean isStateful()
    {
        return false;
    }

    public IComponentStringsSource getComponentStringsSource()
    {
        return componentStringsSource;
    }

    public void setComponentStringsSource(IComponentStringsSource componentStringsSource)
    {
        this.componentStringsSource = componentStringsSource;
    }

    public DataSqueezer getDataSqueezer()
    {
        return null;
    }

    public boolean isRefreshing()
    {
        return _refreshing;
    }

    public void setRefreshing(boolean refreshing)
    {
        _refreshing = refreshing;
    }

    public IPropertySource getPropertySource()
    {
        return null;
    }

    public Pool getPool()
    {
        return _pool;
    }

}
