package com.primix.tapestry.script;

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
 *  Generates a String from its child tokens, then applies it
 *  to {@link ScriptSession#setInitialization(String)}.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 0.2.9
 */
 
class InitToken extends AbstractToken
{
	private int bufferLength = 100;
	
	public void write(StringBuffer buffer, ScriptSession session)
	throws ScriptException
	{
		if (buffer != null)
			throw new IllegalArgumentException();
		
		buffer = new StringBuffer(bufferLength);
		
		writeChildren(buffer, session);
		
		session.setInitialization(buffer.toString());
		
		// Store the buffer length from this run for the next run, since its
		// going to be approximately the right size.
		
		bufferLength = Math.max(bufferLength, buffer.length());
	}
}
