//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.junit;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.asset.ResourceChecksumSource;
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
import org.apache.tapestry.util.DefaultResourceResolver;
import org.apache.tapestry.util.io.DataSqueezer;
import org.apache.tapestry.util.pool.Pool;

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
    private IResourceResolver _resolver;
    private IComponentMessagesSource componentStringsSource;

    private Pool _pool = new Pool();
    private String _servletPath;
    private IApplicationSpecification applicationSpecification;

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
        return _servletPath;
    }

    public String getContextPath()
    {
        return null;
    }

    public IApplicationSpecification getSpecification()
    {
        return applicationSpecification;
    }

    public void setSpecification(IApplicationSpecification appSpec)
    {
        this.applicationSpecification = appSpec;
    }

    public ISpecificationSource getSpecificationSource()
    {
        return null;
    }

    public ITemplateSource getTemplateSource()
    {
        return null;
    }

    public boolean service(RequestContext context) throws ServletException, IOException
    {
        return false;
    }

    public IResourceResolver getResourceResolver()
    {
        if (_resolver == null)
            _resolver = new DefaultResourceResolver();

        return _resolver;
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

    public IComponentMessagesSource getComponentMessagesSource()
    {
        return componentStringsSource;
    }

    public void setComponentStringsSource(IComponentMessagesSource componentStringsSource)
    {
        this.componentStringsSource = componentStringsSource;
    }

    public DataSqueezer getDataSqueezer()
    {
        return null;
    }

    public IPropertySource getPropertySource()
    {
        return null;
    }

    public Pool getPool()
    {
        return _pool;
    }

    public Object getGlobal()
    {
        return null;
    }

    public IComponentClassEnhancer getComponentClassEnhancer()
    {
        return null;
    }

    public void setServletPath(String servletPath)
    {
        _servletPath = servletPath;
    }

    public String getOutputEncoding()
    {
        return "UTF-8";
    }

    public ResourceChecksumSource getResourceChecksumSource()
    {
        return null;
    }
}
