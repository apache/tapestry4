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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class LinkFactoryImpl implements LinkFactory
{
    private DataSqueezer _dataSqueezer;

    private final Object[] EMPTY = new Object[0];

    public ILink constructLink(IRequestCycle cycle, String serviceName, String[] context,
            Object[] serviceParameters, boolean stateful)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(serviceName, "serviceName");

        String[] squeezed = squeeze(serviceParameters);

        return new EngineServiceLink(cycle, serviceName, context, squeezed, stateful);
    }

    public Object[] extractServiceParameters(IRequestCycle cycle)
    {
        RequestContext context = cycle.getRequestContext();

        String[] squeezed = context.getParameters(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);

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