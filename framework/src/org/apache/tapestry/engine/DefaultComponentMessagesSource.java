//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Messages;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.MultiKey;
import org.apache.tapestry.util.text.LocalizedProperties;

/**
 *  Global object (stored in the servlet context) that accesses
 *  localized properties for a component.
 *
 *  @author Howard Lewis Ship
 *  @since 2.0.4
 *
 **/

public class DefaultComponentMessagesSource implements IComponentMessagesSource
{
    private static final Log LOG = LogFactory.getLog(DefaultComponentMessagesSource.class);

    private Properties _emptyProperties = new Properties();

    /**
     *  The name of the component/application/etc property that will be used to
     *  determine the encoding to use when loading the messages
     * 
     **/ 
         
    public static final String MESSAGES_ENCODING_PROPERTY_NAME = "org.apache.tapestry.messages-encoding";
    
    /**
     *  Map of {@link Properties}, keyed on a {@link MultiKey} of
     *  component specification path and locale.
     * 
     **/

    private Map _cache = new HashMap();

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
            throw new IllegalArgumentException(
                Tapestry.format("invalid-null-parameter", "component"));

        Resource specificationLocation = component.getSpecification().getSpecificationLocation();
        Locale locale = component.getPage().getLocale();

        // Check to see if already in the cache

        MultiKey key = buildKey(specificationLocation, locale);

        Properties result = (Properties) _cache.get(key);

        if (result != null)
            return result;

        // Not found, create it now.

        result = assembleProperties(component, specificationLocation, locale);

        _cache.put(key, result);

        return result;
    }

    private static final String SUFFIX = ".properties";

    private Properties assembleProperties(IComponent component, Resource baseResourceLocation, Locale locale)
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
            parent = readProperties(component, baseResourceLocation, baseName, null, null);

            if (parent == null) {
                parent = _emptyProperties;
                _cache.put(baseResourceLocation, parent);
            }
        }

        Properties result = parent;

        if (!Tapestry.isBlank(language))
        {
            Locale l = new Locale(language, "");
            MultiKey key = buildKey(baseResourceLocation, l);

            result = (Properties) _cache.get(key);

            if (result == null) {
                result = readProperties(component, baseResourceLocation, baseName, l, parent);
                _cache.put(key, result);
            }

            parent = result;
        }
        else
            language = "";

        if (Tapestry.isNonBlank(country))
        {
            Locale l = new Locale(language, country);
            MultiKey key = buildKey(baseResourceLocation, l);

            result = (Properties) _cache.get(key);

            if (result == null) {
                result = readProperties(component, baseResourceLocation, baseName, l, parent);
                _cache.put(key, result);
            }

            parent = result;
        }
        else
            country = "";

        if (Tapestry.isNonBlank(variant))
        {
            Locale l = new Locale(language, country, variant);
            MultiKey key = buildKey(baseResourceLocation, l);

            result = (Properties) _cache.get(key);

            if (result == null) {
                result = readProperties(component, baseResourceLocation, baseName, l, parent);
                _cache.put(key, result);
            }
        }

        return result;
    }

    private MultiKey buildKey(Resource location, Locale locale)
    {
        return new MultiKey(new Object[] { location, locale.toString()}, false);
    }

    private Properties readProperties(
    	IComponent component,
        Resource baseLocation,
        String baseName,
        Locale locale,
        Properties parent)
    {
        StringBuffer buffer = new StringBuffer(baseName);

        if (locale != null)
        {
            buffer.append('_');
            buffer.append(locale.toString());
        }

        buffer.append(SUFFIX);

        Resource localized = baseLocation.getRelativeResource(buffer.toString());

        URL propertiesURL = localized.getResourceURL();

        if (propertiesURL == null)
            return parent;

        Properties result = null;

        if (parent == null)
            result = new Properties();
        else
            result = new Properties(parent);

        LocalizedProperties localizedResult = new LocalizedProperties(result);
        String encoding = getMessagesEncoding(component, locale);
        
        try
        {
            InputStream input = propertiesURL.openStream();

            if (encoding == null)
            	localizedResult.load(input);
            else
            	localizedResult.load(input, encoding);

            input.close();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ComponentPropertiesStore.unable-to-read-input", propertiesURL),
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

    public Messages getMessages(IComponent component)
    {
        return new ComponentMessages(
            component.getPage().getLocale(),
            getLocalizedProperties(component));
    }

    private String getMessagesEncoding(IComponent component, Locale locale)
    {
    	String encoding = null;

    	IRequestCycle cycle = component.getPage().getRequestCycle();
    	if (cycle != null) {
	    	IPropertySource applicationSource = cycle.getEngine().getPropertySource();
	        IPropertySource source = new DefaultComponentPropertySource(component, applicationSource, locale);

	        encoding = source.getPropertyValue(MESSAGES_ENCODING_PROPERTY_NAME);
	        if (encoding == null)
	        	encoding = source.getPropertyValue(DefaultTemplateSource.TEMPLATE_ENCODING_PROPERTY_NAME);
    	}
        
        return encoding;
    }

}