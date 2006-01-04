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

package org.apache.tapestry.engine.encoders;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.PageServiceEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPageServiceEncoder extends HiveMindTestCase
{
    public void testEncodeOtherService()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getParameterValue(ServiceConstants.SERVICE);
        control.setReturnValue("foo");

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");

        encoder.encode(e);

        verifyControls();
    }

    public void testEncode()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getParameterValue(ServiceConstants.SERVICE);
        control.setReturnValue("page");

        e.getParameterValue(ServiceConstants.PAGE);
        control.setReturnValue("Home");

        e.setServletPath("/Home.html");
        e.setParameterValue(ServiceConstants.SERVICE, null);
        e.setParameterValue(ServiceConstants.PAGE, null);

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");
        encoder.setExtension("html");

        encoder.encode(e);

        verifyControls();
    }

    public void testEncodeInNamespace()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getParameterValue(ServiceConstants.SERVICE);
        control.setReturnValue("page");

        e.getParameterValue(ServiceConstants.PAGE);
        control.setReturnValue("contrib:Foo");

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setServiceName("page");

        encoder.encode(e);

        verifyControls();
    }

    public void testDecodeNoExtension()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getServletPath();
        control.setReturnValue("/app");

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();

        encoder.decode(e);

        verifyControls();
    }

    public void testDecodeEndsWithDot()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getServletPath();
        control.setReturnValue("/ends.with.dot.");

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();

        encoder.decode(e);

        verifyControls();
    }

    public void testDecodeWrongExtension()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getServletPath();
        control.setReturnValue("/Home.direct");

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();

        encoder.decode(e);

        verifyControls();
    }

    public void testDecodeSuccess()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding e = (ServiceEncoding) control.getMock();

        e.getServletPath();
        control.setReturnValue("/Home.html");

        e.setParameterValue(ServiceConstants.SERVICE, "page");
        e.setParameterValue(ServiceConstants.PAGE, "Home");

        replayControls();

        PageServiceEncoder encoder = new PageServiceEncoder();
        encoder.setExtension("html");
        encoder.setServiceName("page");

        encoder.decode(e);

        verifyControls();
    }
}