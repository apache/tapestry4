package net.sf.tapestry;

/**
 *  Exception thrown when a resource is not available, because it is not
 *  found, or could not be used.  The latter case occurs with files which
 *  must be parsed or processed once located, such as component specification
 *  files.
 *
 *  @deprecated To be removed in 2.3.  No longer used; uses converted
 *  to {@link net.sf.tapestry.ApplicationRuntimeException}.
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ResourceUnavailableException extends Exception
{
	private Throwable rootCause;

	/**
	*  Constructor when no underlying exception is known.
	*
	*  @param message Describes the resource and the reason it is unavailable.
	*
	*/

	public ResourceUnavailableException(String message)
	{
		super(message);

	}

	/**
	*  Standard constructor
	*
	*  @param message Describes the resource and the reason it is unavailable.
	*  @param rootCause Exception which made the resource unavailable.
	*/

	public ResourceUnavailableException(String message, Throwable rootCause)
	{
		super(message);
		this.rootCause = rootCause;
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}
}