package com.primix.foundation.io;

import java.util.*;
import java.io.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
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
 *  Squeezes a String (which is pretty simple, most of the time).
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

class StringAdaptor
implements ISqueezeAdaptor
{
    private static final String PREFIX = "S";
    private static final char PREFIX_CH = 'S';

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, String.class, this);
    }

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        String string = (String)data;
        char ch;

        // An empty String is encoded as an 'S', that is, a String with
        // a length of zero.

        if (string.length() == 0)
            return PREFIX;

        ch = string.charAt(0);

        // If the first character of the string is claimed
        // this or some other adaptor, then prefix it
        // with this adaptor's prefix.

        if (ch == PREFIX_CH ||
            squeezer.isPrefixRegistered(ch))
            return PREFIX + string;
        else
            // Otherwise, the string is OK as is.
            return string;
    }

    /**
     *  Strips the prefix from the string.  This method is only
     *  invoked by the {@link DataSqueezer} if the string leads
     *  with its normal prefix (an 'S').
     *
     */

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        if (string.length() == 1)
            return "";

        return string.substring(1);
    }
}