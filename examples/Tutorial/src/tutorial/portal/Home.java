package tutorial.portal;

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *  Home page for the Portal tutorial.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Home extends BasePage
{
    private PortletModel _model;
    private int _newPortletId;

    public void initialize()
    {
        _model = null;
        _newPortletId = 0;
    }

    public void setModel(PortletModel value)
    {
        _model = value;
    }

    public PortletModel getModel()
    {
        return _model;
    }

    public void setNewPortletId(int value)
    {
        _newPortletId = value;
    }

    public int getNewPortletId()
    {
        return _newPortletId;
    }

    /**
     *  Listener for the close button.  Closes the portlet currently
     *  being rendered.
     * 
     **/
    
    public void closeModel(IRequestCycle cycle)
    {        
        Visit visit = (Visit) getVisit();

        visit.removeModel(_model);
    }

    /**
     *  Adds the selected portlet (by requesting that the Visit
     *  add the portlet with the identified portlet id.
     *
     **/
    
    public void addModel(IRequestCycle cycle)
    {
        Visit visit = (Visit) getVisit();

        visit.addModel(_newPortletId);
    }
}