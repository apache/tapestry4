package com.primix.servlet;

/*
 * ServletUtils - Support library for improved Servlets and JavaServer Pages.
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
 * included    with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * An interface defining a method to service a single HTTP Request cycle, encapsulated
 * inside a {@link RequestContext}. A servlet's delegate will implement
 * this interface.
 *
 * @see GatewayServlet
 *
 * @version $Id$
 * @author Howard Ship
 */

public interface IService
{

/**
 * A method that is invoked as part of the HTTP Request cycle to delegate
 * behaviour from one object to another. This is used by a {@link GatewayServlet}
 * which identifies a delegate object to perform its operations.
 *
 * @param context Provides access to the request, response, session and servlet.
 */
public void service(RequestContext context) throws ServletException, IOException;
}
