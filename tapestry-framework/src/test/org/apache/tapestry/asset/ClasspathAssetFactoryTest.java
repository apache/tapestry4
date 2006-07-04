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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.LocationImpl;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.l10n.DefaultResourceLocalizer;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.asset.ClasspathAssetFactory}.
 * 
 * @author Howard M. Lewis Ship
 */
@Test
public class ClasspathAssetFactoryTest extends BaseComponentTestCase
{
    public void testCreateAsset()
    {
        IEngineService assetService = newService();
        Location l = newLocation();

        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        Resource base = newBaseResource();

        IAsset asset = factory.createAsset(base, "relative-resource.txt", Locale.FRENCH, l);

        assertTrue(asset instanceof PrivateAsset);
        assertEquals("/org/apache/tapestry/asset/relative-resource_fr.txt", asset
                .getResourceLocation().getPath());
        assertSame(l, asset.getLocation());

        verify();
    }

    public void testCreateAssetMissing()
    {
        IEngineService assetService = newService();
        Location l = newLocation();

        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        Resource base = newBaseResource();

        try
        {
            factory.createAsset(base, "does-not-exist.txt", Locale.ENGLISH, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to locate resource 'does-not-exist.txt' relative to classpath:/org/apache/tapestry/asset/base-resource.txt.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testCreateAbsoluteAsset()
    {
        IEngineService assetService = newService();
        Location l = newLocation();

        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        IAsset asset = factory.createAbsoluteAsset(
                "/org/apache/tapestry/asset/relative-resource.txt",
                Locale.FRENCH,
                l);

        assertTrue(asset instanceof PrivateAsset);
        assertEquals("/org/apache/tapestry/asset/relative-resource_fr.txt", asset
                .getResourceLocation().getPath());
        assertSame(l, asset.getLocation());

        verify();
    }

    public void testCreateAbsoluteAssetMissing()
    {
        IEngineService assetService = newService();
        Location l = newLocation();

        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        try
        {
            factory.createAbsoluteAsset(
                    "/org/apache/tapestry/asset/does-not-exist.txt",
                    Locale.ENGLISH,
                    l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Missing classpath resource '/org/apache/tapestry/asset/does-not-exist.txt'.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testCreateDirectoryAsset()
    {
        IEngineService assetService = newService();
        Location l = newLocation();
        
        replay();
        
        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());
        
        String path = "/org/apache/tapestry/html/dojo";
        
        Resource subResource = new ClasspathResource(getClassResolver(), path);
        IAsset asset = factory.createAsset(subResource, l);
        
        assertTrue(asset instanceof PrivateAsset);
        assertEquals(path, asset
                .getResourceLocation().getPath());
        assertSame(l, asset.getLocation());
        
        verify();
    }
    
    public void testCreateRelativeDirectoryAsset()
    {
        IEngineService assetService = newService();
        Resource shell = new ClasspathResource(getClassResolver(),
            "/org/apache/tapestry/html/Shell.jwc");
        Location l = new LocationImpl(shell);
        
        replay();
        
        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());
        
        String path = "/dojo/dojo.js";
        
        IAsset asset = factory.createAsset(shell, path, 
                Locale.getDefault(),
                l);
        
        assertTrue(asset instanceof PrivateAsset);
        assertEquals(path, asset
                .getResourceLocation().getPath());
        assertSame(l, asset.getLocation());
        
        verify();
    }
    
    public void testCreateRelativeDirectoryMissingAsset()
    {
        IEngineService assetService = newService();
        Resource shell = new ClasspathResource(getClassResolver(),
            "/org/apache/tapestry/html/Shell.jwc");
        Location l = new LocationImpl(shell);
        
        replay();
        
        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());
        
        String path = "/dojo/";
        
        IAsset asset = factory.createAsset(shell, path, 
                Locale.getDefault(),
                l);
        
        assertTrue(asset instanceof PrivateAsset);
        assertEquals(path, asset
                .getResourceLocation().getPath());
        assertSame(l, asset.getLocation());
        
        verify();
    }
    
    /**
     * Tests relative sub-directory paths.
     */
    public void testRelativeDirectoryPath()
    {
        IEngineService assetService = newService();
        Location l = newLocation();
        
        replay();
        
        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());
        
        Resource subResource = new ClasspathResource(getClassResolver(),
                "/org/apache/tapestry/asset/subresource/sub-resource.txt");
        IAsset asset = factory.createAsset(subResource, l);
        
        assertTrue(asset instanceof PrivateAsset);
        assertEquals("/org/apache/tapestry/asset/subresource/sub-resource.txt",
                asset.getResourceLocation().getPath());
        assertSame(l, asset.getLocation());
        
        verify();
    }
    
    private ClasspathResource newBaseResource()
    {
        return new ClasspathResource(getClassResolver(),
                "/org/apache/tapestry/asset/base-resource.txt");
    }

    private IEngineService newService()
    {
        return newMock(IEngineService.class);
    }
}
