// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import java.util.List;

import org.apache.tapestry.describe.HTMLDescriber;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebActivator;
import org.apache.tapestry.web.WebContext;

/**
 * Implementation of {@link ApplicationGlobals}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ApplicationGlobalsImpl implements ApplicationGlobals
{
    private WebActivator _activator;

    private IApplicationSpecification _specification;

    private WebContext _webContext;

    private List _factoryServices;

    private ResponseRenderer _responseRenderer;

    private LinkFactory _linkFactory;

    private HTMLDescriber _htmlDescriber;

    public void storeActivator(WebActivator activator)
    {
        _activator = activator;
    }

    public void storeSpecification(IApplicationSpecification applicationSpecification)
    {
        _specification = applicationSpecification;
    }

    public WebActivator getActivator()
    {
        return _activator;
    }

    public IApplicationSpecification getSpecification()
    {
        return _specification;
    }

    public String getActivatorName()
    {
        return _activator.getActivatorName();
    }

    public WebContext getWebContext()
    {
        return _webContext;
    }

    public void storeContext(WebContext context)
    {
        _webContext = context;
    }

    public void storeFactoryServices(List factoryServices)
    {
        _factoryServices = factoryServices;
    }

    public List getFactoryServices()
    {
        return _factoryServices;
    }

    public ResponseRenderer getResponseRenderer()
    {
        return _responseRenderer;
    }

    public void storeResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    public LinkFactory getLinkFactory()
    {
        return _linkFactory;
    }

    public void storeLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    public HTMLDescriber getHTMLDescriber()
    {
        return _htmlDescriber;
    }

    public void storeHTMLDescriber(HTMLDescriber describer)
    {
        _htmlDescriber = describer;
    }
}