package net.sf.tapestry.contrib.ejb;

import javax.ejb.EJBException;

/**
 *  Extended version of {@link EJBException} that includes a root cause.
 *  {@link EJBException} doesn't have quite the right constructor for this ...
 *  it has an equivalent to the rootCause property, (causedByException), but
 *  doesn't have a constructor that allows us to set a custom message.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class XEJBException extends EJBException
{
    private Throwable rootCause;

    public XEJBException(String message)
    {
        super(message);
    }

    public XEJBException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public XEJBException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}