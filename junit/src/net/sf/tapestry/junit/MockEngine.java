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

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import net.sf.tapestry.IComponentStringsSource;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IPageSource;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.util.io.DataSqueezer;

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

    public IPageRecorder getPageRecorder(String pageName)
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

}
