package net.sf.tapestry.valid;

import net.sf.tapestry.form.IFormComponent;

/**
 *  Interface for a number of components that act as a normal
 *  {@link net.sf.tapestry.form.TextField} component, but perform extra validation.
 * 
 * 
 *  <p>In release 1.0.9, it was changed to extend
 *  {@link IFormComponent} (not {@link net.sf.tapestry.IComponent}) and most of its
 *  methods were moved there.
 *  
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IField extends IFormComponent
{
	/**
	 *  Returns the type of value, derived from the fields value binding.
	 * 
	 **/
	
	public Class getValueType();

}