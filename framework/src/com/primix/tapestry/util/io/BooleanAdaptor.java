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
 *  Squeezes a {@link Boolean}.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */


package com.primix.tapestry.util.io;

import java.util.*;
import java.io.*;

class BooleanAdaptor
implements ISqueezeAdaptor
{
    private static final String PREFIX = "TF";

    /**
     *  Registers using the prefixes 'T' and 'F' (for TRUE and FALSE).
     *
     */

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Boolean.class, this);
    }

 
    /**
     *  Squeezes the {@link Boolean} data to either 'T' or 'F'.
     *
     */

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        Boolean bool = (Boolean)data;

        if (bool.booleanValue())
            return "T";
        else
            return "F";
    }

    /**
     *  Unsqueezes the string to either {@link Boolean#TRUE} or {@link Boolean#FALSE},
     *  depending on the prefix character.
     *
     */

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        char ch = string.charAt(0);

        if (ch == 'T')
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

}