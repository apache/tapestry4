package com.primix.tapestry.parse;

import org.xml.sax.*;

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
 *  Exception thrown by {@link SpecificationParser} when there's a problem
 *  parsing a specification, or when there's a non-schema related error
 *  in the specification.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class SpecificationParseException extends Exception
{
	private int lineNumber = 0;
	private int column = 0;
	private String resourcePath;
	private Throwable rootCause;
	
	public SpecificationParseException(String message, String resourcePath, 
									   Locator locator, Throwable rootCause)
	{
		super(message);
		
		this.resourcePath = resourcePath;
		
		if (locator != null)
		{
			lineNumber = locator.getLineNumber();
			column  = locator.getColumnNumber();
		}
		
		this.rootCause = rootCause;
	}
	
	public int getLineNumber()
	{
		return lineNumber;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public String getResourcePath()
	{
		return resourcePath;
	}
	
	public Throwable getRootCause()
	{
		return rootCause;
	}
}
