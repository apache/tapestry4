package net.sf.tapestry;

/**
 *  A particular type of component usuable with the
 *  action service.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.1
 */

public interface IAction extends IComponent
{
    /**
     *  Returns true if the component requires 
     *  an existing, not new, {@link javax.servlet.http.HttpSession}
     *  to operate.  Components who are not dependant on page state
     *  (or the visit object) are non-stateful and can return false.
     *
     **/

    public boolean getRequiresSession();
}