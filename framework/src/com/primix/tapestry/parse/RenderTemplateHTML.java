package com.primix.tapestry.parse;

import com.primix.tapestry.*;

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
 *  Renders static HTML text from a template.  To neaten up the response HTML, leading
 *  and trailing whitespace is reduced to a single character.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class RenderTemplateHTML implements IRender
{
	private char[] templateData;
	private int offset;
	private int length;
	private boolean needsTrim = true;

	public RenderTemplateHTML(char[] templateData, int offset, int length)
	{
		this.templateData = templateData;
		this.offset = offset;
		this.length = length;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		if (needsTrim)
			trim();
			
		if (length == 0)
			return;
			
		if (!cycle.isRewinding())
			writer.printRaw(templateData, offset, length);
	}
	
	/**
	 *  Strip off all leading and trailing whitespace by adjusting offset and length.
	 *
	 */
	 
	private void trim()
	{
		char ch;
		boolean didTrim = false;
		
		needsTrim = false;

		if (length == 0)
			return;
			
		// Shave characters off the end until we hit a non-whitespace
		// character.
		
		while (length > 0)
		{
			ch = templateData[offset + length - 1];
			
			if (!Character.isWhitespace(ch))
				break;
				
			length--;
			didTrim = true;
		}
		
		// Restore one character of whitespace to the end
		
		if (didTrim)
			length++;
		
		didTrim = false;
			
		// Strip characters off the front until we hit a non-whitespace
		// character.
		
		while (length > 0)
		{
			ch = templateData[offset];
			
			if (!Character.isWhitespace(ch))
				break;
				
			offset++;
			length--;
			didTrim = true;
		}
		
		// Again, restore one character of whitespace.
		
		if (didTrim)
		{
			offset--;
			length++;
		}
		
		
		// Ok, this isn't perfect.  I don't want to write into templateData[] even
		// though I'd prefer that my single character of whitespace was always a space.
		// It would also be kind of neat to shave whitespace within the static HTML, rather
		// than just on the edges.
	}
}

