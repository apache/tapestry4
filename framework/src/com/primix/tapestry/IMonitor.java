/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry;

/**
 *  Basic support for application monitoring and metrics.  
 *  This interface defines events; the implementation
 *  decides what to do with them (such as record them to a database).
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public interface IMonitor
{
	/**
	 *  Invoked before constructing a page.
	 *
	 */

	public void pageCreateBegin(String pageName);

	/**
	 *  Invoked after successfully constructing a page and all of its components.
	 *
	 */

	public void pageCreateEnd(String pageName);

	/**
	 *  Invoked when a page is loaded.  This includes time to locate or create an instance
	 *  of the page and rollback its state (to any previously recorded value).
	 *
	 */

	public void pageLoadBegin(String pageName);

	/**
	 *  Invoked once a page is completely loaded and rolled back to its prior state.
	 *
	 */

	public void pageLoadEnd(String pageName);

	/**
	 *  Invoked before a page render begins.
	 *
	 */

	public void pageRenderBegin(String pageName);

	/**
	 *  Invoked after a page has succesfully rendered.
	 *
	 */

	public void pageRenderEnd(String pageName);

	/**
	 *  Invoked before a page rewind (to respond to an action) begins.
	 *
	 */

	public void pageRewindBegin(String pageName);

	/**
	 *  Invoked after a page has succesfully been rewound (which includes
	 *  any activity related to the action listener).
	 *
	 */

	public void pageRewindEnd(String pageName);

	/**
	 *  Invoked when a service begins processing.
	 *
	 */

	public void serviceBegin(String serviceName, String detailMessage);

	/**
	 *  Invoked when a service successfully ends.
	 *
	 */

	public void serviceEnd(String serviceName);

	/**
	 *  Invoked when a service throws an exception rather than completing normally.
	 *  Processing of the request may continue with the display of an exception
	 *  page.
	 *
	 */

	public void serviceException(Throwable exception);

	/**
	 *  Invoked when a session is initiated.  This is typically
	 *  done from the implementation of the home service.
	 *
	 */

	public void sessionBegin();
}