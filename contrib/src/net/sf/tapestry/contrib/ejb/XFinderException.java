package net.sf.tapestry.contrib.ejb;

import javax.ejb.FinderException;

/**
 *  Extended version of {@link FinderException} that includes a root cause.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class XFinderException extends FinderException
{
    private Throwable rootCause;

    public XFinderException(String message)
    {
        super(message);
    }

    public XFinderException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public XFinderException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}