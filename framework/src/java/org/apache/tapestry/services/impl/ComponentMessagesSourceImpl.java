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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Messages;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.LocalizedNameGenerator;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.engine.DefaultNamespacePropertySource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ComponentMessagesSource;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.util.text.LocalizedProperties;

/**
 * Service used to access localized properties for a component.
 * 
 * @author Howard Lewis Ship
 * @since 2.0.4
 */

public class ComponentMessagesSourceImpl implements ComponentMessagesSource, ResetEventListener
{
    private Properties _emptyProperties = new Properties();

    private static final String SUFFIX = ".properties";

    /**
     * The name of the component/application/etc property that will be used to determine the
     * encoding to use when loading the messages
     */

    public static final String MESSAGES_ENCODING_PROPERTY_NAME = "org.apache.tapestry.messages-encoding";

    /**
     * Map of Maps. The outer map is keyed on component specification location (a{@link Resource}.
     * This inner map is keyed on locale and the value is a {@link Properties}.
     */

    private Map _componentCache = new HashMap();

    private IPropertySource _globalPropertySource;

    private ComponentPropertySource _componentPropertySource;

    /**
     * Returns an instance of {@link Properties}containing the properly localized messages for the
     * component, in the {@link Locale}identified by the component's containing page.
     */

    protected synchronized Properties getLocalizedProperties(IComponent component)
    {
        Defense.notNull(component, "component");

        Resource specificationLocation = component.getSpecification().getSpecificationLocation();
        Locale locale = component.getPage().getLocale();

        Map propertiesMap = findPropertiesMapForResource(specificationLocation);

        Properties result = (Properties) propertiesMap.get(locale);

        if (result == null)
        {

            // Not found, create it now.

            result = assembleComponentProperties(
                    component,
                    specificationLocation,
                    propertiesMap,
                    locale);

            propertiesMap.put(locale, result);
        }

        return result;
    }

    private Map findPropertiesMapForResource(Resource resource)
    {
        Map result = (Map) _componentCache.get(resource);

        if (result == null)
        {
            result = new HashMap();
            _componentCache.put(resource, result);
        }

        return result;
    }

    private Properties getNamespaceProperties(IComponent component, Locale locale)
    {
        INamespace namespace = component.getNamespace();

        Resource namespaceLocation = namespace.getSpecificationLocation();

        Map propertiesMap = findPropertiesMapForResource(namespaceLocation);

        Properties result = (Properties) propertiesMap.get(locale);

        if (result == null)
        {
            result = assembleNamespaceProperties(namespace, propertiesMap, locale);

            propertiesMap.put(locale, result);
        }

        return result;
    }

    private Properties assembleComponentProperties(IComponent component,
            Resource baseResourceLocation, Map propertiesMap, Locale locale)
    {
        List localizations = findLocalizationsForResource(baseResourceLocation, locale);

        Properties parent = getNamespaceProperties(component, locale);

        Iterator i = localizations.iterator();

        while (i.hasNext())
        {
            ResourceLocalization rl = (ResourceLocalization) i.next();

            Locale l = rl.getLocale();

            Properties properties = (Properties) propertiesMap.get(l);

            if (properties == null)
            {
                properties = readComponentProperties(component, l, rl.getResource(), parent);

                propertiesMap.put(l, properties);
            }

            parent = properties;
        }

        return parent;
    }

    private Properties assembleNamespaceProperties(INamespace namespace, Map propertiesMap,
            Locale locale)
    {
        List localizations = findLocalizationsForResource(
                namespace.getSpecificationLocation(),
                locale);

        // Build them back up in reverse order.

        Properties parent = _emptyProperties;

        Iterator i = localizations.iterator();

        while (i.hasNext())
        {
            ResourceLocalization rl = (ResourceLocalization) i.next();

            Locale l = rl.getLocale();

            Properties properties = (Properties) propertiesMap.get(l);

            if (properties == null)
            {
                properties = readNamespaceProperties(namespace, l, rl.getResource(), parent);

                propertiesMap.put(l, properties);
            }

            parent = properties;
        }

        return parent;

    }

