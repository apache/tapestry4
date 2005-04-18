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
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.AssetEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestAssetEncoder extends HiveMindTestCase
{
    public void testWrongService()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        encoding.getParameterValue(ServiceConstants.SERVICE);
        control.setReturnValue("foo");

        replayControls();

        new AssetEncoder().encode(encoding);

        verifyControls();
    }

    public void testWrongPath()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        encoding.getServletPath();
        control.setReturnValue("/Home.page");

        replayControls();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets/");

        encoder.decode(encoding);

        verifyControls();
    }

    public void testEncode()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        encoding.getParameterValue(ServiceConstants.SERVICE);
        control.setReturnValue(Tapestry.ASSET_SERVICE);

        encoding.getParameterValue(AssetService.PATH);
        control.setReturnValue("/foo/bar/Baz.gif");

        encoding.getParameterValue(AssetService.DIGEST);
        control.setReturnValue("12345");

        encoding.setServletPath("/assets/12345/foo/bar/Baz.gif");
        encoding.setParameterValue(AssetService.PATH, null);
        encoding.setParameterValue(AssetService.DIGEST, null);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        
        replayControls();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets/");

        encoder.encode(encoding);

        verifyControls();
    }

    public void testDecode()
    {
        MockControl control = newControl(ServiceEncoding.class);
        ServiceEncoding encoding = (ServiceEncoding) control.getMock();

        encoding.getServletPath();
        control.setReturnValue("/assets/12345/foo/bar/Baz.gif");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, "12345");
        encoding.setParameterValue(AssetService.PATH, "/foo/bar/Baz.gif");
        
        replayControls();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets/");

        encoder.decode(encoding);

        verifyControls();
    }
}