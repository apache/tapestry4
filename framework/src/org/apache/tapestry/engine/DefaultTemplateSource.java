/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

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
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.parse.ComponentTemplate;
import org.apache.tapestry.parse.ITemplateParserDelegate;
import org.apache.tapestry.parse.TemplateParseException;
import org.apache.tapestry.parse.TemplateParser;
import org.apache.tapestry.parse.TemplateToken;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.DelegatingPropertySource;
import org.apache.tapestry.util.IRenderDescription;
import org.apache.tapestry.util.LocalizedPropertySource;
import org.apache.tapestry.util.MultiKey;
import org.apache.tapestry.util.PropertyHolderPropertySource;

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


    // The name of the component/application/etc property that will be used to
    // determine the encoding to use when loading the template
         
    private static final String TEMPLATE_ENCODING_PROPERTY_NAME = "org.apache.tapestry.template-encoding"; 

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

    private TemplateParser _parser;

    /** @since 2.2 **/

    private IResourceLocation _applicationRootLocation;

    /** @since 3.0 **/

    private ITemplateSourceDelegate _delegate;

    private static class ParserDelegate implements ITemplateParserDelegate
    {
        private IComponent _component;
        private ComponentSpecificationResolver _resolver;
        private IRequestCycle _cycle;

        ParserDelegate(IComponent component, IRequestCycle cycle)
        {
            _component = component;
            _resolver = new ComponentSpecificationResolver(cycle);
            _cycle = cycle;
        }

        public boolean getKnownComponent(String componentId)
        {
            return _component.getSpecification().getComponent(componentId) != null;
        }

        public boolean getAllowBody(String componentId, ILocation location)
        {
            IComponent embedded = _component.getComponent(componentId);

            if (embedded == null)
                throw Tapestry.createNoSuchComponentException(_component, componentId, location);

            return embedded.getSpecification().getAllowBody();
        }

        public boolean getAllowBody(String libraryId, String type, ILocation location)
        {
            INamespace namespace = _component.getNamespace();

            _resolver.resolve(_cycle, namespace, libraryId, type, location);

            IComponentSpecification spec = _resolver.getSpecification();

            return spec.getAllowBody();
        }

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
        IComponentSpecification specification = component.getSpecification();
        IResourceLocation specificationLocation = specification.getSpecificationLocation();

        Locale locale = component.getPage().getLocale();

        Object key = new MultiKey(new Object[] { specificationLocation, locale }, false);

        ComponentTemplate result = searchCache(key);
        if (result != null)
            return result;

        result = findTemplate(cycle, specificationLocation, component, locale);

        if (result == null)
        {
            result = getTemplateFromDelegate(cycle, component, locale);

            if (result != null)
                return result;

            String stringKey =
                component.getSpecification().isPageSpecification()
                    ? "DefaultTemplateSource.no-template-for-page"
                    : "DefaultTemplateSource.no-template-for-component";

            throw new ApplicationRuntimeException(
                Tapestry.format(stringKey, component.getExtendedId(), locale),
                component,
                component.getLocation(),
                null);
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

    private ComponentTemplate getTemplateFromDelegate(
        IRequestCycle cycle,
        IComponent component,
        Locale locale)
    {
        if (_delegate == null)
        {
            IEngine engine = cycle.getEngine();
            IApplicationSpecification spec = engine.getSpecification();

            if (spec.checkExtension(Tapestry.TEMPLATE_SOURCE_DELEGATE_EXTENSION_NAME))
                _delegate =
                    (ITemplateSourceDelegate) spec.getExtension(
                        Tapestry.TEMPLATE_SOURCE_DELEGATE_EXTENSION_NAME,
                        ITemplateSourceDelegate.class);
            else
                _delegate = NullTemplateSourceDelegate.getSharedInstance();

        }

        return _delegate.findTemplate(cycle, component, locale);
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
     *  @return the template, or null if not found
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
            return readTemplateFromAsset(cycle, component, templateAsset);

        String name = location.getName();
        int dotx = name.lastIndexOf('.');
        String templateBaseName = name.substring(0, dotx + 1) + getTemplateExtension(component);

        ComponentTemplate result =
            findStandardTemplate(cycle, location, component, templateBaseName, locale);

        if (result == null
            && component.getSpecification().isPageSpecification()
            && component.getNamespace().isApplicationNamespace())
            result = findPageTemplateInApplicationRoot(cycle, component, templateBaseName, locale);

        return result;
    }

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

        IResourceLocation baseLocation =
            _applicationRootLocation.getRelativeLocation(templateBaseName);
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
        IAsset asset)
    {
        InputStream stream = asset.getResourceAsStream(cycle);

        char[] templateData = null;

        try
        {
            String encoding = getTemplateEncoding(cycle, component, null);
            
            templateData = readTemplateStream(stream, encoding);

            stream.close();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("DefaultTemplateSource.unable-to-read-template", asset),
                ex);
        }

        IResourceLocation resourceLocation = asset.getResourceLocation();

        return constructTemplateInstance(cycle, templateData, resourceLocation, component);
    }

    /**
     *  Search for the template corresponding to the resource and the locale.
     *  This may be in the template map already, or may involve reading and
     *  parsing the template.
     *
     *  @return the template, or null if not found.
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

    private ComponentTemplate getOrParseTemplate(
        IRequestCycle cycle,
        IResourceLocation location,
        IComponent component)
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

    private ComponentTemplate parseTemplate(
        IRequestCycle cycle,
        IResourceLocation location,
        IComponent component)
    {
        String encoding = getTemplateEncoding(cycle, component, location.getLocale());
        
        char[] templateData = readTemplate(location, encoding);
        if (templateData == null)
            return null;

        return constructTemplateInstance(cycle, templateData, location, component);
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
        IResourceLocation location,
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
                Tapestry.format("DefaultTemplateSource.unable-to-parse-template", location),
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

    private char[] readTemplate(IResourceLocation location, String encoding)
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

            return readTemplateStream(stream, encoding);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("DefaultTemplateSource.unable-to-read-template", location),
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

    private char[] readTemplateStream(InputStream stream, String encoding) throws IOException
    {
        char[] charBuffer = new char[BUFFER_SIZE];
        StringBuffer buffer = new StringBuffer();

        InputStreamReader reader;
        if (encoding != null)
            reader = new InputStreamReader(new BufferedInputStream(stream), encoding);
        else
            reader = new InputStreamReader(new BufferedInputStream(stream));

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

    /**
     *  Checks for the {@link Tapestry#TEMPLATE_EXTENSION_PROPERTY} in the component's
     *  specification, then in the component's namespace's specification.  Returns
     *  {@link Tapestry#DEFAULT_TEMPLATE_EXTENSION} if not otherwise overriden.
     * 
     **/

    private String getTemplateExtension(IComponent component)
    {
        String extension =
            component.getSpecification().getProperty(Tapestry.TEMPLATE_EXTENSION_PROPERTY);

        if (extension != null)
            return extension;

        extension =
            component.getNamespace().getSpecification().getProperty(
                Tapestry.TEMPLATE_EXTENSION_PROPERTY);

        if (extension != null)
            return extension;

        return Tapestry.DEFAULT_TEMPLATE_EXTENSION;
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
    
    private String getTemplateEncoding(IRequestCycle cycle, IComponent component, Locale locale)
    {
        IPropertySource source = getComponentPropertySource(cycle, component);

        if (locale != null)
            source = new LocalizedPropertySource(locale, source);

        return getTemplateEncodingProperty(source);
    }
    
    private IPropertySource getComponentPropertySource(IRequestCycle cycle, IComponent component)
    {
        DelegatingPropertySource source = new DelegatingPropertySource();

        // Search for the encoding property in the following order:
        // First search the component specification
        source.addSource(new PropertyHolderPropertySource(component.getSpecification()));

        // Then search its library specification
        source.addSource(new PropertyHolderPropertySource(component.getNamespace().getSpecification()));

        // Then search the rest of the standard path
        source.addSource(cycle.getEngine().getPropertySource());
        
        return source;
    }
    
    private String getTemplateEncodingProperty(IPropertySource source)
    {
        return source.getPropertyValue(TEMPLATE_ENCODING_PROPERTY_NAME);
    }
    
}
