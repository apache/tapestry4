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

package com.primix.tapestry.parse;

import com.primix.tapestry.*;
import java.util.*;

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
	
	private int startIndex = -1;
	private int endIndex = -1;
	
	private IRender render;
	
	private Map attributes;
	
	/**
	 *  Constructs a TEXT token with the given template data.
	 *
	 */
	
	public TemplateToken(char[] templateData, int startIndex, int endIndex)
	{
		type = TokenType.TEXT;
		
		this.templateData = templateData;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		
		if (startIndex < 0 || endIndex < 0 ||
				startIndex > templateData.length ||
				endIndex > templateData.length)
			throw new IllegalArgumentException(this + " out of range for template length " +
							templateData.length + ".");
	}
	
	/**
	 *  Constructs token, typically used with CLOSE.
	 *
	 */
	
	public TemplateToken(TokenType type)
	{
		this.type = type;
	}
	
	/**
	 *  Constructs an OPEN token with the given id.
	 *
	 */
	
	public TemplateToken(String id)
	{
		this(id, null);
	}
	
	/**
	 *  Contructs and OPEN token with the given id and attributes.
	 *
	 * @since 1.0.2
	 */
	
	public TemplateToken(String id, Map attributes)
	{
		type = TokenType.OPEN;
		this.id = id;
		this.attributes = attributes;
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
		if (type != TokenType.TEXT)
			throw new ApplicationRuntimeException(type + " tokens may not render.");
		
		if (render == null)
		{
			synchronized(this)
			{
				if (render == null)
					render = new RenderTemplateHTML(templateData, 
							startIndex, endIndex - startIndex + 1);
			}
		}
		
		return render;
	}
	
	/**
	 *  Returns the starting index of the token.  Will return -1 for any non-TEXT
	 * token.
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
	
	/**
	 *  Returns the attributes associated with an OPEN tag, which may
	 *  be null.
	 *
	 *  @since 1.0.2
	 */
	
	public Map getAttributes()
	{
		return attributes;
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
		
		boolean first = true;
		
		if (attributes != null)
		{
			Iterator i = attributes.keySet().iterator();
			
			while (i.hasNext())
			{
				if (first)
				{
					buffer.append(" attributes: ");
					first = false;
				}
				else
					buffer.append(", ");
				
				Map.Entry e = (Map.Entry)i.next();
				
				buffer.append(e.getKey());
				buffer.append('=');
				buffer.append(e.getValue());
			}				
		}
		
		buffer.append(']');
		
		return buffer.toString();
	}
}

