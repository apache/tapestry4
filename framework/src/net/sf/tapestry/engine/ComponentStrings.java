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
package net.sf.tapestry.engine;

import java.util.Properties;

import net.sf.tapestry.IComponentStrings;

/**
 *  Implementation of {@link IComponentStrings}.  This is basically
 *  a wrapper around an instance of {@link Properties}.  This ensures
 *  that the properties are, in fact, read-only (which ensures that
 *  they don't have to be synchronized).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class ComponentStrings implements IComponentStrings
{
	private Properties properties;
	
	public ComponentStrings(Properties properties)
	{
	    this.properties = properties;
	}
	
    public String getString(String key, String defaultValue)
		{
		    return  properties.getProperty(key, defaultValue);
    }

    public String getString(String key)
    {
		String result = properties.getProperty(key);
		
		if (result == null)
			result = "[" + key.toUpperCase() + "]";
			
		return result;
    }

}
