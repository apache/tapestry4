/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship and Primix
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
 *  The result of executing a script, the session is used during the parsing
 *  process as well.  Following {@link ParsedScript#execute(Map)}, the session
 *  provides access to output symbols as well as the body and initialization
 *  blocks created by the script tokens.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 0.2.9
 */
 
package com.primix.tapestry.script;

import java.util.*;
import java.io.*;

public class ScriptSession
{
	private String scriptPath;
	private Map symbols;
	private String body;
	private String initialization;
	
	public ScriptSession(String scriptPath, Map symbols)
	{
		this.scriptPath = scriptPath;
		this.symbols = symbols;
	}
	
	public String getScriptPath()
	{
		return scriptPath;
	}
	
	public String getSymbol(String key)
	throws ScriptException
	{
		try
		{
			return (String)symbols.get(key);
		}
		catch (ClassCastException ex)
		{
			throw new ScriptException("Symbol " + key + " is not a String.", 
					null, this, ex);
		}		
	}
	
	public void setSymbol(String key, String value)
	{
		symbols.put(key, value);
	}
	
	public void setBody(String value)
	{
		body = value;
	}
	
	public String getBody()
	{
		return body;
	}
	
	public String getInitialization()
	{
		return initialization;
	}
	
	public void setInitialization(String value)
	{
		initialization = value;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("ScriptSession[");
		buffer.append(scriptPath);
		buffer.append(']');
		
		return buffer.toString();
	}
}
