package net.sf.tapestry.callback;

import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Simple callback for returning to a page.
 *  <p>
 *  Example usage of <tt>PageCallback</tt>:
 *  <p>
 *  The Home page ensure a user is 
 *  authenticated in the {@link net.sf.tapestry.IPage#validate(IRequestCycle)} 
 *  method.  If the user is not authenticated, they are redirected to the Login 
 *  page, after setting a callback in the Login page.
 *  <p>
 *  The Login page <tt>formSubmit()</tt> {@link net.sf.tapestry.IActionListener} 
 *  authenticates the user and then invokes {@link ICallback#performCallback(IRequestCycle)} 
 *  to the Home page.
 *  <pre>
 *  public class Home extends BasePage {
 * 
 *      public void validate(IRequestCycle cycle) throws RequestCycleException {            
 *          Visit visit = (Visit) getVisit();
 *      
 *          if (!visit.isAuthenticated()) {
 *              Login login = (Login) cycle.getPage("Login");
 *
 *              login.setCallback(new PageCallback(this));
 *              
 *              throw new PageRedirectException(login);
 *          }            
 *      }
 *  }
 *
 *  public Login extends BasePage {
 * 
 *      private ICallback _callback;
 *
 *      public void setCallback(ICallback _callback) {
 *          _callback = callback;
 *      }
 *
 *      public void formSubmit(IRequestCycle cycle) throws RequestCycleException {
 *          // Authentication code
 *          ..
 *   
 *          Visit visit = (Visit) getVisit();
 *
 *          visit.setAuthenticated(true);
 *  
 *          if (_callback != null) {
 *              _callback.performCallback(cycle);
 *          }
 *      }
 *  }    
 *  </pre>
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 *
 **/

public class PageCallback implements ICallback
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -3286806776105690068L;

    private String _pageName;

    public PageCallback(String pageName)
    {
        _pageName = pageName;
    }

    public PageCallback(IPage page)
    {
        this(page.getName());
    }

    public String toString()
    {
        return "PageCallback[" + _pageName + "]";
    }

    /**
     *  Invokes {@link IRequestCycle#setPage(String)} to select the previously
     *  identified page as the response page.
     *
     **/

    public void performCallback(IRequestCycle cycle) throws RequestCycleException
    {
        cycle.setPage(_pageName);
    }
}