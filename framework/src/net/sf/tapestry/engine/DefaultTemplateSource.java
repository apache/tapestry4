package net.sf.tapestry.engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRenderDescription;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.NoSuchComponentException;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.parse.ComponentTemplate;
import net.sf.tapestry.parse.ITemplateParserDelegate;
import net.sf.tapestry.parse.TemplateParseException;
import net.sf.tapestry.parse.TemplateParser;
import net.sf.tapestry.parse.TemplateToken;
import net.sf.tapestry.resolver.ComponentSpecificationResolver;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.util.MultiKey;

/**
 *  Default implementation of {@link ITemplateSource}.  Templates, once parsed,
 *  stay in memory until explicitly cleared.
 *
 *  <p>An instance of this class acts as a singleton shared by all sessions, so it
 *  must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class DefaultTemplateSource implements ITemplateSource, IRenderDescription
{
    private static final Log LOG = LogFactory.getLog(DefaultTemplateSource.class);

    // Cache of previously retrieved templates.  Key is a multi-key of 
    // specification resource path and locale (local may be null), value
    // is the ComponentTemplate.

    private Map _cache = Collections.synchronizedMap(new HashMap());

    // Previously read templates; key is the IResourceLocation, value
    // is the ComponentTemplate.

    private Map _templates = Collections.synchronizedMap(new HashMap());

    /**
     *  Number of tokens (each template contains multiple tokens).
     *
     **/

    private int _tokenCount;

    private static final int BUFFER_SIZE = 2000;

    private IResourceResolver _resolver;
    private TemplateParser _parser;

    private static class ParserDelegate implements ITemplateParserDelegate
    {
        private IComponent _component;
        private ComponentSpecificationResolver _resolver;

        ParserDelegate(IComponent component, IRequestCycle cycle)
        {
            _component = component;
            _resolver = new ComponentSpecificationResolver(cycle);
        }

        public boolean getKnownComponent(String componentId)
        {
            return _component.getSpecification().getComponent(componentId) != null;
        }

        public boolean getAllowBody(String componentId) throws NoSuchComponentException
        {
            IComponent embedded = _component.getComponent(componentId);

            if (embedded == null)
                throw new NoSuchComponentException(componentId, _component);

            return embedded.getSpecification().getAllowBody();
        }

        public boolean getAllowBody(String libraryId, String type) throws PageLoaderException
        {
            INamespace namespace = _component.getNamespace();

            _resolver.resolve(namespace, libraryId, type);

            ComponentSpecification spec = _resolver.getSpecification();

            return spec.getAllowBody();
        }

    }

    public DefaultTemplateSource(IResourceResolver resolver)
    {
        _resolver = resolver;
    }

    /**
     *  Clears the template cache.  This is used during debugging.
     *
     **/

    public void reset()
    {
        _cache.clear();
        _templates.clear();

        _tokenCount = 0;
    }

    /**
     *  Reads the template for the component.
     *
     *  <p>Returns null if the template can't be found.
     * 
     **/

    public ComponentTemplate getTemplate(IRequestCycle cycle, IComponent component)
    {
        ComponentSpecification specification = component.getSpecification();
        IResourceLocation specificationLocation = specification.getSpecificationLocation();

        Locale locale = component.getPage().getLocale();

        Object key = new MultiKey(new Object[] { specificationLocation, locale }, false);

        ComponentTemplate result = searchCache(key);
        if (result != null)
            return result;

        result = findTemplate(cycle, specificationLocation, component, locale);

        if (result == null)
        {
            String stringKey =
                component.getSpecification().isPageSpecification()
                    ? "DefaultTemplateSource.no-template-for-page"
                    : "DefaultTemplateSource.no-template-for-component";

            throw new ApplicationRuntimeException(Tapestry.getString(stringKey, component.getExtendedId(), locale));
        }

        saveToCache(key, result);

        return result;
    }

    private ComponentTemplate searchCache(Object key)
    {
        return (ComponentTemplate) _cache.get(key);
    }

    private void saveToCache(Object key, ComponentTemplate template)
    {
        _cache.put(key, template);

    }

    /**
     *  Finds the template for the given component, using the following rules:
     *  <ul>
     *  <li>If the component has a $template asset, use that
     *  <li>Look for a template in the same folder as the component
     *  <li>If a page in the application namespace, search in the application root
     *  <li>Fail!
     *  </ul>
     * 
     *  @returns the template, or null if not found
     * 
     **/

    private ComponentTemplate findTemplate(
        IRequestCycle cycle,
        IResourceLocation location,
        IComponent component,
        Locale locale)
    {
        IAsset templateAsset = component.getAsset(TEMPLATE_ASSET_NAME);

        if (templateAsset != null)
            return readTemplateFromAsset(cycle, component, templateAsset, locale);

        String name = location.getName();
        int dotx = name.lastIndexOf('.');
        String templateBaseName = name.substring(0, dotx) + ".html";

        ComponentTemplate result = findStandardTemplate(cycle, location, component, templateBaseName, locale);

        if (result == null
            && component.getSpecification().isPageSpecification()
            && component.getNamespace().isApplicationNamespace())
            result = findPageTemplateInApplicationRoot(cycle, component, templateBaseName, locale);

        return result;
    }

    private IResourceLocation _applicationRootLocation;

    private ComponentTemplate findPageTemplateInApplicationRoot(
        IRequestCycle cycle,
        IComponent component,
        String templateBaseName,
        Locale locale)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Checking for " + templateBaseName + " in application root");

        if (_applicationRootLocation == null)
            _applicationRootLocation = Tapestry.getApplicationRootLocation(cycle);

        IResourceLocation baseLocation = _applicationRootLocation.getRelativeLocation(templateBaseName);
        IResourceLocation localizedLocation = baseLocation.getLocalization(locale);

        if (localizedLocation == null)
            return null;

        return getOrParseTemplate(cycle, localizedLocation, component);
    }

    /**
     *  Reads an asset to get the template.
     * 
     **/

    private ComponentTemplate readTemplateFromAsset(
        IRequestCycle cycle,
        IComponent component,
        IAsset asset,
        Locale locale)
    {
        InputStream stream = asset.getResourceAsStream(cycle, locale);

        char[] templateData = null;

        try
        {
            templateData = readTemplateStream(stream);

            stream.close();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultTemplateSource.unable-to-read-template", asset),
                ex);
        }

        return constructTemplateInstance(cycle, templateData, asset.toString(), component);
    }

    /**
     *  Search for the template corresponding to the resource and the locale.
     *  This may be in the template map already, or may involve reading and
     *  parsing the template.
     *
     *  @returns the template, or null if not found.
     * 
     **/

    private ComponentTemplate findStandardTemplate(
        IRequestCycle cycle,
        IResourceLocation location,
        IComponent component,
        String templateBaseName,
        Locale locale)
    {
        if (LOG.isDebugEnabled())
            LOG.debug(
                "Searching for localized version of template for "
                    + location
                    + " in locale "
                    + locale.getDisplayName());

        IResourceLocation baseTemplateLocation = location.getRelativeLocation(templateBaseName);

        IResourceLocation localizedTemplateLocation = baseTemplateLocation.getLocalization(locale);

        if (localizedTemplateLocation == null)
            return null;

        return getOrParseTemplate(cycle, localizedTemplateLocation, component);

    }

    /**
     *  Returns a previously parsed template at the specified location (which must already
     *  be localized).  If not already in the template Map, then the
     *  location is parsed and stored into the templates Map, then returned.
     * 
     **/

    private ComponentTemplate getOrParseTemplate(IRequestCycle cycle, IResourceLocation location, IComponent component)
    {

        ComponentTemplate result = (ComponentTemplate) _templates.get(location);
        if (result != null)
            return result;

        // Ok, see if it exists.

        result = parseTemplate(cycle, location, component);

        if (result != null)
            _templates.put(location, result);

        return result;
    }

    /**
     *  Reads the template for the given resource; returns null if the
     *  resource doesn't exist.  Note that this method is only invoked
     *  from a synchronized block, so there shouldn't be threading
     *  issues here.
     *
     **/

    private ComponentTemplate parseTemplate(IRequestCycle cycle, IResourceLocation location, IComponent component)
    {
        char[] templateData = readTemplate(location);
        if (templateData == null)
            return null;

        return constructTemplateInstance(cycle, templateData, location.toString(), component);
    }

    /** 
     *  This method is currently synchronized, because
     *  {@link TemplateParser} is not threadsafe.  Another good candidate
     *  for a pooling mechanism, especially because parsing a template
     *  may take a while.
     * 
     **/

    private synchronized ComponentTemplate constructTemplateInstance(
        IRequestCycle cycle,
        char[] templateData,
        String location,
        IComponent component)
    {
        if (_parser == null)
            _parser = new TemplateParser();

        ITemplateParserDelegate delegate = new ParserDelegate(component, cycle);

        TemplateToken[] tokens;

        try
        {
            tokens = _parser.parse(templateData, delegate, location);
        }
        catch (TemplateParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultTemplateSource.unable-to-parse-template", location),
                ex);
        }

        if (LOG.isDebugEnabled())
            LOG.debug("Parsed " + tokens.length + " tokens from template");

        _tokenCount += tokens.length;

        return new ComponentTemplate(templateData, tokens);
    }

    /**
     *  Reads the template, given the complete path to the
     *  resource.  Returns null if the resource doesn't exist.
     *
     **/

    private char[] readTemplate(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Reading template " + location);

        URL url = location.getResourceURL();

        if (url == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Template does not exist.");

            return null;
        }

        if (LOG.isDebugEnabled())
            LOG.debug("Reading template from URL " + url);

        InputStream stream = null;

        try
        {
            stream = url.openStream();

            return readTemplateStream(stream);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("DefaultTemplateSource.unable-to-read-template", location),
                ex);
        }
        finally
        {
            Tapestry.close(stream);
        }

    }

    /**
     *  Reads a Stream into memory as an array of characters.
     *
     **/

    private char[] readTemplateStream(InputStream stream) throws IOException
    {
        char[] charBuffer = new char[BUFFER_SIZE];
        StringBuffer buffer = new StringBuffer();

        InputStreamReader reader = new InputStreamReader(new BufferedInputStream(stream));

        try
        {
            while (true)
            {
                int charsRead = reader.read(charBuffer, 0, BUFFER_SIZE);

                if (charsRead <= 0)
                    break;

                buffer.append(charBuffer, 0, charsRead);
            }
        }
        finally
        {
            reader.close();
        }

        // OK, now reuse the charBuffer variable to
        // produce the final result.

        int length = buffer.length();

        charBuffer = new char[length];

        // Copy the character out of the StringBuffer and into the
        // array.

        buffer.getChars(0, length, charBuffer, 0);

        return charBuffer;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("tokenCount", _tokenCount);

        builder.append("templates", _templates.keySet());

        return builder.toString();
    }

    /** @since 1.0.6 **/

    public synchronized void renderDescription(IMarkupWriter writer)
    {
        writer.print("DefaultTemplateSource[");

        if (_tokenCount > 0)
        {
            writer.print(_tokenCount);
            writer.print(" tokens");
        }

        if (_cache != null)
        {
            boolean first = true;
            Iterator i = _cache.entrySet().iterator();

            while (i.hasNext())
            {
                if (first)
                {
                    writer.begin("ul");
                    first = false;
                }

                Map.Entry e = (Map.Entry) i.next();
                Object key = e.getKey();
                ComponentTemplate template = (ComponentTemplate) e.getValue();

                writer.begin("li");
                writer.print(key.toString());
                writer.print(" (");
                writer.print(template.getTokenCount());
                writer.print(" tokens)");
                writer.println();
                writer.end();
            }

            if (!first)
            {
                writer.end(); // <ul>
                writer.beginEmpty("br");
            }
        }

        writer.print("]");

    }
}