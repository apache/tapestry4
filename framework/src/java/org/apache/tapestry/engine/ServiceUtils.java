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

package org.apache.tapestry.engine;

import java.io.IOException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.StringSplitter;

/**
 * Utilities often needed by {@link org.apache.tapestry.engine.IEngineService}&nbsp;
 * implementations.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServiceUtils
{
    /**
     * Returns the service context as an array of Strings. Returns null if there are no service
     * context strings.
     */

    public static String[] getServiceContext(RequestContext context)
    {
        String service = context.getParameter(Tapestry.SERVICE_QUERY_PARAMETER_NAME);

        int slashx = service.indexOf('/');

        if (slashx < 0)
            return null;

        String serviceContext = service.substring(slashx + 1);

        return new StringSplitter('/').splitToArray(serviceContext);
    }

}