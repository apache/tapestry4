//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.wml;

import javax.servlet.ServletException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.BaseEngine;

/**
 *  Subclass of {@link BaseEngine} used for WML applications to change the
 *  Exception, StaleLink and StaleSession pages.
 *
 *  @author David Solis
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public class WMLEngine extends BaseEngine
{
	protected void activateExceptionPage(
		IRequestCycle cycle,
		org.apache.tapestry.request.ResponseOutputStream output,
		Throwable cause)
		throws ServletException
	{
		super.activateExceptionPage(cycle, output, cause);
		// Sometimes the exception page isn't enough
        reportException(
            Tapestry.getMessage("AbstractEngine.unable-to-process-client-request"),
            cause);
	}


	/** @since 3.0 **/

	protected String getExceptionPageName()
	{
		return EXCEPTION_PAGE;
	}
	
	/** @since 3.0 **/

	protected String getStaleLinkPageName()
	{
		return STALE_LINK_PAGE;
	}

	/** @since 3.0 **/

	protected String getStaleSessionPageName()
	{
		return STALE_SESSION_PAGE;
	}

	/**
	 *  The name of the page used for reporting exceptions.
	 *  
	 **/
	private static final String EXCEPTION_PAGE = "WMLException";

	/**
	 *  The name of the page used for reporting stale links.
	 *
	 * */

	private static final String STALE_LINK_PAGE = "WMLStaleLink";

	/**
	 *  The name of the page used for reporting state sessions.
	 *
	 **/

	private static final String STALE_SESSION_PAGE = "WMLStaleSession";
}
