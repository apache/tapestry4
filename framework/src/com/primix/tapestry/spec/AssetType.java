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

package com.primix.tapestry.spec;

import com.primix.tapestry.util.*;

/**
 *  Defines the types of assets.
 *
 *  @see AssetSpecification
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public final class AssetType extends Enum
{
    /**
    *  An external resource.
    *
    */

    public static final AssetType EXTERNAL = new AssetType("EXTERNAL");

    /**
    *  A resource visible to the {@link javax.servlet.ServletContext}.
    *
    */

    public static final AssetType CONTEXT = new AssetType("CONTEXT");

    /**
    *  An internal resource visible only on the classpath.  Typically,
    *  a resource package in a WAR or JAR file alongside the classes.
    *
    */

    public static final AssetType PRIVATE = new AssetType("PRIVATE");

    private AssetType(String name)
    {
        super(name);
    }

    private Object readResolve()
    {
        return getSingleton();
    }
}

