package com.primix.tapestry.parse;

import com.primix.tapestry.IRender;

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
 * A token parsed from a Tapestry HTML template.
 *
 * <p>TBD:  Use a single token to represent an bodyless component.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class TemplateToken
{

	private TokenType type;

	private String id;

	char[] templateData;

	private int startIndex;
	private int endIndex;

	private IRender render;

	/**
	*  Constructs an TEXT token with the given template data.
	*
	*/

	public TemplateToken(char[] templateData, int startIndex, int endIndex)
	{
		type = TokenType.TEXT;

		this.templateData = templateData;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	/**
	*  Constructs a CLOSE token.
	*
	*/

	public TemplateToken(int startIndex, int endIndex)
	{
		this.type = TokenType.CLOSE;

		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	/**
	*  Constructs an OPEN token with the given id.
	*
	*/

	public TemplateToken(String id, int startIndex, int endIndex)
	{
		type = TokenType.OPEN;

		this.id = id;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public int getEndIndex()
	{
		return endIndex;
	}

	/**
	*  Returns the id of the component.  This is only valid when the type
	*  is OPEN.
	*
	*/

	public String getId()
	{
		return id;
	}

	public IRender getRender()
	{
		// Some earlier code attempted to trim down excess whitespace in the template data here,
		// but it caused some problems and wasn't very necessary.

		if (render == null)
			render = new RenderTemplateHTML(templateData, 
				startIndex, endIndex - startIndex + 1);

		return render;
	}

	/**
	*  Returns the starting index of the token.  May return -1 if the token
	*  is synthesized (a close token derived from a <jwc/> tag.
	*
	*/

	public int getStartIndex()
	{
		return startIndex;
	}

	public TokenType getType()
	{
		return type;
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("TemplateToken[");

		buffer.append(type.getEnumerationId());

		if (id != null)
		{
			buffer.append(' ');
			buffer.append(id);
		}

		if (startIndex >= 0)
		{
			buffer.append(" start:");
			buffer.append(startIndex);

			buffer.append(" end:");
			buffer.append(endIndex);
		}

		buffer.append(']');

		return buffer.toString();
	}
}

