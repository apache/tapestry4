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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

import java.net.URL;
import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.l10n.DefaultResourceLocalizer;
import org.apache.tapestry.web.WebContext;
import org.testng.annotations.Test;

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

    public void testCreateAsset()
    {
        Resource base = newResource();
        Resource relative = newResource();
        Resource localized = newResource();
        Location l = newLocation();
        URL url = newURL();

        trainGetRelativeResource(base, "asset.png", relative);
        trainGetResourceURL(relative, url);
        trainGetLocalization(relative, Locale.FRENCH, localized);

        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());

        factory.setContextPath("/context");

        IAsset asset = factory.createAsset(base, "asset.png", Locale.FRENCH, l);

        assertTrue(asset instanceof ContextAsset);
        assertSame(localized, asset.getResourceLocation());
        assertSame(l, asset.getLocation());

        verify();
    }

    public void testCreateAssetMissing()
    {
        Resource base = newResource();
        Resource relative = newResource();
        Location l = newLocation();
        trainGetRelativeResource(base, "asset.png", relative);
        trainGetResourceURL(relative, null);
        trainGetLocalization(relative, Locale.FRENCH, null);

        replay();

        ContextAssetFactory factory = new ContextAssetFactory();
        factory.setLocalizer(new DefaultResourceLocalizer());
        factory.setContextPath("/context");

        try
        {
            factory.createAsset(base, "asset.png", Locale.FRENCH, l);
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

    public void testCreateAssetForClasspath()
    {
        Resource base = newResource();
        Resource relative = newResource();
        Location l = newLocation();
        AssetFactory classpathFactory = newMock(AssetFactory.class);
        IAsset asset = newMock(IAsset.class);

        trainGetRelativeResource(base, "/asset.png", relative);
        trainGetResourceURL(relative, null);

        expect(classpathFactory.createAbsoluteAsset("/asset.png", Locale.FRENCH, l))
        .andReturn(asset);

        replay();

        ContextAssetFactory factory = new ContextAssetFactory();

        factory.setContextPath("/context");
        factory.setClasspathAssetFactory(classpathFactory);

        assertSame(asset, factory.createAsset(base, "/asset.png", Locale.FRENCH, l));

        verify();
    }

    public void testCreateAbsoluteAsset()
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

    public void testCreateAbsoluteAssetMissing()
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
    
    public void testCreateAssetEncodeURL()
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