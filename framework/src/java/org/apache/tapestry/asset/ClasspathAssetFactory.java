package org.apache.tapestry.asset;

import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IAsset;

/**
 * @author Howard M. Lewis Ship
 */
public class ClasspathAssetFactory implements AssetFactory
{

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        Resource asset = baseResource.getRelativeResource(path);
        Resource localized = asset.getLocalization(locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingAsset(path, baseResource),
                    location, null);

        return new PrivateAsset((ClasspathResource) localized, location);
    }

}