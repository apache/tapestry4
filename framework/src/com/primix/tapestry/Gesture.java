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

package com.primix.tapestry;

import java.util.*;

/**
 *  A Gesture represents a possible action within the client web browser;
 *  either clicking a link or submitting a form.  A full URL for the Gesture
 *  can be generated, or the query parameters for the Gesture can be extracted
 *  (seperately from the servlet path).  The latter case is used when submitting
 *  forms.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.3
 */


public class Gesture
{
	private String servletPath;
	private Map queryParameters;
	
	/**
	 *  Creates a new Gesture, for the given servlet and with a set
	 *  or query parameters.
	 *  
	 * @param servletPath the complete path (including context path) of
	 *  the servlet (see {@link IEngine#getServletPath()}).
	 * @param queryParameters a {@link Map} of parameters.  Keys and values
	 *  are both String.  Map not be null; one query parameter must be
	 *  specify the engine service.
	 */
	
	public Gesture(String servletPath, Map queryParameters)
	{
		this.servletPath = servletPath;
		
		this.queryParameters = new HashMap(queryParameters);
	}
	

	/**
	 *  Returns the {@link Iterator} for the query parameter map's entry set.
	 *  Each value will be {@link Map.Entry}.
	 *
	 */
	
	public Iterator getQueryParameters()
	{
		return queryParameters.entrySet().iterator();
	}
	
	public String getServletPath()
	{
		return servletPath;
	}
	
	/**
	 *  Returns the full URL, with all query parameters encoded into the URL.
	 *  This must still be filtered through {@link HttpServletResponse#encodeURL(String)}.
	 */
	
	public String getFullURL()
	{
		StringBuffer buffer = new StringBuffer(servletPath);
		
		boolean first = true;
		
		Iterator i = getQueryParameters();
		
		while (i.hasNext())
		{
			Map.Entry entry = (Map.Entry)i.next();
			
			if (first)
				buffer.append('?');
			else
				buffer.append('&');
			
			first = false;
			
			buffer.append(entry.getKey().toString());
			buffer.append('=');
			buffer.append(entry.getValue().toString());
		}
	
		return buffer.toString();
	}
	
	/**
	 *  As with {@link #getFullURL()}, but runs the result through
	 *  {@link IRequestCycle#encodeURL(String)}.
	 *
	 */
	
	public String getFullURL(IRequestCycle cycle)
	{
		return cycle.encodeURL(getFullURL());
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer("Gesture[");
		
		buffer.append(servletPath);
		buffer.append(' ');
		
		buffer.append(queryParameters);
		buffer.append(']');
		
		return buffer.toString();
	}
}

