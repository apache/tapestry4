/*
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Logs the user out, invalidating the {@link HttpSession} and showing
 *  a goodbye message.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.*;

// To appease Javadoc

import javax.servlet.http.HttpSession;


public class Logout
extends BasePage
{
    /**
    *  Sets the application user to null.
    *
    */

    public void beginResponse(IResponseWriter writer,
        IRequestCycle cycle)
    throws RequestCycleException
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;

        vengine.logout();
    }

}
