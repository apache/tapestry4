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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *  An implementation of {@link javax.servlet.ServletConfig} used
 *  for Mock testing. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class MockServletConfig implements ServletConfig, IInitParameterHolder
{
    private String _name;
    private ServletContext _context;
    private Map _initParameters = new HashMap();

    public MockServletConfig(String name, ServletContext context)
    {
        _name = name;
        _context = context;
    }

    public String getInitParameter(String name)
    {
        return (String) _initParameters.get(name);
    }

    public Enumeration getInitParameterNames()
    {
        return Collections.enumeration(_initParameters.keySet());
    }

    public ServletContext getServletContext()
    {
        return _context;
    }

    public String getServletName()
    {
        return _name;
    }

    public void setInitParameter(String name, String value)
    {
        _initParameters.put(name, value);
    }

}
