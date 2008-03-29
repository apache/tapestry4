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

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TestBase;
import org.apache.tapestry.asset.AssetService;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.encoders.AssetEncoder}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class AssetEncoderTest extends TestBase {
    public void test_Wrong_Service()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, "foo");

        replay();

        new AssetEncoder().encode(encoding);

        verify();
    }

    protected void trainGetParameterValue(ServiceEncoding encoding, String name, String value)
    {
        expect(encoding.getParameterValue(name)).andReturn(value);
    }

    protected ServiceEncoding newEncoding()
    {
        return newMock(ServiceEncoding.class);
    }

    public void test_Wrong_Path()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/Home.page");

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verify();
    }

    protected void trainGetServletPath(ServiceEncoding encoding, String servletPath)
    {
        expect(encoding.getServletPath()).andReturn(servletPath);
    }

    public void test_Encode()
    {
        ServiceEncoding encoding = newEncoding();
        
        trainGetParameterValue(encoding, ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        trainGetParameterValue(encoding, AssetService.PATH, "/foo/bar/Baz.gif");
        trainGetParameterValue(encoding, AssetService.DIGEST, "12345");
        
        encoding.setServletPath("/assets/12345/foo/bar/Baz.gif");
        encoding.setParameterValue(AssetService.PATH, null);
        encoding.setParameterValue(AssetService.DIGEST, null);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.encode(encoding);

        verify();
    }

    public void test_Encode_Missing_Path()
    {
        ServiceEncoding encoding = newEncoding();
        
        trainGetParameterValue(encoding, ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        trainGetParameterValue(encoding, AssetService.PATH, "foo/bar/Baz.gif");
        trainGetParameterValue(encoding, AssetService.DIGEST, "12345");
        
        encoding.setServletPath("/assets/12345/foo/bar/Baz.gif");
        encoding.setParameterValue(AssetService.PATH, null);
        encoding.setParameterValue(AssetService.DIGEST, null);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        
        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.encode(encoding);

        verify();
    }
    
    public void test_Decode()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/assets");
        trainGetPathInfo(encoding, "/12345/foo/bar/Baz.gif");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, "12345");
        encoding.setParameterValue(AssetService.PATH, "/foo/bar/Baz.gif");

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verify();
    }

    protected void trainGetPathInfo(ServiceEncoding encoding, String pathInfo)
    {
        expect(encoding.getPathInfo()).andReturn(pathInfo);
    }
    
    public void test_Encode_Unprotected()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetParameterValue(encoding, ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        trainGetParameterValue(encoding, AssetService.PATH, "/foo/bar/Baz.gif");
        trainGetParameterValue(encoding, AssetService.DIGEST, null);
        
        encoding.setServletPath("/assets/" + AssetEncoder.DIGEST_STATIC + "/foo/bar/Baz.gif");
        encoding.setParameterValue(AssetService.PATH, null);
        encoding.setParameterValue(AssetService.DIGEST, null);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.encode(encoding);

        verify();
    }
    
    public void test_Decode_Unprotected()
    {
        ServiceEncoding encoding = newEncoding();
        
        trainGetServletPath(encoding, "/assets");
        trainGetPathInfo(encoding, "/" + AssetEncoder.DIGEST_STATIC + "/foo/bar/Baz.gif");
        
        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, AssetEncoder.DIGEST_STATIC);
        encoding.setParameterValue(AssetService.PATH, "/foo/bar/Baz.gif");

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verify();
    }

    public void test_Decode_Null_Pathinfo()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/assets");
        trainGetPathInfo(encoding, null);

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, "");
        encoding.setParameterValue(AssetService.PATH, "");

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verify();
    }
    
    public void test_Decode_Empty_Pathinfo()
    {
        ServiceEncoding encoding = newEncoding();

        trainGetServletPath(encoding, "/assets");
        trainGetPathInfo(encoding, "/");

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, "");
        encoding.setParameterValue(AssetService.PATH, "");         

        replay();

        AssetEncoder encoder = new AssetEncoder();
        encoder.setPath("/assets");

        encoder.decode(encoding);

        verify();
    }
}
