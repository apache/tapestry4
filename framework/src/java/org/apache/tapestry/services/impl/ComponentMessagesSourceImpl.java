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
import java.util.HashMap;
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
import org.apache.tapestry.engine.DefaultComponentPropertySource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ComponentMessagesSource;
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

    private IPropertySource _applicationPropertySource;

    public void setApplicationPropertySource(IPropertySource source)
    {
        _applicationPropertySource = source;
    }

    /**
     * Returns an instance of {@link Properties}containing the properly localized messages for the
     * component, in the {@link Locale}identified by the component's containing page.
     */

    protected synchronized Properties getLocalizedProperties(IComponent component)
    {
        Defense.notNull(component, "component");

        Resource specificationLocation = component.getSpecification().getSpecificationLocation();
        Locale locale = component.getPage().getLocale();

        Map propertiesMap = (Map) _componentCache.get(specificationLocation);
        if (propertiesMap == null)
        {
            propertiesMap = new HashMap();
            _componentCache.put(specificationLocation, propertiesMap);
        }

        Properties result = (Properties) propertiesMap.get(locale);

        if (result != null)
            return result;

        // Not found, create it now.

        result = assembleProperties(component, specificationLocation, propertiesMap, locale);

        propertiesMap.put(locale, result);

        return result;
    }

    private Properties assembleProperties(IComponent component, Resource baseResourceLocation,
            Map propertiesMap, Locale locale)
    {
        List locales = new ArrayList();
        List resources = new ArrayList();

        String fileName = baseResourceLocation.getName();
        int dotx = fileName.lastIndexOf('.');
        String baseName = fileName.substring(0, dotx);

        LocalizedNameGenerator g = new LocalizedNameGenerator(baseName, locale, SUFFIX);

        while (g.more())
        {
            String localizedName = g.next();
            Locale l = g.getCurrentLocale();

            locales.add(l);
            resources.add(baseResourceLocation.getRelativeResource(localizedName));
        }

        // Build them back up in reverse order.

        Properties parent = _emptyProperties;
        int count = locales.size();
        for (int i = count - 1; i >= 0; i--)
        {
            Locale l = (Locale) locales.get(i);
            Properties properties = (Properties) propertiesMap.get(locale);

            if (properties == null)
            {
                Resource propertiesResource = (Resource) resources.get(i);

                properties = readProperties(component, l, propertiesResource, parent);

                propertiesMap.put(l, properties);
            }

            parent = properties;
        }

        return parent;
    }

    private Properties readProperties(IComponent component, Locale locale,
            Resource propertiesResource, Properties parent)
    {
        URL resourceURL = propertiesResource.getResourceURL();

        if (resourceURL == null)
            return parent;

        String encoding = getMessagesEncoding(component, locale);

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

    private String getMessagesEncoding(IComponent component, Locale locale)
    {
        IPropertySource source = new DefaultComponentPropertySource(component,
                _applicationPropertySource, locale);

        String encoding = source.getPropertyValue(MESSAGES_ENCODING_PROPERTY_NAME);

        if (encoding == null)
            encoding = source.getPropertyValue(TemplateSourceImpl.TEMPLATE_ENCODING_PROPERTY_NAME);

        return encoding;
    }

}