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

    public void testUnkownPrefix()
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