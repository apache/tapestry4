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
 *  Squeezes a {@link Long}. 
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

class LongAdaptor
implements ISqueezeAdaptor
{
    private static final String PREFIX = "l";

    /**
     *  Registers using the prefix 'l'.
     *
     */

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Long.class, this);
    }

    /**
     *  Invoked <code>toString()</code> on data (which is type {@link Long}),
     *  and prefixs the result.
     *
     */

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     *  Constructs a {@link Long} from the string, after stripping
     *  the prefix.
     *
     */

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Long(string.substring(1));
    }

}