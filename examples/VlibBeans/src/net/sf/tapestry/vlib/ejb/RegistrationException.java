package net.sf.tapestry.vlib.ejb;

/**
 *  Exception thrown by {@link IOperations#registerNewUser(String, String, String, String)}
 *  if the registration is not allowed (usually, because of a duplicate email or
 *  name).
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class RegistrationException extends Exception
{
    private Throwable rootCause;

    public RegistrationException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public RegistrationException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public RegistrationException(String message)
    {
        super(message);
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}