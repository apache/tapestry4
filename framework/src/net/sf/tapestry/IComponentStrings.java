package net.sf.tapestry;

/**
 *  A set of localized Strings used by a component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public interface IComponentStrings
{
    /**
     *  Searches for a localized string with the given key.
     *  If not found, a modified version of the key
     *  is returned (all upper-case and surrounded by square
     *  brackets).
     * 
     **/
    
	public String getString(String key);
	
	/**
	 *  Searches for a localized string with the given key.
	 *  If not found, then the default value (which should already
	 *  be localized) is returned.  Passing a default of null
	 *  is useful when trying to determine if the strings contains
	 *  a given key.
	 * 
	 **/
	
	public String getString(String key, String defaultValue);    
}
