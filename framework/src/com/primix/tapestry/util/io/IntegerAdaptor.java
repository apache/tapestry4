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

package com.primix.tapestry.util.io;

import java.util.*;
import java.io.*;

/**
 *  Squeezes a {@link Integer}.  This adaptor claims all the digits as prefix
 *  characters, so its the very simplest conversion of all!
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

class IntegerAdaptor
implements ISqueezeAdaptor
{
    /**
     *  Registers this adaptor using all nine digits and the minus sign.
     *
     */

    public void register(DataSqueezer squeezer)
    {
        squeezer.register("-0123456789", Integer.class, this);
    }

    /**
     *  Simply invokes <code>toString()</code> on the data,
     *  which is actually type {@link Integer}.
     *
     */

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return data.toString();
    }

    /**
     *  Constructs an {@link Integer} from the string.
     *
     */

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Integer(string);
    }

}