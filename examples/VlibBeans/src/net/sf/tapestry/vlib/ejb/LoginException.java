package net.sf.tapestry.vlib.ejb;

/**
 *  Exception thrown on a login failure.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class LoginException extends Exception
{
    private boolean passwordError;

    public LoginException(String message, boolean passwordError)
    {
        super(message);

        this.passwordError = passwordError;
    }

    /**
     *  Returns true if the error is related to the password.  Otherwise,
     *  the error is related to the email address (either not found,
     *  or the user has been invalidated or otherwise locked out).
     *
     **/

    public boolean isPasswordError()
    {
        return passwordError;
    }

}