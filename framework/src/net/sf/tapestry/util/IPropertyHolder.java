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

package net.sf.tapestry.util;

import java.util.Collection;

/**
 *  An interface that defines an object that can store named propertys.  The names
 *  and the properties are Strings.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPropertyHolder
{
    /**
     *  Returns a Collection of Strings, the names of all
     *  properties held by the receiver.  May return an empty collection.
     *
     **/

    public Collection getPropertyNames();

    /**
     *  Sets a named property.  The new value replaces the existing value, if any.
     *  Setting a property to null is the same as removing the property.
     *
     **/

    public void setProperty(String name, String value);

    /**
     *  Removes the named property, if present.
     *
     **/

    public void removeProperty(String name);

    /**
     *  Retrieves the named property, or null if the property is not defined.
     *
     **/

    public String getProperty(String name);
}