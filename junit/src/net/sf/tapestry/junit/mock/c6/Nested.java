package net.sf.tapestry.junit.mock.c6;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;

/**
 *  More testing of persistent component properties.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class Nested extends BaseComponent implements PageDetachListener
{
    private  String _message;
    
    public void initialize()
    {
        _message = "Nested";
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
    
    public void finishLoad()
    {
        initialize();
    }

    public void pageDetached(PageEvent event)
    {
        initialize();
    }

}
