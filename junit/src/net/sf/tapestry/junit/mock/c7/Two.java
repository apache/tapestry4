package net.sf.tapestry.junit.mock.c7;

import net.sf.tapestry.html.BasePage;

/**
 *  Page used to test listener bindings.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class Two extends BasePage
{
    private  String _message;
    
    public void initialize()
    {
        _message = "No message.";
    }
    
    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String message)
    {
        _message = message;
    }
}
