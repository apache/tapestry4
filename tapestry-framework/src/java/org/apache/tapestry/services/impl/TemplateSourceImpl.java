// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.services.impl;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.*;
import org.apache.tapestry.engine.ITemplateSourceDelegate;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.l10n.ResourceLocalizer;
import org.apache.tapestry.parse.*;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.resolver.IComponentResourceResolver;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.services.TemplateSource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.MultiKey;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of {@link org.apache.tapestry.services.TemplateSource}. Templates, once parsed,
 * stay in memory until explicitly cleared.
 * 
 * @author Howard Lewis Ship
 */

public class TemplateSourceImpl implements TemplateSource, ResetEventListener, ReportStatusListener
{

    // The name of the component/application/etc property that will be used to
    // determine the encoding to use when loading the template

    public static final String TEMPLATE_ENCODING_PROPERTY_NAME = "org.apache.tapestry.template-encoding";

    private static final int BUFFER_SIZE = 2000;

    private String _serviceId;

    private Log _log;
    
    // Cache of previously retrieved templates. Key is a multi-key of
    // specification resource path and locale (local may be null), value
    // is the ComponentTemplate.

    private Map _cache = new ConcurrentHashMap();

    // Previously read templates; key is the Resource, value
    // is the ComponentTemplate.

    private Map _templates = new ConcurrentHashMap();

    private ITemplateParser _parser;

    /** @since 2.2 */

    private Resource _contextRoot;

    /** @since 3.0 */

    private ITemplateSourceDelegate _delegate;

    /** @since 4.0 */

    private ComponentSpecificationResolver _componentSpecificationResolver;

    /** @since 4.0 */

    private ComponentPropertySource _componentPropertySource;

    /** @since 4.0 */

    private ResourceLocalizer _localizer;

    /** @since 4.1.2 */
    
    private IComponentResourceResolver _resourceResolver;

    /**
     * Clears the template cache. This is used during debugging.
     */

    public void resetEventDidOccur()
    {
        _cache.clear();
        _templates.clear();
    }

