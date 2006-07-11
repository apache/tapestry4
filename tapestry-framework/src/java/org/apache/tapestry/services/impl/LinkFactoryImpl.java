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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.net.URLCodec;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.order.Orderer;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.engine.ServiceEncodingImpl;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class LinkFactoryImpl implements LinkFactory
{
 
    protected URLCodec _codec = new URLCodec();
    
    protected String _contextPath;
    
    protected PropertyPersistenceStrategySource _persistenceStrategySource;
    
    protected IRequestCycle _requestCycle;
    
    protected WebRequest _request;
    
    private DataSqueezer _dataSqueezer;

    private ErrorLog _errorLog;

    /**
     * List of {@link org.apache.tapestry.services.impl.ServiceEncoderContribution}.
     */

    private List _contributions;

    private ServiceEncoder[] _encoders;

    private String _servletPath;

    private final Object[] _empty = new Object[0];
    
    
    public void initializeService()
    {
        Orderer orderer = new Orderer(_errorLog, "encoder");

        Iterator i = _contributions.iterator();

        while (i.hasNext())
        {
            ServiceEncoderContribution c = (ServiceEncoderContribution) i.next();

            orderer.add(c, c.getId(), c.getAfter(), c.getBefore());
        }

        List ordered = orderer.getOrderedObjects();
        int count = ordered.size();

        _encoders = new ServiceEncoder[count];

        for (int j = 0; j < count; j++)
        {
            ServiceEncoderContribution c = (ServiceEncoderContribution) ordered.get(j);

            _encoders[j] = c.getEncoder();
        }

    }

    public ILink constructLink(IEngineService service, boolean post, Map parameters,
            boolean stateful)
    {
        finalizeParameters(service, parameters);

        IEngine engine = _requestCycle.getEngine();

        ServiceEncoding serviceEncoding = createServiceEncoding(parameters);

        // Give persistent property strategies a chance to store extra data
        // into the link.

        if (stateful)
            _persistenceStrategySource.addParametersForPersistentProperties(serviceEncoding, post);

        String fullServletPath = _contextPath + serviceEncoding.getServletPath();
        
        return new EngineServiceLink(fullServletPath, engine.getOutputEncoding(),
                _codec, _request, parameters, stateful);
    }

    protected void finalizeParameters(IEngineService service, Map parameters)
    {
        Defense.notNull(service, "service");
        Defense.notNull(parameters, "parameters");

        String serviceName = service.getName();

        if (serviceName == null)
            throw new ApplicationRuntimeException(ImplMessages.serviceNameIsNull());

        parameters.put(ServiceConstants.SERVICE, serviceName);

        squeezeServiceParameters(parameters);
    }

    public ServiceEncoder[] getServiceEncoders()
    {
        return _encoders;
    }

    /**
     * Creates a new service encoding, and allows the encoders to modify it before returning.
     */

    protected ServiceEncoding createServiceEncoding(Map parameters)
    {
        ServiceEncodingImpl result = new ServiceEncodingImpl(_servletPath, parameters);

        for (int i = 0; i < _encoders.length; i++)
        {
            _encoders[i].encode(result);

            if (result.isModified())
                break;
        }

        return result;
    }

    protected void squeezeServiceParameters(Map parameters)
    {
        Object[] serviceParameters = (Object[]) parameters.get(ServiceConstants.PARAMETER);

        if (serviceParameters == null)
            return;

        String[] squeezed = squeeze(serviceParameters);

        parameters.put(ServiceConstants.PARAMETER, squeezed);
    }

    public Object[] extractListenerParameters(IRequestCycle cycle)
    {
        String[] squeezed = cycle.getParameters(ServiceConstants.PARAMETER);

        if (Tapestry.size(squeezed) == 0)
            return _empty;

        try
        {
            return _dataSqueezer.unsqueeze(squeezed);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private String[] squeeze(Object[] input)
    {
        try
        {
            return _dataSqueezer.squeeze(input);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    public void setDataSqueezer(DataSqueezer dataSqueezer)
    {
        _dataSqueezer = dataSqueezer;
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public void setServletPath(String servletPath)
    {
        _servletPath = servletPath;
    }

    public void setContextPath(String contextPath)
    {
        _contextPath = contextPath;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    /**
     * This is kind of limiting; it's possible that other things beyond persistence strategies will
     * want to have a hand at encoding data into URLs. If that comes to pass, we'll need to
     * implement an event coordinator/listener combo to let implementations know about links being
     * generated.
     */

    public void setPersistenceStrategySource(
            PropertyPersistenceStrategySource persistenceStrategySource)
    {
        _persistenceStrategySource = persistenceStrategySource;
    }

    public void setRequestCycle(IRequestCycle requestCycle)
    {
        _requestCycle = requestCycle;
    }
}
