package net.sf.tapestry.asset;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;

/**
 *  A reference to an external URL.  {@link ExternalAsset}s are not
 *  localizable.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExternalAsset implements IAsset
{
    private String _URL;

    public ExternalAsset(String URL)
    {
        _URL = URL;
    }

    /**
     *  Simply returns the URL of the external asset.
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        return _URL;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        return getResourceAsStream(cycle, cycle.getPage().getLocale());
    }

    /**
     *  Ignores the locale and attempts to get the stream to the external URL.
     * 
     *  @since 2.2
     * 
     **/

    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale)
    {
        URL url;

        try
        {
            url = new URL(_URL);

            return url.openStream();
        }
        catch (Exception ex)
        {
            // MalrformedURLException or IOException

            throw new ApplicationRuntimeException(Tapestry.getString("ExternalAsset.resource-missing", _URL), ex);
        }

    }

    public String toString()
    {
        return "ExternalAsset[" + _URL + "]";
    }
}