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
package net.sf.tapestry.util.prop;

/**
 *  Used by {@link net.sf.tapestry.util.prop.PropertyFinder}
 *  to identify information about a property. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class PropertyInfo
{
    private String _name;
    private Class _type;
    private boolean _read;
    private boolean _write;
    
    PropertyInfo(String name, Class type, boolean read, boolean write)
    {
        _name = name;
        _type = type;
        _read = read;
        _write = write;
    }
    
    public String getName()
    {
        return _name;
    }

    public Class getType()
    {
        return _type;
    }

    public boolean isRead()
    {
        return _read;
    }

    public boolean isWrite()
    {
        return _write;
    }
    
    public boolean isReadWrite()
    {
        return _read && _write;
    }

}
