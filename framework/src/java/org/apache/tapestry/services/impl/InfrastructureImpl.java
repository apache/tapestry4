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

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.services.ComponentMessagesSource;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ObjectPool;
import org.apache.tapestry.services.RequestCycleFactory;
import org.apache.tapestry.services.RequestExceptionReporter;
import org.apache.tapestry.services.ResetEventCoordinator;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.services.TemplateSource;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Allows access to selected HiveMind services.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class InfrastructureImpl implements Infrastructure
{

    private String _applicationId;

    private IPropertySource _applicationPropertySource;

    private IApplicationSpecification _applicationSpecification;

    private ApplicationStateManager _applicationStateManager;

    private ClassResolver _classResolver;

    private ComponentMessagesSource _componentMessagesSource;

    private ComponentPropertySource _componentPropertySource;

    private WebContext _context;

    private DataSqueezer _dataSqueezer;

    private IPropertySource _globalPropertySource;

    private LinkFactory _linkFactory;

    private ObjectPool _objectPool;

    private IPageSource _pageSource;

    private WebRequest _request;

    private RequestCycleFactory _requestCycleFactory;

    private RequestExceptionReporter _requestExceptionReporter;

    private ResetEventCoordinator _resetEventCoordinator;

    private WebResponse _response;

    private ResponseRenderer _responseRenderer;

    private IScriptSource _scriptSource;

    private ServiceMap _serviceMap;

    private ISpecificationSource _specificationSource;

    private TemplateSource _templateSource;

    private ThreadLocale _threadLocale;

    public String getApplicationId()
    {
        return _applicationId;
    }

    public IPropertySource getApplicationPropertySource()
    {
        return _applicationPropertySource;
    }

    public IApplicationSpecification getApplicationSpecification()
    {
        return _applicationSpecification;
    }

    public ApplicationStateManager getApplicationStateManager()
    {
        return _applicationStateManager;
    }

    public ClassResolver getClassResolver()
    {
        return _classResolver;
    }

    public ComponentMessagesSource getComponentMessagesSource()
    {
        return _componentMessagesSource;
    }

    public ComponentPropertySource getComponentPropertySource()
    {
        return _componentPropertySource;
    }

    public String getContextPath()
    {
        return _request.getContextPath();
    }

    public Resource getContextRoot()
    {
        return new WebContextResource(_context, "/");
    }

    public DataSqueezer getDataSqueezer()
    {
        return _dataSqueezer;

    }

    public IPropertySource getGlobalPropertySource()
    {
        return _globalPropertySource;
    }

    public LinkFactory getLinkFactory()
    {
        return _linkFactory;
    }

    public ObjectPool getObjectPool()
    {
        return _objectPool;
    }

    public IPageSource getPageSource()
    {
        return _pageSource;
    }

    public WebRequest getRequest()
    {
        return _request;
    }

    public RequestCycleFactory getRequestCycleFactory()
    {
        return _requestCycleFactory;
    }

    public RequestExceptionReporter getRequestExceptionReporter()
    {
        return _requestExceptionReporter;
    }

    public ResetEventCoordinator getResetEventCoordinator()
    {
        return _resetEventCoordinator;
    }

    public WebResponse getResponse()
    {
        return _response;
    }

    public ResponseRenderer getResponseRenderer()
    {
        return _responseRenderer;
    }

    public IScriptSource getScriptSource()
    {
        return _scriptSource;
    }

    public ServiceMap getServiceMap()
    {
        return _serviceMap;
    }

    public ISpecificationSource getSpecificationSource()
    {
        return _specificationSource;
    }

    public TemplateSource getTemplateSource()
    {
        return _templateSource;
    }

    public void setApplicationId(String applicationId)
    {
        _applicationId = applicationId;
    }

    public void setApplicationPropertySource(IPropertySource source)
    {
        _applicationPropertySource = source;
    }

    public void setApplicationSpecification(IApplicationSpecification specification)
    {
        _applicationSpecification = specification;
    }

    public void setApplicationStateManager(ApplicationStateManager applicationStateManager)
    {
        _applicationStateManager = applicationStateManager;
    }

    public void setClassResolver(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

    public void setComponentMessagesSource(ComponentMessagesSource source)
    {
        _componentMessagesSource = source;
    }

    public void setComponentPropertySource(ComponentPropertySource componentPropertySource)
    {
        _componentPropertySource = componentPropertySource;
    }

    public void setContext(WebContext context)
    {
        _context = context;
    }

    public void setDataSqueezer(DataSqueezer dataSqueezer)
    {
        _dataSqueezer = dataSqueezer;
    }

    public void setGlobalPropertySource(IPropertySource globalPropertySource)
    {
        _globalPropertySource = globalPropertySource;
    }

    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    public void setLocale(Locale value)
    {
        _threadLocale.setLocale(value);
    }

    public void setObjectPool(ObjectPool pool)
    {
        _objectPool = pool;
    }

    public void setPageSource(IPageSource source)
    {
        _pageSource = source;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    public void setRequestCycleFactory(RequestCycleFactory requestCycleFactory)
    {
        _requestCycleFactory = requestCycleFactory;
    }

    public void setRequestExceptionReporter(RequestExceptionReporter requestExceptionReporter)
    {
        _requestExceptionReporter = requestExceptionReporter;
    }

    public void setResetEventCoordinator(ResetEventCoordinator coordinator)
    {
        _resetEventCoordinator = coordinator;
    }

    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    public void setScriptSource(IScriptSource scriptSource)
    {
        _scriptSource = scriptSource;
    }

    public void setServiceMap(ServiceMap serviceMap)
    {
        _serviceMap = serviceMap;
    }

    public void setSpecificationSource(ISpecificationSource source)
    {
        _specificationSource = source;
    }

    public void setTemplateSource(TemplateSource source)
    {
        _templateSource = source;
    }

    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }
}