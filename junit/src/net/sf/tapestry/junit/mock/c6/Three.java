package net.sf.tapestry.junit.mock.c6;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  Tests for when a persistent property is an EJB reference.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/
public class Three extends BasePage
{
    private FakeEJBObject _ejb;
    
    public void initialize()
    {
        _ejb = null;
    }
        
    public FakeEJBObject getEjb()
    {
        return _ejb;
    }

    public void setEjb(FakeEJBObject ejb)
    {
        _ejb = ejb;
        
        fireObservedChange("ejb", ejb);
    }

    public void create(IRequestCycle cycle)
    {
        setEjb(new FakeEJBObject(997));
    }
    
    public void discard(IRequestCycle cycle)
    {
        cycle.discardPage(getPageName());
    }
}
