package net.sf.tapestry.pageload;

/**
 * @author mindbridge
 *
 */
public class ComponentClassLoaderException extends Exception
{
    

	/**
	 * Constructor for ComponentClassLoaderException.
	 */
	public ComponentClassLoaderException()
	{
		super();
	}

	/**
	 * Constructor for ComponentClassLoaderException.
	 * @param message
	 */
	public ComponentClassLoaderException(String message)
	{
		super(message);
	}

	/**
	 * Constructor for ComponentClassLoaderException.
	 * @param message
	 * @param cause
	 */
	public ComponentClassLoaderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructor for ComponentClassLoaderException.
	 * @param cause
	 */
	public ComponentClassLoaderException(Throwable cause)
	{
		super(cause);
	}

}
