/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.asset;

import java.net.URL;
import com.primix.tapestry.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;

/**
 *  An implementation of {@link IAsset} for localizable assets within
 *  the JVM's classpath.
 *
 *  <p>The localization code here is largely cut-and-paste from 
 *  {@link ContextAsset}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class PrivateAsset implements IAsset
{
	private static final Category CAT = Category.getInstance(PrivateAsset.class);

	private AssetExternalizer externalizer;

	private String resourcePath;

	private static final int MAP_SIZE = 7;

	/**
	*  Map, keyed on Locale, value is the localized resourcePath (as a String)
	*/

	private Map localizations;

	public PrivateAsset(String resourcePath)
	
	{
		this.resourcePath = resourcePath;
	}

	/**
	*  Gets the localized version of the resource.  Build
	*  the URL for the resource.  If possible, the application's
	*  {@link AssetExternalizer} is located, to copy the resource to
	*  a directory visible to the web server.
	*
	*/

	public String buildURL(IRequestCycle cycle)
	{
		String[] parameters;
		String externalURL;
		IEngineService service;
		String URL;
		String localizedResourcePath;

		try
		{
			localizedResourcePath = findLocalization(cycle);
		}
		catch (ResourceUnavailableException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}

		if (externalizer == null)
			externalizer = AssetExternalizer.get(cycle);

		try
		{
			externalURL = externalizer.getURL(localizedResourcePath);
		}
		catch (ResourceUnavailableException e)
		{
			throw new ApplicationRuntimeException(
				"Could not build URL for private asset " + localizedResourcePath + ".",
				e);
		}

		if (externalURL != null)
			return externalURL;

		// Otherwise, the service is responsible for dynamically retrieving the
		// resource.	

		parameters = new String[] { localizedResourcePath };

		service = cycle.getEngine().getService(IEngineService.ASSET_SERVICE);

		// Since it is no longer necessary to have an active HttpSession to
		// use the asset service, there's no need to encode the URL anymore.
		// This change was made in release 1.0.1.

		Gesture g = service.buildGesture(cycle, null, parameters);

		return g.getFullURL();
	}

	public InputStream getResourceAsStream(IRequestCycle cycle)
		throws ResourceUnavailableException
	{
		try
		{
			IResourceResolver resolver = cycle.getEngine().getResourceResolver();

			URL url = resolver.getResource(findLocalization(cycle));

			return url.openStream();
		}
		catch (Exception ex)
		{
			throw new ResourceUnavailableException(
				"Could not access private asset " + resourcePath + ".",
				ex);
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

	private String findLocalization(IRequestCycle cycle)
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
		String result;

		if (localizations == null)
		{
			synchronized (this)
			{
				if (localizations == null)
					localizations = new HashMap(MAP_SIZE);
			}
		}

		synchronized (localizations)
		{
			result = (String) localizations.get(locale);
			if (result != null)
				return result;
		}

		if (CAT.isDebugEnabled())
			CAT.debug(
				"Searching for localization of private asset "
					+ resourcePath
					+ " in locale "
					+ locale.getDisplayName());

		dotx = resourcePath.lastIndexOf('.');
		suffix = resourcePath.substring(dotx);

		buffer = new StringBuffer(dotx + 30);

		buffer.append(resourcePath.substring(0, dotx));
		rawLength = buffer.length();

		country = locale.getCountry();
		if (country.length() > 0)
			start--;

		// This assumes that you never have the case where there's
		// a null language code and a non-null country code.

		language = locale.getLanguage();
		if (language.length() > 0)
			start--;

		IResourceResolver resolver = cycle.getEngine().getResourceResolver();

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

			if (resolver.getResource(candidatePath) != null)
			{
				synchronized (localizations)
				{
					localizations.put(locale, candidatePath);
				}

				if (CAT.isDebugEnabled())
					CAT.debug("Found " + candidatePath);

				return candidatePath;
			}

		}

		throw new ResourceUnavailableException(
			"Could not find private asset " + resourcePath + " for locale " + locale + ".");

	}

	public String toString()
	{
		return "PrivateAsset[" + resourcePath + "]";
	}
}