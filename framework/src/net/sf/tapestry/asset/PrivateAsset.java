/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.asset;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.LocalizedResourceFinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  An implementation of {@link IAsset} for localizable assets within
 *  the JVM's classpath.
 *
 *  <p>The localization code here is largely cut-and-paste from
 *  {@link ContextAsset}.
 *
 *  @author Howard Ship
 *  @version $Id$
 * 
 **/

public class PrivateAsset implements IAsset
{
    private static final Log LOG = LogFactory.getLog(PrivateAsset.class);

    private AssetExternalizer _externalizer;

    private String _resourcePath;

    /**
     *  Map, keyed on Locale, value is the localized resourcePath (as a String)
     **/

    private Map _localizations;

    public PrivateAsset(String resourcePath)
    {
        _resourcePath = resourcePath;
    }

    /**
     *  Gets the localized version of the resource.  Build
     *  the URL for the resource.  If possible, the application's
     *  {@link AssetExternalizer} is located, to copy the resource to
     *  a directory visible to the web server.
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        String localizedResourcePath = findLocalization(cycle, cycle.getPage().getLocale());

        if (_externalizer == null)
            _externalizer = AssetExternalizer.get(cycle);

        String externalURL = _externalizer.getURL(localizedResourcePath);

        if (externalURL != null)
            return externalURL;

        // Otherwise, the service is responsible for dynamically retrieving the
        // resource.

        String[] parameters = new String[] { localizedResourcePath };

        IEngineService service = cycle.getEngine().getService(IEngineService.ASSET_SERVICE);

        Gesture g = service.buildGesture(cycle, null, parameters);

        return g.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        return getResourceAsStream(cycle, cycle.getPage().getLocale());
    }

    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale)
    {
        try
        {
            IResourceResolver resolver = cycle.getEngine().getResourceResolver();

            URL url = resolver.getResource(findLocalization(cycle, locale));

            return url.openStream();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("PrivateAsset.resource-missing", _resourcePath),
                ex);
        }
    }

    /**
     *  Poke around until we find the localized version of the asset.
     *
     *  <p>A lot of this is cut-and-paste from DefaultTemplateSource.  I haven't
     *  come up with a good, general, efficient way to do this search without
     *  a huge amount of mechanism.
     *
     **/

    private synchronized String findLocalization(IRequestCycle cycle, Locale locale)
    {
        if (_localizations == null)
            _localizations = new HashMap();

        String result = (String) _localizations.get(locale);
        if (result != null)
            return result;

        if (LOG.isDebugEnabled())
            LOG.debug(
                "Searching for localization of private asset "
                    + _resourcePath
                    + " in locale "
                    + locale.getDisplayName());

        IResourceResolver resolver = cycle.getEngine().getResourceResolver();

        LocalizedResourceFinder finder = new LocalizedResourceFinder(resolver);

        String localizedPath = finder.resolve(_resourcePath, locale);

        if (localizedPath == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("PrivateAsset.resource-unavailable", _resourcePath, locale));

        _localizations.put(locale, localizedPath);

        if (LOG.isDebugEnabled())
            LOG.debug("Found " + localizedPath);

        return localizedPath;
    }

    public String toString()
    {
        return "PrivateAsset[" + _resourcePath + "]";
    }

}