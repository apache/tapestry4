//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
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
