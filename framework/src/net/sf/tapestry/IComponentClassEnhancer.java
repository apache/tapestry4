package net.sf.tapestry;

import net.sf.tapestry.spec.ComponentSpecification;

/**
 *
 *  A provider of enhanced classes, classes with new methods 
 *  and new attributes, and possibly, implementing new
 *  Java interfaces.  The primary use of class enhancement is to
 *  automate the creation of transient and persistant properties.
 * 
 *  <p>
 *  Implementations of this interface must be threadsafe.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public interface IComponentClassEnhancer
{
	/**
	 *  Clears all cached data for the enhancer; this includes references to
	 *  enhanced classes.
	 * 
	 **/
	
	public void reset();
	
	/**
	 *  Used to access the class for a given component (or page).  Returns the
	 *  specified class, or an enhanced version of the class if the
	 *  component requires enhancement.
	 * 
	 *  @param specification the specification for the component
	 *  @param className the name of base class to enhance, as extracted
	 *  from the specification (or possibly, from a default).
	 * 
	 *  @throws ApplicationRuntimeException if the class does not exist, is invalid,
	 *  or may not be enhanced.
	 * 
	 **/
	
	public Class getEnhancedClass(ComponentSpecification specification, String className);
}
