package net.sf.tapestry.junit.mock.c6;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  Used for testing page property persistance.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class Home extends BasePage
{
    private String _message;
    
    public void initialize()
    {
        _message = "Hello";
    }
        
    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String message)
    {
        _message = message;
        
        fireObservedChange("message", message);
    }

    public void updateMessage(IRequestCycle cycle)
    {
        setMessage("Changed");
    }

}
