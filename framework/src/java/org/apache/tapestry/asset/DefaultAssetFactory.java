package org.apache.tapestry.asset;

import java.util.Locale;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.IAsset;

/**
 * Default asset factory used when the asset path contains
 * a prefix that is not recognized. It is assumed that
 * the prefix is, in fact, the scheme of an external URL.
 * Retur
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DefaultAssetFactory implements AssetFactory
{

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        return new ExternalAsset(path, location);
    }

}