//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.asset;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;

/**
 *  A reference to an external URL.  {@link ExternalAsset}s are not
 *  localizable.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExternalAsset implements IAsset
{
    private String _URL;

    public ExternalAsset(String URL)
    {
        _URL = URL;
    }

    /**
     *  Simply returns the URL of the external asset.
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        return _URL;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        return getResourceAsStream(cycle, cycle.getPage().getLocale());
    }

    /**
     *  Ignores the locale and attempts to get the stream to the external URL.
     * 
     *  @since 2.2
     * 
     **/

    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale)
    {
        URL url;

        try
        {
            url = new URL(_URL);

            return url.openStream();
        }
        catch (Exception ex)
        {
            // MalrformedURLException or IOException

            throw new ApplicationRuntimeException(Tapestry.getString("ExternalAsset.resource-missing", _URL), ex);
        }

    }

    public String toString()
    {
        return "ExternalAsset[" + _URL + "]";
    }
}