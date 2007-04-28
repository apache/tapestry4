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
import org.apache.hivemind.impl.LocationImpl;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.l10n.DefaultResourceLocalizer;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

import java.util.Locale;

/**
 * Tests for {@link org.apache.tapestry.asset.ClasspathAssetFactory}.
 * 
 * @author Howard M. Lewis Ship
 */
@Test
public class ClasspathAssetFactoryTest extends BaseComponentTestCase
{
    public void test_Create_Asset()
    {
        IEngineService assetService = newService();
        Location l = newLocation();
        IComponentSpecification spec = newSpec();
        
        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        Resource base = newBaseResource();

        IAsset asset = factory.createAsset(base, spec, "relative-resource.txt", Locale.FRENCH, l);

        assertTrue(asset instanceof PrivateAsset);
        assertEquals("/org/apache/tapestry/asset/relative-resource_fr.txt", asset.getResourceLocation().getPath());
        assertSame(l, asset.getLocation());

        verify();
    }

    public void test_Absolute_Asset_Exists()
    {
        String path = "/org/apache/tapestry/html/Shell.jwc";

        IEngineService assetService = newService();
        IComponentSpecification spec = newSpec();
        
        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        Resource base = newBaseResource();

        assert factory.assetExists(spec, base, path, null);

        verify();
    }

    public void test_Create_Asset_Missing()
    {
        IEngineService assetService = newService();
        Location l = newLocation();
        IComponentSpecification spec = newSpec();

        replay();

        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());

        Resource base = newBaseResource();

        try
        {
            factory.createAsset(base, spec, "does-not-exist.txt", Locale.ENGLISH, l);
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

    public void test_Create_Absolute_Asset()
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

    public void test_Create_Absolute_Asset_Missing()
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

    public void test_Create_Directory_Asset()
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
    
    public void test_Create_Relative_Directory_Asset()
    {
        IEngineService assetService = newService();
        Resource shell = new ClasspathResource(getClassResolver(),
            "/org/apache/tapestry/html/Shell.jwc");
        Location l = new LocationImpl(shell);
        IComponentSpecification spec = newSpec();
        
        replay();
        
        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());
        
        String path = "/dojo/dojo.js";
        
        IAsset asset = factory.createAsset(shell, spec, path, 
                Locale.getDefault(),
                l);
        
        assertTrue(asset instanceof PrivateAsset);
        assertEquals(path, asset
                .getResourceLocation().getPath());
        assertSame(l, asset.getLocation());
        
        verify();
    }
    
    public void test_Create_Relative_Directory_Missing_Asset()
    {
        IEngineService assetService = newService();
        Resource shell = new ClasspathResource(getClassResolver(),
            "/org/apache/tapestry/html/Shell.jwc");
        Location l = new LocationImpl(shell);
        IComponentSpecification spec = newSpec();
        
        replay();
        
        ClasspathAssetFactory factory = new ClasspathAssetFactory();
        factory.setClassResolver(getClassResolver());
        factory.setAssetService(assetService);
        factory.setLocalizer(new DefaultResourceLocalizer());
        
        String path = "/dojo/";
        
        IAsset asset = factory.createAsset(shell, spec, path, 
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
    public void test_Relative_Directory_Path()
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
