package net.sf.tapestry.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IComponentStrings;
import net.sf.tapestry.IComponentStringsSource;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.MultiKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Global object (stored in the servlet context) that accesses
 *  localized properties for a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class DefaultStringsSource implements IComponentStringsSource
{
    private static final Log LOG = LogFactory.getLog(DefaultStringsSource.class);

    private Properties _emptyProperties = new Properties();

    /**
     *  Map of {@link Properties}, keyed on a {@link MultiKey} of
     *  component specification path and locale.
     * 
     **/

    private Map _cache = new HashMap();

    public DefaultStringsSource()
    {
    }

    /**
     *  Returns an instance of {@link Properties} containing
     *  the properly localized messages for the component,
     *  in the {@link Locale} identified by the component's
     *  containing page.
     * 
     **/

    protected synchronized Properties getLocalizedProperties(IComponent component)
    {
        if (component == null)
            throw new IllegalArgumentException(Tapestry.getString("invalid-null-parameter", "component"));

        IResourceLocation specificationLocation = component.getSpecification().getSpecificationLocation();
        Locale locale = component.getPage().getLocale();

        // Check to see if already in the cache

        MultiKey key = buildKey(specificationLocation, locale);

        Properties result = (Properties) _cache.get(key);

        if (result != null)
            return result;

        // Not found, create it now.

        result = assembleProperties(specificationLocation, locale);

        _cache.put(key, result);

        return result;
    }

    private static final String SUFFIX = ".properties";

    private Properties assembleProperties(IResourceLocation baseResourceLocation, Locale locale)
    {
        boolean debug = LOG.isDebugEnabled();
        if (debug)
            LOG.debug("Assembling properties for " + baseResourceLocation + " " + locale);

        String name = baseResourceLocation.getName();

        int dotx = name.indexOf('.');
        String baseName = name.substring(0, dotx);

        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        Properties parent = (Properties) _cache.get(baseResourceLocation);

        if (parent == null)
        {
            parent = readProperties(baseResourceLocation, baseName, null, null);

            if (parent == null)
                parent = _emptyProperties;

            _cache.put(baseResourceLocation, parent);
        }

        Properties result = parent;

        if (!Tapestry.isNull(language))
        {
            Locale l = new Locale(language, "");
            MultiKey key = buildKey(baseResourceLocation, l);

            result = (Properties) _cache.get(key);

            if (result == null)
                result = readProperties(baseResourceLocation, baseName, l, parent);

            _cache.put(key, result);

            parent = result;
        }
        else
            language = "";

        if (!Tapestry.isNull(country))
        {
            Locale l = new Locale(language, country);
            MultiKey key = buildKey(baseResourceLocation, l);

            result = (Properties) _cache.get(key);

            if (result == null)
                result = readProperties(baseResourceLocation, baseName, l, parent);

            _cache.put(key, result);

            parent = result;
        }
        else
            country = "";

        if (!Tapestry.isNull(variant))
        {
            Locale l = new Locale(language, country, variant);
            MultiKey key = buildKey(baseResourceLocation, l);

            result = (Properties) _cache.get(key);

            if (result == null)
                result = readProperties(baseResourceLocation, baseName, l, parent);

            _cache.put(key, result);
        }

        return result;
    }

    private MultiKey buildKey(IResourceLocation location, Locale locale)
    {
        return new MultiKey(new Object[] { location, locale.toString()}, false);
    }

    private Properties readProperties(IResourceLocation baseLocation, String baseName, Locale locale, Properties parent)
    {
        StringBuffer buffer = new StringBuffer(baseName);

        if (locale != null)
        {
            buffer.append('_');
            buffer.append(locale.toString());
        }

        buffer.append(SUFFIX);
        
        IResourceLocation localized = baseLocation.getRelativeLocation(buffer.toString());

        URL propertiesURL = localized.getResourceURL();

        if (propertiesURL == null)
            return parent;

        Properties result = null;

        if (parent == null)
            result = new Properties();
        else
            result = new Properties(parent);

        try
        {
            InputStream input = propertiesURL.openStream();

            result.load(input);
            
            input.close();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("ComponentPropertiesStore.unable-to-read-input", propertiesURL),
                ex);
        }

        return result;
    }

    /**
     *  Clears the cache of read properties files.
     * 
     **/

    public void reset()
    {
        _cache.clear();
    }

    public IComponentStrings getStrings(IComponent component)
    {
        return new ComponentStrings(getLocalizedProperties(component));
    }

}