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

package org.apache.tapestry.engine.encoders;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests {@link org.apache.tapestry.engine.encoders.ServiceExtensionEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestServiceExtensionEncoder extends HiveMindTestCase
{
    public void testEncode()
    {
        MockControl c = newControl(ServiceEncoding.class);
        ServiceEncoding sec = (ServiceEncoding) c.getMock();

        sec.getParameterValue(ServiceConstants.SERVICE);
        c.setReturnValue("heavy");

        sec.setServletPath("/heavy.svc");
        sec.setParameterValue(ServiceConstants.SERVICE, null);

        replayControls();

        ServiceExtensionEncoder e = new ServiceExtensionEncoder();
        e.setExtension("svc");

        e.encode(sec);

        verifyControls();
    }

    public void testDecodeWrongExtension()
    {
        MockControl c = newControl(ServiceEncoding.class);
        ServiceEncoding sec = (ServiceEncoding) c.getMock();

        sec.getServletPath();
        c.setReturnValue("/foo/bar/baz.direct");

        replayControls();

        ServiceExtensionEncoder e = new ServiceExtensionEncoder();
        e.setExtension("svc");

        e.decode(sec);

        verifyControls();
    }
    
    public void testDecode()
    {
        MockControl c = newControl(ServiceEncoding.class);
        ServiceEncoding sec = (ServiceEncoding) c.getMock();

        sec.getServletPath();
        c.setReturnValue("/hitter.svc");

        sec.setParameterValue(ServiceConstants.SERVICE, "hitter");
        
        replayControls();

        ServiceExtensionEncoder e = new ServiceExtensionEncoder();
        e.setExtension("svc");

        e.decode(sec);

        verifyControls();     
    }
}