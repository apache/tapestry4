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
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.DirectServiceEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestDirectServiceEncoder extends BaseComponentTestCase
{
    private ServiceEncoding newEncoding(String servletPath)
    {
        ServiceEncoding encoding = newMock(ServiceEncoding.class);

        expect(encoding.getServletPath()).andReturn(servletPath);

        return encoding;
    }

    private void train(ServiceEncoding encoding, String parameterName,
            String parameterValue)
    {
        expect(encoding.getParameterValue(parameterName)).andReturn(parameterValue);
    }

    public void testEncodeWrongService()
    {
        ServiceEncoding encoding = newMock(ServiceEncoding.class);

        train(encoding, ServiceConstants.SERVICE, "foo");

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();

        encoder.encode(encoding);

        verify();
    }

    public void testEncodePageInNamespace()
    {
        ServiceEncoding encoding = newMock(ServiceEncoding.class);

        train(encoding, ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        train(encoding, ServiceConstants.PAGE, "foo:Bar");

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();

        encoder.encode(encoding);

        verify();
    }

    public void testEncodeStateless()
    {
        ServiceEncoding encoding = newMock(ServiceEncoding.class);

        train(encoding, ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        train(encoding, ServiceConstants.PAGE, "admin/Menu");
        train(encoding, ServiceConstants.SESSION, null);
        train(encoding, ServiceConstants.COMPONENT, "border.link");

        encoding.setServletPath("/admin/Menu,border.link.direct");
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, null);

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");

        encoder.encode(encoding);

        verify();
    }

    public void testEncodeStateful()
    {
        ServiceEncoding encoding = newMock(ServiceEncoding.class);

        train(encoding, ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        train(encoding, ServiceConstants.PAGE, "admin/Menu");
        train(encoding, ServiceConstants.SESSION, "T");
        train(encoding, ServiceConstants.COMPONENT, "border.link");

        encoding.setServletPath("/admin/Menu,border.link.sdirect");
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, null);

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatefulExtension("sdirect");

        encoder.encode(encoding);

        verify();
    }

    public void testDecodeWrongExtension()
    {
        ServiceEncoding encoding = newEncoding("/foo.svc");

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");
        encoder.setStatefulExtension("sdirect");

        encoder.decode(encoding);

        verify();
    }

    public void testDecodeStateless()
    {
        ServiceEncoding encoding = newEncoding("/admin/Menu,border.link.direct");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, "admin/Menu");
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, "border.link");

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");
        encoder.setStatefulExtension("sdirect");

        encoder.decode(encoding);

        verify();
    }
    
    public void testDecodeStateful()
    {
        ServiceEncoding encoding = newEncoding("/admin/Menu,border.link.sdirect");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, "admin/Menu");
        encoding.setParameterValue(ServiceConstants.SESSION, "T");
        encoding.setParameterValue(ServiceConstants.COMPONENT, "border.link");

        replay();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");
        encoder.setStatefulExtension("sdirect");

        encoder.decode(encoding);

        verify();
    }    
}