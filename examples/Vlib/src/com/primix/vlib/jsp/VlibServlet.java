package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import java.io.IOException;

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
 *  Servlet for the {@link HomeDelegate} page.  Should be mapped
 *  to the URI <code>/home/*</code>.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
 public abstract class VlibServlet extends GatewayServlet
 {
 	/**
	 *  Method invoked by subclasses.  Encodes the URL and build a
	 *  &lt;a&gt; tag around the URL and the label.
	 *
	 */
	 
 	protected static void writeLink(RequestContext context, HTMLWriter writer,
			String URI, String label)
	{
		boolean compressed;
		
		compressed = writer.compress(true);
		writer.begin("a");
		writer.attribute("href", buildURL(context, URI));
		writer.print(label);
		writer.end();
		
		writer.setCompressed(compressed);
	}
	
	protected void handleServletException(RequestContext context,
		IService delegate, ServletException e)
	throws IOException, ServletException
	{
		context.setAttribute("javax.servlet.jsp.jspException", e);
		
		context.forward(buildURL(context, "/jsp/Error.jsp"));
	}

    /**
     *  Converts a partial URI into a complete URL.  The URI passed in
     *  is relative to the context.  The final URL is prefixed with
     *  the context path and properly encoded.
     *
     */

    public static String buildURL(RequestContext context, String URI)
    {
        StringBuffer buffer;
        String URL;

        buffer = new StringBuffer();
        buffer.append(context.getRequest().getContextPath());
        buffer.append(URI);

        URL =  context.getResponse().encodeURL(buffer.toString());

        return URL;
    }
 }
 