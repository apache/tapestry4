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

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;

import net.sf.tapestry.IPropertySource;

/**
 *  Implementation of {@link net.sf.tapestry.IPropertySource}
 *  that returns values defined as Servlet initialization parameters
 *  (defined as <code>&lt;init-param&gt;</code> in the
 *  <code>web.xml</code> deployment descriptor.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/ 

public class ServletPropertySource implements IPropertySource
{
    private ServletConfig _config;
    
    public ServletPropertySource(ServletConfig config)
    {
        _config = config;
    }   
    
    /**
     *  Invokes {@link ServletConfig#getInitParameter(java.lang.String)}.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        return _config.getInitParameter(propertyName);
    }

}
