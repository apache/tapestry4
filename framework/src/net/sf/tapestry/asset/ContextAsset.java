package net.sf.tapestry.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.LocalizedNameGenerator;

/**
 *  An asset whose path is relative to the {@link ServletContext} containing
 *  the application.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ContextAsset implements IAsset
{
    private static final Log LOG = LogFactory.getLog(ContextAsset.class.getName());

    private static class Localization
    {
        String assetPath;
        String URL;

        Localization(String assetPath, String URL)
        {
            this.assetPath = assetPath;
            this.URL = URL;
        }
    }

    /**
     *  Map, keyed on Locale, value is an instance of Localization.
     * 
     **/

    private Map _localizations;
    private String _assetPath;

    public ContextAsset(String assetPath)
    {
        _assetPath = assetPath;
    }

    /**
     *  Generates a URL for the client to retrieve the asset.  The context path
     *  is prepended to the asset path, which means that assets deployed inside
     *  web applications will still work (if things are configured properly).
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        Localization localization = findLocalization(cycle);

        return localization.URL;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        return getResourceAsStream(cycle, cycle.getPage().getLocale());
    }

    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale)
    {
        Localization localization = findLocalization(cycle, locale);

        try
        {
            ServletContext context = cycle.getRequestContext().getServlet().getServletContext();
            URL url = context.getResource(localization.assetPath);

            return url.openStream();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(Tapestry.getString("ContextAsset.resource-missing", _assetPath), ex);
        }
    }

    private Localization findLocalization(IRequestCycle cycle)
    {
        return findLocalization(cycle, cycle.getPage().getLocale());
    }

    /**
     *  Poke around until we find the localized version of the asset.
     *
     **/

    private synchronized Localization findLocalization(IRequestCycle cycle, Locale locale)
    {
        if (_localizations == null)
            _localizations = new HashMap();

        Localization result = (Localization) _localizations.get(locale);
        if (result != null)
            return result;

        if (LOG.isDebugEnabled())
            LOG.debug("Searching for localization of context resource " + _assetPath);

        int dotx = _assetPath.lastIndexOf('.');
        String basePath = _assetPath.substring(0, dotx);
        String suffix = _assetPath.substring(dotx);

        ServletContext context = cycle.getRequestContext().getServlet().getServletContext();
        String contextPath = cycle.getEngine().getContextPath();

        LocalizedNameGenerator generator = new LocalizedNameGenerator(basePath, locale, suffix);

        while (generator.more())
        {
            String candidatePath = generator.next();

            try
            {
                URL candidateURL = context.getResource(candidatePath);
                if (candidateURL != null && exists(candidateURL))
                {
                    result = new Localization(candidatePath, contextPath + candidatePath);

                    _localizations.put(locale, result);

                    if (LOG.isDebugEnabled())
                        LOG.debug("Found " + result);

                    return result;
                }
            }
            catch (MalformedURLException ex)
            {
                // We just ignore this!
            }

        }

        throw new ApplicationRuntimeException(
            Tapestry.getString("ContextAsset.resource-unavailable", _assetPath, locale));
    }

    /** @since 1.0.6 **/

    private boolean exists(URL url)
    {
        // Because Jetty doesn't unpack JARs, it appears that File.exists() return false.
        // Meanwhile, Tomcat returns a URL even for files that aren't in the WAR.
        // The only way to be safe is to open the URL for reading.

        InputStream in = null;

        try
        {
            in = url.openStream();

            in.read();

            in.close();
            in = null;
        }
        catch (IOException ex)
        {
            return false;
        }
        finally
        {
            close(in);
        }

        return true;
    }

    private void close(InputStream stream)
    {
        if (stream == null)
            return;

        try
        {
            stream.close();
        }
        catch (IOException ex)
        {
            // Ignore.
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ContextAsset[");
        buffer.append(_assetPath);

        buffer.append(']');

        return buffer.toString();
    }

}