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

package net.sf.tapestry.junit.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 *  
 *  Base class for holders of named attributes such as
 *  {@link javax.servlet.http.HttpSession}, 
 *  {@link javax.servlet.http.HttpServletRequest}
 *  and {@link javax.servlet.ServletContext}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class AttributeHolder
{
    private Map _attributes = new HashMap();
    
    public Object getAttribute(String name)
    {
        return _attributes.get(name);
    }


    public Enumeration getAttributeNames()
    {
        return getEnumeration(_attributes);
    }

    protected Enumeration getEnumeration(Map map)
    {
        return Collections.enumeration(map.keySet());
    }

    public String[] getAttributeNamesArray()
    {
        Set keys = _attributes.keySet();
        int count = keys.size();
        
        String[] array = new String[count];
        
        return (String[])keys.toArray(array);
    }

    public void setAttribute(String name, Object value)
    {
        _attributes.put(name, value);
    }


    public void removeAttribute(String name)
    {
        _attributes.remove(name);
    }

}
