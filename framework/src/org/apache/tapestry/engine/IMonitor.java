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

package org.apache.tapestry.engine;

/**
 *  Basic support for application monitoring and metrics.  
 *  This interface defines events; the implementation
 *  decides what to do with them (such as record them to a database).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IMonitor
{
	/**
	 *  Invoked before constructing a page.
	 *
	 **/

	public void pageCreateBegin(String pageName);

	/**
	 *  Invoked after successfully constructing a page and all of its components.
	 *
	 **/

	public void pageCreateEnd(String pageName);

	/**
	 *  Invoked when a page is loaded.  This includes time to locate or create an instance
	 *  of the page and rollback its state (to any previously recorded value).
	 *
	 **/

	public void pageLoadBegin(String pageName);

	/**
	 *  Invoked once a page is completely loaded and rolled back to its prior state.
	 *
	 **/

	public void pageLoadEnd(String pageName);

	/**
	 *  Invoked before a page render begins.
	 *
	 **/

	public void pageRenderBegin(String pageName);

	/**
	 *  Invoked after a page has succesfully rendered.
	 *
	 **/

	public void pageRenderEnd(String pageName);

	/**
	 *  Invoked before a page rewind (to respond to an action) begins.
	 *
	 **/

	public void pageRewindBegin(String pageName);

	/**
	 *  Invoked after a page has succesfully been rewound (which includes
	 *  any activity related to the action listener).
	 *
	 **/

	public void pageRewindEnd(String pageName);

	/**
	 *  Invoked when a service begins processing.
	 *
	 **/

	public void serviceBegin(String serviceName, String detailMessage);

	/**
	 *  Invoked when a service successfully ends.
	 *
	 **/

	public void serviceEnd(String serviceName);

	/**
	 *  Invoked when a service throws an exception rather than completing normally.
	 *  Processing of the request may continue with the display of an exception
	 *  page.
	 * 
	 *  <p>
	 *  serviceException() is always invoked <em>before</em>
	 * {@link #serviceEnd(String)}.
	 *
	 **/

	public void serviceException(Throwable exception);

	/**
	 *  Invoked when a session is initiated.  This is typically
	 *  done from the implementation of the home service.
	 *
	 **/

	public void sessionBegin();
}