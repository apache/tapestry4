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
 *  A source for configuration properties.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public interface IPropertySource
{
    /**
     *  Returns the value for a given property, or null if the
     *  source does not provide a value for the named property.
     *  Implementations of IPropertySource may use delegation
     *  to resolve the value (that is, if one property source returns null,
     *  it may forward the request to another source).
     * 
     **/
    
    public String getPropertyValue(String propertyName);
}
