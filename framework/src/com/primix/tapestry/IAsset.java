/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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
 *  Representation of a asset (GIF, JPEG, etc.) that may be owned by a
 *  {@link IComponent}.
 *
 * <p>Assets may be completely external (i.e., on some other web site), 
 * contained by the {@link javax.servlet.ServletContext},  
 * or stored somewhere in the classpath.
 *
 * <p>In the latter two cases, the resource may be localized.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


package com.primix.tapestry;

import java.io.*;
import java.util.*;

public interface IAsset
{
    /**
    *  Returns a URL for the asset, ready to be inserted into the output HTML.
    *  If the asset can be localized, the localized version (matching the
    *  {@link Locale} of the current {@link IPage page}) is returned.
    *
    */

    public String buildURL(IRequestCycle cycle);

    /**
    *  Accesses the localized version of the resource (if possible) and returns it as
    *  an input stream.  A version of the resource localized to the
    *  current {@link IPage page} is returned.
    *
    */

    public InputStream getResourceAsStream(IRequestCycle cycle)
    throws ResourceUnavailableException;
}
