package net.sf.tapestry.param;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Define a type of connector between a binding of a component and a JavaBeans
 *  property of the component (with the same name).  Allows
 *  for the parameter to be set before the component is rendered,
 *  then cleared after the component is rendered.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 * 
 **/

public interface IParameterConnector
{
    /**
     *  Sets the parameter from the binding.
     *  
     *  @throws RequiredParameterException if the parameter is
     *  required, but the {@link net.sf.tapestry.IBinding}
     *  supplies a null value.
     * 
     **/
    
	public void setParameter(IRequestCycle cycle)
	throws RequiredParameterException;
	
	/**
	 *  Clears the parameters to a null, 0 or false value
	 *  (depending on type).
	 * 
	 **/
	
	public void resetParameter(IRequestCycle cycle);
}
