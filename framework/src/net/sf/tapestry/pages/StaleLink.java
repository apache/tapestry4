package net.sf.tapestry.pages;

import net.sf.tapestry.html.BasePage;

/**
 *  Stores a message (taken from the {@link net.sf.tapestry.StaleLinkException})
 *  that is displayed as part of the page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class StaleLink extends BasePage
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

}
