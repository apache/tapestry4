package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.html.BasePage;

/**
 *  Displays the results passed as a parameter to {@link net.sf.tapestry.junit.mock.app.ProtectedLink} 
 *  page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class ProtectedLinkResult extends BasePage
{
    private Object[] _parameters;
    
    public void detach()
    {
        _parameters = null;
        
        super.detach();
    }
    
    
    public Object[] getParameters()
    {
        return _parameters;
    }

    public void setParameters(Object[] parameters)
    {
        _parameters = parameters;
    }
}
