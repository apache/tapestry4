package com.primix.tapestry.script;

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
 *  A token that writes the value of a named symbol.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

class SymbolToken
implements ITemplateToken
{
    private String name;

    SymbolToken(String name)
    {
        this.name = name;
    }
    
    /**
     *  Gets the named symbol from the symbols {@link Map}, verifies that
     *  it is a String, and writes it to the {@link Writer}.
     *
     */

	public void write(Writer writer, Map symbols)
	throws IOException
	{
        Object raw = null;
	    String value;

        try
        {
            raw = symbols.get(name);
            value = (String)raw;
        }
        catch (ClassCastException ex)
        {
            throw new IOException("Symbol '" + name + "' is class " +
                raw.getClass().getName() + ", not String.");
        }

        if (value == null)
            throw new IOException(
                "No symbol '" + name + "' provided for this script.");

		writer.write(value);
	}
}