package net.sf.tapestry;

/**
 *  Interface that defines classes that may be messaged by the direct
 *  service.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IDirect extends IComponent
{
	/**
	 *  Invoked by the direct service to have the component peform
	 *  the appropriate action.  The {@link DirectLink} component will
	 *  notify its listener.
	 *
	 **/

	public void trigger(IRequestCycle cycle)
		throws RequestCycleException;
        
     /**
      *  Invoked by the direct service to query the component as to
      *  whether it is stateful.  If stateful and no 
      *  {@link HttpSession} is active, then a 
      *  {@link net.sf.tapestry.StaleSessionException} is
      *  thrown by the service.
      * 
      *  @since 2.3
      * 
      **/
     
    public boolean isStateful();
     
    
}