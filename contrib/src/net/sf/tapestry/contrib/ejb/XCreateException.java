package net.sf.tapestry.contrib.ejb;

import javax.ejb.CreateException;

/**
 *  Extended version of {@link CreateException} that includes a root cause.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class XCreateException extends CreateException
{
    private Throwable rootCause;

    public XCreateException(String message)
    {
        super(message);
    }

    public XCreateException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public XCreateException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}