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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IAsset;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.asset.AssetSourceImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestAssetSource extends HiveMindTestCase
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

    private Resource newResource()
    {
        return (Resource) newMock(Resource.class);
    }

    private AssetFactory newAssetFactory(Resource base, String path, Locale locale,
            Location location, IAsset asset)
    {
        MockControl control = newControl(AssetFactory.class);
        AssetFactory f = (AssetFactory) control.getMock();

        f.createAsset(base, path, locale, location);
        control.setReturnValue(asset);

        return f;
    }

    private IAsset newAsset()
    {
        return (IAsset) newMock(IAsset.class);
    }

    public void testKnownPrefix()
    {
        Location l = fabricateLocation(17);

        Resource r = newResource();
        IAsset asset = newAsset();

        List contributions = newContributions("known", newAssetFactory(
                r,
                "path/to/asset",
                Locale.ENGLISH,
                l,
                asset));

        replayControls();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setContributions(contributions);

        as.initializeService();

        IAsset actual = as.findAsset(r, "known:path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verifyControls();
    }

    public void testUnknownPrefix()
    {
        Location l = fabricateLocation(17);

        Resource r = newResource();
        IAsset asset = newAsset();

        AssetFactory f = newAssetFactory(r, "unknown:path/to/asset", Locale.ENGLISH, l, asset);

        replayControls();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setDefaultAssetFactory(f);

        IAsset actual = as.findAsset(r, "unknown:path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verifyControls();
    }

    public void testNoPrefix()
    {
        Location l = fabricateLocation(17);

        Resource r = newResource();
        IAsset asset = newAsset();

        AssetFactory f = newAssetFactory(r, "path/to/asset", Locale.ENGLISH, l, asset);

        replayControls();

        AssetSourceImpl as = new AssetSourceImpl();
        as.setLookupAssetFactory(f);

        IAsset actual = as.findAsset(r, "path/to/asset", Locale.ENGLISH, l);

        assertSame(actual, asset);

        verifyControls();
    }
}