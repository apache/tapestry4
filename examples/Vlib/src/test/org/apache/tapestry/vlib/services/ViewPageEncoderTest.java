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

package org.apache.tapestry.vlib.services;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.internal.ArrayMatcher;

/**
 * Tests for {@link org.apache.tapestry.vlib.services.ViewPageEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ViewPageEncoderTest extends HiveMindTestCase
{
    private ViewPageEncoder _encoder;

    {
        _encoder = new ViewPageEncoder();
        _encoder.setPageName("ViewBook");
        _encoder.setUrl("/book");
    }

    public void testEncodeWrongService()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, "wrong");

        replayControls();

        _encoder.encode(encoding);

        verifyControls();
    }

    public void testEncodeWrongPage()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, Tapestry.EXTERNAL_SERVICE);
        trainGetParameterValue(encoding, ServiceConstants.PAGE, "Foo");

        replayControls();

        _encoder.encode(encoding);

        verifyControls();
    }

    public void testEncodeMatch()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, Tapestry.EXTERNAL_SERVICE);
        trainGetParameterValue(encoding, ServiceConstants.PAGE, "ViewBook");
        trainGetParameterValues(encoding, ServiceConstants.PARAMETER, new String[]
        { "1234" });

        encoding.setServletPath("/book/1234");
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.PARAMETER, null);

        replayControls();

        _encoder.encode(encoding);

        verifyControls();
    }

    public void testDecodeWrongURL()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/Home.html");

        replayControls();

        _encoder.decode(encoding);

        verifyControls();
    }

    public void testDecodeMatch()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/book");
        trainGetPathInfo(encoding, "/2001");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.EXTERNAL_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, "ViewBook");
        encoding.setParameterValues(ServiceConstants.PARAMETER, new String[]
        { "2001" });
        getControl(encoding).setMatcher(new ArrayMatcher());

        replayControls();

        _encoder.decode(encoding);

        verifyControls();
    }

    protected void trainGetPathInfo(ServiceEncoding encoding, String pathInfo)
    {
        encoding.getPathInfo();
        setReturnValue(encoding, pathInfo);
    }

    private void trainGetServletPath(ServiceEncoding encoding, String servletPath)
    {
        encoding.getServletPath();
        setReturnValue(encoding, servletPath);
    }

    private void trainGetParameterValues(ServiceEncoding encoding, String name, String[] values)
    {
        encoding.getParameterValues(name);
        setReturnValue(encoding, values);
    }

    private ServiceEncoding newEncoding()
    {
        return (ServiceEncoding) newMock(ServiceEncoding.class);
    }

    private void trainGetParameterValue(ServiceEncoding encoding, String name, String value)
    {
        encoding.getParameterValue(name);
        setReturnValue(encoding, value);
    }
}
