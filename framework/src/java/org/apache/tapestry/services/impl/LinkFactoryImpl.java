// Copyright 2004 The Apache Software Foundation
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
import java.util.Map;

import org.apache.commons.codec.net.URLCodec;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Defense;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class LinkFactoryImpl implements LinkFactory
{
    private DataSqueezer _dataSqueezer;

    private final Object[] EMPTY = new Object[0];

    private URLCodec _codec = new URLCodec();

    public ILink constructLink(IRequestCycle cycle, Map parameters, boolean stateful)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(parameters, "parameters");

        squeezeServiceParameters(parameters);

        IEngine engine = cycle.getEngine();

        return new EngineServiceLink(cycle, engine.getServletPath(), engine.getOutputEncoding(),
                _codec, parameters, stateful);
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

}