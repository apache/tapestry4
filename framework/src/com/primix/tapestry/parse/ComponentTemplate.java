package com.primix.tapestry.parse;

import com.primix.tapestry.*;

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
 *  Enapsulates a parsed component template, allowing access to the
 *  tokens parsed.
 *
 *  <p>TBD:  Record the name of the resource (or other location) from which
 * the template was parsed (useful during debugging).
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ComponentTemplate
{
	/**
	*  The HTML template from which the tokens were generated.  This is a string
	*  read from a resource.  The tokens represents offsets and lengths into
	*  this string.
	*
	*/

		private char[] templateData;

	private TemplateToken[] tokens;

	/**
	*  Creates a new ComponentTemplate.
	*
	*  @param templateData The template data.  This is <em>not</em> copied, so
	*  the array passed in should not be modified further.
	*
	*  @param tokens  The tokens making up the template.
	*
	*/

	public ComponentTemplate(char[] templateData, TemplateToken[] tokens)
	{
		this.templateData = templateData;
		this.tokens = tokens;
	}

	public char[] getTemplateData()
	{
		return templateData;
	}

	public TemplateToken getToken(int index)
	{
		return tokens[index];
	}

	public int getTokenCount()
	{
		return tokens.length;
	}
}

