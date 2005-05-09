// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.resolver;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Base class for testing specification resolvers.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class AbstractSpecificationResolverTestCase extends HiveMindTestCase
{

    protected IComponentSpecification newSpecification()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected URL newURL(String file)
    {
        return getClass().getResource(file);
    }

    protected Resource newResource(URL url)
    {
        MockControl control = newControl(Resource.class);
        Resource resource = (Resource) control.getMock();

        resource.getResourceURL();
        control.setReturnValue(url);

        return resource;
    }

    protected Resource newResource(String path)
    {
        return new URLResource(newURL(path));
    }

    protected void train(Log log, MockControl control, String message)
    {
        log.isDebugEnabled();
        control.setReturnValue(true);

        log.debug(message);
    }

}