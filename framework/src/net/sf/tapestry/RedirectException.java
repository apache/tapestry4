package net.sf.tapestry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 *  Exception thrown to force a redirection to an arbitrary location.
 *  This is used when, after processing a request (such as a form
 *  submission or a link being clicked), it is desirous to go
 *  to some arbitrary new location.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 **/

public class RedirectException extends RequestCycleException
{
	private String location;

	public RedirectException(String location)
	{
		this(null, location);
	}

	/** 
	 *  @param message A message describing why the redirection is taking place.
	 *  @param location The location to redirect to, may be a relative path (relative
	 *  to the {@link ServletContext}).
	 *
	 *  @see HttpServletResponse#sendRedirect(String)
	 *  @see HttpServletResponse#encodeRedirectURL(String)
	 *
	 **/

	public RedirectException(String message, String location)
	{
		super(message);

		this.location = location;
	}

	public String getLocation()
	{
		return location;
	}
}