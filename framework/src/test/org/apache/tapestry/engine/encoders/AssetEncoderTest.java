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
import org.apache.tapestry.asset.AssetService;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.AssetEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AssetEncoderTest extends HiveMindTestCase
{
    public void testWrongService()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, "foo");

        replayControls();

        new AssetEncoder().encode(encoding);

        verifyControls();
    }

    protected void trainGetParameterValue(ServiceEncoding encoding, String name, String value)
    {
        encoding.getParameterValue(name);
        setReturnValue(encoding, value);
    }

    protected ServiceEncoding newEncoding()
    {
        return (ServiceEncoding) newMock(ServiceEncoding.class);
    }

    public void testWrongPath()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/Home.page");

        replayControls();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verifyControls();
    }

    protected void trainGetServletPath(ServiceEncoding encoding, String servletPath)
    {
        encoding.getServletPath();
        setReturnValue(encoding, servletPath);
    }

    public void testEncode()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        trainGetParameterValue(encoding, AssetService.PATH, "/foo/bar/Baz.gif");
        trainGetParameterValue(encoding, AssetService.DIGEST, "12345");

        encoding.setServletPath("/assets/12345/foo/bar/Baz.gif");
        encoding.setParameterValue(AssetService.PATH, null);
        encoding.setParameterValue(AssetService.DIGEST, null);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);

        replayControls();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.encode(encoding);

        verifyControls();
    }

    public void testDecode()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/assets");
        trainGetPathInfo(encoding, "/12345/foo/bar/Baz.gif");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, "12345");
        encoding.setParameterValue(AssetService.PATH, "/foo/bar/Baz.gif");

        replayControls();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verifyControls();
    }

    protected void trainGetPathInfo(ServiceEncoding encoding, String pathInfo)
    {
        encoding.getPathInfo();
        setReturnValue(encoding, pathInfo);
    }
}