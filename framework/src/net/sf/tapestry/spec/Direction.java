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


package net.sf.tapestry.spec;

import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.Enum;

/**
 *  Represents different types of parameters.  Currently only 
 *  in and custom are supported, but this will likely change
 *  when Tapestry supports out parameters is some form (that reflects
 *  form style processing).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/
 
 
public class Direction extends Enum
{
    /**
     *  The parameter value is input only; the component property value
     *  is unchanged or not relevant after the component renders.
     * 
     **/
    
	public static final Direction IN = new Direction("IN");
	
	/**
	 *  Processing of the parameter is entirely the responsibility
	 *  of the component, which must obtain an manipulate
	 *  the {@link net.sf.tapestry.IBinding} (if any) for the parameter.
	 * 
	 **/
	
	public static final Direction CUSTOM = new Direction("CUSTOM");

    protected Direction(String enumerationId)
    {
        super(enumerationId);
    }

    /**
     *  Returns a user-presentable name for the direction.
     * 
     **/
    
    public String getName()
    {
        return Tapestry.getString("Direction." + getEnumerationId());
    }
}
