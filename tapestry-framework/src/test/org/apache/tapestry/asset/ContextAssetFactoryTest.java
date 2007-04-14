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

package org.apache.tapestry.asset;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.l10n.DefaultResourceLocalizer;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.web.WebContext;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Locale;

@Test
public class ContextAssetFactoryTest extends BaseComponentTestCase
{
    protected Resource newResource()
    {
        return newMock(Resource.class);
    }

    protected URL newURL()
    {
        return getClass().getResource("base-resource.txt");
    }

    public void test_Create_Asset()
    {
        Resource base = newResource();
        Resource relative = newResource();
        Resource localized = newResource();
        Location l = newLocation();
        URL url = newURL();
        IComponentSpecification spec = newSpec();

        trainGetRelativeResource(base, "/", base);
        trainGetRelativeResource(base, "asset.png", relative);
        trainGetLocalization(relative, Locale.FRENCH, localized);
        expect(localized.getResourceURL()).andReturn(url).anyTimes();
        
        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());

        factory.setContextPath("/context");

        IAsset asset = factory.createAsset(spec, base, "asset.png", Locale.FRENCH, l);

        assertTrue(asset instanceof ContextAsset);
        assertSame(localized, asset.getResourceLocation());
        assertSame(l, asset.getLocation());

        verify();
    }

    public void test_Create_Asset_Missing()
    {
        Resource base = newResource();
        Resource relative = newResource();
        Location l = newLocation();
        IComponentSpecification spec = newMock(IComponentSpecification.class);
        WebContext context = newMock(WebContext.class);

        trainGetRelativeResource(base, "/", base);
        trainGetRelativeResource(base, "asset.png", relative);
        trainGetLocalization(relative, Locale.FRENCH, null);

        expect(spec.getLocation()).andReturn(l);
        expect(l.getResource()).andReturn(null);

        expect(context.getResource("asset_fr.png")).andReturn(null);
        expect(context.getResource("asset.png")).andReturn(null);

        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());
        factory.setContextPath("/context");
        factory.setWebContext(context);

        try
        {
            factory.createAsset(spec, base, "asset.png", Locale.FRENCH, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to locate resource 'asset.png' relative to EasyMock for interface org.apache.hivemind.Resource.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }
    
    public void test_Create_Absolute_Asset()
    {
        Location l = newLocation();
        URL url = newURL();
        WebContext context = newMock(WebContext.class);
        trainGetResource(context, "/asset_fr.png", url);

        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());
        factory.setContextPath("/context");
        factory.setWebContext(context);

        IAsset asset = factory.createAbsoluteAsset("/asset.png", Locale.FRENCH, l);

        assertTrue(asset instanceof ContextAsset);
        assertEquals("/asset_fr.png", asset.getResourceLocation().getPath());
        assertSame(l, asset.getLocation());

        verify();
    }

    public void test_Create_Absolute_Asset_Missing()
    {
        Location l = newLocation();
        WebContext context = newMock(WebContext.class);

        trainGetResource(context, "/asset_fr.png", null);
        trainGetResource(context, "/asset.png", null);

        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());
        factory.setContextPath("/context");
        factory.setWebContext(context);

        try
        {
            factory.createAbsoluteAsset("/asset.png", Locale.FRENCH, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Missing context resource '/asset.png'.", ex.getMessage());
            assertSame(l, ex.getLocation());
        }
        verify();
    }
    
    public void test_Create_Asset_Encode_URL()
    {
        Location l = newLocation();
        URL url = newURL();
        WebContext context = newMock(WebContext.class);
        IRequestCycle rc = newMock(IRequestCycle.class);

        trainGetResource(context, "/asset_fr.png", url);

        trainEncodeURL(rc, "/context/asset_fr.png", "/context/asset_fr.png?encoded");
        
        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());
        factory.setContextPath("/context");
        factory.setWebContext(context);
        factory.setRequestCycle(rc);

        String assetUrl = factory.createAbsoluteAsset("/asset.png", Locale.FRENCH, l).buildURL();

        assertTrue(assetUrl.endsWith("?encoded"));

        verify();
    }

    private void trainGetLocalization(Resource resource, Locale locale, Resource localized)
    {
        expect(resource.getLocalization(locale)).andReturn(localized);
    }

    protected void trainGetResourceURL(Resource resource, URL url)
    {
        expect(resource.getResourceURL()).andReturn(url);
    }

    protected void trainGetResource(WebContext context, String path, URL url)
    {
        expect(context.getResource(path)).andReturn(url);
    }

    protected void trainGetRelativeResource(Resource base, String path, Resource relative)
    {
        expect(base.getRelativeResource(path)).andReturn(relative);
    }

    protected void trainEncodeURL(IRequestCycle rc, String URL, String encodedURL)
    {
        expect(rc.encodeURL(URL)).andReturn(encodedURL);
    }
}
