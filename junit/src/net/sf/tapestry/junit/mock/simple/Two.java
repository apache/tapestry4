package net.sf.tapestry.junit.mock.simple;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  Part of the test suite.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class Two extends BasePage
{
    private String _message;
    
    public void detach()
    {
        _message = null;
        
        super.detach();
    }
    
    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String message)
    {
        _message = message;
    }

    public void go(IRequestCycle cycle)
    {
        setMessage("You clicked the link!");
    }
}
