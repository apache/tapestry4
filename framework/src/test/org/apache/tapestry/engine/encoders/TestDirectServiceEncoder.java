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
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.DirectServiceEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDirectServiceEncoder extends HiveMindTestCase
{
    private ServiceEncoding newEncoding(String servletPath)
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        encoding.getServletPath();
        control.setReturnValue(servletPath);

        return encoding;
    }

    private void train(MockControl control, ServiceEncoding encoding, String parameterName,
            String parameterValue)
    {
        encoding.getParameterValue(parameterName);
        control.setReturnValue(parameterValue);
    }

    public void testEncodeWrongService()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        train(control, encoding, ServiceConstants.SERVICE, "foo");

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();

        encoder.encode(encoding);

        verifyControls();
    }

    public void testEncodePageInNamespace()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        train(control, encoding, ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        train(control, encoding, ServiceConstants.PAGE, "foo:Bar");

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();

        encoder.encode(encoding);

        verifyControls();
    }

    public void testEncodeStateless()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        train(control, encoding, ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        train(control, encoding, ServiceConstants.PAGE, "admin/Menu");
        train(control, encoding, ServiceConstants.SESSION, null);
        train(control, encoding, ServiceConstants.COMPONENT, "border.link");

        encoding.setServletPath("/admin/Menu,border.link.direct");
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, null);

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");

        encoder.encode(encoding);

        verifyControls();
    }

    public void testEncodeStateful()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        train(control, encoding, ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        train(control, encoding, ServiceConstants.PAGE, "admin/Menu");
        train(control, encoding, ServiceConstants.SESSION, "T");
        train(control, encoding, ServiceConstants.COMPONENT, "border.link");

        encoding.setServletPath("/admin/Menu,border.link.sdirect");
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, null);

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatefulExtension("sdirect");

        encoder.encode(encoding);

        verifyControls();
    }

    public void testDecodeWrongExtension()
    {
        ServiceEncoding encoding = newEncoding("/foo.svc");

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");
        encoder.setStatefulExtension("sdirect");

        encoder.decode(encoding);

        verifyControls();
    }

    public void testDecodeStateless()
    {
        ServiceEncoding encoding = newEncoding("/admin/Menu,border.link.direct");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, "admin/Menu");
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, "border.link");

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");
        encoder.setStatefulExtension("sdirect");

        encoder.decode(encoding);

        verifyControls();
    }
    
    public void testDecodeStateful()
    {
        ServiceEncoding encoding = newEncoding("/admin/Menu,border.link.sdirect");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, "admin/Menu");
        encoding.setParameterValue(ServiceConstants.SESSION, "T");
        encoding.setParameterValue(ServiceConstants.COMPONENT, "border.link");

        replayControls();

        DirectServiceEncoder encoder = new DirectServiceEncoder();
        encoder.setStatelessExtension("direct");
        encoder.setStatefulExtension("sdirect");

        encoder.decode(encoding);

        verifyControls();
    }    
}