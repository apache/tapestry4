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

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 *  Mock implementation of {@link javax.servlet.http.HttpSession}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockSession extends AttributeHolder implements HttpSession
{
    private MockContext _context;
    private String _id;
    
    public MockSession(MockContext context, String id)
    {
        _context = context;
        _id = id;
    }
    
    public long getCreationTime()
    {
        return 0;
    }

    public String getId()
    {
        return _id;
    }

    public long getLastAccessedTime()
    {
        return 0;
    }

    public ServletContext getServletContext()
    {
        return _context;
    }

    public void setMaxInactiveInterval(int arg0)
    {
    }

    public int getMaxInactiveInterval()
    {
        return 0;
    }

    public HttpSessionContext getSessionContext()
    {
        return null;
    }

    public Object getValue(String name)
    {
        return getAttribute(name);
    }

    public String[] getValueNames()
    {
        return getAttributeNamesArray();
    }

    public void putValue(String name, Object value)
    {
        setAttribute(name, value);
    }

    public void removeValue(String name)
    {
        removeAttribute(name);
    }

    public void invalidate()
    {
    }

    public boolean isNew()
    {
        return false;
    }

}
