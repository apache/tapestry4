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
 *  A {@link IResponseWriter} that does absolutely <em>nothing</em>; this
 *  is used during the rewind phase of the request cycle when output
 *  is discarded anyway.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 0.2.9
 *
 */ 

package com.primix.tapestry.engine;

import com.primix.tapestry.*;

public class NullResponseWriter
implements IResponseWriter
{
	private static IResponseWriter shared;
	
	public static IResponseWriter getSharedInstance()
	{
		if (shared == null)
			shared = new NullResponseWriter();
		
		return shared;	
	}

	public void printRaw(char[] buffer, int offset, int length)
	{ 
	}

	public void printRaw(String value)
	{ 
	}

	public void println()
	{ 
	}

	public void print(char[] data, int offset, int length)
	{ 
	}

	public void print(char value)
	{ 
	}

	public void print(int value)
	{ 
	}

	public void print(String value)
	{ 
	}

	/**
	 *  Returns <code>this</code>: since a NullResponseWriter doesn't actually
	 *  do anything, one is as good as another!.
	 *
	 */
	 
	public IResponseWriter getNestedWriter()
	{
		return this;
	}

	public String getContentType()
	{ 
		return null;
	}

	public void flush()
	{ 
	}

	public void end()
	{ 
	}

	public void end(String name)
	{ 
	}

	public void comment(String value)
	{ 
	}

	public void closeTag()
	{ 
	}

	public void close()
	{ 
	}

	/**
	 *  Always returns false.
	 *
	 */
	 
	public boolean checkError()
	{
		return false;
	}

	public void beginEmpty(String name)
	{ 
	}

	public void begin(String name)
	{ 
	}

	public void attribute(String name)
	{ 
	}

	public void attribute(String name, int value)
	{ 
	}

	public void attribute(String name, String value)
	{ 
	}
}

