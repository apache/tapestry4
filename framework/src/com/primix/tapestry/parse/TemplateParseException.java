/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.parse;

/**
 *  Class TemplateParseException
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class TemplateParseException extends Exception
{
	private String resourcePath;
	private int line = -1;

	public TemplateParseException(String message)
	{
		this(message, -1, null);
	}

	public TemplateParseException(String message, int line, String resourcePath)
	{
		super(message);

		this.line = line;
		this.resourcePath = resourcePath;
	}

	public int getLine()
	{
		return line;
	}

	public String getResourcePath()
	{
		return resourcePath;
	}
}