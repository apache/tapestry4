package tutorial.portal;

import com.primix.tapestry.*;

/**
 *  Home page for the Portal tutorial.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Home
    extends ErrorPage
{
    private PortletModel model;
    private int newPortletId;
    
    public void detach()
    {
        model = null;
        newPortletId = 0;
        
        super.detach();
    }
    
    public void setModel(PortletModel value)
    {
        model = value;
    }
    
    public PortletModel getModel()
    {
        return model;
    }
    
    public void setNewPortletId(int value)
    {
        newPortletId = value;
    }
    
    public int getNewPortletId()
    {
        return newPortletId;
    }
    
    public IActionListener getCloseListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle)
            {
                closeModel();
            }
        };
    }
    
    private void closeModel()
    {
        Visit visit = (Visit)getVisit();
        
        visit.removeModel(model);
    }
    
    public IActionListener getFormListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle)
            {
                addModel();
            }
        };
    }
    
    private void addModel()
    {
        Visit visit = (Visit)getVisit();
        
        visit.addModel(newPortletId);
    }
}
