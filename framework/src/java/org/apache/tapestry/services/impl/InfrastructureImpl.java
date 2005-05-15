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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Resource;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.describe.HTMLDescriber;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.error.ExceptionPresenter;
import org.apache.tapestry.error.RequestExceptionReporter;
import org.apache.tapestry.error.StaleLinkExceptionPresenter;
import org.apache.tapestry.error.StaleSessionExceptionPresenter;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.listener.ListenerMapSource;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.ComponentMessagesSource;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ObjectPool;
import org.apache.tapestry.services.RequestCycleFactory;
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
 * @since 4.0
 */
public class InfrastructureImpl implements Infrastructure
{
    /**
     * List of {@link org.apache.tapestry.services.impl.InfrastructureContribution}.
     */
    private List _normalContributions;

    /**
     * List of {@link org.apache.tapestry.services.impl.InfrastructureContribution}.
     */
    private List _overrideContributions;

    private Map _properties = new HashMap();

    private boolean _initialized;

    private String _mode;

    private ErrorLog _errorLog;

    private ClassResolver _classResolver;

    private ThreadLocale _threadLocale;

    public void setLocale(Locale locale)
    {
        _threadLocale.setLocale(locale);
    }

    public String getApplicationId()
    {
        return (String) getProperty("applicationId");
    }

    public IPropertySource getApplicationPropertySource()
    {
        return (IPropertySource) getProperty("applicationPropertySource");
    }

    public IApplicationSpecification getApplicationSpecification()
    {
        return (IApplicationSpecification) getProperty("applicationSpecification");
    }

    public ApplicationStateManager getApplicationStateManager()
    {
        return (ApplicationStateManager) getProperty("applicationStateManager");
    }

    public ClassResolver getClassResolver()
    {
        return _classResolver;
    }

    public ComponentMessagesSource getComponentMessagesSource()
    {
        return (ComponentMessagesSource) getProperty("componentMessagesSource");
    }

    public ComponentPropertySource getComponentPropertySource()
    {
        return (ComponentPropertySource) getProperty("componentPropertySource");
    }

    public String getContextPath()
    {
        return getRequest().getContextPath();
    }

    public Resource getContextRoot()
    {
        WebContext context = (WebContext) getProperty("context");

        return new WebContextResource(context, "/");
    }

    public DataSqueezer getDataSqueezer()
    {
        return (DataSqueezer) getProperty("dataSqueezer");
    }

    public IPropertySource getGlobalPropertySource()
    {
        return (IPropertySource) getProperty("globalPropertySource");
    }

    public LinkFactory getLinkFactory()
    {
        return (LinkFactory) getProperty("linkFactory");
    }

    public ObjectPool getObjectPool()
    {
        return (ObjectPool) getProperty("objectPool");
    }

    public IPageSource getPageSource()
    {
        return (IPageSource) getProperty("pageSource");
    }

    public WebRequest getRequest()
    {
        return (WebRequest) getProperty("request");
    }

    public RequestCycleFactory getRequestCycleFactory()
    {
        return (RequestCycleFactory) getProperty("requestCycleFactory");
    }

    public RequestExceptionReporter getRequestExceptionReporter()
    {
        return (RequestExceptionReporter) getProperty("requestExceptionReporter");
    }

    public ResetEventCoordinator getResetEventCoordinator()
    {
        return (ResetEventCoordinator) getProperty("resetEventCoordinator");
    }

    public WebResponse getResponse()
    {
        return (WebResponse) getProperty("response");
    }

    public ResponseRenderer getResponseRenderer()
    {
        return (ResponseRenderer) getProperty("responseRenderer");
    }

    public IScriptSource getScriptSource()
    {
        return (IScriptSource) getProperty("scriptSource");
    }

    public ServiceMap getServiceMap()
    {
        return (ServiceMap) getProperty("serviceMap");
    }

    public ISpecificationSource getSpecificationSource()
    {
        return (ISpecificationSource) getProperty("specificationSource");
    }

