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
 * This class is used to create WML output.
 *
 * <p>The <code>WMLResponseWriter</code> handles the necessary escaping 
 * of invalid characters.
 * Specifically, the '$', '&lt;', '&gt;' and '&amp;' characters are properly
 * converted to their WML entities by the <code>print()</code> methods.
 * Similar measures are taken by the {@link #attribute(String, String)} method.
 * Other invalid characters are converted to their numeric entity equivalent.
 *
 * <p>This class makes it easy to generate trivial and non-trivial WML pages.
 * It is also useful to generate WML snippets. It's ability to do simple
 * formatting is very useful. A JSP may create an instance of the class
 * and use it as an alternative to the simple-minded <b>&lt;%= ... %&gt;</b>
 * construct, espcially because it can handle null more cleanly.
 *
 * @version $Id$
 * @author David Solis
 * @since 0.2.9
 */

package com.primix.tapestry.wml;

import com.primix.tapestry.*;
import java.io.*;

public class WMLResponseWriter
    extends AbstractResponseWriter
{

	private static String[] entities = new String[64];
	private static boolean[] safe = new boolean[128];
	private static final String SAFE_CHARACTERS =
		"01234567890" +
		"abcdefghijklmnopqrstuvwxyz" +
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
		"\t\n\r !\"#%'()*+,-./:;=?@[\\]^_`{|}~";

	static
	{
		entities['"'] = "&quot;";
		entities['<'] = "&lt;";
		entities['>'] = "&gt;";
		entities['&'] = "&amp;";
		entities['$'] = "$$";

		int length = SAFE_CHARACTERS.length();
		for (int i = 0; i < length; i++)
			safe[SAFE_CHARACTERS.charAt(i)] = true;
	}

	protected String[] getEntities()
	{
		return entities;
    }
	
	protected boolean[] getSafe()
	{
		return safe;
	}

	/**
	*  Protected constructor, needed by {@link NestedWMLResponseWriter}.
	*
	*/

	protected WMLResponseWriter()
	{
	}

	/**
	*  Sends output to the stream.  Internally, an instance of <code>PrintWriter</code>
	*  is created, which will be closed when the <code>WMLResponseWriter</code>
	*  is closed.
	*
	*/

	public WMLResponseWriter(OutputStream stream)
	{
		writer = new PrintWriter(stream);
	}
	
	public IResponseWriter getNestedWriter()
	{
		return new NestedWMLResponseWriter(this);
	}

	public String getContentType()
	{
		return "text/vnd.wap.wml";
	}
}