    public void reportStatus(ReportStatusEvent event)
    {
        event.title(_serviceId);

        int templateCount = 0;
        int tokenCount = 0;
        int characterCount = 0;

        Iterator i = _templates.values().iterator();

        while (i.hasNext())
        {
            ComponentTemplate template = (ComponentTemplate) i.next();

            templateCount++;

            int count = template.getTokenCount();

            tokenCount += count;

            for (int j = 0; j < count; j++)
            {
                TemplateToken token = template.getToken(j);

                if (token.getType() == TokenType.TEXT)
                {
                    TextToken tt = (TextToken) token;

                    characterCount += tt.getLength();
                }
            }
        }

        event.property("parsed templates", templateCount);
        event.property("total template tokens", tokenCount);
        event.property("total template characters", characterCount);

        event.section("Parsed template token counts");

        i = _templates.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            String key = entry.getKey().toString();

            ComponentTemplate template = (ComponentTemplate) entry.getValue();

            event.property(key, template.getTokenCount());
        }
    }

    /**
     * Reads the template for the component.
     */

    public ComponentTemplate getTemplate(IRequestCycle cycle, IComponent component)
    {
        IComponentSpecification specification = component.getSpecification();
        Resource resource = specification.getSpecificationLocation();

        Locale locale = component.getPage().getLocale();

        Object key = new MultiKey(new Object[] { resource, locale }, false);

        ComponentTemplate result = searchCache(key);
        if (result != null)
            return result;

        result = findTemplate(cycle, resource, component, locale);

        if (result == null)
        {
            result = _delegate.findTemplate(cycle, component, locale);

            if (result != null)
                return result;

            String message = component.getSpecification().isPageSpecification() ? ImplMessages
                    .noTemplateForPage(component.getExtendedId(), locale) : ImplMessages
                    .noTemplateForComponent(component.getExtendedId(), locale);

            throw new ApplicationRuntimeException(message, component, component.getLocation(), null);
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
     * Finds the template for the given component, using the following rules:
     * <ul>
     * <li>If the component has a $template asset, use that
     * <li>Look for a template in the same folder as the component
     * <li>If a page in the application namespace, search in the application root
     * <li>Fail!
     * </ul>
     * 
     * @return the template, or null if not found
     */

    private ComponentTemplate findTemplate(IRequestCycle cycle, Resource resource,
            IComponent component, Locale locale)
    {
        IAsset templateAsset = component.getAsset(TEMPLATE_ASSET_NAME);

        if (templateAsset != null && templateAsset.getResourceLocation() != null && templateAsset.getResourceLocation().getResourceURL() != null)
            return readTemplateFromAsset(cycle, component, templateAsset.getResourceLocation());
        
        String name = resource.getName();
        int dotx = name.lastIndexOf('.');
        String templateExtension = getTemplateExtension(component);
        String templateBaseName = name.substring(0, dotx + 1) + templateExtension;

        ComponentTemplate result = findStandardTemplate(
                cycle,
                resource,
                component,
                templateBaseName,
                locale);

        if (result == null && component.getSpecification().isPageSpecification()
                && component.getNamespace().isApplicationNamespace())
            result = findPageTemplateInApplicationRoot(
                    cycle,
                    (IPage) component,
                    templateExtension,
                    locale);

        if (result == null) {

            Resource template = _resourceResolver.findComponentResource(component, cycle, null, "." + templateExtension, locale);
            
            if (template != null && template.getResourceURL() != null)
                return readTemplateFromAsset(cycle, component, template);
        }

        return result;
    }

    private ComponentTemplate findPageTemplateInApplicationRoot(IRequestCycle cycle, IPage page,
            String templateExtension, Locale locale)
    {
        // Note: a subtle change from release 3.0 to 4.0.
        // In release 3.0, you could use a <page> element to define a page named Foo whose
        // specification was Bar.page. We would then search for /Bar.page. Confusing? Yes.
        // In 4.0, we are more reliant on the page name, which may include a folder prefix (i.e.,
        // "admin/EditUser", so when we search it is based on the page name and not the
        // specification resource file name. We would search for Foo.html. Moral of the
        // story is to use the page name for the page specifiation and the template.

        String templateBaseName = page.getPageName() + "." + templateExtension;

        if (_log.isDebugEnabled())
            _log.debug("Checking for " + templateBaseName + " in application root");

        Resource baseLocation = _contextRoot.getRelativeResource(templateBaseName);
        Resource localizedLocation = _localizer.findLocalization(baseLocation, locale);

        if (localizedLocation == null)
            return null;

        return getOrParseTemplate(cycle, localizedLocation, page);
    }



    /**
     * Reads an asset to get the template.
     */

    private ComponentTemplate readTemplateFromAsset(IRequestCycle cycle, IComponent component,
            Resource asset)
    {
        InputStream stream = null;

        char[] templateData = null;

        try
        {
            stream = asset.getResourceURL().openStream();

            String encoding = getTemplateEncoding(component, null);

            templateData = readTemplateStream(stream, encoding);

            stream.close();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToReadTemplate(asset), ex);
        }

        return constructTemplateInstance(cycle, templateData, asset, component);
    }

    /**
     * Search for the template corresponding to the resource and the locale. This may be in the
     * template map already, or may involve reading and parsing the template.
     * 
     * @return the template, or null if not found.
     */

    private ComponentTemplate findStandardTemplate(IRequestCycle cycle, Resource resource,
            IComponent component, String templateBaseName, Locale locale)
    {
        if (_log.isDebugEnabled())
            _log.debug("Searching for localized version of template for " + resource
                    + " in locale " + locale.getDisplayName());

        Resource baseTemplateLocation = resource.getRelativeResource(templateBaseName);
        Resource localizedTemplateLocation = _localizer.findLocalization(baseTemplateLocation, locale);

        if (localizedTemplateLocation == null)
            return null;

        return getOrParseTemplate(cycle, localizedTemplateLocation, component);

    }

    /**
     * Returns a previously parsed template at the specified location (which must already be
     * localized). If not already in the template Map, then the location is parsed and stored into
     * the templates Map, then returned.
     */

    private ComponentTemplate getOrParseTemplate(IRequestCycle cycle, Resource resource,
            IComponent component)
    {

        ComponentTemplate result = (ComponentTemplate) _templates.get(resource);
        if (result != null)
            return result;

        // Ok, see if it exists.

        result = parseTemplate(cycle, resource, component);

        if (result != null)
            _templates.put(resource, result);

        return result;
    }

    /**
     * Reads the template for the given resource; returns null if the resource doesn't exist. Note
     * that this method is only invoked from a synchronized block, so there shouldn't be threading
     * issues here.
     */

    private ComponentTemplate parseTemplate(IRequestCycle cycle, Resource resource,
            IComponent component)
    {
        String encoding = getTemplateEncoding(component, resource.getLocale());

        char[] templateData = readTemplate(resource, encoding);
        if (templateData == null)
            return null;

        return constructTemplateInstance(cycle, templateData, resource, component);
    }

    /**
     * This method is currently synchronized, because {@link org.apache.tapestry.parse.TemplateParser} is not threadsafe.
     * Another good candidate for a pooling mechanism, especially because parsing a template may
     * take a while.
     */

    private synchronized ComponentTemplate constructTemplateInstance(IRequestCycle cycle,
            char[] templateData, Resource resource, IComponent component)
    {
        String componentAttributeName = _componentPropertySource.getComponentProperty(
                component,
                "org.apache.tapestry.jwcid-attribute-name");

        ITemplateParserDelegate delegate = new DefaultParserDelegate(component,
                componentAttributeName, cycle, _componentSpecificationResolver);

        TemplateToken[] tokens;

        try
        {
            tokens = _parser.parse(templateData, delegate, resource);
        }
        catch (TemplateParseException ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToParseTemplate(resource), ex);
        }

        if (_log.isDebugEnabled())
            _log.debug("Parsed " + tokens.length + " tokens from template");

        return new ComponentTemplate(templateData, tokens);
    }

    /**
     * Reads the template, given the complete path to the resource. Returns null if the resource
     * doesn't exist.
     */

    private char[] readTemplate(Resource resource, String encoding)
    {
        if (_log.isDebugEnabled())
            _log.debug("Reading template " + resource);

        URL url = resource.getResourceURL();

        if (url == null)
        {
            if (_log.isDebugEnabled())
                _log.debug("Template does not exist.");

            return null;
        }

        if (_log.isDebugEnabled())
            _log.debug("Reading template from URL " + url);

        InputStream stream = null;

        try
        {
            stream = url.openStream();

            return readTemplateStream(stream, encoding);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToReadTemplate(resource), ex);
        }
        finally
        {
            Tapestry.close(stream);
        }

    }

    /**
     * Reads a Stream into memory as an array of characters.
     */

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

    /**
     * Checks for the {@link Tapestry#TEMPLATE_EXTENSION_PROPERTY}in the component's specification,
     * then in the component's namespace's specification. Returns
     * {@link Tapestry#TEMPLATE_EXTENSION_PROPERTY} if not otherwise overriden.
     */

    private String getTemplateExtension(IComponent component)
    {
        return _componentPropertySource.getComponentProperty(
                component,
                Tapestry.TEMPLATE_EXTENSION_PROPERTY);
    }

    private String getTemplateEncoding(IComponent component, Locale locale)
    {
        return _componentPropertySource.getLocalizedComponentProperty(
                component,
                locale,
                TEMPLATE_ENCODING_PROPERTY_NAME);
    }

    /** @since 4.0 */

    public void setParser(ITemplateParser parser)
    {
        _parser = parser;
    }

    /** @since 4.0 */

    public void setLog(Log log)
    {
        _log = log;
    }

    /** @since 4.0 */

    public void setDelegate(ITemplateSourceDelegate delegate)
    {
        _delegate = delegate;
    }

    /** @since 4.0 */

    public void setComponentSpecificationResolver(ComponentSpecificationResolver resolver)
    {
        _componentSpecificationResolver = resolver;
    }

    /** @since 4.0 */
    public void setContextRoot(Resource contextRoot)
    {
        _contextRoot = contextRoot;
    }

    /** @since 4.0 */
    public void setComponentPropertySource(ComponentPropertySource componentPropertySource)
    {
        _componentPropertySource = componentPropertySource;
    }

    /** @since 4.0 */
    public void setServiceId(String serviceId)
    {
        _serviceId = serviceId;
    }

    /** @since 4.0 */
    public void setLocalizer(ResourceLocalizer localizer)
    {
        _localizer = localizer;
    }

    public void setComponentResourceResolver(IComponentResourceResolver resourceResolver)
    {
        _resourceResolver = resourceResolver;
    }
}
