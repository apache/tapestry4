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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

/**
 * Tests {@link org.apache.tapestry.engine.encoders.ServiceExtensionEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestServiceExtensionEncoder extends BaseComponentTestCase
{
    public void testEncode()
    {
        ServiceEncoding sec = newMock(ServiceEncoding.class);

        expect(sec.getParameterValue(ServiceConstants.SERVICE)).andReturn("heavy");

        sec.setServletPath("/heavy.svc");
        sec.setParameterValue(ServiceConstants.SERVICE, null);

        replay();

        ServiceExtensionEncoder e = new ServiceExtensionEncoder();
        e.setExtension("svc");

        e.encode(sec);

        verify();
    }

    public void testDecodeWrongExtension()
    {
        ServiceEncoding sec = newMock(ServiceEncoding.class);

        expect(sec.getServletPath()).andReturn("/foo/bar/baz.direct");

        replay();

        ServiceExtensionEncoder e = new ServiceExtensionEncoder();
        e.setExtension("svc");

        e.decode(sec);

        verify();
    }
    
    public void testDecode()
    {
        ServiceEncoding sec = newMock(ServiceEncoding.class);

        expect(sec.getServletPath()).andReturn("/hitter.svc");

        sec.setParameterValue(ServiceConstants.SERVICE, "hitter");
        
        replay();

        ServiceExtensionEncoder e = new ServiceExtensionEncoder();
        e.setExtension("svc");

        e.decode(sec);

        verify();     
    }
}