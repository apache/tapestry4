package tests.tapestry;

import com.primix.tapestry.*;

/*
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 

public class SimpleMonitor implements IMonitor
{
    public void pageCreateBegin(java.lang.String pageName)
    {
	    log("BEGIN CREATE " + pageName);
    }

    public void pageCreateEnd(java.lang.String pageName)
    {
	    log("END CREATE " + pageName);
    }

    public void pageLoadBegin(String pageName)
    {
	    log("BEGIN LOAD " + pageName);
    }

    public void pageLoadEnd(String pageName) 
    {
	    log("END LOAD " + pageName);
    }

    public void pageRenderBegin(String pageName)
    {
	    log("BEGIN RENDER " + pageName);
    }

    public void pageRenderEnd(String pageName)
    {
	    log("END RENDER " + pageName);
    }

    public void pageRewindBegin(String pageName)
    {
        log("BEGIN REWIND " + pageName);
    }

    public void pageRewindEnd(String pageName)
    {
	    log("END REWIND " + pageName);
    }

    public void serviceBegin(String serviceName, String detailMessage)
    {
	    log("BEGIN SERVICE " + serviceName + " " + detailMessage);
    }

    public void serviceEnd(String serviceName)
    {
	    log("END SERVICE " + serviceName);
    }

    public void serviceException(Throwable exception)
    {
	    log("EXCEPTION");
	    exception.printStackTrace();
    }

    public void sessionBegin()
    {
	    log("Start of session.");
    }

    private final void log(String message)
    {
        System.out.println(message);
    }
}
