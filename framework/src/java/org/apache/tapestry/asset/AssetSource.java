package org.apache.tapestry.asset;

import java.util.Locale;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IAsset;

/**
 * Used to create an {@link org.apache.tapestry.IAsset}instance for a particular asset path. The
 * path may have a prefix that indicates its type, or it may be relative to some existing resource.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface AssetSource
{
    public IAsset findAsset(Resource base, String path, Locale locale, Location location);
}