package net.sf.tapestry.contrib.ejb;

import javax.ejb.RemoveException;

/**
 *  Extended version of {@link RemoveException} that includes a root cause.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class XRemoveException extends RemoveException
{
    private Throwable rootCause;

    public XRemoveException(String message)
    {
        super(message);
    }

    public XRemoveException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public XRemoveException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}