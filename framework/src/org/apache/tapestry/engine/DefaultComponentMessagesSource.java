/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMessages;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.MultiKey;

/**
 *  Global object (stored in the servlet context) that accesses
 *  localized properties for a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class DefaultComponentMessagesSource implements IComponentMessagesSource
{
    private static final Log LOG = LogFactory.getLog(DefaultComponentMessagesSource.class);

    private Properties _emptyProperties = new Properties();

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

        IResourceLocation specificationLocation =
            component.getSpecification().getSpecificationLocation();
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

    private Properties readProperties(
        IResourceLocation baseLocation,
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

    public IMessages getMessages(IComponent component)
    {
        return new ComponentMessages(getLocalizedProperties(component));
    }

}