package net.sf.tapestry;

/**
 *  Defines an object that can provide a component with its
 *  {@link IComponentStrings}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public interface IComponentStringsSource
{
	public IComponentStrings getStrings(IComponent component);
	
	/**
	 *  Clears all cached information for the source.
	 * 
	 **/
	
	public void reset();
}
