package net.sf.tapestry;

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
	 **/

	public void serviceException(Throwable exception);

	/**
	 *  Invoked when a session is initiated.  This is typically
	 *  done from the implementation of the home service.
	 *
	 **/

	public void sessionBegin();
}