    public TemplateSource getTemplateSource()
    {
        return (TemplateSource) getProperty("templateSource");
    }

    public String getOutputEncoding()
    {
        return getApplicationPropertySource().getPropertyValue(
                "org.apache.tapestry.output-encoding");
    }

    public MarkupWriterSource getMarkupWriterSource()
    {
        return (MarkupWriterSource) getProperty("markupWriterSource");
    }

    public HTMLDescriber getHTMLDescriber()
    {
        return (HTMLDescriber) getProperty("HTMLDescriber");
    }

    public ExceptionPresenter getExceptionPresenter()
    {
        return (ExceptionPresenter) getProperty("exceptionPresenter");
    }

    public ListenerMapSource getListenerMapSource()
    {
        return (ListenerMapSource) getProperty("listenerMapSource");
    }

    public StaleSessionExceptionPresenter getStaleSessionExceptionPresenter()
    {
        return (StaleSessionExceptionPresenter) getProperty("staleSessionExceptionPresenter");
    }

    public StaleLinkExceptionPresenter getStaleLinkExceptionPresenter()
    {
        return (StaleLinkExceptionPresenter) getProperty("staleLinkExceptionPresenter");
    }

    public ValueConverter getValueConverter()
    {
        return (ValueConverter) getProperty("valueConverter");
    }

    public ListenerInvoker getListenerInvoker()
    {
        return (ListenerInvoker) getProperty("listenerInvoker");
    }

    public Object getProperty(String propertyName)
    {
        Defense.notNull(propertyName, "propertyName");

        if (!_initialized)
            throw new IllegalStateException(ImplMessages.infrastructureNotInitialized());

        Object result = _properties.get(propertyName);

        if (result == null)
            throw new ApplicationRuntimeException(ImplMessages
                    .missingInfrastructureProperty(propertyName));

        return result;
    }

    public synchronized void initialize(String mode)
    {
        Defense.notNull(mode, "mode");

        if (_initialized)
            throw new IllegalStateException(ImplMessages.infrastructureAlreadyInitialized(
                    mode,
                    _mode));

        Map normalByMode = buildMapFromContributions(_normalContributions, mode);
        Map normal = buildMapFromContributions(_normalContributions, null);
        Map overrideByMode = buildMapFromContributions(_overrideContributions, mode);
        Map override = buildMapFromContributions(_overrideContributions, null);

        addToProperties(overrideByMode);
        addToProperties(override);
        addToProperties(normalByMode);
        addToProperties(normal);

        _mode = mode;
        _initialized = true;
    }

    private Map buildMapFromContributions(List contributions, String mode)
    {
        Map result = new HashMap();

        Iterator i = contributions.iterator();
        while (i.hasNext())
        {
            InfrastructureContribution ic = (InfrastructureContribution) i.next();

            if (!ic.matchesMode(mode))
                continue;

            String propertyName = ic.getProperty();

            InfrastructureContribution existing = (InfrastructureContribution) result
                    .get(propertyName);

            if (existing != null)
            {
                _errorLog.error(ImplMessages.duplicateInfrastructureContribution(ic, existing
                        .getLocation()), ic.getLocation(), null);
                continue;
            }

            result.put(propertyName, ic);
        }

        return result;
    }

    /**
     * Adds to the master set of properties contributed objects that don't match an already existing
     * key.
     * 
     * @param map
     *            map of {@link org.apache.tapestry.services.impl.InfrastructureContribution}keyed
     *            on property name (String).
     */

    private void addToProperties(Map map)
    {
        Iterator i = map.values().iterator();
        while (i.hasNext())
        {
            InfrastructureContribution ic = (InfrastructureContribution) i.next();
            String propertyName = ic.getProperty();

            if (_properties.containsKey(propertyName))
                continue;

            _properties.put(propertyName, ic.getObject());
        }
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }

    public void setNormalContributions(List normalContributions)
    {
        _normalContributions = normalContributions;
    }

    public void setOverrideContributions(List overrideContributions)
    {
        _overrideContributions = overrideContributions;
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}