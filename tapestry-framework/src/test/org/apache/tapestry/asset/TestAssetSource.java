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

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link org.apache.tapestry.asset.AssetSourceImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestAssetSource extends BaseComponentTestCase
{
    private AssetFactoryContribution newContribution(String prefix, AssetFactory factory)
    {
        AssetFactoryContribution c = new AssetFactoryContribution();
        c.setPrefix(prefix);
        c.setFactory(factory);

        return c;
    }

    private List newContributions(String prefix, AssetFactory factory)
    {
        return Collections.singletonList(newContribution(prefix, factory));
    }

    private AssetFactory newAssetFactory(Resource base, String path, Locale locale, Location location, IAsset asset)
    {
        AssetFactory f = newMock(AssetFactory.class);
        
        expect(f.createAsset(null, base, path, locale, location)).andReturn(asset);

        return f;
    }
    
    public void test_Known_Prefix()
    {
        Location l = newLocation();

        Resource r = newResource();
        IAsset asset = newAsset();

        List contributions = newContributions("known", newAssetFactory(
                r,
                "path/to/asset",
                Locale.ENGLISH,
                l,
                asset));

        replay();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setContributions(contributions);

        as.initializeService();

        IAsset actual = as.findAsset(r, "known:path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verify();
    }

    public void test_Unknown_Prefix()
    {
        Location l = fabricateLocation(17);

        Resource r = newResource();
        IAsset asset = newAsset();

        AssetFactory f = newAssetFactory(r, "unknown:path/to/asset", Locale.ENGLISH, l, asset);

        replay();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setDefaultAssetFactory(f);

        IAsset actual = as.findAsset(r, "unknown:path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verify();
    }

    public void test_No_Prefix()
    {
        Location l = fabricateLocation(17);

        Resource r = newResource();
        IAsset asset = newAsset();

        AssetFactory classFactory = newAssetFactory(r, "path/to/asset", Locale.ENGLISH, l, asset);

        expect(classFactory.assetExists(null, r, "path/to/asset", Locale.ENGLISH)).andReturn(true);

        replay();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setClasspathAssetFactory(classFactory);

        IAsset actual = as.findAsset(r, "path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verify();
    }
    
    public void test_Known_Prefix_Null_Base()
    {
        Location l = newLocation();
        IAsset asset = newAsset();

        List contributions = newContributions("known", newAssetFactory(
                null,
                "path/to/asset",
                Locale.ENGLISH,
                l,
                asset));

        replay();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setContributions(contributions);

        as.initializeService();

        IAsset actual = as.findAsset(null, "known:path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verify();
    }
}