package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.html.BasePage;

/**
 *  Page for testing a validator, with client side validation, but no Body.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class ValidFieldNoBody extends BasePage
{
    private int _intValue;        
    
    protected void initialize()
    {
        _intValue = 10;
    }
        
    public int getIntValue()
    {
        return _intValue;
    }

    public void setIntValue(int intValue)
    {
        _intValue = intValue;
    }

}
