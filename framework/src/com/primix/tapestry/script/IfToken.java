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

package com.primix.tapestry.script;

import com.primix.tapestry.util.prop.*;
import com.primix.tapestry.*;
import java.util.*;

/**
 *  A conditional portion of the generated script.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.1
 *
 */

class IfToken extends AbstractToken
{
	private boolean condition;
	private String propertyPath;
	private String[] properties;
	
	IfToken(boolean condition, String propertyPath)
	{
		this.condition = condition;
		this.propertyPath = propertyPath;
	}
	
	private boolean evaluate(ScriptSession session)
	{
		if (properties == null)
			properties = PropertyHelper.splitPropertyPath(propertyPath);
		
		Map symbols = session.getSymbols();
		
		PropertyHelper helper = PropertyHelper.forInstance(symbols);
		
		Object value = helper.getPath(symbols, properties);
		
		return Tapestry.evaluateBoolean(value);
	}
	
    public void write(StringBuffer buffer, ScriptSession session)
	throws ScriptException
	{
		if (evaluate(session) == condition)
			writeChildren(buffer, session);
	}
}
