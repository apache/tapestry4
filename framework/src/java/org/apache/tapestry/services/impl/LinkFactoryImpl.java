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

import java.io.IOException;
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
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.engine.ServiceEncodingImpl;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class LinkFactoryImpl implements LinkFactory
{
    private DataSqueezer _dataSqueezer;

    private ErrorLog _errorLog;

    /**
     * List of {@link org.apache.tapestry.services.impl.ServiceEncoderContribution}.
     */

    private List _contributions;

    private ServiceEncoder[] _encoders;

    private String _contextPath;

    private String _servletPath;

    private final Object[] EMPTY = new Object[0];

    private URLCodec _codec = new URLCodec();
    
    private WebRequest _request;

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

    public ILink constructLink(IRequestCycle cycle, Map parameters, boolean stateful)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(parameters, "parameters");

        squeezeServiceParameters(parameters);

        IEngine engine = cycle.getEngine();

        ServiceEncoding serviceEncoding = createServiceEncoding(parameters);

        String fullServletPath = _contextPath + serviceEncoding.getServletPath();

        return new EngineServiceLink(cycle, fullServletPath, engine.getOutputEncoding(), _codec,
                _request, parameters, stateful);
    }

    public ServiceEncoder[] getServiceEncoders()
    {
        return _encoders;
    }

    /**
     * Creates a new service encoding, and allows the encoders to modify it before returning.
     */

    private ServiceEncoding createServiceEncoding(Map parameters)
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

    private void squeezeServiceParameters(Map parameters)
    {
        Object[] serviceParameters = (Object[]) parameters.get(ServiceConstants.PARAMETER);

        if (serviceParameters == null)
            return;

        String[] squeezed = squeeze(serviceParameters);

        parameters.put(ServiceConstants.PARAMETER, squeezed);
    }

    public Object[] extractServiceParameters(IRequestCycle cycle)
    {
        String[] squeezed = cycle.getParameters(ServiceConstants.PARAMETER);

        if (Tapestry.size(squeezed) == 0)
            return EMPTY;

        try
        {
            return _dataSqueezer.unsqueeze(squeezed);
        }
        catch (IOException ex)
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
        catch (IOException ex)
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
}