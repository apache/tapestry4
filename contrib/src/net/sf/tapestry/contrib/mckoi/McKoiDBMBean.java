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
package net.sf.tapestry.contrib.mckoi;

import org.jboss.system.ServiceMBean;

/**
 *  MBean interface for {@link net.sf.tapestry.contrib.mckoi.McKoiDB}.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 * 
 **/

public interface McKoiDBMBean extends ServiceMBean
{
    public String getRootPath();
    
    /**
     *  Sets the root directory name, which is where database files are stored.
     * 
     **/
    
    public void setRootPath(String path);
    
    public String getConfigPath();
    
    /**
     *  Sets the config path, the path to a properties file that sets database
     *  parameters.
     * 
     **/
    
    public void setConfigPath(String path);
}