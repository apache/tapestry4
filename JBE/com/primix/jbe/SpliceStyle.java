package com.primix.jbe;

import java.io.*;
import java.util.*;

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
 *  Used to enapsulate one style of block edits.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

class SpliceStyle
{
    private String begin;
    private String end;

    SpliceStyle(String tag, String beginPrefix, String beginSuffix, String endPrefix, String endSuffix)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append(beginPrefix);
        buffer.append(tag);
        if (beginSuffix != null)
            buffer.append(beginSuffix);

        begin = buffer.toString();

        buffer.setLength(0);
        buffer.append(endPrefix);
        buffer.append(tag);
        if (endSuffix != null)
            buffer.append(endSuffix);

        end = buffer.toString();
    }

    /**
     *  Returns true if the line matches the begin delimiter for this tag.
     *  Line should be trimmed before calling.
     *
     */

    boolean isBegin(String line)
    {
       return line.equalsIgnoreCase(begin);
    }

    /**
     *  Returns tru if the line matches the end delimiter for this tag.
     *  Line should be trimmed before calling.
     *
     */

    boolean isEnd(String line)
    {
        return line.trim().equalsIgnoreCase(end);
    }

    void writeBegin(PrintWriter writer)
    {
        writer.println(begin);
    }

    void writeEnd(PrintWriter writer)
    {
        writer.println(end);
    }
}