package tests.tapestry;

import com.ibm.logging.*;
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
 

public class SimpleMonitor implements IRecordType, IMonitor
{
    private MessageLogger logger;

    public SimpleMonitor(MessageLogger logger)
    {
	this.logger = logger;
    }

    public void pageCreateBegin(java.lang.String pageName)
    {
	logger.text(TYPE_INFO, this, "pageCreateBegin",
                    "BEGIN CREATE {0}", pageName);
    }

    public void pageCreateEnd(java.lang.String pageName)
    {
	logger.text(TYPE_INFO, this, "pageCreateEnd",
                    "END CREATE {0}", pageName);
    }

    public void pageLoadBegin(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageLoadBegin",
                    "BEGIN LOAD {0}", pageName);
    }

    public void pageLoadEnd(String pageName) 
    {
	logger.text(TYPE_INFO, this, "pageLoadEnd",
                    "END LOAD {0}", pageName);
    }

    public void pageRenderBegin(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRenderBegin",
                    "BEGIN RENDER {0}", pageName);
    }

    public void pageRenderEnd(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRenderEnd",
                    "END RENDER {0}", pageName);
    }

    public void pageRewindBegin(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRewindBegin",
                    "BEGIN REWIND {0}", pageName);
    }

    public void pageRewindEnd(String pageName)
    {
	logger.text(TYPE_INFO, this, "pageRewindEnd", "END REWIND {0}", pageName);
    }

    public void serviceBegin(String serviceName, String detailMessage)
    {
	logger.text(TYPE_INFO, this, "serviceBegin",
                    "BEGIN SERVICE {0} {1}", serviceName, detailMessage);
    }

    public void serviceEnd(String serviceName)
    {
	logger.text(TYPE_INFO, this, "serviceEnd", "END SERVICE {0}", serviceName);
    }

    public void serviceException(Throwable exception)
    {
	logger.exception(TYPE_INFO, this, "serviceException", exception);
    }

    public void sessionBegin()
    {
	logger.text(TYPE_INFO, this, "sessionBegin",
                    "Start of session.");
    }
}
