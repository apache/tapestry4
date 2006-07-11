// Copyright Jul 10, 2006 The Apache Software Foundation
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
package org.apache.tapestry.portlet;

import java.util.Map;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.impl.LinkFactoryImpl;


/**
 * Creates {@link EngineServiceLink}s that will re-encode asset URL's
 * using {@link IRequestCycle#encodeUrl()}.
 * 
 * @author jkuhnert
 */
public class PortletAssetLinkFactoryImpl extends LinkFactoryImpl
{
    
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
        
        return new EngineServiceLink(_requestCycle, fullServletPath, engine.getOutputEncoding(),
                _codec, _request, parameters, stateful);
    }
}
