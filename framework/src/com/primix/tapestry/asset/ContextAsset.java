package com.primix.tapestry.asset;

import java.net.*;
import javax.servlet.*;
import java.io.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import java.util.*;
import org.log4j.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Internal asset; one that is visible to the servlet container directly.
 *  In retrospect, a better name for this would be <code>ContextAsset</code>
 *  since the path is relative to the {@link ServletContext} containing
 *  the application.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ContextAsset implements IAsset
{
	private static final Category CAT = 
		Category.getInstance(ContextAsset.class.getName());
		
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

    private static final int MAP_SIZE = 7;

    /**
     *  Map, keyed on Locale, value is an instance of Localization
     */

    private Map localizations;
    private String assetPath;

    public ContextAsset(String assetPath)
    {
        this.assetPath = assetPath;
    }

    /**
    *  Generates a URL for the client to retrieve the asset.  The context path
    *  is prepended to the asset path, which means that assets deployed inside
    *  web applications will still work (if things are configured properly).
    *
    */

    public String buildURL(IRequestCycle cycle)
    {
        Localization localization;
        
        try
        {
            localization = findLocalization(cycle);
        }
        catch (ResourceUnavailableException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        return localization.URL;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    throws ResourceUnavailableException
    {
        ServletContext context;
        URL url;
        Localization localization = findLocalization(cycle);

        context = cycle.getRequestContext().getServlet().getServletContext();

        try
        {
            url = context.getResource(localization.assetPath);

            return url.openStream();
        }
        catch (Exception ex)
        {
            throw new ResourceUnavailableException("Could not access internal asset " +
                assetPath + ".", ex);
        }
    }

    /**
    *  Poke around until we find the localized version of the asset.
    *
    *  <p>A lot of this is cut-and-paste from DefaultTemplateSource.  I haven't
    * come up with a good, general, efficient way to do this search without
    * a huge amount of mechanism.
    *
    */

    private Localization findLocalization(IRequestCycle cycle)
    throws ResourceUnavailableException
    {
        Locale locale = cycle.getPage().getLocale();
        int dotx;
        StringBuffer buffer;
        int rawLength;
        String candidatePath;
        String language = null;
        String country = null;
        int start = 2;
        String suffix;
        Localization result;

        if (localizations == null)
        {
            synchronized(this)
            {
                if (localizations == null)
                    localizations = new HashMap(MAP_SIZE);
            }
        }

        synchronized(localizations)
        {
            result = (Localization)localizations.get(locale);
            if (result != null)
                return result;
        }

		if (CAT.isDebugEnabled())
			CAT.debug("Searching for localization of context resource " + assetPath);

        dotx = assetPath.lastIndexOf('.');
        suffix = assetPath.substring(dotx);

        buffer = new StringBuffer (dotx + 30);

        buffer.append(assetPath.substring(0, dotx));
        rawLength = buffer.length();

        country = locale.getCountry();
        if (country.length() > 0)
            start--;

        // This assumes that you never have the case where there's
        // a null language code and a non-null country code.

        language = locale.getLanguage();
        if (language.length() > 0)
            start--;

        ServletContext context = cycle.getRequestContext().getServlet().getServletContext();

        // On pass #0, we use language code and country code
        // On pass #1, we use language code
        // On pass #2, we use neither.
        // We skip pass #0 or #1 depending on whether the language code
        // and/or country code is null.

        for (int i = start; i < 3; i++)
        {
            buffer.setLength(rawLength);

            if (i < 2)
            {
                buffer.append('_');
                buffer.append(language);
            }

            if (i == 0)
            {
                buffer.append('_');
                buffer.append(country);
            }

            buffer.append(suffix);

            candidatePath = buffer.toString();

            try
            {
                if (context.getResource(candidatePath) != null)
                {
                    result = new Localization(candidatePath,
                                cycle.getEngine().getContextPath() + candidatePath);

                    synchronized(localizations)
                    {
                        localizations.put(locale, result);
                    }

                    if (CAT.isDebugEnabled())
                    	CAT.debug("Found " + assetPath);
            

                    return result;
                }
            }
            catch (MalformedURLException ex)
            {
                // We just ignore this!
            }

        }

        throw new ResourceUnavailableException
            ("Could not find internal asset " +
                assetPath + " for locale " + locale + ".");

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ContextAsset[");
        buffer.append(assetPath);

        buffer.append(']');

        return buffer.toString();
    }
}

