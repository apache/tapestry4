package org.apache.tapestry.asset;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.IAsset;

/**
 * For the moment, all "context:" prefixed asset paths are interpreted relative to the servlet
 * context (the web application's root folder).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ContextAssetFactory implements AssetFactory
{
    private ServletContext _servletContext;

    private ContextResource _servletRoot;

    public void initializeService()
    {
        _servletRoot = new ContextResource(_servletContext, "/");
    }

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        Resource assetResource = _servletRoot.getRelativeResource(path);
        Resource localized = assetResource.getLocalization(locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingAsset(path, _servletRoot),
                    location, null);

        return new ContextAsset((ContextResource) localized, location);
    }

    public void setServletContext(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }
}