    /**
     * Finds the localizations of the provided resource. Returns a List of
     * {@link ResourceLocalization}(each pairing a locale with a localized resource). The list is
     * ordered from most general (i.e., "foo.properties") to most specific (i.e.,
     * "foo_en_US_yokel.properties").
     */

    private List findLocalizationsForResource(Resource resource, Locale locale)
    {
        List result = new ArrayList();

        String baseName = extractBaseName(resource);

        LocalizedNameGenerator g = new LocalizedNameGenerator(baseName, locale, SUFFIX);

        while (g.more())
        {
            String localizedName = g.next();
            Locale l = g.getCurrentLocale();
            Resource localizedResource = resource.getRelativeResource(localizedName);

            result.add(new ResourceLocalization(l, localizedResource));
        }

        Collections.reverse(result);

        return result;
    }

    private String extractBaseName(Resource baseResourceLocation)
    {
        String fileName = baseResourceLocation.getName();
        int dotx = fileName.lastIndexOf('.');

        return fileName.substring(0, dotx);
    }

    private Properties readComponentProperties(IComponent component, Locale locale,
            Resource propertiesResource, Properties parent)
    {
        String encoding = getComponentMessagesEncoding(component, locale);

        return readPropertiesResource(propertiesResource.getResourceURL(), encoding, parent);
    }

    private Properties readNamespaceProperties(INamespace namespace, Locale locale,
            Resource propertiesResource, Properties parent)
    {
        String encoding = getNamespaceMessagesEncoding(namespace, locale);

        return readPropertiesResource(propertiesResource.getResourceURL(), encoding, parent);
    }

    private Properties readPropertiesResource(URL resourceURL, String encoding, Properties parent)
    {
        if (resourceURL == null)
            return parent;

        Properties result = new Properties(parent);

        LocalizedProperties wrapper = new LocalizedProperties(result);

        InputStream input = null;

        try
        {
            input = new BufferedInputStream(resourceURL.openStream());

            if (encoding == null)
                wrapper.load(input);
            else
                wrapper.load(input, encoding);

            input.close();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ImplMessages.unableToLoadProperties(
                    resourceURL,
                    ex), ex);
        }
        finally
        {
            close(input);
        }

        return result;
    }

    private void close(InputStream is)
    {
        if (is != null)
            try
            {
                is.close();
            }
            catch (IOException ex)
            {
                // Ignore.
            }
    }

    /**
     * Clears the cache of read properties files.
     */

    public synchronized void resetEventDidOccur()
    {
        _componentCache.clear();
    }

    public Messages getMessages(IComponent component)
    {
        return new ComponentMessages(component.getPage().getLocale(),
                getLocalizedProperties(component));
    }

    private String getComponentMessagesEncoding(IComponent component, Locale locale)
    {
        String encoding = _componentPropertySource.getLocalizedComponentProperty(
                component,
                locale,
                MESSAGES_ENCODING_PROPERTY_NAME);

        if (encoding == null)
            encoding = _componentPropertySource.getLocalizedComponentProperty(
                    component,
                    locale,
                    TemplateSourceImpl.TEMPLATE_ENCODING_PROPERTY_NAME);

        return encoding;
    }

    private String getNamespaceMessagesEncoding(INamespace namespace, Locale locale)
    {
        IPropertySource source = new DefaultNamespacePropertySource(namespace,
                _globalPropertySource, locale);

        return source.getPropertyValue(MESSAGES_ENCODING_PROPERTY_NAME);
    }

    public void setGlobalPropertySource(IPropertySource source)
    {
        _globalPropertySource = source;
    }

    public void setComponentPropertySource(ComponentPropertySource componentPropertySource)
    {
        _componentPropertySource = componentPropertySource;
    }
}