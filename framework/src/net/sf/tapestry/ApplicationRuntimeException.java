package net.sf.tapestry;

/**
 *  General wrapper for any exception (normal or runtime) that may occur during
 *  a request cycle.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ApplicationRuntimeException extends RuntimeException
{
    private Throwable rootCause;

    public ApplicationRuntimeException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public ApplicationRuntimeException(String message)
    {
        super(message);
    }

    public ApplicationRuntimeException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}